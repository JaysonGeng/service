
//检测心跳包的监听器

package server;



import connect.AConnection;
import connect.AConnection.OnRecevieMsgListener;
import message.AMessage;
import message.AMessageType;

public class OnLineListener extends MessageSender implements OnRecevieMsgListener 
{
	private AConnection conn = null;

	public OnLineListener(AConnection conn) {
		super();
		this.conn = conn;
	}
	public void onReceive(AMessage fromOneClient) {
		//检测到客户端发来的心跳包，把在线开关打开
		if (AMessageType.MSG_TYPE_ONLINE.equals(fromOneClient.type)) 
			conn.check=true;
	}

}
