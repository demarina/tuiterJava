package ast.a.garciagomez3.Message;

public class TuitMsg extends MsgString{
	public TuitMsg(String info){
		super(info);
		this.type = MsgType.TuitMsg;
	}
}
