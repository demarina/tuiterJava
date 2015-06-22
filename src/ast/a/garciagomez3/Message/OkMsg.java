package ast.a.garciagomez3.Message;

public class OkMsg extends MsgString{
	public OkMsg (String info){
		super(info);
		this.type = MsgType.OkMsg;
	}
}
