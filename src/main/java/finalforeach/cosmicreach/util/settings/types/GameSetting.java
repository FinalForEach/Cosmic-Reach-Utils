package finalforeach.cosmicreach.util.settings.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import finalforeach.cosmicreach.util.SaveLocation;

public class GameSetting
{
	public static final String SETTINGS_FILE_NAME = "gameSettings.json";
	protected static final SettingsDictionary ALL_SETTINGS = new SettingsDictionary();
	private final static Json JSON = new Json();

	static
	{
		loadSettings();
	}

	public static void setSetting(String key, Object value)
	{
		ALL_SETTINGS.put(key, value);
	}

	public static Object getSetting(String key, Object value, Object defaultValue)
	{
		return ALL_SETTINGS.getOrDefault(key, defaultValue);
	}

	public static void loadSettings()
	{
		File f = new File(SaveLocation.getSaveFolderLocation() + "/" + SETTINGS_FILE_NAME);

		if (!f.exists())
		{
			return;
		}
		try (FileInputStream fis = new FileInputStream(f))
		{
			ALL_SETTINGS.read(JSON, new JsonReader().parse(fis));
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void saveSettings()
	{
		JSON.setOutputType(OutputType.json);
		String jsonStr = JSON.prettyPrint(ALL_SETTINGS);

		File f = new File(SaveLocation.getSaveFolderLocation() + "/" + SETTINGS_FILE_NAME);
		try (FileOutputStream fos = new FileOutputStream(f))
		{
			fos.write(jsonStr.getBytes(StandardCharsets.UTF_8));
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}