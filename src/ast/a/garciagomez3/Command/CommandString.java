package ast.a.garciagomez3.Command;

public class CommandString extends Command{
	protected String info;
	public CommandString(String info){
		this.info = info;
	}
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
}
