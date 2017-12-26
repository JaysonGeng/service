
//数据库所有用户信息的类

package message;



public class Buddy extends ProtocalObj {
	public long number;			// 账号 
	public String name = "";	// 昵称
	public int avatar;			// 头像
	public String sign="";		//个性签名
	public String groupInfo="";	//添加的群组信息
	public String online="离线";	//在线状态
}
