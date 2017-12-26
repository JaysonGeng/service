
//用户退出监听器

package server;


import connect.AConnection;
import connect.AConnection.OnRecevieMsgListener;
import connect.AConnectionManager;
import message.AMessage;
import message.AMessageType;
import message.BuddyList;
import message.Db;

public class LogOutListener extends MessageSender implements OnRecevieMsgListener {

	public void onReceive(AMessage fromCient) {
		if (AMessageType.MSG_TYPE_LOGOUT.equals(fromCient.type)) {
			try {
				//接到退出的消息后移除用户
				AConnectionManager.remove(fromCient.from );
				Db.getBuddyByAccount(fromCient.from).online="离线";

				//给所有客户端发送新的用户列表
				send();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	//给所有客户端发送新的用户列表的方法
	public void send()
	{
		try{
		AMessage toAllClient = new AMessage();
		toAllClient.type = AMessageType.MSG_TYPE_BUDDYLIST;
		BuddyList list = Db.dblist;
		toAllClient.content = list.toJson();
		toEveryClient(toAllClient);} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}