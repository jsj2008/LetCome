package com.letcome.controller;

import com.letcome.entity.ImageEntity;
import com.letcome.entity.ReturnEntity;
import com.letcome.service.ImageService;
import com.letcome.util.SystemUtil;
import com.letcome.vo.ImageVO;
import org.hibernate.Hibernate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by rjt on 16/8/12.
 */
@Controller
@RequestMapping("/image")
public class ImageController {
    @Resource(name="imageService")
    private ImageService service;
//    @RequestMapping("/upload")
//    @ResponseBody
//    public ReturnEntity addImage(@RequestHeader("let_come_uid") String uid ,@RequestParam("myfiles") CommonsMultipartFile[] files,HttpServletRequest request){
//
//        if(files!=null && files.length>0 ) {
//            CommonsMultipartFile file = files[0];
//            System.out.println("fileName---------->" + file.getOriginalFilename());
//
//            if (!file.isEmpty()) {
//                try {
//                    String str = request.getSession().getServletContext().getRealPath("upload/img/product");
//                    System.out.println(str);
//                    str = request.getSession().getServletContext().getRealPath("upload/img/product");
//                    System.out.println(str);
//                    return service.addImage(Integer.valueOf(uid), file.getOriginalFilename(), file.getBytes());
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    ReturnEntity ret = new ReturnEntity();
//                    ret.setResult(ReturnEntity.RETURN_FAILED);
//                    ret.setError_msg(e.getMessage());
//                    return ret;
//                }
//            }else{
//                ReturnEntity ret = new ReturnEntity();
//                ret.setResult(ReturnEntity.RETURN_FAILED);
//                ret.setError_msg("上传文件为空");
//                return ret;
//            }
//        }else{
//            ReturnEntity ret = new ReturnEntity();
//            ret.setResult(ReturnEntity.RETURN_FAILED);
//            ret.setError_msg("上传文件为空");
//            return ret;
//        }
//
//    }

    @RequestMapping("/upload")
    @ResponseBody
    public ReturnEntity addImage(
            @RequestHeader(value = "User-Agent" ,required = false,defaultValue = "") String ua ,
            @RequestHeader("let_come_uid") String uid ,
            @RequestParam("myfiles") MultipartFile file,
            HttpServletRequest request){

//        if(files!=null && files.length>0 ) {
//            CommonsMultipartFile file = files[0];
            System.out.println("fileName---------->" + file.getOriginalFilename());

            if (!file.isEmpty()) {
                try {
                    int platform = SystemUtil.PLATEFORM_IOS;
                    if(ua.indexOf("Android")>0){
                        platform = SystemUtil.PLATEFORM_ANDROID;
                    }
                    return service.addImage(Integer.valueOf(uid), file.getOriginalFilename(), file.getBytes(),platform);
                } catch (Exception e) {
                    e.printStackTrace();
                    ReturnEntity ret = new ReturnEntity();
                    ret.setResult(ReturnEntity.RETURN_FAILED);
                    ret.setError_msg(e.getMessage());
                    return ret;
                }
            }else{
                ReturnEntity ret = new ReturnEntity();
                ret.setResult(ReturnEntity.RETURN_FAILED);
                ret.setError_msg("上传文件为空");
                return ret;
            }
//        }else{
//            ReturnEntity ret = new ReturnEntity();
//            ret.setResult(ReturnEntity.RETURN_FAILED);
//            ret.setError_msg("上传文件为空");
//            return ret;
//        }

    }

//    @RequestMapping(value = "getimg", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<InputStreamResource> downloadUserAvatarImage(@RequestParam("id") String id) {
//        Blob blob = service.getImage(Integer.valueOf(id));
//        try {
//            File f =
//            ResponseEntity ret = ResponseEntity.ok()
//                    .contentLength(blob.length())
//                    .contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
//                    .body(new InputStreamResource(blob.getBinaryStream()));
//            return ret;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }


//    public void getImage(HttpServletRequest request,HttpServletResponse response) {
//        FileInputStream fis = null;
//        response.setContentType("image/gif");
//        String id = request.getParameter("id");
//        try {
//            ImageEntity e = service.getImage(Integer.valueOf(id));
//            OutputStream out = response.getOutputStream();
//            File file = new File("D:"+File.separator+"timg.jpg");
//            fis = new FileInputStream(file);
//            byte[] b = new byte[fis.available()];
//            fis.read(e.getImage().getBytes());
//            out.write(b);
//            out.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    @RequestMapping(value = "getimg")
    public void getImage(@RequestParam("id") String id,
                         @RequestParam(value = "type",required = false,defaultValue = ImageVO.IMAGE_TYPE_FULL) String type,
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception {

        ImageEntity e = service.getImage(Integer.valueOf(id),type);

        response.setContentType("image/png");

        OutputStream stream = response.getOutputStream();
        stream.write(e.getImage());
        stream.flush();
        stream.close();
    }




}
