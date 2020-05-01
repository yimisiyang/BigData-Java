package com.mlamp;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class RSADecoder {

    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";
    public static final String RSA_ALGORITHM_SIGN = "SHA256WithRSA";
    private static final String pKey = "MIICsw";

    private RSAPrivateKey privateKey;

    public RSADecoder(){
        try{
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            //通过PKCS#8编码的Key指令获得私钥对象
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(pKey));
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        }catch (Exception e){
            throw new RuntimeException("不支持的密钥",e);
        }
    }

    public String privateDecrypt(String data){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,privateKey);
            return new String(rsaSplitCodec(cipher,Cipher.DECRYPT_MODE, Base64.decodeBase64(data),
                    privateKey.getModulus().bitLength()), CHARSET);
        }catch (Exception e){
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常",e);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher,int opmode, byte[] datas, int keySize){
        int maxBlock = 0;
        if(opmode == Cipher.DECRYPT_MODE){
            maxBlock = keySize /8;
        }else{
            maxBlock = keySize / 8 -11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try{
            while(datas.length > offSet){
                if(datas.length - offSet > maxBlock){
                    buff = cipher.doFinal(datas,offSet,maxBlock);
                }else{
                    buff = cipher.doFinal(datas,offSet,datas.length-offSet);
                }
                out.write(buff,0,buff.length);
                i++;
                offSet = i * maxBlock;
            }
        }catch (Exception e){
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常",e);
        }
        byte[] resultDatas = out.toByteArray();
        //IOUtils.closeQuietly(out);
        return resultDatas;
    }
}
