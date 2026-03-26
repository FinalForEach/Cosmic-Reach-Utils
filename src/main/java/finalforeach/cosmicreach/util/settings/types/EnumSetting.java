package finalforeach.cosmicreach.util.settings.types;

public class EnumSetting<T extends Enum<T>> extends GameSetting
{
	private final String key;
	private T value;
	
	@SuppressWarnings("unchecked")
	public EnumSetting(String key, T defaultValue)
	{
		this.key = key;
		Object mapping = ALL_SETTINGS.getOrDefault(key, defaultValue);
		
		if(mapping instanceof String name) 
		{
			var clazz = defaultValue.getClass();
			try 
			{
				value = Enum.valueOf((Class<T>) clazz, name);
			}catch(IllegalArgumentException ex) 
			{
				value = defaultValue;
				Enum<T>[] values = clazz.getEnumConstants();
				for(var v : values) 
				{
					if(v.name().equalsIgnoreCase(name)) 
					{
						value = (T) v;
						break;
					}
				}
			}
		}else 
		{
			value = defaultValue;
		}
	}
	
	public T getValue() 
	{
		return value;
	}
	
	private void save() 
	{
		ALL_SETTINGS.put(key, value.name());
		saveSettings();
	}
	
	public void setValue(T newValue) 
	{
		value = newValue;
		save();
	}
	
	@SuppressWarnings("unchecked")
	public void cycleNext() 
	{
		var clazz = value.getClass();
		Enum<T>[] values = clazz.getEnumConstants();
		
		value = (T) values[(value.ordinal() + 1) % values.length];
		save();
	}
}