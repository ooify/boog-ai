package me.ooify.boogai.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import me.ooify.boogai.dto.cart.CartItemDTO;
import me.ooify.boogai.entity.Cart;
import me.ooify.boogai.entity.CartItem;
import me.ooify.boogai.service.impl.CartItemServiceImpl;
import me.ooify.boogai.service.impl.CartServiceImpl;
import me.ooify.boogai.utils.Result;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jonsan
 * @since 2023-12-07 07:11:14
 */
@Transactional
@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private CartServiceImpl cartService;

    @Resource
    private CartItemServiceImpl cartItemService;

    @Resource
    private ModelMapper modelMapper;


    @GetMapping
    public Result getCartByUser() {
        Cart cart = cartService.getOne(new QueryWrapper<Cart>().eq("user_id", StpUtil.getLoginIdAsLong()));
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(StpUtil.getLoginIdAsLong());
            cartService.save(cart);
        }
        return Result.ok("查询成功")
                .setData(cartService.getCartItems(cart.getId()));
    }

    @PostMapping
    public Result addCartItem(@Valid @RequestBody CartItemDTO cartItemDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        if (cartItemDTO.getQuantity() <= 0) {
            return Result.error("数量不合法");
        }
        Cart cart = cartService.getOne(new QueryWrapper<Cart>().eq("user_id", StpUtil.getLoginIdAsLong()));
        CartItem item = modelMapper.map(cartItemDTO, CartItem.class);
        item.setCartId(cart.getId());
        CartItem cartItem = cartItemService.getOne(new QueryWrapper<CartItem>().eq("cart_id", cart.getId()).eq("book_id", item.getBookId()));
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            return Result.ok("添加成功");
        }
        if (cartItemService.save(item)) return Result.ok("添加成功");
        else return Result.error("添加失败");
    }

    @PutMapping
    public Result updateCartItemQuantity(@Valid @RequestBody CartItemDTO cartItemDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        Cart cart = cartService.getOne(new QueryWrapper<Cart>().eq("user_id", StpUtil.getLoginIdAsLong()));
        CartItem cartItem = cartItemService.getOne(new QueryWrapper<CartItem>().eq("cart_id", cart.getId()).eq("book_id", cartItemDTO.getBookId()));
        if (cartItem == null) {
            return Result.error("购物车中没有该商品");
        }
        if (cartItem.getQuantity() + cartItemDTO.getQuantity() <= 0) {
            return Result.error("数量不合法");
        }
        cartItem.setQuantity(cartItemDTO.getQuantity());
        if (cartItemService.updateById(cartItem)) return Result.ok("添加成功");
        else return Result.error("添加失败");
    }

    @DeleteMapping("/{id}")
    public Result deleteCartItem(@RequestBody List<CartItemDTO> cartItemDTOs) {
        Cart cart = cartService.getOne(new QueryWrapper<Cart>().eq("user_id", StpUtil.getLoginIdAsLong()));
        for (CartItemDTO cartItemDTO : cartItemDTOs) {
            CartItem cartItem = cartItemService.getOne(new QueryWrapper<CartItem>().eq("cart_id", cart.getId()).eq("book_id", cartItemDTO.getBookId()));
            if (cartItem == null) {
                return Result.error("购物车中没有该商品");
            }
            if (cartItemService.removeById(cartItem.getId())) return Result.ok("删除成功");
            else return Result.error("删除失败");
        }
        return Result.ok("删除成功");
    }


}
