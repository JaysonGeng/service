
//存储传输的信息列表的类

package message;



import java.util.ArrayList;
import java.util.List;

public class AMessageList {

	public List<AMessage> messageList;
	public long listnum;//列表名称
	
	public AMessageList(long num)
	{
		messageList = new ArrayList<AMessage>();
		listnum=num;
	}
	
}
