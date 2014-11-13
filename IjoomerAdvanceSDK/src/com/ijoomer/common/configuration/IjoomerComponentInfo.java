package com.ijoomer.common.configuration;

import java.util.HashMap;

/**
 * This Class Contains All Method Related To IjoomerComponentInfo.
 * 
 * @author tasol
 * 
 */
public class IjoomerComponentInfo {
	
	@SuppressWarnings("serial")
	public static HashMap<String, Integer> installedComponents = new HashMap<String, Integer>() {
		{
			put("com_content",1);

		}
	};

}
