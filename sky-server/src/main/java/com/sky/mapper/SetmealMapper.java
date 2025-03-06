package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal>{

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据套餐名称查询套餐id
     * @param name
     * @return
     */
    @Select("select id from setmeal where name = #{name}")
    Long getIdByName(String name);

    /**
     * 保存套餐信息
     * @param setmeal
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into setmeal(category_id, name, price, status, description, image, create_time, update_time, create_user, update_user)" +
            "values (#{categoryId},#{name}, #{price}, #{status},#{description},#{image},#{createTime} ,#{updateTime} ,#{createUser} ,#{updateUser})")
    void save(Setmeal setmeal);

    /**
     * 套餐启售停售
     * @param setmeal
     */
    @AutoFill(value = OperationType.UPDATE)
    @Update("update setmeal set status = #{status},update_user = #{updateUser},update_time = #{updateTime} where id = #{id}")
    void startOrStop(Setmeal setmeal);
}
