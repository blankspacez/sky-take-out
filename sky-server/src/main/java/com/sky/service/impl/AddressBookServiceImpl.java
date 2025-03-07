package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 条件查询
     *
     * @param addressBook
     * @return
     */
    public List<AddressBook> list(AddressBook addressBook) {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<AddressBook>()
                .eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId())
                .eq(addressBook.getPhone()!=null, AddressBook::getPhone, addressBook.getPhone())
                .eq(addressBook.getIsDefault() != null, AddressBook::getIsDefault, addressBook.getIsDefault());
        return addressBookMapper.selectList(queryWrapper);
    }

    /**
     * 新增地址
     *
     * @param addressBook
     */
    public void save(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.add(addressBook);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public AddressBook getById(Long id) {
        AddressBook addressBook = addressBookMapper.getById(id);
        return addressBook;
    }

    /**
     * 根据id修改地址
     *
     * @param addressBook
     */
    public void update(AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<AddressBook>()
                .eq(AddressBook::getId, addressBook.getId())
                .set(addressBook.getConsignee() != null, AddressBook::getConsignee, addressBook.getConsignee())
                .set(addressBook.getSex() != null, AddressBook::getSex, addressBook.getSex())
                .set(addressBook.getPhone() != null, AddressBook::getPhone, addressBook.getPhone())
                .set(addressBook.getDetail() != null, AddressBook::getDetail, addressBook.getDetail())
                .set(addressBook.getLabel() != null, AddressBook::getLabel, addressBook.getLabel())
                .set(addressBook.getIsDefault() != null, AddressBook::getIsDefault, addressBook.getIsDefault());
        addressBookMapper.update(wrapper);
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     */
    @Transactional
    public void setDefault(AddressBook addressBook) {
        //1、将当前用户的所有地址修改为非默认地址
        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.updateIsDefaultByUserId(addressBook);

        //2、将当前地址改为默认地址
        addressBook.setIsDefault(1);
        this.update(addressBook);
    }

    /**
     * 根据id删除地址
     *
     * @param id
     */
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }

}
