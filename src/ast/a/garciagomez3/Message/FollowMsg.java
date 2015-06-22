package ast.a.garciagomez3.Message;

public class FollowMsg extends MsgString{
	public FollowMsg(String info){
		super(info);
		this.type = MsgType.FollowMsg;
	}
}
