package me.ooify.boogai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import me.ooify.boogai.dto.address.AddressDTO;
import me.ooify.boogai.dto.order.OrdersDTO;
import me.ooify.boogai.entity.Address;
import me.ooify.boogai.entity.Order;
import me.ooify.boogai.mapper.AddressMapper;
import me.ooify.boogai.mapper.OrderItemMapper;
import me.ooify.boogai.mapper.OrderMapper;
import me.ooify.boogai.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private AddressMapper addressMapper;
    @Resource
    private ModelMapper modelMapper;

    public IPage<OrdersDTO> getOrders(Page<Order> page, QueryWrapper<Order> queryWrapper) {
        Page<Order> orderPage = baseMapper.selectPage(page, queryWrapper);
        IPage<OrdersDTO> ordersDTOPage = orderPage.convert(order -> {
            OrdersDTO ordersDTO1 = modelMapper.map(order, OrdersDTO.class);
            ordersDTO1.setOrderItems(orderItemMapper.findOrderItemsByOrderId(order.getId()));
            Long addressId = order.getAddressId();
            Address address = addressMapper.selectById(addressId);
            AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);
            ordersDTO1.setAddress(addressDTO);
            return ordersDTO1;
        });
        return ordersDTOPage;
    }
}
