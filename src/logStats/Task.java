package logStats;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import model.LogLine;
import model.UserStats;

public class Task {

	private final static String LOGLINE_REGEX = "^(\\([0-9]+\\) [0-9]+/[0-9]+/[0-9]+ [0-9]+:[0-9]+:[0-9]+ - [a-zA-ZñÑ]+).*";

	public static List<LogLine> readLogFile(String logFile) {

		List<String> lines;
		List<LogLine> logLines = new ArrayList<LogLine>();

		try {
			lines = Files.readAllLines(Paths.get(logFile));
			lines.removeIf(line -> (!line.matches(LOGLINE_REGEX)));

			for (String line : lines)
				logLines.add(new LogLine(line));

		} catch (IOException e) {
			e.printStackTrace();
		}

		return logLines;
	}

	public static List<LogLine> loadAllLogLines(String logDirectoryPath) {

		File logDirectory = new File(logDirectoryPath);
		List<File> fileNames = Arrays.asList(logDirectory.listFiles(file -> file.getName().endsWith(".log")));
		List<LogLine> logLines = new ArrayList<LogLine>();

		for (File file : fileNames)
			logLines.addAll(readLogFile(file.getAbsolutePath()));

		return logLines;
	}

	public static List<UserStats> generateUserStats(List<LogLine> logLines) {
		List<UserStats> userStats = new ArrayList<UserStats>();
		int index;

		for (LogLine logLine : logLines) {
			index = userStats.indexOf(new UserStats(logLine));
			if (index != -1) {
				userStats.get(index).addLogLine(logLine);
			} else {
				userStats.add(new UserStats(logLine));
			}
		}

		return userStats;
	}

	public static List<LogLine> filterLogLinesByUser(List<LogLine> logLines, String userName) {
		List<LogLine> userList = new ArrayList<LogLine>();

		for (LogLine logLine : logLines) {
			if (logLine.getUserName().equals(userName))
				userList.add(logLine);
		}

		return userList;
	}

	public static List<LogLine> filterLogLinesByDate(List<LogLine> logLines, Date firstConnection,
			Date lastConnection) {
		List<LogLine> dateList = new ArrayList<LogLine>();
		boolean afterFirst = true, beforeLast = true;

		for (LogLine logLine : logLines) {
			afterFirst = true;
			beforeLast = true;

			afterFirst = (firstConnection == null)
					|| (firstConnection != null && logLine.getDate().after(firstConnection));
			beforeLast = (lastConnection == null)
					|| (lastConnection != null && logLine.getDate().before(lastConnection));

			if (afterFirst && beforeLast)
					dateList.add(logLine);

		}

		return dateList;
	}

	public static List<String> loadUserNames(List<LogLine> logLines) {
		List<String> usernames = new ArrayList<String>();

		for (LogLine logLine : logLines) {
			if (!usernames.contains(logLine.getUserName()))
				usernames.add(logLine.getUserName());
		}

		return usernames;
	}

}
