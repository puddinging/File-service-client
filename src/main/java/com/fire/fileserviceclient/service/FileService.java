package com.fire.fileserviceclient.service;

import com.alibaba.fastjson.JSONObject;
import com.fire.fileserviceclient.utils.HttpUtil;
import com.fire.fileserviceclient.utils.RSAEncrypt;
import com.fire.fileserviceclient.utils.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${rsa.private.priRsaKeyPath}")
    private String priRsaKeyPath;

    @Value("${url.downloadUrl}")
    private String downloadUrl;

    @Value("${url.uploadUrl}")
    private String uploadUrl;

    @Value("${url.getDataUrl}")
    private String getDataUrl;

    /**
     * 通过uuid获得加密的文件，解密通过response响应给前端
     * @param uuid
     */
    public void downloadFile(String uuid, HttpServletResponse resp) {
//        获取文件
        // TODO: 2019/11/15 需要加一个头信息来响应
        Map<String,String> map = new HashMap<>();
        map.put("fileName",uuid);
        byte[] result = restTemplate.postForObject(downloadUrl,map,byte[].class);

//        获取密钥并使用私钥进行解密
        // TODO: 2019/11/15 需要加一个头信息来请求 
        String data = restTemplate.postForObject(getDataUrl,map,String.class);
//        json解析
        JSONObject jsonObject = JSONObject.parseObject(data);
//        获取密钥
        String digitalEnvelope = (String) jsonObject.get("digitalEnvelope");
//        密钥解密
        String key = null;
        try {
            key = RsaUtil.encryptionByPri("fjdkfjkd", RSAEncrypt.loadPrivateKeyByFile(priRsaKeyPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        文件解密并响应
        byte[] decryptFile = fileDecryptByKey(key,result);

        try {
            ServletOutputStream sos = resp.getOutputStream();
            sos.write(decryptFile);
            sos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] fileDecryptByKey(String key, byte[] result) {
        // TODO: 2019/11/15 实现文件解密，返回一个byte[]
        return new byte[0];
    }

    /**
     * 调用服务端接口将文件上传，用服务端的的元数据接
     * 口获取文件详细信息返回给前端
     * @param file
     * @return
     */
    public String uploadFile(MultipartFile file) {
//        请求头信息
        

//        请求体信息
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", file);
        form.add("fileName",file.getOriginalFilename());

        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, new HttpUtil().getHeader());

        return restTemplate.postForObject(uploadUrl, files, String.class);
    }

    public String test() {
//        String filepath="E:\\tmp";
//
//        //生成公钥和私钥文件
//        RSAEncrypt.genKeyPair(filepath);
//        try {
//            return RsaUtil.encryptionByPri("fjdkfjkd", RSAEncrypt.loadPrivateKeyByFile(filepath));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
        restTemplate.getForObject("",String.class);
        return null;
    }
}
