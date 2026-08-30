package finalforeach.cosmicreach.util;

public final class FileUtils 
{
	public static String getFileSafeName(String desiredFileName)
	{
		String safe = desiredFileName.replaceAll("[^\\p{L}\\p{N}._ -]", "_");
		return safe.substring(0, Math.min(255, safe.length()));
	}
}
