package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.EmployeeMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Transactional
    @Override
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(StatusConstant.ENABLE);

        // 保存套餐数据
        setmealMapper.save(setmeal);

        // 保存套餐和菜品的关联关系
        Long setmealId = setmealMapper.getIdByName(setmealDTO.getName());
        List<SetmealDish> list = setmealDTO.getSetmealDishes();
        list.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));

        log.info("保存套餐和菜品的关联关系:{}", list);
        setmealDishMapper.insert(list);
        /*setmealDishMapper.saveByList(list);*/
        log.info("保存套餐和菜品的关联关系成功");
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        String name = setmealPageQueryDTO.getName();
        Integer status = setmealPageQueryDTO.getStatus();
        Integer categoryId = setmealPageQueryDTO.getCategoryId();

        // 创建 LambdaQueryWrapper 对象进行动态查询
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<Setmeal>()
                .like(name != null && !name.isEmpty(), Setmeal::getName, name)
                .eq(status != null, Setmeal::getStatus, status)
                .eq(categoryId != null, Setmeal::getCategoryId, categoryId);

        List<Setmeal> setmealList = setmealMapper.selectList(wrapper);
        log.info("查询完毕");
        //封装封装SetmealVO对象
        List<SetmealVO> setmealVOList = new ArrayList<>();
        for (Setmeal setmeal : setmealList) {
            SetmealVO setmealVO = new SetmealVO();
            BeanUtils.copyProperties(setmeal, setmealVO);
            // 设置套餐分类名称
            setmealVO.setCategoryName(categoryMapper.getCategoryNameById(setmeal.getCategoryId()));
            // 设置套餐菜品关系列表
            setmealVO.setSetmealDishes(setmealDishMapper.selectByDishId(setmeal.getId()));
            setmealVOList.add(setmealVO);
        }
        PageInfo<SetmealVO> pageInfo = new PageInfo<SetmealVO>(setmealVOList);
        // 封装分页结果并返回
        PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());

        return pageResult;
    }

    /**
     * 套餐启售停售
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.startOrStop(setmeal);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        // 判断套餐是否在售
        for (Long id : ids){
            Setmeal setmeal = setmealMapper.selectById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                // 当前套餐正在启售中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        // 根据id批量删除套餐数据
        setmealMapper.deleteByIds(ids);
        // 根据id批量删除套餐和菜品的关联关系
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<SetmealDish>()
                .in(SetmealDish::getSetmealId, ids);  // 根据 setmealId 字段进行批量删除
        setmealDishMapper.delete(wrapper);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO selectById(Long id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(id);
        log.info("查询中包含的菜品：{}", setmealDishes);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void changeSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(StatusConstant.ENABLE);
        // 修改套餐数据
        setmealMapper.updateById(setmeal);
        // 删除套餐和菜品的关联关系
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        // 新增套餐和菜品的关联关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealDTO.getId()));

        setmealDishMapper.insert(setmealDishes);
    }

    /**
     * 根据条件查询套餐
     * @param setmeal
     * @return
     */
    @Override
    public List<Setmeal> getListByCondition(Setmeal setmeal) {
        String name = setmeal.getName();
        Long categoryId = setmeal.getCategoryId();
        Integer status = setmeal.getStatus();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<Setmeal>()
                .like(name!=null, Setmeal::getName, name)
                .eq(categoryId!=null, Setmeal::getCategoryId, categoryId)
                .eq(status!=null, Setmeal::getStatus, status);
        List<Setmeal> list = setmealMapper.selectList(queryWrapper);
        return list;
    }

    /**
     * 根据套餐id查询包含的菜品
     * @param setmealId
     * @return
     */
    @Override
    public List<DishItemVO> getDishItemById(Long setmealId) {
        return setmealMapper.getDishItemBySetmealId(setmealId);
    }

}
