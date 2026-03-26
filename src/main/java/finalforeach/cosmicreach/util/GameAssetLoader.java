package finalforeach.cosmicreach.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import finalforeach.cosmicreach.util.logging.AnsiColours;
import finalforeach.cosmicreach.util.logging.Logger;

public class GameAssetLoader
{
	private static WeakHashMap<Identifier, FileHandle> locationCache = new WeakHashMap<Identifier, FileHandle>();
	private static final JsonReader JSON_READER = new JsonReader();

	static
	{
		GameAssetLoaderUtils.addAssetList(Gdx.files.internal("assets.txt").readString().split("\n"));
	}

	private static final HashMap<String, FileHandle> ALL_ASSETS = new HashMap<>();

	public static <ASSET_TYPE> ASSET_TYPE getAssetOfType(GameAssetCache<ASSET_TYPE> assetCache, String fileName)
	{
		return GameAssetLoaderUtils.get(loadAsset(fileName), assetCache.allAssetsOfType, assetCache.instantiator);
	}

	public static <ASSET_TYPE> ASSET_TYPE getAssetOfType(GameAssetCache<ASSET_TYPE> assetCache, Identifier location)
	{
		return GameAssetLoaderUtils.get(loadAsset(location), assetCache.allAssetsOfType, assetCache.instantiator);
	}

	public static JsonValue loadJson(String path)
	{
		return JSON_READER.parse(loadAsset(path));
	}

	public static JsonValue loadJson(Identifier identifier)
	{
		return JSON_READER.parse(loadAsset(identifier));
	}

	public static JsonValue loadJson(FileHandle fileHandle)
	{
		return JSON_READER.parse(fileHandle);
	}

	public static FileHandle loadAsset(String fileName)
	{
		return loadAsset(fileName, true);
	}

	public static FileHandle loadAsset(String fileName, boolean warnIfNull)
	{
		FileHandle asset = ALL_ASSETS.get(fileName);
		if (asset != null)
		{
			return asset;
		}

		synchronized (ALL_ASSETS)
		{
			asset = ALL_ASSETS.get(fileName);
			if (asset != null)
			{
				return asset;
			}
			Identifier location = Identifier.of(fileName);
			asset = loadAsset(location, warnIfNull);
			ALL_ASSETS.put(fileName, asset);
		}

		return asset;
	}

	public static void clearAssetCache()
	{
		locationCache.clear();
	}

	public static FileHandle loadAsset(Identifier location)
	{
		return loadAsset(location, true);
	}

	public static FileHandle loadAsset(Identifier location, boolean warnIfNull)
	{
		var cached = locationCache.get(location);
		if (cached != null)
		{
			return cached;
		}

		FileHandle modLocationFile = Gdx.files
				.absolute(SaveLocation.getSaveFolderLocation() + "/mods/" + location.toPath());
		if (modLocationFile.exists())
		{
			Logger.info("Loading " + AnsiColours.CYAN + "\"" + location.getName() + "\"" + AnsiColours.RESET
					+ " from Mods Folder");
			locationCache.put(location, modLocationFile);
			return modLocationFile;
		}

		FileHandle classpathLocationFile = Gdx.files
				.classpath("assets/%s/%s".formatted(location.getNamespace(), location.getName()));
		if (classpathLocationFile.exists())
		{
			Logger.info("Loading " + AnsiColours.PURPLE + "\"" + location.getName() + "\"" + AnsiColours.RESET
					+ " from Java Mod " + AnsiColours.GREEN + "\"" + location.getNamespace() + "\""
					+ AnsiColours.WHITE);
			locationCache.put(location, classpathLocationFile);
			return classpathLocationFile;
		}

		FileHandle vanillaLocationFile = Gdx.files.internal(location.toPath());
		if (vanillaLocationFile.exists())
		{
			Logger.info("Loading " + AnsiColours.YELLOW + "\"" + location.getName() + "\"" + AnsiColours.RESET
					+ " from the base game");
			locationCache.put(location, vanillaLocationFile);
			return vanillaLocationFile;
		}

		if (warnIfNull)
		{
			Logger.warn("Cannot find the resource " + location + " (Expected path: " + location.toPath() + ")");
		}
		return null;
	}

	public static void forEachAsset(String prefix, String extension, BiConsumer<String, FileHandle> assetConsumer)
	{
		forEachAsset(prefix, extension, assetConsumer, false);
	}

	public static void forEachAsset(String prefix, String extension, BiConsumer<String, FileHandle> assetConsumer,
			boolean includeDirectories)
	{
		Set<Identifier> allPaths = new HashSet<>();
		Set<Identifier> moddedPaths = new HashSet<>();

		for (var assetPath : GameAssetLoaderUtils.defaultAssetList)
		{
			String[] splitPath = assetPath.split("/");
			if (assetPath.startsWith(splitPath[0] + "/" + prefix.replaceFirst(splitPath[0] + ":", ""))
					&& assetPath.endsWith(extension))
			{
				allPaths.add(Identifier.of(splitPath[0], assetPath.replaceFirst(splitPath[0] + "/", "")));
			}
		}

		String modAssetFolder = SaveLocation.getSaveFolderLocation() + "/mods/";
		String modAssetRoot = Gdx.files.absolute(modAssetFolder).path().replace("\\", "/");
		for (var modFolder : Gdx.files.absolute(modAssetRoot).list())
		{
			if (!modFolder.isDirectory() && !includeDirectories)
			{
				continue;
			}

			String postPrefix = prefix;

			if (prefix.startsWith(modFolder.file().getName() + ":"))
			{
				postPrefix = prefix.substring(modFolder.file().getName().length() + 1);
			}

			String modPrefix = modFolder + "/" + postPrefix;
			modPrefix = modPrefix.replace("\\", "/");
			Array<FileHandle> assetList = new Array<>(Gdx.files.absolute(modPrefix).list());
			while (!assetList.isEmpty())
			{
				var asset = assetList.pop();
				var assetPath = asset.path().replace("\\", "/").replace(modAssetRoot, "");
				if (assetPath.startsWith("/"))
				{
					assetPath = assetPath.substring(1);
				}
				String namespace = modFolder.name();
				String name = assetPath.replaceFirst(Pattern.quote(namespace + "/"), "");
				if (name.startsWith(postPrefix) && assetPath.endsWith(extension))
				{
					moddedPaths.add(Identifier.of(namespace, name));
				}

				if (asset.isDirectory())
				{
					assetList.addAll(asset.list());
				}
			}
		}

		allPaths.addAll(moddedPaths);
		for (Identifier path : allPaths)
		{
			assetConsumer.accept(path.toString(), loadAsset(path));
		}
	}
}
