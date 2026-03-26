package finalforeach.cosmicreach.util.settings.types;


public class IntSetting extends GameSetting implements INumberSetting
{
	private final String key;
	private int value;
	
	public IntSetting(String key, int defaultValue)
	{
		this.key = key;
		Object mapping = ALL_SETTINGS.getOrDefault(key, defaultValue);
		
		if(mapping instanceof Number n) 
		{
			value = n.intValue();
		}else 
		{
			value = defaultValue;
		}
	}
	
	public int getValue() 
	{
		return value;
	}
	
	private void save() 
	{
		ALL_SETTINGS.put(key, value);
		saveSettings();
	}
	
	public void setValue(int newValue) 
	{
		value = newValue;
		save();
	}

	@Override
	public float getValueAsFloat() 
	{
		return getValue();
	}

	@Override
	public void setValue(float newValue) {
		setValue((int)newValue);
	}
}