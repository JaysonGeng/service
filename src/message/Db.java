
//��̬�洢������Ϣ���Ƿ�������Ϣ����ĺ���

package message;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import main.Service;

public class Db 
{
		
	public static HashMap<Long, User> map = new HashMap<Long, User>();
	public static BuddyList dblist = new BuddyList();
	static {	
		try {
			ResultSet rs = Service.stmt.executeQuery( "SELECT * FROM COMPANY;" );
			while (rs.next()) 
			{	    	
				//������������û��б����
				User user = new User();
				user.number = rs.getInt("number");
				user.name = rs.getString("name");
				user.password = rs.getString("password");
				user.sign=rs.getString("sign");
				user.groupInfo=rs.getString("friend");
				user.avatar = 0;
				map.put(user.number, user);
				
				//�����ݿ�洢���û���Ϣ�б������
				Buddy item = new Buddy();
				item.number = rs.getInt("number");
				item.avatar = 0;
				item.name = rs.getString("name");
				item.groupInfo = rs.getString("friend");
				item.sign = rs.getString("sign");
				Db.dblist.buddyList.add(item);
			} 
		      rs.close();
						
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//������ͨ���˺Ż�ȡ�û���Ϣ
	public static User getUserByAccount(long number) {
		if (map.containsKey(number)) {
			return map.get(number);
		}
		return null;

	}
	
	//������ͨ���˺Ż�ȡ���ݿ��û���Ϣ
	public static Buddy getBuddyByAccount(long number) {
		for(Buddy a:dblist.buddyList)
		{
			if(a.number==number)
				return a;
		}
		return null;
	}

}
