package ast.a.garciagomez3.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ast.a.garciagomez3.tools.Marshaller;

public class MsgInt extends Msg{
	protected int id;
	public MsgInt(int id){
		this.size = INTSIZE;
		this.id = id;
	}
	public MsgInt(){
		this.size = 0;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public byte[] marshall() {
		byte b1[];
		b1 = Marshaller.mars_int(this.size);
		byte b2[];
		b2 = Marshaller.mars_int(this.type.ordinal());
		byte b3[];
		b3 = Marshaller.mars_int(this.id);
		byte btotal[] = new byte [3*INTSIZE];
		System.arraycopy(b1, 0, btotal, 0, b1.length);
		System.arraycopy(b2, 0, btotal, INTSIZE, b2.length);
		System.arraycopy(b3, 0, btotal, 2*INTSIZE, b3.length);
		return btotal;
	}
	@Override
	public void unmarshall(byte[] b) {
	}
	
	public static MsgInt receive(DataInputStream idata, int size, MsgType type){
		byte b1[] = new byte[INTSIZE];
		RetuitMsg msg = new RetuitMsg();
		try {
			idata.readFully(b1);
			int id = Marshaller.unmars_int(b1);
			msg = new RetuitMsg(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
	@Override
	public void send(DataOutputStream odata) {
		byte[] b = this.marshall();
		try {
			odata.write(b);
			odata.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
