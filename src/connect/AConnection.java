
//主机端存储连接的类，每一个连接进来的客户端都会被存进一个这样的类

package connect;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import message.AMessage;
import message.AMessageType;
import message.User;
import server.LogOutListener;

public class AConnection extends Thread {
	private Socket scoket = null;
	public DataOutputStream writer = null;
	public DataInputStream reader = null;
	public User who = null;
	public boolean check=true;

	public String ip;
	public int port;

	//构造方法，参数是客户端的socket
	public AConnection(Socket scoket) {
		super();
		try {
			this.scoket = scoket;
			writer = new DataOutputStream(this.scoket.getOutputStream());
			reader = new DataInputStream(this.scoket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//构造方法，参数是客户端的ip和端口
	public AConnection(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
		init(ip, port);
	}

	private void init(String ip, int port) {
		try {
			this.scoket = new Socket(ip, port);
			writer = new DataOutputStream(this.scoket.getOutputStream());
			reader = new DataInputStream(this.scoket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 连接
	public void connect() {
		if (this.scoket == null) {
			init(ip, port);
		}
		flag = true;
		start();
	}

	// 断开连接
	public void disconnect() {
		try {
			flag = false;
			writer.close();
			reader.close();
			// stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ----------监听器------
	// 1.申明监听器与响应方法
	public static interface OnRecevieMsgListener {
		public void onReceive(AMessage msg);
	}

	// 2.支持多个监听器
	private List<OnRecevieMsgListener> listeners = new ArrayList<OnRecevieMsgListener>();

	// 3.添加监听器
	public void addOnRecevieMsgListener(OnRecevieMsgListener listener) {
		listeners.add(listener);
	}

	// 4.删除监听器
	public void removeOnRecevieMsgListener(OnRecevieMsgListener listener) {
		listeners.remove(listener);
	}

	private boolean flag = true;

	@Override
	public void run() {
		super.run();
		// 等待 数据
		
		while (flag) {
			try {
				String xml = reader.readUTF();
				if (xml != null && !"".equals(xml)) {
					AMessage msg = new AMessage();
					msg = (AMessage) msg.fromXml(xml);
					for (OnRecevieMsgListener l : listeners) {
						l.onReceive(msg);
						if (!msg.type.equals(AMessageType.MSG_TYPE_ONLINE))
							System.out.println(xml);
					}
				}

			} catch (EOFException e) {
//				e.printStackTrace();
				System.out.println("=-=EOFException---");
				if (who != null) {
					AConnectionManager.remove(who.number);
					send();
				}
				disconnect();
			} catch (Exception e) {
				e.printStackTrace();
				if (who != null) {
					AConnectionManager.remove(who.number);
					send();
				}
				disconnect();
			}
		}

	}
	
	//发送方法，给每个客户端发送一份好友列表
	public void send()
	{
		new LogOutListener().send();
	}
}
