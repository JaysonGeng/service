
//�û���½������

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
						// ������
						toClient.type = AMessageType.MSG_TYPE_FAIL;
						toClient.content = "�Բ����˻�������";
						toClient(toClient, conn);
					} 					
					else if(AConnectionManager.get(Long.parseLong(number))==null)
					{
						// ����
						if (user.password.equals(pwd)) {
							// ��¼ �ɹ�
							toClient.type = AMessageType.MSG_TYPE_BUDDYLIST;
							toClient.fromName=user.name;
							// �����û�����
							// ��������ݵ����Ӷ���
							conn.who = user;
							AConnectionManager.put(user.number, conn);
							Db.getBuddyByAccount(user.number).online="����";
							
							BuddyList list = Db.dblist;
							toClient.content = list.toJson();
							toEveryClient(toClient);
							
							Thread.sleep(10);
							
							// ����Ⱥ������
							AMessage toClient2 = new AMessage();
							GroupList grouplist=AConnectionManager.grouplist;
							toClient2.content = grouplist.toJson();
							toClient2.type = AMessageType.MSG_TYPE_GROUPLIST;
							toClient2.fromName=user.name;
							toClient(toClient2,conn);
							
							Thread.sleep(10);
							
							// ���ػ����
							AMessage toClient3 = new AMessage();

							toClient3.type = AMessageType.MSG_TYPE_PLANLIST;
							toClient3.fromName=user.name;
							toClient(toClient3,conn);
							
							//�����������̣߳�ÿ5����һ�����ӿ��أ�������عر����ж���ÿͻ��˵�����
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
											//����رտ���
											conn.check=false;
											Thread.sleep(5000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}
							}.start();
							
							Thread.sleep(600);
							//���ʹ洢����Ϣ
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
							//�ʺ��������
							toClient.type = AMessageType.MSG_TYPE_FAIL;
							toClient.content = "�ʺŻ��������";
							toClient(toClient, conn);
						}
					}
					else
					{
						//�û��ѵ�½
						toClient.type = AMessageType.MSG_TYPE_FAIL;
						toClient.content = "�Բ����˻��ѵ�½�������ظ���¼";
						toClient(toClient, conn);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}