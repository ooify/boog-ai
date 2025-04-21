package me.jonsan.server.service;

import java.util.List;
import me.jonsan.server.domain.SecretKey;

/**
 * 密钥Service接口
 * 
 * @author jonsan
 * @date 2025-04-21
 */
public interface ISecretKeyService 
{
    /**
     * 查询密钥
     * 
     * @param keyId 密钥主键
     * @return 密钥
     */
    public SecretKey selectSecretKeyByKeyId(Long keyId);

    /**
     * 查询密钥列表
     * 
     * @param secretKey 密钥
     * @return 密钥集合
     */
    public List<SecretKey> selectSecretKeyList(SecretKey secretKey);

    /**
     * 新增密钥
     * 
     * @param secretKey 密钥
     * @return 结果
     */
    public int insertSecretKey(SecretKey secretKey);

    /**
     * 修改密钥
     * 
     * @param secretKey 密钥
     * @return 结果
     */
    public int updateSecretKey(SecretKey secretKey);

    /**
     * 批量删除密钥
     * 
     * @param keyIds 需要删除的密钥主键集合
     * @return 结果
     */
    public int deleteSecretKeyByKeyIds(Long[] keyIds);

    /**
     * 删除密钥信息
     * 
     * @param keyId 密钥主键
     * @return 结果
     */
    public int deleteSecretKeyByKeyId(Long keyId);
}
