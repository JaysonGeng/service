
//�洢�������Ϣ�б����

package message;



import java.util.ArrayList;
import java.util.List;

public class AMessageList {

	public List<AMessage> messageList;
	public long listnum;//�б�����
	
	public AMessageList(long num)
	{
		messageList = new ArrayList<AMessage>();
		listnum=num;
	}
	
}
