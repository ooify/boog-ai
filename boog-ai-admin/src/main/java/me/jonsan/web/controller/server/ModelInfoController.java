package me.jonsan.web.controller.server;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import me.jonsan.common.annotation.Log;
import me.jonsan.common.core.controller.BaseController;
import me.jonsan.common.core.domain.AjaxResult;
import me.jonsan.common.enums.BusinessType;
import me.jonsan.server.domain.ModelInfo;
import me.jonsan.server.service.IModelInfoService;
import me.jonsan.common.utils.poi.ExcelUtil;
import me.jonsan.common.core.page.TableDataInfo;

/**
 * 模型Controller
 * 
 * @author jonsan
 * @date 2025-04-21
 */
@RestController
@RequestMapping("/server/model")
public class ModelInfoController extends BaseController
{
    @Autowired
    private IModelInfoService modelInfoService;

    /**
     * 查询模型列表
     */
    @PreAuthorize("@ss.hasPermi('server:model:list')")
    @GetMapping("/list")
    public TableDataInfo list(ModelInfo modelInfo)
    {
        startPage();
        List<ModelInfo> list = modelInfoService.selectModelInfoList(modelInfo);
        return getDataTable(list);
    }

    /**
     * 导出模型列表
     */
    @PreAuthorize("@ss.hasPermi('server:model:export')")
    @Log(title = "模型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ModelInfo modelInfo)
    {
        List<ModelInfo> list = modelInfoService.selectModelInfoList(modelInfo);
        ExcelUtil<ModelInfo> util = new ExcelUtil<ModelInfo>(ModelInfo.class);
        util.exportExcel(response, list, "模型数据");
    }

    /**
     * 获取模型详细信息
     */
    @PreAuthorize("@ss.hasPermi('server:model:query')")
    @GetMapping(value = "/{modelId}")
    public AjaxResult getInfo(@PathVariable("modelId") Long modelId)
    {
        return success(modelInfoService.selectModelInfoByModelId(modelId));
    }

    /**
     * 新增模型
     */
    @PreAuthorize("@ss.hasPermi('server:model:add')")
    @Log(title = "模型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ModelInfo modelInfo)
    {
        return toAjax(modelInfoService.insertModelInfo(modelInfo));
    }

    /**
     * 修改模型
     */
    @PreAuthorize("@ss.hasPermi('server:model:edit')")
    @Log(title = "模型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ModelInfo modelInfo)
    {
        return toAjax(modelInfoService.updateModelInfo(modelInfo));
    }

    /**
     * 删除模型
     */
    @PreAuthorize("@ss.hasPermi('server:model:remove')")
    @Log(title = "模型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{modelIds}")
    public AjaxResult remove(@PathVariable Long[] modelIds)
    {
        return toAjax(modelInfoService.deleteModelInfoByModelIds(modelIds));
    }
}
