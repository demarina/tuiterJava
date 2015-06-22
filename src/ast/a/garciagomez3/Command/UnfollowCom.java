package ast.a.garciagomez3.Command;

public class UnfollowCom extends CommandString{
	public UnfollowCom(String info){
		super(info);
		this.type = ComType.UnfollowCom;
	}
}
