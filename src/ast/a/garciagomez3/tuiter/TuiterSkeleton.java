package ast.a.garciagomez3.tuiter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ast.a.garciagomez3.Message.ErrorMsg;
import ast.a.garciagomez3.Message.FollowMsg;
import ast.a.garciagomez3.Message.LoginMsg;
import ast.a.garciagomez3.Message.Msg;
import ast.a.garciagomez3.Message.MsgInt;
import ast.a.garciagomez3.Message.MsgString;
import ast.a.garciagomez3.Message.MsgType;
import ast.a.garciagomez3.Message.OkMsg;
import ast.a.garciagomez3.Message.RequestTuitMsg;
import ast.a.garciagomez3.Message.RetuitMsg;
import ast.a.garciagomez3.Message.TuitListMsg;
import ast.a.garciagomez3.Message.TuitMsg;
import ast.a.garciagomez3.Message.UnfollowMsg;
import ast.a.garciagomez3.Message.UsersListMsg;
import ast.a.garciagomez3.Message.UsersMsg;
import ast.a.garciagomez3.tools.Marshaller;

public class TuiterSkeleton {
	private DataOutputStream odata;
	private static final int INTSIZE = 4;

	public TuiterSkeleton(DataOutputStream odata, DataInputStream idata){
		this.odata = odata;
	}

	public Msg hear(DataInputStream idata){
		try {
			byte i[] = new byte[INTSIZE];
			idata.readFully(i);
			int size = Marshaller.unmars_int(i);
			byte b[] = new byte[INTSIZE];
			idata.readFully(b);
			MsgType type = MsgType.values()[(Marshaller.unmars_int(b))];
			switch (type){
			case FollowMsg:
				FollowMsg msgF = (FollowMsg)MsgString.receive(idata, size, type);
				return msgF;
			case LoginMsg:
				LoginMsg msgL = (LoginMsg)MsgString.receive(idata, size, type);
				return msgL;
			case RetuitMsg:
				RetuitMsg msgRet = (RetuitMsg)MsgInt.receive(idata, size, type);
				return msgRet;
			case TuitMsg:
				TuitMsg msgT = (TuitMsg)MsgString.receive(idata, size, type);
				return msgT;
			case UnfollowMsg:
				UnfollowMsg msgUf = (UnfollowMsg)MsgString.receive(idata, size, type);
				return msgUf;
			case UsersMsg:
				UsersMsg msgUs = new UsersMsg();
				return msgUs;
			case RequestTuitMsg:
				RequestTuitMsg msgReq = new RequestTuitMsg();
				return msgReq;
			default:
				break;
			}
		} catch (IOException e) {
			System.err.println("Client disconnecting...");
		}
		return null;
	}
	public void Response(Msg msg){
		MsgType type = msg.getType();
		switch (type){
		case ErrorMsg:
			((ErrorMsg)msg).send(odata);
			break;
		case TuitListMsg:
			((TuitListMsg)msg).send(odata);
			break;
		case OkMsg:
			((OkMsg)msg).send(odata);
			break;
		case UsersListMsg:
			((UsersListMsg)msg).send(odata);
			break;
		default:
			break;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
