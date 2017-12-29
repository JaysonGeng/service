
//��Ϣ���͵��࣬�������ĸ��࣬�Ƿ�������ͻ���ͨ�ŵĺ���

package server;




import java.io.IOException;
import java.util.Map;
import java.util.Set;

import connect.AConnection;
import connect.AConnectionManager;
import message.AMessage;

public class MessageSender {

	//˽��һ���ͻ���
	public void toClient(AMessage msg, AConnection conn) throws IOException {
		System.out.println("������ǰ�ͻ���to Client \n" + msg.toXml());
		if (conn != null) {
			conn.writer.writeUTF(msg.toXml());
		}
	}
	
	public void toClient(AMessage msg, long number) throws IOException {
		System.out.println("������ǰ�ͻ���to Client \n" + msg.toXml());
		AConnection conn = AConnectionManager.get(number);
		if (conn != null) {
			conn.writer.writeUTF(msg.toXml());
		}
	}

	//�������пͻ���
	public void toEveryClient(AMessage msg) throws IOException {
		System.out.println("Ⱥ�����пͻ���  to toEveryClient Client \n" + msg.toXml());
		Set<Map.Entry<Long, AConnection>> allOnLines = AConnectionManager.conns
				.entrySet();
		for (Map.Entry<Long, AConnection> entry : allOnLines) {
			entry.getValue().writer.writeUTF(msg.toXml());
		}
	}
}
