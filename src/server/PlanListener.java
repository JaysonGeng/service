
//�޸Ļ��Ϣ�ļ�����
//��Ϊʱ���ϵĿǰֻ������޸�λ�õĹ���

package server;



import connect.AConnection.OnRecevieMsgListener;
import main.Zhuji;
import message.AMessage;
import message.AMessageType;
import message.APlanList;

public class PlanListener extends MessageSender implements OnRecevieMsgListener {
	
	public void onReceive(AMessage msg) {
		//�޸Ļλ�ò��������ݿ�
		if (AMessageType.MSG_TYPE_PLANPOSITION.equals(msg.type)) {
			try {
			      String sql = "UPDATE COMPANY set POSITION = '"+msg.content+"' where NUMBER = "+msg.to+";";
			      Zhuji.stmt3.executeUpdate(sql);
			      Zhuji.e.commit();
			      
			      //���µĻ�б������пͻ���
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
