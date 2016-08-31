package com.gxq.tpm.tools.crypt;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** 
 * 对外提供getMD5(String)方法  结果同PHP
 * 
 */  
public class MD5 {  
      
	public static String md5(String inputStr) {
    	String md5Str = inputStr;
        if (inputStr != null) {
            try {
            	MessageDigest md = MessageDigest.getInstance("MD5");
	            md.update(inputStr.getBytes("UTF-8"));
	            BigInteger hash = new BigInteger(1, md.digest());
	            md5Str = hash.toString(16);
	            if((md5Str.length() % 2) != 0) {
	                md5Str = "0" + md5Str;
	            }
            } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
            	e.printStackTrace();
			}
        }
        return md5Str;
	}  

} 
