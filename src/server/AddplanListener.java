
//��ӻ�ļ�����

package server;



import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import connect.AConnection.OnRecevieMsgListener;
import connect.AConnectionManager;
import main.Zhuji;
import message.AMessage;
import message.AMessageType;
import message.APlanList;

public class AddplanListener extends MessageSender implements OnRecevieMsgListener 
{
	public void onReceive(AMessage fromOneClient) 
	{
		//�����ݿ��޸Ļ�ĳ�Ա��Ϣ
		if (AMessageType.MSG_TYPE_ADDPLAN.equals(fromOneClient.type)) 
		{
				long c=fromOneClient.to,w=fromOneClient.from;
			    ResultSet rs;
				try {
					rs = Zhuji.stmt3.executeQuery( "SELECT * FROM COMPANY;" );
				
			      while ( rs.next() ) 
			      {
			    	  long d=rs.getInt("number");
			    	  if(d==c)
			    	  {
					      String sql = "UPDATE COMPANY set MEMBER = '"+rs.getString("member")+"#"+w+"' where NUMBER = "+c+";";
					      Zhuji.stmt3.executeUpdate(sql);
						  Zhuji.e.commit();
			    	  }
			      }
			      rs.close();
			      
			    //�����µĻ�б�
			      AConnectionManager.planlist=new APlanList();
			      
			    //���µĻ�б������пͻ���
					AMessage toAllClient = new AMessage();
					APlanList planlist=AConnectionManager.planlist;
					toAllClient.content = planlist.toJson();
					toAllClient.type = AMessageType.MSG_TYPE_PLANLIST;
					toEveryClient(toAllClient);
				} catch (SQLException | IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
		} 
		
	}

}
