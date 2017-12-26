
//用户注册监听器

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
		//检测客户端的注册信息，并添加到数据库
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
						"VALUES ( "+num+", "+(num+10000)+", '"+name+"', '"+pwd+"', '', '"+(num+10000)+"/自己' );"; 
					Zhuji.stmt.executeUpdate(sql);
				    Zhuji.c.commit();
				    r.close();
				    			    
				    //返回成功信息
					toClient.type = AMessageType.MSG_TYPE_SUCCESS;
					toClient.content = ""+(num+10000);
					toClient(toClient, conn);
					
					//向服务器管理用户列表添加
					User user = new User();
					user.number = num+10000;
					user.name = name;
					user.password = pwd;
					user.sign="";
					user.groupInfo=(num+10000)+"/自己";
					user.avatar = 0;
					Db.map.put(user.number, user);
					
					//向数据库存储的用户信息列表里添加
					Buddy item = new Buddy();
					item.number = num+10000;
					item.avatar = 0;
					item.name = name;
					item.groupInfo = (num+10000)+"/自己";
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