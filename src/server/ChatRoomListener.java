
//Ⱥ����Ϣ������

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
				//����Ⱥ������ѯȺ���Ա��Ϣ
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
				
				//����Ϣת����Ⱥ���Ա
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
