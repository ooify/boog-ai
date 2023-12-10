package me.ooify.boogai.service.impl;

import me.ooify.boogai.entity.OrderItem;
import me.ooify.boogai.mapper.OrderItemMapper;
import me.ooify.boogai.service.OrderItemService;
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
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
