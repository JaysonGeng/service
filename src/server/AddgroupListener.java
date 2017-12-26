
//添加群组的监听器

package server;



import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import connect.AConnection.OnRecevieMsgListener;
import connect.AConnectionManager;
import main.Zhuji;
import message.AMessage;
import message.AMessageType;
import message.GroupList;

public class AddgroupListener extends MessageSender implements OnRecevieMsgListener 
{
	public void onReceive(AMessage fromOneClient) 
	{
		//向数据库修改群组的成员信息
		if (AMessageType.MSG_TYPE_ADDGROUP.equals(fromOneClient.type)) 
		{
				long c=fromOneClient.to,w=fromOneClient.from;
			    ResultSet rs;
				try {
					rs = Zhuji.stmt2.executeQuery( "SELECT * FROM COMPANY;" );
				
			      while ( rs.next() ) 
			      {
			    	  long d=rs.getInt("number");
			    	  if(d==c)
			    	  {
					      String sql = "UPDATE COMPANY set MEMBER = '"+rs.getString("member")+"/"+w+"' where NUMBER = "+c+";";
					      Zhuji.stmt2.executeUpdate(sql);
						  Zhuji.d.commit();
			    	  }
			      }
			      rs.close();
			      
			      //生成新的群组列表
			      AConnectionManager.grouplist=new GroupList();
			      
			      //把新的群组列表发给所有客户端
					AMessage toAllClient = new AMessage();
					GroupList grouplist=AConnectionManager.grouplist;
					toAllClient.content = grouplist.toJson();
					toAllClient.type = AMessageType.MSG_TYPE_GROUPLIST;
					toEveryClient(toAllClient);
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
		} 
		
	}

}
