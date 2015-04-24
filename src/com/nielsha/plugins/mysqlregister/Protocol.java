package com.nielsha.plugins.mysqlregister;

import java.io.Serializable;

public class Protocol implements Serializable{
	private static final long serialVersionUID = -5718652912352708320L;
	private String ip;
	private String motd;
	private String version;
	private int port;
	private int online;
	private int maxplayers;
	private long time;

	public Protocol(String ip, int port, String motd, String version, int online, int maxplayers, long time){
		this.ip = ip;
		this.port = port;
		this.motd = motd;
		this.version = version;
		this.online = online;
		this.maxplayers = maxplayers;
		this.time = time;
	}
	
	public String getIP() {
		return this.ip;
	}
	
	public String getMOTD() {
		return this.motd;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public int getOnline() {
		return this.online;
	}
	
	public int getMaxPlayers() {
		return this.maxplayers;
	}
	
	public long getTime() {
		return this.time;
	}
}