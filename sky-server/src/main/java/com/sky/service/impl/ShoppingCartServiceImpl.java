package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 判断商品是否已存在
        Long userId = BaseContext.getCurrentId();
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        String dishFlavor = shoppingCartDTO.getDishFlavor();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(userId!=null, ShoppingCart::getUserId, userId)
                .eq(dishId!=null, ShoppingCart::getDishId, dishId)
                .eq(setmealId!=null, ShoppingCart::getSetmealId, setmealId)
                .eq(dishFlavor!=null, ShoppingCart::getDishFlavor, dishFlavor);
        List<ShoppingCart> list = shoppingCartMapper.selectList(queryWrapper);
        // 如果已存在，数量加1, 不存在，添加
        if(list!=null && !list.isEmpty()){
            ShoppingCart shoppingCart = list.get(0); // 获取购物车对象,查到了只可能是一条
            shoppingCart.setNumber(shoppingCart.getNumber()+1);
            shoppingCartMapper.updateById(shoppingCart);
        }else{
            ShoppingCart shoppingCart = new ShoppingCart();
            // 判断是菜品还是套餐
            if(dishId != null){
                // 菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart = ShoppingCart.builder()
                        .userId(userId)
                        .dishId(dishId)
                        .name(dish.getName())
                        .image(dish.getImage())
                        .amount(dish.getPrice())
                        .createTime(LocalDateTime.now())
                        .dishFlavor(dishFlavor)
                        .number(1)
                        .build();
            }else{
                // 套餐
                Setmeal setmeal = setmealMapper.selectById(setmealId);
                shoppingCart = ShoppingCart.builder()
                        .userId(userId)
                        .setmealId(setmealId)
                        .name(setmeal.getName())
                        .image(setmeal.getImage())
                        .amount(setmeal.getPrice())
                        .createTime(LocalDateTime.now())
                        .number(1)
                        .build();
            }
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(userId!=null, ShoppingCart::getUserId, userId);
        List<ShoppingCart> list = shoppingCartMapper.selectList(queryWrapper);
        return list;
    }

    /**
     * 清空购物车
     */
    @Override
    public void cleanShppingCart() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(userId!=null, ShoppingCart::getUserId, userId);
        shoppingCartMapper.delete(queryWrapper);
    }

    /**
     * 删除购物车中的一个商品
     * @param shoppingCartDTO
     */
    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        String dishFlavor = shoppingCartDTO.getDishFlavor();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(userId!=null, ShoppingCart::getUserId, userId)
                .eq(dishId!=null, ShoppingCart::getDishId, dishId)
                .eq(setmealId!=null, ShoppingCart::getSetmealId, setmealId)
                .eq(dishFlavor!=null, ShoppingCart::getDishFlavor, dishFlavor);
        List<ShoppingCart> list = shoppingCartMapper.selectList(queryWrapper);
        if(list!=null && !list.isEmpty()){
            list.forEach(shoppingCart -> {
                if(shoppingCart.getNumber() == 1){
                    //数量为1时，删除数据库中的这条商品记录
                    shoppingCartMapper.deleteById(shoppingCart);
                }else{
                    //数量大于1时，数量减1
                    shoppingCart.setNumber(shoppingCart.getNumber()-1);
                    shoppingCartMapper.updateById(shoppingCart);
                }
            });
        }
    }
}
