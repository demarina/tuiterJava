package ast.a.garciagomez3.Message;

public class RetuitMsg extends MsgInt{
	public RetuitMsg(int id){
		super(id);
		this.type = MsgType.RetuitMsg;
	}
	public RetuitMsg(){
		super();
		this.type = MsgType.RetuitMsg;
	}
}
