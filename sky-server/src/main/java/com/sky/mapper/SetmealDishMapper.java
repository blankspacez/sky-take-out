package com.sky.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {

    /**
     * 根据菜品id查询套餐id
     * @param queryWrapper
     * @return
     */
    //被selectObjs取代
    /*List<Long> getSetmealIdsByDishIds(Wrapper queryWrapper);*/

    /**
     * 根据菜品id查询套餐中的菜品
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where dish_id = #{id}")
    List<SetmealDish> selectByDishId(Long id);

    /**
     * 批量插入套餐菜品关系
     * @param list
     */
    //被insert取代
    /*@Insert({
            "<script>",
            "INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies) VALUES ",
            "<foreach collection='list' item='sd' separator=','>",
            "(#{sd.setmealId}, #{sd.dishId}, #{sd.name}, #{sd.price}, #{sd.copies})",
            "</foreach>",
            "</script>"
    })
    void saveByList(List<SetmealDish> list);*/

    /**
     * 根据套餐id查询套餐菜品关系
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> selectBySetmealId(Long setmealId);

    /**
     * 根据套餐id删除套餐菜品关系
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);
}
