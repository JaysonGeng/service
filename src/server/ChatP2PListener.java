
//私聊消息监听器

package server;



import java.io.IOException;

import connect.AConnection;
import connect.AConnection.OnRecevieMsgListener;
import connect.AConnectionManager;
import message.AMessage;
import message.AMessageType;

public class ChatP2PListener extends MessageSender implements OnRecevieMsgListener 
{
	public void onReceive(AMessage fromOneClient) {
		
		//根据信息里的接收端信息转发消息
		if (AMessageType.MSG_TYPE_CHAT_P2P.equals(fromOneClient.type)) {
			AConnection anotherOne = AConnectionManager.get(fromOneClient.to);
			AConnectionManager.addMessage(fromOneClient);
			
			if(AConnectionManager.get(fromOneClient.to)!=null){
				try {
					toClient(fromOneClient, anotherOne);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}
	}

}
