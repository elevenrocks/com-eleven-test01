package com.eleven;


import com.eleven.dao.ProductInfoMapper;
import com.eleven.domain.ProductInfo;
import com.eleven.domain.vo.ProductInfoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

//spring单元测试
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"classpath:applicationContext_dao.xml",
                    "classpath:applicationContext_service.xml"})
public class TestLike {
   @Autowired
    ProductInfoMapper productInfoMapper;
   @Test
    public void testLike(){
       ProductInfoVo vo = new ProductInfoVo();
      // vo.setPname("4");
     //  vo.setTypeid(3);
       vo.setLprice(3000);
       vo.setHprice(3999);
       List<ProductInfo> list = productInfoMapper.selectCondition(vo);
       for (ProductInfo l:list){
           System.out.println(l);
       }
   }
}
