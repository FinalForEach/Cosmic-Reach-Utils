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

	public void log(PrintStream printStream, LoggerLevel level, String message)
	{
		String fullMessage;

		synchronized (strBuilder)
		{
			strBuilder.setLength(0);
			strBuilder.append(level.messagePrefix);
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
