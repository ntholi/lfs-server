package com.breakoutms.lfs.server.config;

import java.io.IOException;
import java.util.Properties;

import com.breakoutms.lfs.server.core.Constants;
import com.breakoutms.lfs.server.util.FileUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ConfigFile {
	
	private static String branchName;
	
	private ConfigFile() {}
	
	private static Properties getProperties() {
		Properties props = null;
		try {
			log.debug("Loading Properties file from "+ Constants.CONFIG_FILE);
			props = FileUtils.readProperties(Constants.CONFIG_FILE);
			log.debug("Properties file '"+ Constants.CONFIG_FILE+"' loaded successfully");
		} catch (IOException e) {
			log.error("Error loading properties file "+Constants.CONFIG_FILE, e);
		}
		return props;
	}

	public static String getBranchName() { 
		if(branchName == null) {
			Properties props = getProperties();
			if(props != null) {
				log.debug("reading value of "+Constants.BRANCH_NAME_PROP+"from properties:"+ props);
				branchName = props.getProperty(Constants.BRANCH_NAME_PROP);
				if(branchName == null) {
					log.fatal("Unable to get value of "+Constants.BRANCH_NAME_PROP+"from properties:"+ props);
				}
			}
		}
		return branchName;
	}
	
	public static String getConfigFileName() {
		return Constants.CONFIG_FILE;
	}
}
