
//�û�ע�������

package server;



import java.sql.ResultSet;

import connect.AConnection;
import connect.AConnection.OnRecevieMsgListener;
import main.Zhuji;
import message.AMessage;
import message.AMessageType;
import message.Buddy;
import message.Db;
import message.User;

public class ZhuceListener extends MessageSender implements OnRecevieMsgListener {
	private AConnection conn = null;
	
	public ZhuceListener(AConnection conn) {
		super();
		this.conn = conn;
	}

	public void onReceive(AMessage fromCient) {
		//���ͻ��˵�ע����Ϣ������ӵ����ݿ�
		if (AMessageType.MSG_TYPE_ZHUCE.equals(fromCient.type)) {
			try {
				AMessage toClient = new AMessage();
				if (AMessageType.MSG_TYPE_ZHUCE.equals(fromCient.type)) 
				{
					String[] params = fromCient.content.split("#");
					String name = params[0];
					String pwd = params[1];					
					
					int num=0;
				    ResultSet r = Zhuji.stmt.executeQuery( "SELECT * FROM COMPANY;" );
				    while ( r.next())
				    	  num++;
				    num++;
					String sql = "INSERT INTO COMPANY (ID,NUMBER,NAME,PASSWORD,SIGN,FRIEND) " +
						"VALUES ( "+num+", "+(num+10000)+", '"+name+"', '"+pwd+"', '', '"+(num+10000)+"/�Լ�' );"; 
					Zhuji.stmt.executeUpdate(sql);
				    Zhuji.c.commit();
				    r.close();
				    			    
				    //���سɹ���Ϣ
					toClient.type = AMessageType.MSG_TYPE_SUCCESS;
					toClient.content = ""+(num+10000);
					toClient(toClient, conn);
					
					//������������û��б����
					User user = new User();
					user.number = num+10000;
					user.name = name;
					user.password = pwd;
					user.sign="";
					user.groupInfo=(num+10000)+"/�Լ�";
					user.avatar = 0;
					Db.map.put(user.number, user);
					
					//�����ݿ�洢���û���Ϣ�б������
					Buddy item = new Buddy();
					item.number = num+10000;
					item.avatar = 0;
					item.name = name;
					item.groupInfo = (num+10000)+"/�Լ�";
					item.sign = "";
					Db.dblist.buddyList.add(item);
					
					conn.disconnect();				
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}