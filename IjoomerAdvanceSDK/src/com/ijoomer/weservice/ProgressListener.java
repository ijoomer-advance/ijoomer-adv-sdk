package com.ijoomer.weservice;

/**
 * A Interface used by {@link CustomMultiPartEntity} to notify call progress byte by byte transfer.
 * @author tasol
 *
 */
public interface ProgressListener {
	
	/**
	 * A method call by {@link CustomMultiPartEntity} to notify call progress byte by byte transfer.
	 * @param num represent call progress (1-100)
	 */
	void transferred(long num);

}
