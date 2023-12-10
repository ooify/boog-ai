package me.ooify.boogai.controller;

import cn.dev33.satoken.annotation.SaCheckRole;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;

import jakarta.validation.Valid;
import me.ooify.boogai.dto.order.OrderDTO;

import me.ooify.boogai.entity.Address;
import me.ooify.boogai.entity.Order;

import me.ooify.boogai.entity.OrderItem;
import me.ooify.boogai.service.impl.AddressServiceImpl;
import me.ooify.boogai.service.impl.BookServiceImpl;
import me.ooify.boogai.service.impl.OrderItemServiceImpl;
import me.ooify.boogai.service.impl.OrderServiceImpl;
import me.ooify.boogai.utils.Result;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderServiceImpl orderService;
    @Resource
    private BookServiceImpl bookService;
    @Resource
    private OrderItemServiceImpl orderItemService;
    @Resource
    private AddressServiceImpl addressService;

    @SaCheckRole("admin")
    @GetMapping("/{id}")
    public Result getOrderById(@PathVariable Long id) {
        Order order = orderService.getOne(new QueryWrapper<Order>().eq("id", id));
        return Result.ok("查询成功")
                .setData(order);
    }

    @GetMapping()
    public Result getOrders(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "status", required = false) Integer status,
                            @RequestParam(value = "sortField", defaultValue = "created_at") String sortField,
                            @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder) {
        pageNum = Math.max(1, pageNum);
        pageSize = Math.max(1, pageSize);
        if (!sortOrder.equals("desc") && !sortOrder.equals("asc")) {
            sortOrder = "desc";
        }
        Page<Order> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(status != null, "status", status)
                .orderByAsc(sortOrder.equals("asc"), sortField)
                .orderByDesc(sortOrder.equals("desc"), sortField);

        return Result.ok("查询成功").setData(orderService.getOrders(page, queryWrapper));
    }

    @SaCheckRole("admin")
    @GetMapping("/list")
    public Result getOrderList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "orderId", required = false) String orderId,
                               @RequestParam(value = "userId", required = false) String userId,
                               @RequestParam(value = "status", required = false) Integer status,
                               @RequestParam(value = "totalAmount", required = false) Double totalAmount,
                               @RequestParam(value = "sortField", defaultValue = "created_at") String sortField,
                               @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder) {

        pageNum = Math.max(1, pageNum);
        pageSize = Math.max(1, pageSize);
        if (!sortOrder.equals("desc") && !sortOrder.equals("asc")) {
            sortOrder = "desc";
        }
        Page<Order> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(StringUtils.isNotBlank(orderId), "id", orderId)
                .eq(StringUtils.isNotBlank(userId), "user_id", userId)
                .eq(status != null, "status", status)
                .eq(totalAmount != null, "total_amount", totalAmount)
                .orderByAsc(sortOrder.equals("asc"), sortField)
                .orderByDesc(sortOrder.equals("desc"), sortField);
        return Result.ok("查询成功")
                .setData(orderService.page(page, queryWrapper));
    }

    @PostMapping
    public Result createOrder(@Valid @RequestBody OrderDTO ordersDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        if (addressService.getOne(new QueryWrapper<Address>().eq("id", ordersDTO.getAddressId()).eq("user_id", StpUtil.getLoginIdAsLong())) == null) {
            return Result.error("地址不存在");
        }
        Long orderId = System.currentTimeMillis();
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(StpUtil.getLoginIdAsLong());
        order.setAddressId(ordersDTO.getAddressId());
        order.setTotalAmount(ordersDTO.getBooks().stream()
                .mapToDouble(orderBookDTO -> bookService.getById(orderBookDTO.getBookId())
                        .getPrice() * orderBookDTO.getQuantity())
                .sum());
        orderService.save(order);
        ordersDTO.getBooks().forEach(orderBookDTO -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setBookId(orderBookDTO.getBookId());
            orderItem.setQuantity(orderBookDTO.getQuantity());
            orderItem.setPrice(bookService.getById(orderBookDTO.getBookId()).getPrice() * orderBookDTO.getQuantity());
            orderItemService.save(orderItem);
        });
        return Result.ok("新增成功");
    }

    @SaCheckRole("admin")
    @PutMapping
    public Result updateOrder(@RequestBody Order order) {
        if (orderService.updateById(order)) {
            return Result.ok("更新成功");
        }
        return Result.error("更新失败");
    }

    @SaCheckRole("admin")
    @DeleteMapping("/{id}")
    public Result deleteOrder(@PathVariable Long id) {
        if (orderService.removeById(id)) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    @DeleteMapping("/user/{id}")
    public Result deleteOrderByUser(@PathVariable Long id) {
        Order order = orderService.getOne(new QueryWrapper<Order>().eq("id", id).eq("user_id", StpUtil.getLoginIdAsLong()));
        if (order != null) {
            order.setStatus(6);
            orderService.updateById(order);
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

}
