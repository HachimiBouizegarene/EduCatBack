package org.hachimi.eduCat.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

@Configuration
public class AESConfiguration {


    @Value("${aes.secret.key}")
    private String aesSecretKey;


    private byte[] generateRandomKey() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        return key;
    }

    @Bean
    public Cipher aesCipher() throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(aesSecretKey.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher;
    }

    @Bean
    public Cipher aesDecipher() throws Exception {
        Cipher decipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(aesSecretKey.getBytes(), "AES");
        decipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return decipher;
    }


}
