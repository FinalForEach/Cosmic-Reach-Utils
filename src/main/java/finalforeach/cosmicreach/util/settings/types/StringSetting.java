package finalforeach.cosmicreach.util.settings.types;

public class StringSetting extends GameSetting
{
	private final String key;
	private String value;
	
	public StringSetting(String key, String defaultValue)
	{
		this.key = key;
		Object mapping = ALL_SETTINGS.getOrDefault(key, defaultValue);
		
		if(mapping instanceof String s) 
		{
			value = s;
		}else 
		{
			value = defaultValue;
		}
	}
	
	public String getValue() 
	{
		return value;
	}
	
	private void save() 
	{
		if(value == null) 
		{
			ALL_SETTINGS.remove(key);
		}else 
		{
			ALL_SETTINGS.put(key, value);
		}
		saveSettings();
	}
	
	public void setValue(String newValue) 
	{
		value = newValue;
		save();
	}
}