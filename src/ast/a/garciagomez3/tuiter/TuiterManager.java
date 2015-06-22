package ast.a.garciagomez3.tuiter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import ast.a.garciagomez3.Command.ComType;
import ast.a.garciagomez3.Command.Command;
import ast.a.garciagomez3.Command.FollowCom;
import ast.a.garciagomez3.Command.LoginCom;
import ast.a.garciagomez3.Command.RequestTuitCom;
import ast.a.garciagomez3.Command.RetuitCom;
import ast.a.garciagomez3.Command.TuitCom;
import ast.a.garciagomez3.Command.UnfollowCom;
import ast.a.garciagomez3.Command.UsersCom;
import ast.a.garciagomez3.Message.MsgString;
import ast.a.garciagomez3.Message.MsgType;
import ast.a.garciagomez3.Message.TuitListMsg;
import ast.a.garciagomez3.Message.UsersListMsg;
import ast.a.garciagomez3.tools.TuitId;

public class TuiterManager implements Runnable {
	private BlockingQueue<Command> blocky; 
	private BlockingQueue<Boolean> blockyLogin;
	private DataOutputStream odata;
	private DataInputStream idata;
	private TuiterStub stub;

	public TuiterManager(BlockingQueue<Command> blocky, BlockingQueue<Boolean> blockyLogin, DataOutputStream odata, DataInputStream idata){
		this.blocky = blocky;
		this.blockyLogin = blockyLogin;
		this.idata = idata;
		this.odata = odata;
		this.stub = new TuiterStub(this.idata, this.odata);
	}
	
	private void processLogin (Command com){
		MsgString msg = stub.Login((LoginCom)com);
		System.err.println(msg.getInfo());
		try{
			if (msg.getType().equals(MsgType.OkMsg)){
				blockyLogin.put(true);
			}else{
				blockyLogin.put(false);
			}
		}catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	private void processTuit (Command com){
		MsgString msg = stub.Tuit((TuitCom)com);
		System.err.println(msg.getInfo());
	}
	private void processFollow (Command com){
		MsgString msg = stub.Follow((FollowCom)com);
		System.err.println(msg.getInfo());
	}
	private void processUnfollow (Command com){
		MsgString msg = stub.Unfollow((UnfollowCom)com);
		System.err.println(msg.getInfo());
	}
	private void showUsers(UsersListMsg msg){
		List<String> list = msg.getUsersList();
		System.out.println("USERS:");
		for(int i = 0;i < list.size();i++){
			String name = list.get(i);
			System.out.println(name + " ");
		}
	}
	private void processUsers (Command com){
		UsersListMsg msg = stub.Users((UsersCom)com);
		showUsers(msg);
	}
	private void showTuits (TuitListMsg msg){
		List<TuitId> list = msg.getTuits();
		if (list.size() != 0){
			for(int i = 0;i < list.size();i++){
				System.out.println(list.get(i).getId() + ". " + list.get(i).getTuit().getCreater()
						+ list.get(i).getTuit().getText());
			}
		}
	}
	private void processRequestTuit(Command com){
		TuitListMsg msg = stub.RequestTuit((RequestTuitCom)com);
		showTuits(msg);
	}
	private void processRetuit(Command com){
		MsgString msg = stub.Retuit((RetuitCom)com);
		System.out.println(msg.getInfo());
	}
	
	@Override
	public void run() {
		for(;;){
			try {
				Command com = blocky.take();
				ComType type = com.getType();
				switch (type){
				case LoginCom: 
					processLogin(com);
					break;
				case FollowCom:
					processFollow(com);
					break;
				case RetuitCom:
					processRetuit(com);
					break;
				case TuitCom:
					processTuit(com);
					break;
				case UnfollowCom:
					processUnfollow(com);
					break;
				case UsersCom:
					processUsers(com);
					break;
				case RequestTuitCom:
					processRequestTuit(com);
					break;
				default:
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
