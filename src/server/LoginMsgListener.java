
//用户登陆监听器

package server;



import connect.AConnection;
import connect.AConnection.OnRecevieMsgListener;
import connect.AConnectionManager;
import message.AMessage;
import message.AMessageList;
import message.AMessageType;
import message.BuddyList;
import message.Db;
import message.Group;
import message.GroupList;
import message.User;

public class LoginMsgListener extends MessageSender implements OnRecevieMsgListener {
	private AConnection conn = null;

	public LoginMsgListener(AConnection conn) {
		super();
		this.conn = conn;
	}

	public void onReceive(AMessage fromCient) {
		if (AMessageType.MSG_TYPE_LOGIN.equals(fromCient.type)) {
			try {
				if (AMessageType.MSG_TYPE_LOGIN.equals(fromCient.type)) 
				{
					String[] params = fromCient.content.split("#");
					String number = params[0];
					String pwd = params[1];
					User user = Db.getUserByAccount(Long.parseLong(number));
					AMessage toClient = new AMessage();
					if (user == null) 
					{
						// 不存在
						toClient.type = AMessageType.MSG_TYPE_FAIL;
						toClient.content = "对不起，账户不存在";
						toClient(toClient, conn);
					} 					
					else if(AConnectionManager.get(Long.parseLong(number))==null)
					{
						// 存在
						if (user.password.equals(pwd)) {
							// 登录 成功
							toClient.type = AMessageType.MSG_TYPE_BUDDYLIST;
							toClient.fromName=user.name;
							// 返回用户名单
							// 创建带身份的连接对象
							conn.who = user;
							AConnectionManager.put(user.number, conn);
							Db.getBuddyByAccount(user.number).online="在线";
							
							BuddyList list = Db.dblist;
							toClient.content = list.toJson();
							toEveryClient(toClient);
							
							Thread.sleep(10);
							
							// 返回群组名单
							AMessage toClient2 = new AMessage();
							GroupList grouplist=AConnectionManager.grouplist;
							toClient2.content = grouplist.toJson();
							toClient2.type = AMessageType.MSG_TYPE_GROUPLIST;
							toClient2.fromName=user.name;
							toClient(toClient2,conn);
							
							Thread.sleep(10);
							
							// 返回活动名单
							AMessage toClient3 = new AMessage();

							toClient3.type = AMessageType.MSG_TYPE_PLANLIST;
							toClient3.fromName=user.name;
							toClient(toClient3,conn);
							
							//开启心跳包线程，每5秒检测一次连接开关，如果开关关闭则切断与该客户端的链接
							new Thread(){
								public void run(){
									while(true)
									{
										try {
											if(!conn.check)
											{
												if (conn.who != null) {
													AConnectionManager.remove(conn.who.number);
													conn.send();
												}
												conn.disconnect();
												break;
											}
											//检测后关闭开关
											conn.check=false;
											Thread.sleep(5000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}
							}.start();
							
							Thread.sleep(600);
							//发送存储的消息
							AMessageList Alist=AConnectionManager.getList(user.number);
							if(Alist!=null){
								for(AMessage a:Alist.messageList){
									toClient(a, conn);}
//								AConnectionManager.clearList(Alist.listnum);
							}
							
							for(Group a:AConnectionManager.grouplist.groupList)
							{
								if(a.member.contains(""+user.number))
								{
									AMessageList Alist2=AConnectionManager.getList(a.number);
									if(Alist2!=null){
										for(AMessage b:Alist2.messageList){
											toClient(b, conn);}
									}
								}
							}
							
						}else {
							//帐号密码错误
							toClient.type = AMessageType.MSG_TYPE_FAIL;
							toClient.content = "帐号或密码错误";
							toClient(toClient, conn);
						}
					}
					else
					{
						//用户已登陆
						toClient.type = AMessageType.MSG_TYPE_FAIL;
						toClient.content = "对不起，账户已登陆，不能重复登录";
						toClient(toClient, conn);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}