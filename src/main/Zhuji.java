//===============================================================
//Zhuji��
//�������˵������࣬�������ĺ���
//===============================================================

package main;

//���ߣ��ź���

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;

import connect.AConnection;
import connect.AConnectionManager;
import message.Db;
import server.AddfriendListener;
import server.AddgroupListener;
import server.AddplanListener;
import server.ChatP2PListener;
import server.ChatRoomListener;
import server.LogOutListener;
import server.LoginMsgListener;
import server.NewGroupListener;
import server.NewPlanListener;
import server.OnLineListener;
import server.PlanListener;
import server.SignListener;
import server.ZhuceListener;

public class Zhuji {

	public static Connection c = null;
	public static Statement stmt = null;
	public static Connection d = null;
	public static Statement stmt2 = null;
	public static Connection e = null;
	public static Statement stmt3 = null;

	public static void main(String[] args) {
		try {
			//����һ���߳� �������ͻ��˵�����
			final ServerSocket server = new ServerSocket(8080);
			System.out.println("---����������---" + new Date().toString());

			//��ʼ�����ݿ�
			Class.forName("org.sqlite.JDBC");
			//�û����ݿ�
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			//Ⱥ�����ݿ�
			d = DriverManager.getConnection("jdbc:sqlite:group.db");
			d.setAutoCommit(false);
			stmt2 = d.createStatement();
			//����ݿ�
			e = DriverManager.getConnection("jdbc:sqlite:plan.db");
			e.setAutoCommit(false);
			stmt3 = e.createStatement();
			new Db();
			new AConnectionManager();

			//����ͻ������ӳɹ�������һ���̣߳����������
			new Thread()
			{
				public void run()
				{
					while (true) {
						AConnection conn = null;
						try {
							Socket client = server.accept();
							System.out.println("---�пͻ��˽���---" + client);
							conn = new AConnection(client);
							conn.addOnRecevieMsgListener(new LoginMsgListener(conn));
							conn.addOnRecevieMsgListener(new ChatP2PListener());
							conn.addOnRecevieMsgListener(new SignListener());
							conn.addOnRecevieMsgListener(new ChatRoomListener());
							conn.addOnRecevieMsgListener(new LogOutListener());
							conn.addOnRecevieMsgListener(new AddfriendListener());
							conn.addOnRecevieMsgListener(new AddgroupListener());
							conn.addOnRecevieMsgListener(new AddplanListener());
							conn.addOnRecevieMsgListener(new PlanListener());
							conn.addOnRecevieMsgListener(new NewGroupListener());
							conn.addOnRecevieMsgListener(new NewPlanListener());
							conn.addOnRecevieMsgListener(new ZhuceListener(conn));
							conn.addOnRecevieMsgListener(new OnLineListener(conn));
							//���߳��ڵȴ��û�����
							conn.connect();
						} catch (IOException e) {
							e.printStackTrace();
							conn.disconnect();
						}
					}
				};
			}.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
