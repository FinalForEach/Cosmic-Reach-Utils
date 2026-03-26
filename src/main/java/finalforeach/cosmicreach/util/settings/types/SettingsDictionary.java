package finalforeach.cosmicreach.util.settings.types;

import java.util.HashMap;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import finalforeach.cosmicreach.util.GameProperties;

public class SettingsDictionary implements Json.Serializable
{
	private HashMap<String, Object> dict = new HashMap<>();

	@Override
	public void write(Json json) 
	{
		for(var e : dict.entrySet()) 
		{
			json.writeValue(e.getKey(), e.getValue());
		}
	}

	@Override
	public void read(Json json, JsonValue jsonData) 
	{
		jsonData.forEach(c -> 
		{
			String jsonVal = c.toJson(OutputType.minimal);
			dict.put(c.name, json.fromJson(null, jsonVal));
		});
	}

	public Object getOrDefault(String key, Object defaultValue) 
	{
		if(!GameProperties.isClient && !dict.containsKey(key)) 
		{
			dict.put(key, defaultValue);
		}
		return dict.getOrDefault(key, defaultValue);
	}

	public Object put(String key, Object value)
	{
		return dict.put(key, value);
	}
	
	public Object remove(String key)
	{		
		return dict.remove(key);
	}
}
