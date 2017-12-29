
//群聊消息监听器

package server;



import java.sql.ResultSet;

import connect.AConnectionManager;
import connect.AConnection.OnRecevieMsgListener;
import main.Service;
import message.AMessage;
import message.AMessageType;

public class ChatRoomListener extends MessageSender implements OnRecevieMsgListener {
			
	public void onReceive(AMessage fromOneClient) {
		if (AMessageType.MSG_TYPE_CHAT_ROOM.equals(fromOneClient.type)) {
			try {
				AConnectionManager.addMessage(fromOneClient);
				//根据群组号码查询群组成员信息
			    ResultSet r = Service.stmt2.executeQuery( "SELECT * FROM COMPANY;" );
				long group=fromOneClient.to;
				String member="";
				
			      while ( r.next() ) {
			    	  if( r.getInt("number")==group)
			    	  {
					      member = r.getString("member");
					      break;
			    	  }
				  }
				r.close();
				
				//把消息转发给群组成员
				String[] members = member.split("/");	
				for(String a:members)		
					if(Long.parseLong(a)!=fromOneClient.from)
						toClient(fromOneClient, Long.parseLong(a));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
