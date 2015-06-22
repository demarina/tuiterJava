package ast.a.garciagomez3.Command;

public class LoginCom extends CommandString{
	public LoginCom(String info){
		super(info);
		this.type = ComType.LoginCom;
	}
}
