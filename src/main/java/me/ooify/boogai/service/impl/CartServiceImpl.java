package me.ooify.boogai.service.impl;

import jakarta.annotation.Resource;
import me.ooify.boogai.dto.cart.CartItemsDTO;

import me.ooify.boogai.entity.Cart;
import me.ooify.boogai.mapper.CartItemMapper;
import me.ooify.boogai.mapper.CartMapper;
import me.ooify.boogai.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jonsan
 * @since 2023-12-07 07:11:14
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {
    @Resource
    private CartItemMapper cartItemMapper;

    public List<CartItemsDTO> getCartItems(Long cartId) {
        return cartItemMapper.findCartItemsByCartId(cartId);
    }
}
