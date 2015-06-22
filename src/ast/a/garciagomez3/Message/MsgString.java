package ast.a.garciagomez3.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import ast.a.garciagomez3.tools.Marshaller;


public class MsgString extends Msg{
	protected String info;
	
	public MsgString(){
		this.size = 0;
		this.type = null;
		this.info = "";
	}
	
	public MsgString(String info){
		try {
			this.size = info.getBytes("UTF-8").length;
			this.info = info;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public byte[] marshall() {
		byte[] b1;
		b1 = Marshaller.mars_int(this.size);
		byte[] b2;
		b2 = Marshaller.mars_int(this.type.ordinal());
		byte[] b3;
		b3 = Marshaller.mars_str(info);
		byte[] btotal = new byte[this.size + INTSIZE + INTSIZE];
		System.arraycopy(b1, 0, btotal, 0, b1.length);
		System.arraycopy(b2, 0, btotal, b1.length, b2.length);
		System.arraycopy(b3, 0, btotal, b1.length + b2.length, b3.length);
		return btotal;
	}

	@Override
	public void unmarshall(byte[] b) {
		this.size = Marshaller.unmars_int(b);
		byte[] b2 = new byte[INTSIZE];
		System.arraycopy(b, INTSIZE, b2, 0, INTSIZE);
		this.type = MsgType.values()[(Marshaller.unmars_int(b2))];
		byte[] b3 = new byte [size];
		System.arraycopy(b, 2*INTSIZE, b3, 0, b3.length);
		this.info = Marshaller.unmars_str(b3,size);
	}

	public static MsgString receive (DataInputStream idata, int size, MsgType type){
		byte log[] = new byte[size];
		MsgString msg = new MsgString();
		try {
			idata.readFully(log);
			String info = Marshaller.unmars_str(log,size);
			switch(type){ //QUITAR LOS TIPOS DE MENSAJE QUE NO SEAN 'MSG-STRING'
			case ErrorMsg:
				msg = new ErrorMsg(info);
				break;
			case FollowMsg:
				msg = new FollowMsg(info);
				break;
			case TuitListMsg:
				break;
			case LoginMsg:
				msg = new LoginMsg(info);
				break;
			case OkMsg:
				msg = new OkMsg(info);
				break;
			case RetuitMsg:
				break;
			case TuitMsg:
				msg = new TuitMsg(info);
				break;
			case UnfollowMsg:
				msg = new UnfollowMsg(info);
				break;
			case UsersListMsg:
				break;
			case UsersMsg:
				break;
			default:
				break; 
			}
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
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
}
