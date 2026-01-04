package finalforeach.cosmicreach.util.logging;

public class Logger
{
	private static LoggerInstance DEFAULT_LOGGER = new LoggerInstance();

	public static void setDefaultLogger(LoggerInstance loggerInstance)
	{
		DEFAULT_LOGGER = loggerInstance;
	}

	public static LoggerInstance getDefaultLogger()
	{
		return DEFAULT_LOGGER;
	}

	public static void log(LoggerLevel level, String message)
	{
		DEFAULT_LOGGER.log(level, message);
	}

	public static void debug(Object message)
	{
		if (DEFAULT_LOGGER.atLeast(LoggerLevel.DEBUG))
			log(LoggerLevel.DEBUG, message.toString());
	}

	public static void info(Object message)
	{
		if (DEFAULT_LOGGER.atLeast(LoggerLevel.INFO))
			log(LoggerLevel.INFO, message.toString());
	}

	public static void warn(Object message)
	{
		if (DEFAULT_LOGGER.atLeast(LoggerLevel.WARNING))
			log(LoggerLevel.WARNING, message.toString());
	}

	public static void error(Object message)
	{
		if (DEFAULT_LOGGER.atLeast(LoggerLevel.ERROR))
			log(LoggerLevel.ERROR, message.toString());
	}

	public static void error(Throwable ex)
	{
		DEFAULT_LOGGER.error(ex);
	}

	public static void error(Object message, Throwable ex)
	{
		if (DEFAULT_LOGGER.atLeast(LoggerLevel.ERROR))
			log(LoggerLevel.ERROR, message.toString());
		DEFAULT_LOGGER.error(ex);
	}
}
