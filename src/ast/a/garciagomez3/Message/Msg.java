package ast.a.garciagomez3.Message;

import java.io.DataOutputStream;

public abstract class Msg {
	protected int size;
	protected MsgType type;
	protected final static int INTSIZE = 4;

	public abstract byte[] marshall();
	public abstract void unmarshall(byte[] b);
	public abstract void send (DataOutputStream odata);
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public MsgType getType() {
		return type;
	}
	public void setType(MsgType type) {
		this.type = type;
	}
}