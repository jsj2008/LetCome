package com.gxq.tpm.tools.crypt;

import java.io.UnsupportedEncodingException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

import com.gxq.tpm.tools.Print;

public class HmacSHA1 {
	private static final String Tag	= "HmacSHA1";
	public static String key    	= "12345ba035678685a46adee051dca85be88eakey";
	public static String strToSign 	= "X_QFGJ_SID+X_QFGJ_UID+X_QFGJ_DID+X_QFGJ_RID+X_QFGJ_RTIME+X_QFGJ_CONTENTMD5+123456";

	public static String signature(String input_key, String input_toHash) {
		if (!input_key.isEmpty())
			key = input_key;
		if (!input_toHash.isEmpty())
			strToSign = input_toHash;
		// String toHashUtf8 = URLEncoder.encode(toHash, "UTF-8");
		
		Print.d(Tag, "key=" + key);
		Print.d(Tag, "strToSign=" + strToSign);
		// System.out.print(res+"\n");

		String signature = "";
		try {
			String res = hmac_sha1(strToSign, key);
			signature = new String(Base64.encode(res.getBytes(), Base64.DEFAULT), "UTF-8");
			Print.i(Tag, "sign=" + signature);
			signature = replaceEqualSign(signature);
			Print.i(Tag, "sign=" + signature);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace( );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signature;

	}

	public static String hmac_sha1(String value, String key) {
		try {
			// Get an hmac_sha1 key from the raw key bytes
			byte[] keyBytes = key.getBytes();
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

			// Get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);

			// Compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(value.getBytes("UTF-8"));

			// Convert raw bytes to Hex
			String hexBytes;
			hexBytes = byte2hex(rawHmac);

			// BigInteger hash = new BigInteger(1, rawHmac);
			// hexBytes = hash.toString(16);
			return hexBytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String byte2hex(final byte[] b)
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++)
		{
			stmp = (java.lang.Integer.toHexString(b[n] & 0xFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}

	private static String replaceEqualSign(String s) {
		// int len = s.length();
		// int appendNum = 8 - (int)(len/8);
		// for (int n=0; n<appendNum; n++){
		// s += "%3D";
		// }
		s = s.replace("=", "%3D");
		return s;
	}
}
