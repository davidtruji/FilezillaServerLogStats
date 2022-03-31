package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.Utils;

public class UserStats {

	private String userName;
	private List<String> knownIps;
	private int totalConnections;
	private Date firstConnection;
	private Date lastConnection;

	public UserStats(LogLine logLine) {
		super();
		this.userName = logLine.getUserName();
		this.knownIps = new ArrayList<String>();
		this.knownIps.add(logLine.getIpAddress());
		this.totalConnections = 1;
		this.firstConnection = logLine.getDate();
		this.lastConnection = logLine.getDate();
	}

	public UserStats(String userName, List<String> knownIps, int totalConnections, Date firstConnection,
			Date lastConnection) {
		super();
		this.userName = userName;
		this.knownIps = knownIps;
		this.totalConnections = totalConnections;
		this.firstConnection = firstConnection;
		this.lastConnection = lastConnection;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getKnownIps() {
		return knownIps;
	}

	public void setKnownIps(List<String> knownIps) {
		this.knownIps = knownIps;
	}

	public int getTotalConnections() {
		return totalConnections;
	}

	public void setTotalConnections(int totalConnections) {
		this.totalConnections = totalConnections;
	}

	public Date getFirstConnection() {
		return firstConnection;
	}

	public void setFirstConnection(Date firstConnection) {
		this.firstConnection = firstConnection;
	}

	public Date getLastConnection() {
		return lastConnection;
	}

	public void setLastConnection(Date lastConnection) {
		this.lastConnection = lastConnection;
	}

	@SuppressWarnings("unlikely-arg-type")
	public void addLogLine(LogLine logLine) {
		if (!this.equals(logLine))
			return;

		if (!knownIps.contains(logLine.getIpAddress()))
			knownIps.add(logLine.getIpAddress());

		totalConnections++;

		if (logLine.getDate().before(firstConnection))
			firstConnection = logLine.getDate();
		else if (logLine.getDate().after(lastConnection))
			lastConnection = logLine.getDate();

	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;

		if (obj instanceof String) {
			equals = ((String) obj).equals(this.userName);
		} else if (obj instanceof UserStats) {
			equals = ((UserStats) obj).getUserName().equals(this.userName);
		} else if (obj instanceof LogLine) {
			equals = ((LogLine) obj).getUserName().equals(this.userName);
		}

		return equals;
	}

	@Override
	public String toString() {

		String ips = "";
		for (String ip : knownIps)
			ips += " [" + ip + "]";

		return this.userName + "\n   Total connections: " + this.totalConnections + "\n   First connection: "
				+ Utils.dateFormat.format(this.firstConnection) + "\n   Last connection: "
				+ Utils.dateFormat.format(this.lastConnection) + "\n   Known IPs:" + ips + "\n";

	}

}
