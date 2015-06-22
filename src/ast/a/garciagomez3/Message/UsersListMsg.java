package ast.a.garciagomez3.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ast.a.garciagomez3.tools.Marshaller;

public class UsersListMsg extends Msg{
	private int numberUsers;
	private List<String> usersList;
	
	private int knowSize(List<String> list){

		int number = INTSIZE;
		for(int i = 0;i < list.size();i++){
			number += INTSIZE;
			try {
				number += list.get(i).getBytes("UTF-8").length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return number;
	}
	public UsersListMsg(){
		this.numberUsers = 0;
		this.usersList = new ArrayList<String>();
		this.size = 0;
	}
	
	public UsersListMsg(List<String> list){		
		this.type = MsgType.UsersListMsg;
		this.numberUsers = list.size();
		this.usersList = list;
		this.size = knowSize(list);
	}
	
	public int getNumberUsers() {
		return numberUsers;
	}
	public void setNumberUsers(int numberUsers) {
		this.numberUsers = numberUsers;
	}
	public List<String> getUsersList() {
		return usersList;
	}
	public void setUsersList(List<String> usersList) {
		this.usersList = usersList;
	}
	@Override
	public byte[] marshall() {
		byte[] b1;
		b1 = Marshaller.mars_int(this.size);
		byte[] b2;
		b2 = Marshaller.mars_int(this.type.ordinal());
		byte[] b5;
		b5 = Marshaller.mars_int(this.numberUsers);
		byte[] btotal = new byte[this.size + INTSIZE + INTSIZE];
		System.arraycopy(b1, 0, btotal, 0, b1.length);
		System.arraycopy(b2, 0, btotal, b1.length, b2.length);
		System.arraycopy(b5, 0, btotal, 2*INTSIZE, INTSIZE);
		int length = 3*INTSIZE;
		for(int i = 0; i < numberUsers; i++){
			byte[] b3;
			b3 = Marshaller.mars_int(this.usersList.get(i).length());
			byte[] b4;
			b4 = Marshaller.mars_str(this.usersList.get(i));
			System.arraycopy(b3, 0, btotal, length, b3.length);
			System.arraycopy(b4, 0, btotal, length + INTSIZE, b4.length);
			length += (INTSIZE + b4.length);
		}
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
		
	}
	
	public static UsersListMsg receive(DataInputStream idata, int size){
		byte b1[] = new byte[INTSIZE];
		List<String> list = new ArrayList<String>();
		try {
			idata.readFully(b1);
			int numberUsers = Marshaller.unmars_int(b1);
			for(int i = 0;i < numberUsers;i++){
				byte b2[] = new byte[INTSIZE];
				idata.readFully(b2);
				int len = Marshaller.unmars_int(b2);
				byte b3[] = new byte[len];
				idata.readFully(b3);
				String name = Marshaller.unmars_str(b3, len);
				list.add(name);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		UsersListMsg msg = new UsersListMsg(list);
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
