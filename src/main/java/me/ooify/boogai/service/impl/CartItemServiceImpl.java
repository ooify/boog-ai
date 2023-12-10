package me.ooify.boogai.service.impl;

import me.ooify.boogai.entity.CartItem;
import me.ooify.boogai.mapper.CartItemMapper;
import me.ooify.boogai.service.CartItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jonsan
 * @since 2023-12-07 07:11:14
 */
@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {

}
