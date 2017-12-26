
//服务器管理所用的用户信息的类

package message;



public class User extends ProtocalObj {
	public long number;			// 账号 
	public String password = "";// 密码
	public String name = "";	// 昵称
	public String sign;			//个性签名
	public String groupInfo;	//群组信息
	public int avatar;			// 头像(未用)

}
