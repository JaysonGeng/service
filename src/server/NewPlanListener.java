
//�½���ļ�����

package server;



import java.sql.ResultSet;

import connect.AConnection.OnRecevieMsgListener;
import connect.AConnectionManager;
import main.Zhuji;
import message.AMessage;
import message.AMessageType;
import message.APlanList;

public class NewPlanListener extends MessageSender implements OnRecevieMsgListener {

	public void onReceive(AMessage fromCient) {
		//������������������Ϣ���½������ӵ����ݿ�
		if (AMessageType.MSG_TYPE_NEWPLAN.equals(fromCient.type)) {
			try {
					String[] params = fromCient.content.split("#");
					String name = params[0];
					String describe = params[1];					
					
					int num=0;
				    ResultSet r = Zhuji.stmt3.executeQuery( "SELECT * FROM COMPANY;" );
				    while ( r.next())
				    	  num++;
				    r.close();
				    num++;
					String sql = "INSERT INTO COMPANY (ID,NUMBER,NAME,DESCRIBE,MEMBER,POSITION,TIME) " +
						"VALUES ( "+num+", "+(num+100000)+", '"+name+"', '"+describe+"', '"+fromCient.from+"', '', '"+fromCient.sendTime+"' );"; 
					Zhuji.stmt3.executeUpdate(sql);
				    Zhuji.e.commit();

				    //���µĻ�б������пͻ���
				      AConnectionManager.planlist=new APlanList();
				      
						AMessage toAllClient = new AMessage();
						APlanList planlist=AConnectionManager.planlist;
						toAllClient.content = planlist.toJson();
						toAllClient.from=num+100000;
						toAllClient.type = AMessageType.MSG_TYPE_PLANLIST;
						toEveryClient(toAllClient);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}