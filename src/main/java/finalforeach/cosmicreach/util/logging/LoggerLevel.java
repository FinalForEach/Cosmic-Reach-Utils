package finalforeach.cosmicreach.util.logging;

public enum LoggerLevel
{
	DEBUG(0), INFO(1), WARNING(2), ERROR(3);

	final String messagePrefix;
	final int level;

	LoggerLevel(int level)
	{
		this.messagePrefix = "[" + name() + "] ";
		this.level = level;
	}
}