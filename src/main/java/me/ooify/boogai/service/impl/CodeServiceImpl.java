package me.ooify.boogai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.ooify.boogai.entity.Code;

import me.ooify.boogai.mapper.CodeMapper;
import me.ooify.boogai.service.CodeService;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jonsan
 * @since 2023-12-07 07:11:14
 */
@Service
public class CodeServiceImpl extends ServiceImpl<CodeMapper, Code> implements CodeService {

}
