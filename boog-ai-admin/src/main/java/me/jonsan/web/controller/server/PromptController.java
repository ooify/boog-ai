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
import me.jonsan.server.domain.Prompt;
import me.jonsan.server.service.IPromptService;
import me.jonsan.common.utils.poi.ExcelUtil;
import me.jonsan.common.core.page.TableDataInfo;

/**
 * 提示词Controller
 * 
 * @author jonsan
 * @date 2025-04-21
 */
@RestController
@RequestMapping("/server/prompt")
public class PromptController extends BaseController
{
    @Autowired
    private IPromptService promptService;

    /**
     * 查询提示词列表
     */
    @PreAuthorize("@ss.hasPermi('server:prompt:list')")
    @GetMapping("/list")
    public TableDataInfo list(Prompt prompt)
    {
        startPage();
        List<Prompt> list = promptService.selectPromptList(prompt);
        return getDataTable(list);
    }

    /**
     * 导出提示词列表
     */
    @PreAuthorize("@ss.hasPermi('server:prompt:export')")
    @Log(title = "提示词", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Prompt prompt)
    {
        List<Prompt> list = promptService.selectPromptList(prompt);
        ExcelUtil<Prompt> util = new ExcelUtil<Prompt>(Prompt.class);
        util.exportExcel(response, list, "提示词数据");
    }

    /**
     * 获取提示词详细信息
     */
    @PreAuthorize("@ss.hasPermi('server:prompt:query')")
    @GetMapping(value = "/{promptId}")
    public AjaxResult getInfo(@PathVariable("promptId") Long promptId)
    {
        return success(promptService.selectPromptByPromptId(promptId));
    }

    /**
     * 新增提示词
     */
    @PreAuthorize("@ss.hasPermi('server:prompt:add')")
    @Log(title = "提示词", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Prompt prompt)
    {
        return toAjax(promptService.insertPrompt(prompt));
    }

    /**
     * 修改提示词
     */
    @PreAuthorize("@ss.hasPermi('server:prompt:edit')")
    @Log(title = "提示词", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Prompt prompt)
    {
        return toAjax(promptService.updatePrompt(prompt));
    }

    /**
     * 删除提示词
     */
    @PreAuthorize("@ss.hasPermi('server:prompt:remove')")
    @Log(title = "提示词", businessType = BusinessType.DELETE)
	@DeleteMapping("/{promptIds}")
    public AjaxResult remove(@PathVariable Long[] promptIds)
    {
        return toAjax(promptService.deletePromptByPromptIds(promptIds));
    }
}
