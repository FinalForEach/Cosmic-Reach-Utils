package finalforeach.cosmicreach.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public final class SaveLocation 
{
	public static String gameDirectory = "cosmic-reach";
	public static String saveLocationOverride;
	
	public static void OpenFolderWithFileManager(File folder) throws IOException
	{
		if(!folder.isDirectory()) 
		{
			throw new RuntimeException("`" + folder + "` is not a directory! Does exist: " + folder.exists());
		}
		try 
		{
			if(OSInfo.isMac) 
			{
				// Avoids Macs freezing using Destkop.open()
				Runtime.getRuntime().exec(new String[] { "open", folder.getAbsolutePath() });
				return;
			}
			else if(Desktop.isDesktopSupported()) 
			{
				Desktop.getDesktop().open(folder);	
			}else 
			{
				throw new RuntimeException("java.awt.Desktop is not supported");
			}
		}catch(Throwable ex)
		{
			// Fixes a windows-specific bug with the JDK
			if(OSInfo.isWindows) 
			{
				//Runtime.getRuntime().exec("explorer.exe \"" + folder.getAbsolutePath() + "\"");
				// TODO: Check if this works, if not, uncomment the above and ignore the deprecation warning
				Runtime.getRuntime().exec(new String[] { "explorer.exe", folder.getAbsolutePath() });
				return;
			}
			throw ex;
		}
	}
	public static File getSaveFolder()
	{
		File dir = new File(getSaveFolderLocation());
		dir.mkdirs();
		return dir;
	}
	public static File getPlayerSkinsFolder()
	{
		File dir = new File(getPlayerSkinsFolderLocation());
		dir.mkdirs();
		return dir;
	}
	
	public static String getSaveFolderLocation()
	{		
		if(saveLocationOverride != null) 
		{
			return saveLocationOverride;
		}
		String rootFolder;
		//XDG_DATA_HOME
		if(OSInfo.isWindows) 
		{
			rootFolder = System.getenv("LOCALAPPDATA");
		}else if(OSInfo.isMac)
		{
			rootFolder = System.getenv("HOME") + "/Library";
		}else 
		{
			rootFolder = System.getenv("XDG_DATA_HOME");
			if(rootFolder == null || rootFolder.isEmpty()) 
			{
				rootFolder = System.getenv("HOME") + "/.local/share";
			}
		}
		String saveFolder = Paths.get(rootFolder).resolve(gameDirectory).toString();
		File dir = new File(saveFolder);
		dir.mkdirs();
		saveLocationOverride = saveFolder;
		return saveFolder;
	}
	
	public static String getScreenshotFolderLocation() 
	{
		return getSaveFolderLocation() + "/screenshots";
	}
	
	public static String getPlayerSkinsFolderLocation()
	{
		return getSaveFolderLocation() + "/skins";
	}
	
	public static String getWorldSaveFolderLocation(String worldFolderName) 
	{
		return getAllWorldsSaveFolderLocation() + "/" + worldFolderName;
	}
	
	public static String getAllWorldsSaveFolderLocation() 
	{
		String rootFolderName = SaveLocation.getSaveFolderLocation();
		return rootFolderName+"/worlds";
	}
}
