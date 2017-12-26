
//主机端管理连接的类，用于管理在线的用户

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

//构造方法，生成用户列表，群组列表和活动列表
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
	
	//上线
	public static void put(long number, AConnection conn) {
		System.out.println("====账号" + number + "上线了");
		conns.put(number, conn);
		Db.getBuddyByAccount(number).online = "在线";
	}

	//下线
	public static void remove(Long number) {
		if (conns.containsKey(number)) {
			System.out.println("====账号" + number + "下线了");
			conns.remove(number);
			Db.getBuddyByAccount(number).online="离线";
		}
	}

	//根据帐号查找用户
	public static AConnection get(long account) {
		if (conns.containsKey(account)) {
			return conns.get(account);
		}
		return null;
	}
	
	//添加消息到消息列表
	public static void addMessage(AMessage message) {
		if(message.type.equals(AMessageType.MSG_TYPE_CHAT_P2P)){
		boolean check=false;
		for(AMessageList a:list)
		{
			//查找消息列表，如果存在这样的列表名就加到该表中
			//没有就新建一个列表，以此实现QQ的消息列表功能，同类消息存储在一起
			//群组和好友消息同理
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
			//查找消息列表，如果存在这样的列表名就加到该表中
			//没有就新建一个列表，以此实现QQ的消息列表功能，同类消息存储在一起
			//群组和好友消息同理
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

	//清理掉一个列表
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
	
	//获取消息列表
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
