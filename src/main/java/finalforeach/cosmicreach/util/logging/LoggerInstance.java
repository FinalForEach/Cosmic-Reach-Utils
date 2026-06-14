package finalforeach.cosmicreach.util.logging;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerInstance
{
	public final static DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm.ss: ");
	private final StringBuilder strBuilder = new StringBuilder();
	public DateTimeFormatter formatter = defaultFormatter;
	public boolean debug = true;
	public LoggerLevel loggerLevel = LoggerLevel.DEBUG;
	private String version;
	private final String[] levelPrefixes = new String[LoggerLevel.values().length];

	{
		updatePrefixes();
	}

	public void setVersion(String version)
	{
		synchronized (strBuilder)
		{
			this.version = version;
			updatePrefixes();
		}
	}

	public String getVersion()
	{
		return version;
	}

	private void updatePrefixes()
	{
		for (LoggerLevel level : LoggerLevel.values())
		{
			if (version != null && !version.isEmpty())
			{
				levelPrefixes[level.ordinal()] = "[" + version + "|" + level.name() + "] ";
			}
			else
			{
				levelPrefixes[level.ordinal()] = level.messagePrefix;
			}
		}
	}

	public void log(PrintStream printStream, LoggerLevel level, String message)
	{
		String fullMessage;

		synchronized (strBuilder)
		{
			strBuilder.setLength(0);
			strBuilder.append(levelPrefixes[level.ordinal()]);
			strBuilder.append(LocalDateTime.now().format(formatter));
			strBuilder.append(message);
			fullMessage = strBuilder.toString();
		}
		printStream.println(fullMessage);
	}

	public void log(LoggerLevel level, String message)
	{
		switch (level)
		{
		case DEBUG:
		case INFO:
			log(System.out, level, message);
			break;
		case WARNING:
		case ERROR:
			log(System.err, level, message);
			break;
		}
	}

	public void error(Throwable ex)
	{
		ex.printStackTrace();
	}

	public boolean atLeast(LoggerLevel logLevel)
	{
		return this.loggerLevel.level <= logLevel.level;
	}
}
