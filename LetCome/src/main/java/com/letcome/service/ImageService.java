package com.letcome.service;

import com.letcome.dao.ImageDAO;
import com.letcome.dao.ProductDAO;
import com.letcome.dao.UsersDAO;
import com.letcome.entity.ImageEntity;
import com.letcome.entity.LoginEntity;
import com.letcome.entity.ReturnEntity;
import com.letcome.util.EncryptUtils;
import com.letcome.util.SystemUtil;
import com.letcome.vo.ImageVO;
import com.letcome.vo.ProductVO;
import com.letcome.vo.UserVO;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by rjt on 16/8/10.
 */
@Transactional
public class ImageService {


    static private String DES_KEY = "www.letcome.com";

    private ImageDAO imageDao;

    public ImageDAO getImageDao() {
        return imageDao;
    }

    public void setImageDao(ImageDAO imageDao) {
        this.imageDao = imageDao;
    }

    private ProductDAO productDao;

    public ProductDAO getProductDao() {
        return productDao;
    }

    public void setProductDao(ProductDAO productDao) {
        this.productDao = productDao;
    }

    public ReturnEntity addImage(Integer uid,String imagename,byte[] bytes,int plateform){
//        ImageVO vo  = new ImageVO();
//        vo.setUid(uid);
//        vo.setImagename(imagename);
//        vo.setImage(bytes);
////        imageDao.insertImage(vo);
//        File f = new File();
        long time = (new Date()).getTime();
        File file = new File("/upload/img/"+imagename);
        while (file.exists()){
            file = new File("/upload/img/"+time+imagename);
        }
        File fileThumb = new File("/upload/img/"+ImageVO.IMAGE_TYPE_THUMB+"_"+imagename);
        while (file.exists()){
            fileThumb = new File("/upload/img/"+ImageVO.IMAGE_TYPE_THUMB+"_"+time+imagename);
        }
        OutputStream fop = null;
        ReturnEntity ret = new ReturnEntity();
        try {
            System.out.println(file.getAbsolutePath());
            file.createNewFile();
            fop = new FileOutputStream(file);
            fop.write(bytes);
            fop.flush();
            fop.close();

            BufferedImage sourceImg =ImageIO.read(new FileInputStream(file));

            fop = new FileOutputStream(fileThumb);
            Thumbnails.of(file).scale(0.1).toOutputStream(fop);
            fop.flush();
            fop.close();

            ProductVO pvo = new ProductVO();
            pvo.setUid(uid);
            pvo.setStatus(ProductVO.STATUS_SELLING);
            ret = productDao.insertProduct(pvo);
            Integer pid = Integer.valueOf(ret.getRetVal().toString());
            if (pid>0){
                ImageVO vo = new ImageVO();
                vo.setImagename(imagename);
                vo.setImagepath(file.getAbsolutePath());
                vo.setThumbpath(fileThumb.getAbsolutePath());
                //相反？
                System.out.println("getHeight=" + sourceImg.getHeight() + ";getWidth=" + sourceImg.getWidth());
                if (plateform == SystemUtil.PLATEFORM_IOS) {
                    vo.setThumbwidth(sourceImg.getHeight()/10);
                    vo.setThumbheight( sourceImg.getWidth()/10);
                    vo.setImagewidth(sourceImg.getHeight());
                    vo.setImageheight(sourceImg.getWidth());
                }else{
                    vo.setThumbwidth(sourceImg.getWidth()/10);
                    vo.setThumbheight( sourceImg.getHeight()/10);
                    vo.setImagewidth(sourceImg.getWidth());
                    vo.setImageheight(sourceImg.getHeight());
                }

                vo.setUid(uid);
                vo.setProductid(pid);
                ret = imageDao.insertImage(vo);
                ret.setRetVal(pid.toString());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ret.setResult(ReturnEntity.RETURN_FAILED);
            ret.setError_msg(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            ret.setResult(ReturnEntity.RETURN_FAILED);
            ret.setError_msg(e.getMessage());
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return ret;
    }

    public ReturnEntity attachImage(Integer uid,Integer pid,String imagename,byte[] bytes,int plateform){
        long time = (new Date()).getTime();
        File file = new File("/upload/img/"+imagename);
        while (file.exists()){
            file = new File("/upload/img/"+time+imagename);
        }
        File fileThumb = new File("/upload/img/"+ImageVO.IMAGE_TYPE_THUMB+"_"+imagename);
        while (file.exists()){
            fileThumb = new File("/upload/img/"+ImageVO.IMAGE_TYPE_THUMB+"_"+time+imagename);
        }
        OutputStream fop = null;
        ReturnEntity ret = new ReturnEntity();
        try {
            System.out.println(file.getAbsolutePath());
            file.createNewFile();
            fop = new FileOutputStream(file);
            fop.write(bytes);
            fop.flush();
            fop.close();

            BufferedImage sourceImg =ImageIO.read(new FileInputStream(file));

            fop = new FileOutputStream(fileThumb);
            Thumbnails.of(file).scale(0.1).toOutputStream(fop);
            fop.flush();
            fop.close();

            if (pid>0){
                ImageVO vo = new ImageVO();
                vo.setImagename(imagename);
                vo.setImagepath(file.getAbsolutePath());
                vo.setThumbpath(fileThumb.getAbsolutePath());
                //相反？
                System.out.println("getHeight=" + sourceImg.getHeight() + ";getWidth=" + sourceImg.getWidth());
                if (plateform == SystemUtil.PLATEFORM_IOS) {
                    vo.setThumbwidth(sourceImg.getHeight()/10);
                    vo.setThumbheight( sourceImg.getWidth()/10);
                    vo.setImagewidth(sourceImg.getHeight());
                    vo.setImageheight(sourceImg.getWidth());
                }else{
                    vo.setThumbwidth(sourceImg.getWidth()/10);
                    vo.setThumbheight( sourceImg.getHeight()/10);
                    vo.setImagewidth(sourceImg.getWidth());
                    vo.setImageheight(sourceImg.getHeight());
                }

                vo.setUid(uid);
                vo.setProductid(pid);
                ret = imageDao.insertImage(vo);
                ret.setRetVal(pid.toString());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ret.setResult(ReturnEntity.RETURN_FAILED);
            ret.setError_msg(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            ret.setResult(ReturnEntity.RETURN_FAILED);
            ret.setError_msg(e.getMessage());
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return ret;
    }

    public ImageEntity getImage(Integer id,String type){
        ImageVO vo  = new ImageVO();
        vo.setId(id);
        vo = imageDao.getImage(vo);
        ImageEntity e = new ImageEntity();
        e.setId(vo.getId());
        e.setUid(vo.getUid());
        File file = null;
        if (type !=null && type.equals(ImageVO.IMAGE_TYPE_THUMB)) {
            file = new File(vo.getThumbpath());
        }else{
            file = new File(vo.getImagepath());
        }

        if (file.exists()){
            try {
                InputStream fi = new FileInputStream(file);
                byte[] b=new byte[fi.available()];//新建一个字节数组
                fi.read(b);//将文件中的内容读取到字节数组中
                fi.close();
                e.setImage(b);
            } catch (Exception e1) {
                e1.printStackTrace();
                throw  new RuntimeException(e1.getMessage());
            }

        }else{
            throw  new RuntimeException("图片不存在");
        }
        e.setImagename(vo.getImagename());
        return e;
    }

//    public List<ImageEntity> getWaterfalls(long pno,long limit,String downloadPath){
//        long start = (pno-1)*limit;
//        long end = pno*limit-1;
//        List<ImageVO> list = imageDao.getImages(start,end);
//        List<ImageEntity> imageEntities = new ArrayList<ImageEntity>();
//        for(int i=0;i<list.size();++i){
//            ImageEntity e = new ImageEntity();
//            ImageVO v = list.get(i);
//            e.setImagename(v.getImagename());
//            e.setImagepath(downloadPath+v.getId());
//            e.setThumbpath(downloadPath+v.getId());
//            imageEntities.add(e);
//        }
//        return imageEntities;
//    }
}