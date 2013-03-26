package com.smart.framework;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

public class SmartFrameworkSecurity {
	
	private Context context;
	private PackageManager packageManager;
	
	public SmartFrameworkSecurity(Context context) {
		this.context = context;
	}
	
	public boolean matchKey(String key) {
		return ((md5(getApplicationSignature()).equals(key)) ? true : false);
	}
	
	private String getApplicationSignature() {
		String strSignature = "";
		packageManager = context.getPackageManager();
        try {
            Signature[] signs = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
            for (Signature signature : signs) {
//                Log.d("Test Signature = ", "sign = " + signature.toCharsString());
                strSignature = signature.toCharsString();
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        
        return strSignature;
        
	}
	
	public boolean isDebugKey() {
		boolean debug = false;
		X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");
        /* ... */
        Signature raw;
		try {
			raw = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures[0];
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
	        X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(raw.toByteArray()));
	        debug = cert.getSubjectX500Principal().equals(DEBUG_DN);
	        
//	        Log.d("Test Signature = ", "sign = " + ((debug) ? "YES" : "NO"));
	        
		} catch (NameNotFoundException e) {
			e.printStackTrace();
	    } catch (CertificateException e) {
			e.printStackTrace();
		}
	    
	    return debug;
	}
	
	private String md5(String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();
	        
	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();
	        
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
}
