package finalforeach.cosmicreach.util.settings.types;


public class FloatSetting extends GameSetting implements INumberSetting
{
	private final String key;
	private float value;
	
	public FloatSetting(String key, float defaultValue)
	{
		this.key = key;
		Object mapping = ALL_SETTINGS.getOrDefault(key, defaultValue);
		
		if(mapping instanceof Number n) 
		{
			value = n.floatValue();
		}else 
		{
			value = defaultValue;
		}
	}
	
	public float getValue() 
	{
		return value;
	}
	
	private void save() 
	{
		ALL_SETTINGS.put(key, value);
		saveSettings();
	}
	
	public void setValue(float newValue) 
	{
		value = newValue;
		save();
	}

	@Override
	public float getValueAsFloat() 
	{
		return getValue();
	}

	public int getValueAsInt()
	{
		return (int)getValue();
	}
}