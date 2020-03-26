package lfs.server.common;

import java.io.File;
import java.util.prefs.Preferences;

public final class Constants {
	
	public static final String BRANCH_KEY = "BRANCH_KEY";
	public static final String USER_KEY = "USER_KEY";
	public static final String CONFIG_FILE;
	public static final String BRANCH_NAME_PROP = "branch-name";
	public static final String APP_DIRECTORY;
	public static final String CREDENTIALS_PATH;
	public static final String MORTUARY_OVERSTAY = "MORTUARY_OVERSTAY";
	public static final String SELECTED_AUDIT_PERIOD = "SELECTED_AUDIT_PERIOD";
	public static final String PAGE_SIZE = "FXModelController_PAGE_SIZE";
	
	public static final String CURRENT_VERSION = "Unknown";
	
	static {
		StringBuilder path = new StringBuilder(System.getProperty("user.home"))
				.append(File.separator)
				.append(".lfs_desktop")
				.append(File.separator);
		APP_DIRECTORY = path.toString();
		CONFIG_FILE  = path +"config";
		CREDENTIALS_PATH = path.toString()+"credentials";
	}
	
	private Constants() {}
	
	private static Preferences prefs;
	public static Preferences getPereferences() {
		if(prefs == null) {
			prefs = Preferences.userRoot().node("lfs_desktop_preferences");
		}
		return prefs;
	}
}
