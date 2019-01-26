package com.loura.classupload.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

/**
 * @ClassName: UploadController
 * @Description: TODO
 * @Author loura
 * @Date 2019-01-26 23:25
 * @Version 1.0
 **/

@Controller
public class UploadController {
    private String uploadDir;

    /**
     * @author loura
     * @Description 初始化上传文件界面，跳转到index.jsp
     * @Date 23:26 2019-01-26
     * @Paream []
     * @return java.lang.String
     **/
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    public void uploadDir(HttpServletRequest request) {
        // 上传目录地址
        uploadDir = request.getSession().getServletContext().getRealPath("/") + "upload/";
        // 如果目录不存在，自动创建文件夹
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * @author loura
     * @Description 提取上传方法为公共方法
     * @Date 23:45 2019-01-26
     * @Paream [uploadDir, file]
     * @return void
     **/
    public void executeUpload(String uploadDir, MultipartFile file) throws Exception {
        // 文件后缀名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        // 上传文件名
        String filename = UUID.randomUUID() + suffix;
        // 服务器端保存的文件对象
        File serverFile = new File(uploadDir + filename);
        // 将上传的文件写入到服务器端文件内
        file.transferTo(serverFile);
    }

    /**
     * @author loura
     * @Description 上传文件方法
     * @Date 23:28 2019-01-26
     * @Paream [file] 前台上传的文件对象
     * @return java.lang.String
     **/
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody String upload(HttpServletRequest request, MultipartFile file) {
        try {
            uploadDir(request);
            // 调用上传方法
            executeUpload(uploadDir, file);
        } catch (Exception e) {
            // 打印错误堆栈信息
            e.printStackTrace();
            return "上传失败";
        }
        return "上传成功";
    }

    /**
     * @author loura
     * @Description 上传多个文件
     * @Date 00:00 2019-01-27
     * @Paream [request, file]
     * @return java.lang.String
     **/
    @RequestMapping(value = "/uploads", method = RequestMethod.POST)
    public @ResponseBody String uploads(HttpServletRequest request, MultipartFile[] file) {
        try {
            uploadDir(request);
            // 遍历文件数组执行上传
            for (int i=0; i<file.length;i++) {
                if (file[i] != null) {
                    // 调用上传方法
                    executeUpload(uploadDir, file[i]);
                }
            }
        } catch (Exception e) {
            // 打印错误堆栈信息
            e.printStackTrace();
            return "上传失败";
        }
        return "上传成功";
    }
}
