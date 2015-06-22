package ast.a.garciagomez3.Message;

public class ErrorMsg extends MsgString{
	public ErrorMsg (String info){
		super(info);
		this.type = MsgType.ErrorMsg;
	}
}
