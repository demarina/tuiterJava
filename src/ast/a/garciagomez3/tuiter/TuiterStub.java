package ast.a.garciagomez3.tuiter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ast.a.garciagomez3.Command.FollowCom;
import ast.a.garciagomez3.Command.LoginCom;
import ast.a.garciagomez3.Command.RequestTuitCom;
import ast.a.garciagomez3.Command.RetuitCom;
import ast.a.garciagomez3.Command.TuitCom;
import ast.a.garciagomez3.Command.UnfollowCom;
import ast.a.garciagomez3.Command.UsersCom;
import ast.a.garciagomez3.Message.FollowMsg;
import ast.a.garciagomez3.Message.LoginMsg;
import ast.a.garciagomez3.Message.MsgString;
import ast.a.garciagomez3.Message.MsgType;
import ast.a.garciagomez3.Message.RequestTuitMsg;
import ast.a.garciagomez3.Message.RetuitMsg;
import ast.a.garciagomez3.Message.TuitListMsg;
import ast.a.garciagomez3.Message.TuitMsg;
import ast.a.garciagomez3.Message.UnfollowMsg;
import ast.a.garciagomez3.Message.UsersListMsg;
import ast.a.garciagomez3.Message.UsersMsg;
import ast.a.garciagomez3.tools.Marshaller;

public class TuiterStub {
	private DataInputStream idata;
	private DataOutputStream odata;
	private static final int INTSIZE = 4;

	public TuiterStub (DataInputStream idata, DataOutputStream odata){
		this.idata = idata;
		this.odata = odata;
	}
	public MsgString receiveMsgString(DataInputStream idata){
		byte i[] = new byte [INTSIZE];
		int size = 0;
		MsgType type = null;
		try {
			idata.readFully(i);
		size = Marshaller.unmars_int(i);
		byte t[] = new byte [INTSIZE];
		idata.readFully(t);
		type = MsgType.values()[(Marshaller.unmars_int(t))];
		} catch (IOException e) {
			e.printStackTrace();
		}
		MsgString msg = MsgString.receive(idata, size, type);
		return msg;
	}
	public UsersListMsg receiveUsersList(DataInputStream idata){
		byte i[] = new byte [INTSIZE];
		int size = 0;
		try {
			idata.readFully(i);
		size = Marshaller.unmars_int(i);
		byte t[] = new byte [INTSIZE];
		idata.readFully(t);
		@SuppressWarnings("unused")
		MsgType type = MsgType.values()[(Marshaller.unmars_int(t))];
		} catch (IOException e){
			e.printStackTrace();
		}
		UsersListMsg msg = UsersListMsg.receive(idata, size);
		return msg;
	}
	public TuitListMsg receiveTuitList(DataInputStream idata){
		byte i[] = new byte [INTSIZE];
		int size = 0;
		try {
			idata.readFully(i);
		size = Marshaller.unmars_int(i);
		byte t[] = new byte [INTSIZE];
		idata.readFully(t);
		@SuppressWarnings("unused")
		MsgType type = MsgType.values()[(Marshaller.unmars_int(t))];
		} catch (IOException e){
			e.printStackTrace();
		}
		TuitListMsg msg = TuitListMsg.receive(idata, size);
		return msg;
	}
	
	public MsgString Login (LoginCom cmd){
		LoginMsg lg = new LoginMsg(cmd.getInfo());
		lg.send(odata);
		MsgString msg = (MsgString)receiveMsgString(idata);
		return msg;
	}
	public MsgString Tuit (TuitCom cmd){
		TuitMsg tu = new TuitMsg(cmd.getInfo());
		tu.send(odata);
		MsgString msg = (MsgString)receiveMsgString(idata);
		return msg;
	}
	public MsgString Follow (FollowCom cmd){
		FollowMsg fo = new FollowMsg(cmd.getInfo());
		fo.send(odata);
		MsgString msg = (MsgString)receiveMsgString(idata);
		return msg;
	}
	public MsgString Unfollow (UnfollowCom cmd){
		UnfollowMsg fo = new UnfollowMsg(cmd.getInfo());
		fo.send(odata);
		MsgString msg = (MsgString)receiveMsgString(idata);
		return msg;
	}
	public UsersListMsg Users(UsersCom cmd){
		UsersMsg us = new UsersMsg();
		us.send(odata);
		UsersListMsg msg = receiveUsersList(idata);
		return msg;
	}
	public TuitListMsg RequestTuit(RequestTuitCom cmd){
		RequestTuitMsg reqT = new RequestTuitMsg();
		reqT.send(odata);
		TuitListMsg msg = receiveTuitList(idata);
		return msg;
	}
	public MsgString Retuit (RetuitCom cmd){
		RetuitMsg reTu = new RetuitMsg(cmd.getId());
		reTu.send(odata);
		MsgString msg = (MsgString)receiveMsgString(idata);
		return msg;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
