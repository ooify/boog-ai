package me.ooify.boogai.service.impl;

import me.ooify.boogai.entity.Address;
import me.ooify.boogai.mapper.AddressMapper;
import me.ooify.boogai.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jonsan
 * @since 2023-12-07 07:11:13
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

}
