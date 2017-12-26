
//��Ӻ��ѵļ�����

package server;



import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import connect.AConnection;
import connect.AConnection.OnRecevieMsgListener;
import connect.AConnectionManager;
import main.Zhuji;
import message.AMessage;
import message.AMessageList;
import message.AMessageType;
import message.BuddyList;
import message.Db;

public class AddfriendListener extends MessageSender implements OnRecevieMsgListener 
{
	public void onReceive(AMessage fromOneClient) 
	{
		//���������"#"��˵���������߷��͵���Ϣ��ת���������߼���
		if (AMessageType.MSG_TYPE_ADDFRIEND.equals(fromOneClient.type)) 
		{
			if(!fromOneClient.content.contains("#")){
				if(fromOneClient.content.equals("refuse"))
				{
					for(AMessageList v:AConnectionManager.list)
					{
						if(v.listnum==fromOneClient.to)
						{
							for(int i=0;i<v.messageList.size();i++)
							{
								if(v.messageList.get(i).from==v.messageList.get(i).from){
									v.messageList.remove(i);
									break;
								}
							}
						}
					}
				}else{
				AConnection anotherOne = AConnectionManager.get(fromOneClient.to);
				AConnectionManager.addMessage(fromOneClient);
			if(AConnectionManager.get(fromOneClient.to)!=null)
			{
				try {
					toClient(fromOneClient, anotherOne);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
				}
			}
			else//�������Ϊ����"#"��˵���ǽ�����ͬ��������
			{
			      try {
				String a[]=fromOneClient.content.split("#");
				String b1[]=a[0].split("/");
				String b2[]=a[1].split("/");
				long c1=Long.parseLong(b1[0]),c2=Long.parseLong(b2[0]);
				
				//�޸����������ݿ�����ѵ�����
			    ResultSet rs = Zhuji.stmt.executeQuery( "SELECT * FROM COMPANY;" );
			      while ( rs.next() ) 
			      {
			    	  long d=rs.getInt("number");
			    	  if(d==c1)
			    	  {
					      String sql = "UPDATE COMPANY set FRIEND = '"+rs.getString("friend")+"#"+c2+"/�ҵĺ���"+"' where NUMBER = "+c1+";";
					      Zhuji.stmt.executeUpdate(sql);
						  Zhuji.c.commit();
						Db.getBuddyByAccount(c1).groupInfo+="#"+c2+"/"+b2[1];
						break;
			    	  }
			      }
			      rs.close();
				    ResultSet re = Zhuji.stmt.executeQuery( "SELECT * FROM COMPANY;" );
			      while ( re.next() ) 
			      {
			    	  long d=re.getInt("number");
			    	  if(d==c2)
			    	  {
					      String sql = "UPDATE COMPANY set FRIEND = '"+re.getString("friend")+"#"+c1+"/�ҵĺ���"+"' where NUMBER = "+c2+";";
					      Zhuji.stmt.executeUpdate(sql);
						  Zhuji.c.commit();
							Db.getBuddyByAccount(c2).groupInfo+="#"+c1+"/"+b1[1];
							break;
			    	  }
			      }
				    re.close();

						for(AMessageList v:AConnectionManager.list)
						{
							if(v.listnum==fromOneClient.to)
							{
								for(int i=0;i<v.messageList.size();i++)
								{
									if(v.messageList.get(i).from==v.messageList.get(i).from){
										v.messageList.remove(i);
										break;
									}
								}
							}
						}
				    
				    //���޸ĺ�ĺ����б������пͻ���
					AMessage toAllClient = new AMessage();
					toAllClient.type = AMessageType.MSG_TYPE_BUDDYLIST;
					BuddyList list = Db.dblist;
					toAllClient.content = list.toJson();
					toEveryClient(toAllClient);

					} catch (SQLException | IOException e) {
						e.printStackTrace();
					}

			}
		}
	}

}
