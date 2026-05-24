package finalforeach.cosmicreach.util.settings;

import finalforeach.cosmicreach.util.settings.types.StringSetting;

public class Preferences 
{
	public static final StringSetting chosenLang = new StringSetting("chosenLangTag", null);
	public static final StringSetting chosenSkin = new StringSetting("chosenSkin", "default");
}
