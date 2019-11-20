package com.fire.fileserviceclient.controller;

import com.fire.fileserviceclient.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api/file")
public class FileController {

    @Autowired
    private FileService service;

    /**
     * 从服务端获取加密之后的文件，解密返回给前端
     * @return
     */
    @RequestMapping(value = "downloadFile",method = RequestMethod.GET)
    public void downloadFile(@RequestParam String uuid, HttpServletResponse response){
        service.downloadFile(uuid,response);
    }


    /**
     * 获取上传的文件，将其上传到服务端，返回加密之后的文件名
     * @param file
     * @return
     */
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file){
        return service.uploadFile(file);
    }

    @RequestMapping(value = "test", method = RequestMethod.POST)
    public String test(){
        return service.test();
    }
}
