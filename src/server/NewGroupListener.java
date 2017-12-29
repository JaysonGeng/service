
//�½�Ⱥ��ļ�����

package server;


import java.sql.ResultSet;

import connect.AConnection.OnRecevieMsgListener;
import connect.AConnectionManager;
import main.Service;
import message.AMessage;
import message.AMessageType;
import message.GroupList;

public class NewGroupListener extends MessageSender implements OnRecevieMsgListener {

	public void onReceive(AMessage fromCient) {
		//������������������Ϣ���½�Ⱥ�鲢��ӵ����ݿ�
		if (AMessageType.MSG_TYPE_NEWGROUP.equals(fromCient.type)) {
			try {
					String[] params = fromCient.content.split("#");
					String name = params[0];
					String describe = params[1];					
					
					int num=0;
				    ResultSet r = Service.stmt2.executeQuery( "SELECT * FROM COMPANY;" );
				    while ( r.next())
				    	  num++;
				    r.close();
				    num++;
					String sql = "INSERT INTO COMPANY (ID,NUMBER,NAME,DESCRIBE,MEMBER) " +
						"VALUES ( "+num+", "+(num+100000)+", '"+name+"', '"+describe+"', '"+fromCient.from+"' );"; 
					Service.stmt2.executeUpdate(sql);
				    Service.d.commit();

				    //���µ�Ⱥ���б������пͻ���
				      AConnectionManager.grouplist=new GroupList();
				      
						AMessage toAllClient = new AMessage();
						GroupList grouplist=AConnectionManager.grouplist;
						toAllClient.content = grouplist.toJson();
						toAllClient.from=num+100000;
						toAllClient.type = AMessageType.MSG_TYPE_GROUPLIST;
						toEveryClient(toAllClient);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}