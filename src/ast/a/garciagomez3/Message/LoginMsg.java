package ast.a.garciagomez3.Message;

public class LoginMsg extends MsgString{
	public LoginMsg(String info){
		super(info);
		this.type = MsgType.LoginMsg;
	}
}
