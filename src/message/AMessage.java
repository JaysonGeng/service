
//�洢�������Ϣ����

package message;



public class AMessage extends ProtocalObj 
{
	public String type = AMessageType.MSG_TYPE_CHAT_P2P;// ��Ϣ����
	public long from = 0;// ������ �ʺ�
	public String fromName = "";// �ǳ�
	public int fromAvatar = 1;// ͷ��
	public long to = 0; // ������ �ʺ�
	public String content = ""; // ��Ϣ������
	public String sendTime = MyTime.geTime(); // ����ʱ��
	public String emoji = "";
}
