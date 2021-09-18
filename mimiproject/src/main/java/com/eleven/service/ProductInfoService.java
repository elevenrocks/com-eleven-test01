package com.eleven.service;

import com.eleven.domain.ProductInfo;
import com.eleven.domain.vo.ProductInfoVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductInfoService {
    /**
     * 显示所用的商品不分页
     * @return
     */
    List<ProductInfo> getAll();

    /**
     * 分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo splitPage(int pageNum,int pageSize);

    /**
     * 添加商品
     * @param productInfo
     * @return
     */
    int save(ProductInfo productInfo);

    /**
     * 根据主键查询商品
     * @param pid
     * @return
     */
    ProductInfo getById(int pid);

    /**
     * 更新商品
     * @param info
     * @return
     */
    int update(ProductInfo info);

    /**
     * 删除商品
     * @param pid
     * @return
     */
    int delete(Integer pid);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteBatch(String[] ids);

    /**
     * 多条件查询
     * @param vo
     * @return
     */
    List<ProductInfo> selectCondition(ProductInfoVo vo);

    /**
     * 多条件查询分页
     * @param vo
     * @param pageSize
     * @return
     */
    PageInfo<ProductInfo> splitPageVo(ProductInfoVo vo,int pageSize);
}
