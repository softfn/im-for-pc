package com.only.net.connect;

/**
 * @Author: XiaHui
 * @Date: 2016-1-4
 * @ModifyUser: XiaHui
 * @ModifyDate: 2016-1-4
 */
public interface Connector {

	public boolean connect(ConnectData connectData);

	public boolean isConnected();

	public void closeConnect();

	public boolean write(Object data);
}
