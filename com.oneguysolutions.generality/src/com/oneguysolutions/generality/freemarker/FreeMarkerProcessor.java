package com.oneguysolutions.generality.freemarker;

import java.io.File;
import java.io.IOException;

import freemarker.template.Configuration;

public class FreeMarkerProcessor {
	
	public final static String TEMPLATES_DIR = "workspace/";
	
	private static Configuration cfg; 
	
	static public Configuration getConfig(){
		if(cfg == null){
			cfg = new Configuration();
	        try {
	        	System.out.println(new File(TEMPLATES_DIR).getAbsolutePath());
				cfg.setDirectoryForTemplateLoading(
						new File(TEMPLATES_DIR)
				);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return cfg;
	}
	
	static public void setConfig(Configuration conf){
		cfg = conf;
	}
}
