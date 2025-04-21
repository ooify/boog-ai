package me.jonsan.server.service.impl;

import java.util.List;
import me.jonsan.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.jonsan.server.mapper.SecretKeyMapper;
import me.jonsan.server.domain.SecretKey;
import me.jonsan.server.service.ISecretKeyService;

/**
 * 密钥Service业务层处理
 * 
 * @author jonsan
 * @date 2025-04-21
 */
@Service
public class SecretKeyServiceImpl implements ISecretKeyService 
{
    @Autowired
    private SecretKeyMapper secretKeyMapper;

    /**
     * 查询密钥
     * 
     * @param keyId 密钥主键
     * @return 密钥
     */
    @Override
    public SecretKey selectSecretKeyByKeyId(Long keyId)
    {
        return secretKeyMapper.selectSecretKeyByKeyId(keyId);
    }

    /**
     * 查询密钥列表
     * 
     * @param secretKey 密钥
     * @return 密钥
     */
    @Override
    public List<SecretKey> selectSecretKeyList(SecretKey secretKey)
    {
        return secretKeyMapper.selectSecretKeyList(secretKey);
    }

    /**
     * 新增密钥
     * 
     * @param secretKey 密钥
     * @return 结果
     */
    @Override
    public int insertSecretKey(SecretKey secretKey)
    {
        secretKey.setCreateTime(DateUtils.getNowDate());
        return secretKeyMapper.insertSecretKey(secretKey);
    }

    /**
     * 修改密钥
     * 
     * @param secretKey 密钥
     * @return 结果
     */
    @Override
    public int updateSecretKey(SecretKey secretKey)
    {
        secretKey.setUpdateTime(DateUtils.getNowDate());
        return secretKeyMapper.updateSecretKey(secretKey);
    }

    /**
     * 批量删除密钥
     * 
     * @param keyIds 需要删除的密钥主键
     * @return 结果
     */
    @Override
    public int deleteSecretKeyByKeyIds(Long[] keyIds)
    {
        return secretKeyMapper.deleteSecretKeyByKeyIds(keyIds);
    }

    /**
     * 删除密钥信息
     * 
     * @param keyId 密钥主键
     * @return 结果
     */
    @Override
    public int deleteSecretKeyByKeyId(Long keyId)
    {
        return secretKeyMapper.deleteSecretKeyByKeyId(keyId);
    }
}
