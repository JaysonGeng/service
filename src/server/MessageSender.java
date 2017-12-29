
//消息发送的类，监听器的父类，是服务器与客户端通信的核心

package server;




import java.io.IOException;
import java.util.Map;
import java.util.Set;

import connect.AConnection;
import connect.AConnectionManager;
import message.AMessage;

public class MessageSender {

	//私发一个客户端
	public void toClient(AMessage msg, AConnection conn) throws IOException {
		System.out.println("单发当前客户端to Client \n" + msg.toXml());
		if (conn != null) {
			conn.writer.writeUTF(msg.toXml());
		}
	}
	
	public void toClient(AMessage msg, long number) throws IOException {
		System.out.println("单发当前客户端to Client \n" + msg.toXml());
		AConnection conn = AConnectionManager.get(number);
		if (conn != null) {
			conn.writer.writeUTF(msg.toXml());
		}
	}

	//发给所有客户端
	public void toEveryClient(AMessage msg) throws IOException {
		System.out.println("群发所有客户端  to toEveryClient Client \n" + msg.toXml());
		Set<Map.Entry<Long, AConnection>> allOnLines = AConnectionManager.conns
				.entrySet();
		for (Map.Entry<Long, AConnection> entry : allOnLines) {
			entry.getValue().writer.writeUTF(msg.toXml());
		}
	}
}
