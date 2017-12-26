
//�û��˳�������

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
				//�ӵ��˳�����Ϣ���Ƴ��û�
				AConnectionManager.remove(fromCient.from );
				Db.getBuddyByAccount(fromCient.from).online="����";

				//�����пͻ��˷����µ��û��б�
				send();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	//�����пͻ��˷����µ��û��б�ķ���
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