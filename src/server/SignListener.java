
//�޸��û���Ϣ�ļ�����
//��Ϊʱ���ϵĿǰֻ������޸�ǩ���Ĺ���

package server;



import connect.AConnection.OnRecevieMsgListener;
import main.Zhuji;
import message.AMessage;
import message.AMessageType;
import message.BuddyList;
import message.Db;

public class SignListener extends MessageSender implements OnRecevieMsgListener {
	
	public void onReceive(AMessage msg) {
		//�޸ĸ���ǩ�����������ݿ�
		if (AMessageType.MSG_TYPE_USERSIGN.equals(msg.type)) {
			try {
			      String sql = "UPDATE COMPANY set SIGN = '"+msg.content+"' where NUMBER = "+msg.from+";";
			      Zhuji.stmt.executeUpdate(sql);
			      Zhuji.c.commit();
			      Db.getBuddyByAccount(msg.from).sign=msg.content;
			      Db.getUserByAccount(msg.from).sign=msg.content;
			      
			      //���µ��û��б������пͻ���
					AMessage toClient = new AMessage();
					toClient.type = AMessageType.MSG_TYPE_BUDDYLIST;
					BuddyList list = Db.dblist;
					toClient.content = list.toJson();
					toEveryClient(toClient);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
