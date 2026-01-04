package finalforeach.cosmicreach.util;

public final class FileUtils 
{
	public static String getFileSafeName(String desiredFileName)
	{
		return desiredFileName
			.replaceAll("[\\\\/:\\*\\?\"<>|\\x00-\\x1F;]", "_")
			.substring(0, Math.min(255, desiredFileName.length()));
	}
}
