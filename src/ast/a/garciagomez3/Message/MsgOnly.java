package ast.a.garciagomez3.Message;

import java.io.DataOutputStream;
import java.io.IOException;

import ast.a.garciagomez3.tools.Marshaller;

public class MsgOnly extends Msg{

	@Override
	public byte[] marshall() {
		byte[] b1;
		b1 = Marshaller.mars_int(this.size);
		byte[] b2;
		b2 = Marshaller.mars_int(this.type.ordinal());
		byte[] btotal = new byte[INTSIZE + INTSIZE];
		System.arraycopy(b1, 0, btotal, 0, b1.length);
		System.arraycopy(b2, 0, btotal, b1.length, b2.length);
		return btotal;
	}

	@Override
	public void unmarshall(byte[] b) {
		this.size = Marshaller.unmars_int(b);
		byte[] b2 = new byte[INTSIZE];
		System.arraycopy(b, INTSIZE, b2, 0, INTSIZE);
		this.type = MsgType.values()[(Marshaller.unmars_int(b2))];
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
