package com.fire.fileserviceclient.utils;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class RsaUtil {

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    //    通过私钥加密sid返回signature
    public static String encryptionByPri(String sid, String privateKey){
        String xSignature = null;
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(spec);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(sid.getBytes(StandardCharsets.UTF_8));

            byte[] sign = signature.sign();
            xSignature = Base64.encode(sign);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }



        return xSignature;
    }
}
