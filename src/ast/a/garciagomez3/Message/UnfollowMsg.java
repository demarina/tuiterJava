package ast.a.garciagomez3.Message;

public class UnfollowMsg extends MsgString{
	public UnfollowMsg(String info){
		super(info);
		this.type = MsgType.UnfollowMsg;
	}
}
