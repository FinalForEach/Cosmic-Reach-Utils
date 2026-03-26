package finalforeach.cosmicreach.util.lang;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;

import finalforeach.cosmicreach.util.assets.GameAssetLoader;
import finalforeach.cosmicreach.util.assets.GameAssetLoaderUtils;
import finalforeach.cosmicreach.util.logging.Logger;
import finalforeach.cosmicreach.util.settings.Preferences;

public class Lang implements Json.Serializable
{
	public static Lang gameDefaultLang;
	public static Lang currentLang;
	private static String defaultLangTag = "en_us";
	private static Array<Lang> languages = new Array<>();
	private String langTag;
	private String name;
	private Map<String, String> mappedStrings = new HashMap<>();
	private Array<Lang> fallBackLanguages;
	private String[] fallbackTags;
	private DecimalFormat percentFormatter;

	@Override
	public String toString()
	{
		return getLangTag();
	}

	public String getLangTag()
	{
		return langTag;
	}

	static ObjectSet<String> loadedLangFolders = new ObjectSet<String>();

	public static void loadLanguages(boolean loadAll)
	{
		Json json = new Json();
		String chosenLangTag = Preferences.chosenLang.getValue();
		String prefix = "lang";
		if (!loadAll && chosenLangTag != null)
		{
			prefix = "lang/" + chosenLangTag;
		}

		ObjectSet<String> langFolders = new ObjectSet<String>();

		Logger.info("Finding languages...");

		String start = "base:lang/";
		String end = "/game.json";
		GameAssetLoader.forEachAsset(prefix, ".json", (p, _) -> {
			if (!p.startsWith(start) || !p.endsWith(end))
			{
				return;
			}
			if (!loadedLangFolders.contains(p))
			{
				langFolders.add(p);
				loadedLangFolders.add(p);
			}
		});

		Logger.info("Loading language files...");
		for (String langPath : langFolders)
		{
			String langTag = langPath.replace(start, "").replace(end, "");
			loadLang(json, langTag, langPath.replace(end, ""), GameAssetLoader.loadAsset(langPath));
		}

		Logger.info("Calculating language fallbacks...");
		for (var l : languages)
		{
			l.calculateFallbacks(new ObjectSet<>());
		}

		languages.sort((a, b) -> a.langTag.compareTo(b.langTag));
	}

	private static void loadLang(Json json, String langTag, String langPath, FileHandle langGameFile)
	{
		Lang lang = json.fromJson(Lang.class, langGameFile);
		lang.langTag = langTag;
		if (lang.langTag.equalsIgnoreCase(Preferences.chosenLang.getValue()))
		{
			currentLang = lang;
		}
		if (lang.langTag.equalsIgnoreCase(defaultLangTag))
		{
			gameDefaultLang = lang;
		}
		languages.add(lang);

		for (String namespace : GameAssetLoaderUtils.getAllNamespaces())
		{
			GameAssetLoader.forEachAsset(namespace + ":lang/" + langTag, "", (_, f) -> {
				if (f.path().equalsIgnoreCase(langGameFile.path()))
				{
					return;
				}

				var jsonValue = GameAssetLoader.loadJson(f);
				var child = jsonValue.child;
				while (child != null)
				{
					lang.mappedStrings.put(child.name, child.asString());
					child = child.next;
				}
			}, true);
		}
	}

	private void calculateFallbacks(ObjectSet<Lang> calculatingSet)
	{
		if (fallbackTags == null)
		{
			if (fallBackLanguages == null)
			{
				fallBackLanguages = new Array<>(0);
			}
			return;
		}
		Array<Lang> workingFallbackLangs = new Array<>();
		calculatingSet.add(this);
		for (var f : fallbackTags)
		{
			Lang l = getLangByTag(f);
			if (l == null)
				continue;
			if (!calculatingSet.contains(l))
			{
				l.calculateFallbacks(calculatingSet);
			}
			workingFallbackLangs.add(l);
			if (l.fallBackLanguages != null)
			{
				for (var ff : l.fallBackLanguages)
				{
					workingFallbackLangs.add(ff);
				}
			}
		}
		fallBackLanguages = workingFallbackLangs;
	}

	public static Lang getLangByTag(String tag)
	{
		for (int l = 0; l < languages.size; l++)
		{
			Lang lang = languages.get(l);
			if (lang.langTag.equalsIgnoreCase(tag))
			{
				return lang;
			}
		}
		return null;
	}

	public Lang()
	{
	}

	public static String get(Lang lang, String key)
	{
		if (lang == null)
		{
			return key;
		}
		return lang.getMappedString(key, true);
	}

	public static String get(String key)
	{
		return get(currentLang, key);
	}

	public static String get(String key, Object... args)
	{
		String s = get(key);
		for (int i = 0; i < args.length; i++)
		{
			s = s.replace("{" + i + "}", args[i].toString());
		}
		return s;
	}

	public String getMappedString(String key, boolean checkFallbacks)
	{
		String s = mappedStrings.get(key);

		if (s != null)
		{
			return s;
		}

		if (checkFallbacks)
		{
			for (var f : fallBackLanguages)
			{
				s = f.getMappedString(key, false);
				if (s != null)
					return s;
			}
			if (gameDefaultLang != null)
			{
				s = gameDefaultLang.getMappedString(key, false);
				if (s != null)
					return s;
			}
			return key;
		} else
		{
			return null;
		}
	}

	public String getName()
	{
		return name;
	}

	@Override
	public void write(Json json)
	{
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		var value = jsonData.child;
		while (value != null)
		{
			if (value.name.equals("$schema"))
			{
				value = value.next;
				continue; // No need to store the schema here.
			} else if (value.name.equals("metadata"))
			{
				var metadataValue = value.child;
				while (metadataValue != null)
				{
					switch (metadataValue.name)
					{
					case "name":
						name = metadataValue.asString();
						break;
					case "fallbacks":
						fallbackTags = metadataValue.asStringArray();
						break;
					default:
						break;
					}
					metadataValue = metadataValue.next;
				}
			} else
			{
				mappedStrings.put(value.name, value.asString());
			}
			value = value.next;
		}
	}

	public static Array<Lang> getLanguages()
	{
		return languages;
	}

	public boolean isCurrentLanguage()
	{
		return this == currentLang;
	}

	public void select()
	{
		currentLang = this;
		Preferences.chosenLang.setValue(langTag);
	}

	public static DecimalFormat getPercentFormatter()
	{
		return getPercentFormatter(currentLang);
	}

	public static DecimalFormat getPercentFormatter(Lang lang)
	{
		if (lang.percentFormatter == null)
		{
			String percentFormat = get("formatPercent");
			if (percentFormat == null)
			{
				percentFormat = "#%";
			}
			lang.percentFormatter = new DecimalFormat(percentFormat);
		}
		return lang.percentFormatter;
	}

	public static String getOnOff(boolean isOn)
	{
		if (isOn)
		{
			return get("on_state");
		}
		return get("off_state");
	}
}
