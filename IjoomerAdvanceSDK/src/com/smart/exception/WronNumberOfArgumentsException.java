package com.smart.exception;

@SuppressWarnings("serial")
public class WronNumberOfArgumentsException extends Exception {

	@Override
	public String toString() {
		return "com.smart.Exception:WronNumberOfArgumentsException: Argument Missing";
	}
}
