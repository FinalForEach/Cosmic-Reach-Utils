package finalforeach.cosmicreach.util.settings.types;

public class BooleanSetting extends GameSetting
{
	private final String key;
	private boolean value;
	
	public BooleanSetting(String key, boolean defaultValue)
	{
		this.key = key;
		Object mapping = ALL_SETTINGS.getOrDefault(key, defaultValue);
		
		if(mapping instanceof Boolean) 
		{
			value = (boolean) mapping;
		}else 
		{
			value = defaultValue;
		}
	}
	
	public boolean getValue() 
	{
		return value;
	}

	public boolean isTrue() 
	{
		return getValue();
	}
	
	public boolean isFalse() 
	{
		return !getValue();
	}
	
	private void save() 
	{
		ALL_SETTINGS.put(key, value);
		saveSettings();
	}
	
	public void setValue(boolean newValue) 
	{
		value = newValue;
		save();
	}
	
	public void toggleValue() 
	{
		value = !value;
		save();
	}
}