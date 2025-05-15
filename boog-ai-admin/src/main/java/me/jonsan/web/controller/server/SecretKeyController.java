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
import me.jonsan.server.domain.SecretKey;
import me.jonsan.server.service.ISecretKeyService;
import me.jonsan.common.utils.poi.ExcelUtil;
import me.jonsan.common.core.page.TableDataInfo;

/**
 * 密钥Controller
 * 
 * @author jonsan
 * @date 2025-04-21
 */
@RestController
@RequestMapping("/server/secret")
public class SecretKeyController extends BaseController
{
    @Autowired
    private ISecretKeyService secretKeyService;

    /**
     * 查询密钥列表
     */
    @PreAuthorize("@ss.hasPermi('server:secret:list')")
    @GetMapping("/list")
    public TableDataInfo list(SecretKey secretKey)
    {
        startPage();
        List<SecretKey> list = secretKeyService.selectSecretKeyList(secretKey);
        return getDataTable(list);
    }

    /**
     * 导出密钥列表
     */
    @PreAuthorize("@ss.hasPermi('server:secret:export')")
    @Log(title = "密钥", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SecretKey secretKey)
    {
        List<SecretKey> list = secretKeyService.selectSecretKeyList(secretKey);
        ExcelUtil<SecretKey> util = new ExcelUtil<SecretKey>(SecretKey.class);
        util.exportExcel(response, list, "密钥数据");
    }

    /**
     * 获取密钥详细信息
     */
    @PreAuthorize("@ss.hasPermi('server:secret:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId)
    {
        return success(secretKeyService.selectSecretKeyByKeyId(keyId));
    }

    /**
     * 新增密钥
     */
    @PreAuthorize("@ss.hasPermi('server:secret:add')")
    @Log(title = "密钥", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SecretKey secretKey)
    {
        return toAjax(secretKeyService.insertSecretKey(secretKey));
    }

    /**
     * 修改密钥
     */
    @PreAuthorize("@ss.hasPermi('server:secret:edit')")
    @Log(title = "密钥", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SecretKey secretKey)
    {
        return toAjax(secretKeyService.updateSecretKey(secretKey));
    }

    /**
     * 删除密钥
     */
    @PreAuthorize("@ss.hasPermi('server:secret:remove')")
    @Log(title = "密钥", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds)
    {
        return toAjax(secretKeyService.deleteSecretKeyByKeyIds(keyIds));
    }
}
