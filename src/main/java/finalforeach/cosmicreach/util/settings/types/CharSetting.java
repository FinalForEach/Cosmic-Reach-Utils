package finalforeach.cosmicreach.util.settings.types;

public class CharSetting extends GameSetting
{
	private final String key;
	private char value;
	
	public CharSetting(String key, char defaultValue)
	{
		this.key = key;
		Object mapping = ALL_SETTINGS.getOrDefault(key, defaultValue);
		
		if(mapping instanceof Character c) 
		{
			value = c;
		}else 
		{
			value = defaultValue;
		}
	}
	
	public char getValue() 
	{
		return value;
	}
	
	private void save() 
	{
		if(value==Character.MIN_VALUE) 
		{
			ALL_SETTINGS.remove(key);
		}else 
		{
			ALL_SETTINGS.put(key, value);
		}
		saveSettings();
	}
	
	public void setValue(char newValue) 
	{
		value = newValue;
		save();
	}
}