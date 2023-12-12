package me.ooify.boogai.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import me.ooify.boogai.dto.address.AddressDTO;
import me.ooify.boogai.dto.address.AddresssDTO;
import me.ooify.boogai.entity.Address;
import me.ooify.boogai.service.impl.AddressServiceImpl;
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
 * @since 2023-12-07 07:11:13
 */
@Transactional
@RestController
@RequestMapping("/address")
public class AddressController {
    @Resource
    private AddressServiceImpl addressService;

    @Resource
    private ModelMapper modelMapper;

    @SaCheckRole("admin")
    @GetMapping("/{id}")
    public Result getAddressById(@PathVariable Long id) {
        return Result.ok("查询成功").setData(modelMapper.map(addressService.getById(id), AddresssDTO.class));
    }

    @GetMapping
    public Result getAddressesByUser() {
        List<Address> addresses = addressService.list(new QueryWrapper<Address>().eq("user_id", StpUtil.getLoginIdAsLong()).orderByDesc("is_default"));
        List<AddresssDTO> list = addresses.stream().map(address -> modelMapper.map(address, AddresssDTO.class)).toList();
        return Result.ok("查询成功").setData(list);
    }

    @GetMapping("/default")
    public Result getDefaultAddress() {
        Address address = addressService.getOne(new QueryWrapper<Address>().eq("user_id", StpUtil.getLoginIdAsLong()).eq("is_default", 1));
        return Result.ok("查询成功").setData(modelMapper.map(address, AddresssDTO.class));
    }

    @PostMapping
    public Result createAddress(@Valid @RequestBody AddressDTO addressDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUserId(StpUtil.getLoginIdAsLong());
        if (address.getIsDefault() == 1) {
            addressService.update(new UpdateWrapper<Address>().set("is_default", 0).eq("user_id", StpUtil.getLoginIdAsLong()));
        }
        addressService.save(address);
        return Result.ok("创建成功");
    }

    @PutMapping
    public Result updateAddress(@Valid @RequestBody AddressDTO addressDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        Address address = modelMapper.map(addressDTO, Address.class);
        if (address.getIsDefault() == 1) {
            addressService.update(new UpdateWrapper<Address>().set("is_default", 0).eq("user_id", StpUtil.getLoginIdAsLong()));
        }
        addressService.updateById(address);
        return Result.ok("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result deleteAddress(@PathVariable Long id) {
        addressService.removeById(id);
        return Result.ok("删除成功");
    }


}
