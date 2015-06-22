package ast.a.garciagomez3.Command;

public class FollowCom extends CommandString{
	public FollowCom(String info){
		super(info);
		this.type = ComType.FollowCom;
	}
}
