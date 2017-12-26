
//修改活动信息的监听器
//因为时间关系目前只添加了修改位置的功能

package server;



import connect.AConnection.OnRecevieMsgListener;
import main.Zhuji;
import message.AMessage;
import message.AMessageType;
import message.APlanList;

public class PlanListener extends MessageSender implements OnRecevieMsgListener {
	
	public void onReceive(AMessage msg) {
		//修改活动位置并加入数据库
		if (AMessageType.MSG_TYPE_PLANPOSITION.equals(msg.type)) {
			try {
			      String sql = "UPDATE COMPANY set POSITION = '"+msg.content+"' where NUMBER = "+msg.to+";";
			      Zhuji.stmt3.executeUpdate(sql);
			      Zhuji.e.commit();
			      
			      //把新的活动列表发给所有客户端
					AMessage toClient = new AMessage();
					APlanList planlist=new APlanList();
					toClient.content = planlist.toJson();
					toClient.type = AMessageType.MSG_TYPE_PLANLIST;
					toEveryClient(toClient);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
