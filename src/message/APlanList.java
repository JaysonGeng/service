
//��б��洢�������APlan

package message;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.Zhuji;

public class APlanList extends ProtocalObj {
	public List<APlan> planList = new ArrayList<APlan>();
	
	//�ڹ��췽������ɴ����ݿ��ȡ����
	public APlanList()
	{
		try {
		     ResultSet rs = Zhuji.stmt3.executeQuery( "SELECT * FROM COMPANY;" );
	      while ( rs.next() ) {
	    	  APlan a=new APlan();
	    	  a.number=rs.getInt("number");
	    	  a.name=rs.getString("name");
	    	  a.member=rs.getString("member");
	    	  a.time=rs.getString("time");
	    	  a.position=rs.getString("position");
	    	  a.describe=rs.getString("describe");
	    	  a.avatar=0;
	    	  planList.add(a);
	      }
	      rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
