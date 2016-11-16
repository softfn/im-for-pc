package com.oim.net.message;

public abstract class AbstractMessage implements Data {
	
	public abstract Head getHead();

	public abstract void setHead(Head head);
}
