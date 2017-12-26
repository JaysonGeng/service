
//�����˹������ӵ��࣬���ڹ������ߵ��û�

package connect;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import message.AMessage;
import message.AMessageList;
import message.AMessageType;
import message.APlanList;
import message.Db;
import message.GroupList;

//���췽���������û��б�Ⱥ���б�ͻ�б�
public class AConnectionManager {
	public static HashMap<Long, AConnection> conns ;
	public static GroupList grouplist ;
	public static APlanList planlist ;
	public static List<AMessageList> list ;
	
	public AConnectionManager(){
		grouplist = new GroupList();
		planlist = new APlanList();
		conns = new HashMap<Long, AConnection>();
		list = new ArrayList<AMessageList>();
	}
	
	//����
	public static void put(long number, AConnection conn) {
		System.out.println("====�˺�" + number + "������");
		conns.put(number, conn);
		Db.getBuddyByAccount(number).online = "����";
	}

	//����
	public static void remove(Long number) {
		if (conns.containsKey(number)) {
			System.out.println("====�˺�" + number + "������");
			conns.remove(number);
			Db.getBuddyByAccount(number).online="����";
		}
	}

	//�����ʺŲ����û�
	public static AConnection get(long account) {
		if (conns.containsKey(account)) {
			return conns.get(account);
		}
		return null;
	}
	
	//�����Ϣ����Ϣ�б�
	public static void addMessage(AMessage message) {
		if(message.type.equals(AMessageType.MSG_TYPE_CHAT_P2P)){
		boolean check=false;
		for(AMessageList a:list)
		{
			//������Ϣ�б���������������б����ͼӵ��ñ���
			//û�о��½�һ���б��Դ�ʵ��QQ����Ϣ�б��ܣ�ͬ����Ϣ�洢��һ��
			//Ⱥ��ͺ�����Ϣͬ��
			if(a.listnum==message.to)
			{
				check=true;
				if(a.messageList.size()>50)
					a.messageList.remove(0);
				a.messageList.add(message);
				break;
			}
		}
		if(!check)
		{
			AMessageList re=new AMessageList(message.to);
			re.messageList.add(message);
			list.add(re);
		}
		
		boolean check2=false;
		for(AMessageList a:list)
		{
			//������Ϣ�б���������������б����ͼӵ��ñ���
			//û�о��½�һ���б��Դ�ʵ��QQ����Ϣ�б��ܣ�ͬ����Ϣ�洢��һ��
			//Ⱥ��ͺ�����Ϣͬ��
			if(a.listnum==message.from)
			{
				check2=true;
				if(a.messageList.size()>50)
					a.messageList.remove(0);
				a.messageList.add(message);
				break;
			}
		}
		if(!check2)
		{
			AMessageList re=new AMessageList(message.from);
			re.messageList.add(message);
			list.add(re);
		}
		
		}
		else if(message.type.equals(AMessageType.MSG_TYPE_CHAT_ROOM)){
			boolean check=false;
			for(AMessageList a:list)
			{
				if(a.listnum==message.to)
				{
					check=true;
					if(a.messageList.size()>100)
						a.messageList.remove(0);
					a.messageList.add(message);
					break;
				}
			}
			if(!check)
			{
				AMessageList re=new AMessageList(message.to);
				re.messageList.add(message);
				list.add(re);
			}}
		else if(message.type.equals(AMessageType.MSG_TYPE_ADDFRIEND)){
		boolean check=false;
		for(AMessageList a:list)
		{
			if(a.listnum==message.to)
			{
				check=true;
				break;
			}
		}
		if(!check)
		{
			AMessageList re=new AMessageList(message.to);
			re.messageList.add(message);
			list.add(re);
		}}
	}

	//�����һ���б�
	public static void clearList(long num)
	{
		for(AMessageList a:list)
		{
			if(a.listnum==num)
			{
				a.messageList.clear();
				break;
			}
		}
	}
	
	//��ȡ��Ϣ�б�
	public static AMessageList getList(long num)
	{
		for(AMessageList r:list)
		{
			if(r.listnum==num)
			{
				return r;
			}
		}
		return null;
	}
}
