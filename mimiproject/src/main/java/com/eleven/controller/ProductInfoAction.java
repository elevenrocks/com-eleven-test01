package com.eleven.controller;

import com.eleven.domain.ProductInfo;
import com.eleven.domain.vo.ProductInfoVo;
import com.eleven.service.ProductInfoService;
import com.eleven.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    //每页显示的记录条数
    public static final int PAGE_SIZE=5;
    //图片名称
    String saveFile ="";

    @Autowired
    ProductInfoService service;

    /**
     * 所有
     * @param request
     * @return
     */
    @RequestMapping("/getAll.action")
    public String getAll(HttpServletRequest request){
        List<ProductInfo> list = service.getAll();
        request.setAttribute("list",list);
        return "product";
    }

    /**
     * 第一页商品
     * @param req
     * @return
     */
    @RequestMapping("/split.action")
    public String split(HttpServletRequest req){
        //第一页数据
        PageInfo info = null;
        Object vo = req.getSession().getAttribute("prodVo");
        if (vo!=null){
             info = service.splitPageVo((ProductInfoVo)vo,PAGE_SIZE);
             req.getSession().removeAttribute("prodVo");
        }else {
            info = service.splitPage(1,PAGE_SIZE);
        }
        info = service.splitPage(1,PAGE_SIZE);
        req.setAttribute("info",info);
        return "product";
    }

    /**
     * 其他页面的显示
     * @param vo
     * @param session
     */
    @ResponseBody
    @RequestMapping("/ajaxSplit.action")
    public void ajaxSplit(ProductInfoVo vo, HttpSession session){
        //取当前页面的page
        PageInfo info = service.splitPageVo(vo,PAGE_SIZE);
        session.setAttribute("info",info);
    }

    /**
     * 多条件查询
     * @param vo
     * @param session
     */
    @ResponseBody
    @RequestMapping("/condition.action")
    public void condition(ProductInfoVo vo,HttpSession session){
        List<ProductInfo> list = service.selectCondition(vo);
        for (ProductInfo l:list){
            System.out.println(l);
        }
        session.setAttribute("list",list);
    }

    /**
     * 异步ajax图片上传
     * @param pimage
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/ajaxImg.action")
    public Object ajaxImg(MultipartFile pimage ,HttpServletRequest request){
        //提取生成文件名后缀名
        saveFile = FileNameUtil.getUUIDFileName()+FileNameUtil.getFileType(pimage.getOriginalFilename());
        //得到项目中图片的路径
        String path = request.getServletContext().getRealPath("/image_big");
        try {
            //转存:当前工程的绝对路径\image_big\**.jpg
            pimage.transferTo(new File(path+File.separator+saveFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回客户端json对象,封装图片的路径,
        JSONObject object = new JSONObject();
        object.put("imgurl",saveFile);

        return object.toString();
    }

    /**
     * 添加商品
     * @param productInfo
     * @param request
     * @return
     */
    @RequestMapping("/save.action")
    public String save(ProductInfo productInfo,HttpServletRequest request){
        System.out.println(saveFile);
        productInfo.setpImage(saveFile);
        productInfo.setpDate(new Date());
        //
        int num = -1;
        try {
            num = service.save(productInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(num>0){
            request.setAttribute("msg","添加成功!!");
        }else {
            request.setAttribute("msg","添加失败!");
        }
        //清空saveFile,方便下次添加和修改的异步ajax的上传处理
        saveFile = "";
        //添加成功后重新访问数据库
        return "forward:/prod/split.action";
    }

    /**
     * 根据主键获取商品信息跳转到更新页面
     * @param pid
     * @param model
     * @return
     */
    @RequestMapping("/one.action")
    public String one(int pid,ProductInfoVo vo, Model model,HttpSession session){
        ProductInfo info = service.getById(pid);
        model.addAttribute("prod",info);
        //多条件数据放入session
        session.setAttribute("prodVo",vo);
        return "update";
    }

    /**
     * 更新商品信息
     * @param info
     * @param request
     * @return
     */
    @RequestMapping("/update.action")
    public String update(ProductInfo info,HttpServletRequest request){
        //如果没有上传,使用实体类的隐藏表单域来注入图片信息
        //处理图片
        if(!saveFile.equals("")){
            //有上传图片,重新注入
            info.setpImage(saveFile);
        }
        int num = -1;

        try {
            num = service.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num>0){
            request.setAttribute("msg","更新成功!");
        }else {
            request.setAttribute("msg","更新失败!");
        }
        //更新完后需要清空
        saveFile = "";
        return "forward:/prod/split.action";
    }

    /**
     * 删除商品
     * @param pid
     * @return
     */
    @RequestMapping("/delete.action")
    public String delete(Integer pid,ProductInfoVo vo,HttpServletRequest req){
        int num = -1;
        try {
            num = service.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num>0){
            //删除成功
            req.setAttribute("msg","删除成功!!");
            req.getSession().setAttribute("deletePodVo",vo);
        }else {
            req.setAttribute("msg","删除失败!!");
        }
        //删除成功后跳转无法到ajax的分页显示
       return "forward:/prod/deleteAjaxsplit.action";
    }
    @ResponseBody
    @RequestMapping(value = "/deleteAjaxsplit.action",produces = "text/html;charset=utf-8")
    public Object deleteAjaxSplit(HttpServletRequest request){
        //取第一页数据
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("deleteProdVo");
        if(vo!=null){
            info = service.splitPageVo((ProductInfoVo)vo ,PAGE_SIZE);

        }else {
            info = service.splitPage(1, PAGE_SIZE);
        }
        request.getSession().setAttribute("info",info);
        return request.getAttribute("msg");
    }

    /**
     * 批量删除
     * @param pids
     * @param request
     * @return
     */
    @RequestMapping("/deleteBatch.action")
    public String deleteBatch(String pids,HttpServletRequest request){
        String[] pidsArr = pids.split(",");
        try {
            int num = service.deleteBatch(pidsArr);
            if(num>0){
                request.setAttribute("msg","批量删除成功!!");

            }else {
                request.setAttribute("msg","批量删除失败!!");
            }
        } catch (Exception e) {
            request.setAttribute("msg","商品不可删除!!!");
        }


        return "forward:/prod/deleteAjaxsplit.action";
    }


}
