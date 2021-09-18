package com.eleven.service.impl;

import com.eleven.dao.ProductTypeMapper;
import com.eleven.domain.ProductType;
import com.eleven.domain.ProductTypeExample;
import com.eleven.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {
    @Autowired
    ProductTypeMapper productTypeMapper;
    @Override
    public List<ProductType> getAll() {
        return productTypeMapper.selectByExample(new ProductTypeExample());
    }
}
