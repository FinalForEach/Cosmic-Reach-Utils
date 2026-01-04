package finalforeach.cosmicreach.util;

public class OSInfo
{
	private static final String osName = System.getProperty("os.name").toLowerCase();
	public static final boolean isMac = osName.contains("mac");
	public static final boolean isWindows = osName.contains("windows");
}
