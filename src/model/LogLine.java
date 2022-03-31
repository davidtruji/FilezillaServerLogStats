package model;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Utils;

public class LogLine {

	private final static String DATE_REGEX = "[0-9]+/[0-9]+/[0-9]+ [0-9]+:[0-9]+:[0-9]+";
	private final static String IP_ADDRESS_REGEX = "([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)|(::1)";
	private final static String USERNAME_REGEX = "[a-zA-ZñÑ]+[a-zA-ZñÑ0-9]*";

	private Date date;
	private String userName;
	private String ipAddress;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public LogLine(String logLine) {
		date = getDateFromLogLine(logLine);
		userName = getUserNameFromLogLine(logLine);
		ipAddress = getIpFromLogLine(logLine);
	}

	public static Date getDateFromLogLine(String logLine) {
		Date date = new Date();
		Pattern pattern = Pattern.compile(DATE_REGEX);
		Matcher matcher = pattern.matcher(logLine);
		if (matcher.find()) {
			try {
				date = Utils.dateFormat.parse(matcher.group(0));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	};

	public static String getIpFromLogLine(String logLine) {
		String ipAddress = "0.0.0.0";
		Pattern pattern = Pattern.compile(IP_ADDRESS_REGEX);
		Matcher matcher = pattern.matcher(logLine);
		if (matcher.find()) {
			ipAddress = matcher.group(0);

			if (ipAddress.equals("::1"))
				ipAddress = "localhost";
		}
		return ipAddress;
	}

	public static String getUserNameFromLogLine(String logLine) {
		String username = "defaultUser";
		Pattern pattern = Pattern.compile(USERNAME_REGEX);
		Matcher matcher = pattern.matcher(logLine);
		if (matcher.find()) {
			username = matcher.group(0);
		}
		return username;
	}

	@Override
	public String toString() {
		return "LogLine [date=" + Utils.dateFormat.format(date) + ", userName=" + userName + ", ipAdress=" + ipAddress
				+ "]";
	}

}
