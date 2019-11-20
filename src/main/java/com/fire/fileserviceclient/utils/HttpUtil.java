package com.fire.fileserviceclient.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.UUID;

public class HttpUtil {

    @Value("${rsa.private.priRsaKeyPath}")
    private String priRsaKeyPath;

    public HttpHeaders getHeader(){

        String s = UUID.randomUUID().toString();
        String s1 = null;
        try {
            s1 = RsaUtil.encryptionByPri(s, RSAEncrypt.loadPrivateKeyByFile(priRsaKeyPath));
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        headers.add("X-SID",s);
        headers.add("X-Signature",s1);

        return headers;
    }
}
