
//Ⱥ���б��洢����Ⱥ���Group

package message;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.Zhuji;

public class GroupList extends ProtocalObj {
	public List<Group> groupList = new ArrayList<Group>();
	
	//�ڹ��췽������ɴ����ݿ��ȡ����
	public GroupList()
	{
		try {
		     ResultSet rs = Zhuji.stmt2.executeQuery( "SELECT * FROM COMPANY;" );
	      while ( rs.next() ) {
	    	  Group a=new Group();
	    	  a.number=rs.getInt("number");
	    	  a.name=rs.getString("name");
	    	  a.member=rs.getString("member");
	    	  a.describe=rs.getString("describe");
	    	  a.avatar=0;
	    	  groupList.add(a);
	      }
	      rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
