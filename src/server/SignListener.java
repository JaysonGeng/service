
//修改用户信息的监听器
//因为时间关系目前只添加了修改签名的功能

package server;



import connect.AConnection.OnRecevieMsgListener;
import main.Zhuji;
import message.AMessage;
import message.AMessageType;
import message.BuddyList;
import message.Db;

public class SignListener extends MessageSender implements OnRecevieMsgListener {
	
	public void onReceive(AMessage msg) {
		//修改个性签名并加入数据库
		if (AMessageType.MSG_TYPE_USERSIGN.equals(msg.type)) {
			try {
			      String sql = "UPDATE COMPANY set SIGN = '"+msg.content+"' where NUMBER = "+msg.from+";";
			      Zhuji.stmt.executeUpdate(sql);
			      Zhuji.c.commit();
			      Db.getBuddyByAccount(msg.from).sign=msg.content;
			      Db.getUserByAccount(msg.from).sign=msg.content;
			      
			      //把新的用户列表发给所有客户端
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
