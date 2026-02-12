package finalforeach.cosmicreach.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class GameAssetLoaderUtils
{
	public static final ArrayList<String> defaultAssetList = new ArrayList<>();
	public static final ArrayList<String> defaultNamespaceList = new ArrayList<>();

	public static <ASSET_TYPE, FILEHANDLE> ASSET_TYPE get(FILEHANDLE assetFile, HashMap<FILEHANDLE, ASSET_TYPE> map,
			Function<FILEHANDLE, ASSET_TYPE> instantiator)
	{
		if (map.containsKey(assetFile))
		{
			return map.get(assetFile);
		}
		ASSET_TYPE asset = instantiator.apply(assetFile);
		map.put(assetFile, asset);
		return asset;
	}

	public static Set<String> getAllNamespaces()
	{
		Set<String> namespaces = new HashSet<String>();
        namespaces.addAll(defaultNamespaceList);
		String modAssetFolder = SaveLocation.getSaveFolderLocation() + "/mods/";
		File modAssetRoot = new File(modAssetFolder);
		if(!modAssetRoot.exists()) 
		{
			return namespaces;
		}
		File[] l = modAssetRoot.listFiles();
		if(l == null) 
		{
			return namespaces;
		}

		for (var f : l)
		{
			if (f.isDirectory() && !f.getName().contains(":"))
			{
				namespaces.add(f.getName());
			}
		}
		return namespaces;
	}

	public static void addAssetList(String[] lines)
	{
		for(String line : lines) 
		{
			if(!defaultAssetList.contains(line)) 
			{
				defaultAssetList.add(line);	
			}
		}
	}

	public static void addAssetList(ArrayList<String> lines)
	{
		addAssetList(lines.toArray(String[]::new));
	}

	public static void addNamespaceList(String[] lines)
	{
		for(String line : lines)
		{
			addNamespace(line);
		}
	}

	public static void addNamespaceList(ArrayList<String> lines)
	{
		for(String line : lines)
		{
			addNamespace(line);
		}
	}

	public static void addNamespace(String namespace)
	{
		if(!defaultNamespaceList.contains(namespace))
		{
			defaultNamespaceList.add(namespace);
		}
	}

	static
	{
		defaultNamespaceList.add("base");
	}
}
