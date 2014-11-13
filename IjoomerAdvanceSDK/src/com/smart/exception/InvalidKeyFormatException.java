package com.smart.exception;

@SuppressWarnings("serial")
public class InvalidKeyFormatException extends Exception{


	@Override
	public String toString() {
		return "com.smart.Exception:InvalidKeyFormatException :Key must not be NULL or EMPTY and Key must be String , e.g : \"testKey\" ";
	}
}
