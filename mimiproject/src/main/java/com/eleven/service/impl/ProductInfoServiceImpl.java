package com.eleven.service.impl;

import com.eleven.dao.ProductInfoMapper;
import com.eleven.domain.ProductInfo;
import com.eleven.domain.ProductInfoExample;
import com.eleven.domain.vo.ProductInfoVo;
import com.eleven.service.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    ProductInfoMapper productInfoMapper;

    /**
     * 所有商品
     * @return
     */
    @Override
    public List<ProductInfo> getAll() {
        return productInfoMapper.selectByExample(new ProductInfoExample());
    }

    /**
     * 第一页
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo splitPage(int pageNum, int pageSize) {
        //分页
        PageHelper.startPage(pageNum,pageSize);
        //数据封装,进行有条件查询操作需要创建对象
        ProductInfoExample example = new ProductInfoExample();
        //设置排序,主键排序
        example.setOrderByClause("p_id desc");
        //返回集合
        List<ProductInfo> list = productInfoMapper.selectByExample(example);
        //封装到PageInfo
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    /**
     * 添加商品
     * @param productInfo
     * @return
     */
    @Override
    public int save(ProductInfo productInfo) {
        return productInfoMapper.insert(productInfo);
    }

    @Override
    public ProductInfo getById(int pid) {
        return productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {

        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(Integer pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deleteBatch(String[] ids) {
        return productInfoMapper.deleteBatch(ids);
    }

    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {
        return productInfoMapper.selectCondition(vo);
    }

    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductInfoVo vo, int pageSize) {
        //设置分页
        PageHelper.startPage(vo.getPageNum(),pageSize);
        List<ProductInfo> list = productInfoMapper.selectCondition(vo);
        return new PageInfo<>(list);
    }
}
