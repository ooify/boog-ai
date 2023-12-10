package me.ooify.boogai.service.impl;

import me.ooify.boogai.entity.Cart;
import me.ooify.boogai.mapper.CartMapper;
import me.ooify.boogai.service.CartService;
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
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

}
