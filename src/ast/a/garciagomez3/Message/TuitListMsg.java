package ast.a.garciagomez3.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ast.a.garciagomez3.tools.Marshaller;
import ast.a.garciagomez3.tools.Tuit;
import ast.a.garciagomez3.tools.TuitId;

public class TuitListMsg extends Msg{
	private int numTuits;
	private List<TuitId> tuits;
	
	private int knowSize(List<TuitId> list){
		int size = INTSIZE;
		for (int i = 0;i < list.size();i++){
			try {
				size += INTSIZE;
				size += list.get(i).getTuit().getCreater().getBytes("UTF-8").length;
				size += INTSIZE;
				size += list.get(i).getTuit().getText().getBytes("UTF-8").length;
				size += INTSIZE;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return size;
	}
	
	public TuitListMsg(List<TuitId> list){
		this.type = MsgType.TuitListMsg;
		this.numTuits = list.size();
		this.tuits = list;
		this.size = knowSize(list);
	}
	
	public int getNumTuits() {
		return numTuits;
	}

	public void setNumTuits(int numTuits) {
		this.numTuits = numTuits;
	}

	public List<TuitId> getTuits() {
		return tuits;
	}

	public void setTuits(List<TuitId> tuits) {
		this.tuits = tuits;
	}

	@Override
	public byte[] marshall() {
		byte b1[];
		b1 = Marshaller.mars_int(this.size);
		byte b2[];
		b2 = Marshaller.mars_int(this.type.ordinal());
		byte b3[];
		b3 = Marshaller.mars_int(this.numTuits);
		byte[] btotal = new byte[this.size + INTSIZE + INTSIZE];
		System.arraycopy(b1, 0, btotal, 0, b1.length);
		System.arraycopy(b2, 0, btotal, b1.length, b2.length);
		System.arraycopy(b3, 0, btotal, 2*INTSIZE, INTSIZE);
		int length = 3*INTSIZE;
		for(int i = 0; i < tuits.size();i++){
			byte b4[];
			b4 = Marshaller.mars_int(tuits.get(i).getTuit().getCreater().length());
			byte b5[];
			b5 = Marshaller.mars_str(tuits.get(i).getTuit().getCreater());
			byte b6[];
			b6 = Marshaller.mars_int(tuits.get(i).getTuit().getText().length());
			byte b7[];
			b7 = Marshaller.mars_str(tuits.get(i).getTuit().getText());
			byte b8[];
			b8 = Marshaller.mars_int(tuits.get(i).getId());
			System.arraycopy(b4, 0, btotal, length, b4.length);
			length += b4.length;
			System.arraycopy(b5, 0, btotal, length, b5.length);
			length += b5.length;
			System.arraycopy(b6, 0, btotal, length, b6.length);
			length += b6.length;
			System.arraycopy(b7, 0, btotal, length, b7.length);
			length += b7.length;
			System.arraycopy(b8, 0, btotal, length, b8.length);
			length += b8.length;
		}
		return btotal;
	}

	@Override
	public void unmarshall(byte[] b) {

	}
	public static TuitListMsg receive (DataInputStream idata, int size){
		byte b1[] = new byte[INTSIZE];
		List<TuitId> list = new ArrayList<TuitId>();
		try {
			idata.readFully(b1);
			int numberTuits = Marshaller.unmars_int(b1);
			for(int i = 0;i < numberTuits;i++){
				byte b2[] = new byte[INTSIZE];
				idata.readFully(b2);
				int lenNick = Marshaller.unmars_int(b2);
				byte b3[] = new byte[lenNick];
				idata.readFully(b3);
				String nick = Marshaller.unmars_str(b3, lenNick);
				byte b4[] = new byte [INTSIZE];
				idata.readFully(b4);
				int lenText = Marshaller.unmars_int(b4);
				byte b5[] = new byte [lenText];
				idata.readFully(b5);
				String text = Marshaller.unmars_str(b5, lenText);
				byte b6[] = new byte [INTSIZE];
				idata.readFully(b6);
				int id = Marshaller.unmars_int(b6);
				Tuit tuit = new Tuit(nick, text, 0);
				TuitId tuitComplet = new TuitId(id, tuit, false);
				list.add(tuitComplet);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		TuitListMsg msg = new TuitListMsg(list);
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
