package ast.a.garciagomez3.tuiter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ast.a.garciagomez3.Message.ErrorMsg;
import ast.a.garciagomez3.Message.FollowMsg;
import ast.a.garciagomez3.Message.RetuitMsg;
import ast.a.garciagomez3.Message.TuitListMsg;
import ast.a.garciagomez3.Message.UnfollowMsg;
import ast.a.garciagomez3.Message.LoginMsg;
import ast.a.garciagomez3.Message.Msg;
import ast.a.garciagomez3.Message.OkMsg;
import ast.a.garciagomez3.Message.TuitMsg;
import ast.a.garciagomez3.Message.UsersListMsg;
import ast.a.garciagomez3.tools.Profile;
import ast.a.garciagomez3.tools.Tuit;
import ast.a.garciagomez3.tools.TuitId;

class Laborer implements Runnable{
	private DataOutputStream odata;
	private DataInputStream idata;
	private String login;
	private TuiterSkeleton skeleton;
	private ConcurrentHashMap<String, Profile> dataBase;
	private final static int retAll = 5;

	public Laborer(Socket socket, ConcurrentHashMap<String,Profile> dataBase) throws Exception{
		try{
			odata = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			idata = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			this.skeleton = new TuiterSkeleton(odata, idata);
			this.dataBase = dataBase;
		}catch (Exception e){
			System.err.println("Error to get stream of the client");
			e.printStackTrace();
			throw e;
		}
	}

	public void process(Msg msg){
		switch (msg.getType()){
		case FollowMsg:
			if (Follow((FollowMsg)msg)){
				OkMsg okF = new OkMsg("Follow OK");
				skeleton.Response(okF);
			}else{
				ErrorMsg errorF = new ErrorMsg("Follow Error");
				skeleton.Response(errorF);
			}
			break;
		case LoginMsg:
			if (Login((LoginMsg)msg)){
				OkMsg okL = new OkMsg("Logged correctly! with username " + this.login);
				skeleton.Response(okL);
			}else{
				ErrorMsg errorL = new ErrorMsg("Error to login, try again");
				skeleton.Response(errorL);
			}
			break;
		case RetuitMsg:
			if (Retuit((RetuitMsg)msg)){
				OkMsg okR = new OkMsg("Retuit OK");
				skeleton.Response(okR);
			}else{
				ErrorMsg errorR = new ErrorMsg("Retuit Error");
				skeleton.Response(errorR);
			}
			break;
		case TuitMsg:
			if (Tuit((TuitMsg)msg)){
				OkMsg okT = new OkMsg("Tuit sent correctly");
				skeleton.Response(okT);
			}else{
				ErrorMsg errorT = new ErrorMsg("Error to send Tuit");
				skeleton.Response(errorT);
			}
			break;
		case UnfollowMsg:
			if (Unfollow((UnfollowMsg)msg)){
				OkMsg okUf = new OkMsg("Unfollow Ok");
				skeleton.Response(okUf);
			}else{
				ErrorMsg errorUf = new ErrorMsg("Unfollow Error");
				skeleton.Response(errorUf);
			}
			break;
		case UsersMsg:
			UsersListMsg usList = Users();
			skeleton.Response(usList);
			break;
		case RequestTuitMsg:
			TuitListMsg tuitList = RequestTuit();
			skeleton.Response(tuitList);
		default:
			break;
		}
	}
	public boolean Login (LoginMsg log){
		this.login = log.getInfo();
		boolean success = false;
		if (!dataBase.containsKey(this.login)){
			Profile prof = new Profile(this.login);
			dataBase.put(this.login, prof);
			success = true;
			System.err.println(log.getInfo() + " ,Logged correctly!");
		}else{
			System.err.println(log.getInfo() + " Logged error, the nick already exists");
		}
		return success;
	}
	public boolean Tuit (TuitMsg tu){
		Tuit tuit = new Tuit(this.login, tu.getInfo(), 0);	
		dataBase.get(this.login).tuitear(tuit);
		List<String> followersList = dataBase.get(this.login).getFollowers();
		for (int i = 0;i < followersList.size();i++){
			String name = followersList.get(i);
			dataBase.get(name).tuitear(tuit);
		}
		System.err.println(this.login + "'s TUIT stored correctly");
		return true;
	}
	public boolean Follow (FollowMsg fo){
		boolean success = false;
		if (!this.login.equals(fo.getInfo()) && (dataBase.containsKey(fo.getInfo()))
				&& !dataBase.get(this.login).getFollows().contains(fo.getInfo())){
			dataBase.get(this.login).getFollows().add(fo.getInfo());
			System.err.println(this.login + " FOLLOW at " + fo.getInfo());
			dataBase.get(fo.getInfo()).getFollowers().add(this.login);
			success = true;
		}else{
			System.err.println("Error to make FOLLOW " + this.login + " -> " + fo.getInfo());
		}
		return success;
	}
	public boolean Unfollow (UnfollowMsg uf){
		boolean success = false;
		if (!this.login.equals(uf.getInfo()) && (dataBase.containsKey(uf.getInfo()))){
			if(dataBase.get(this.login).getFollows().contains(uf.getInfo())){
				dataBase.get(this.login).getFollows().remove(uf.getInfo());
				dataBase.get(uf.getInfo()).getFollowers().remove(this.login);
				System.err.println(this.login + " UNFOLLOW at " + uf.getInfo());
				success = true;
			}
		}else{
			System.err.println("Error to make UNFOLLOW " + this.login + " -> " + uf.getInfo());
		}
		return success;
	}
	private void RetuitAll(Tuit tuit){
		Set<String> set = dataBase.keySet();
		for(String name : set){
			if(!dataBase.get(name).exists(tuit)){
				dataBase.get(name).tuitear(tuit);
			}
		}	
	}
	private void RetuitFollowers(Tuit tuit){
		List<String> list = dataBase.get(this.login).getFollowers();
		for(String name : list){
			if(!dataBase.get(name).exists(tuit)){
				dataBase.get(name).tuitear(tuit);
			}
		}
	}	
	private void Retuitear(int id){
		Tuit tuit = dataBase.get(this.login).getTuit(id);
		tuit.newRetuit();
		if(tuit.getRetuits() >= retAll){
			RetuitAll(tuit);
		}else{
			RetuitFollowers(tuit);
		}
	}
	private boolean Retuit(RetuitMsg msg){
		boolean success = false;
		if ((dataBase.get(this.login).getId() >= msg.getId()) && (msg.getId() >= 1)){
			Retuitear(msg.getId());
			success = true;
		}
		return success;
	}
	private List<TuitId> getTuitUnread (){
		List<TuitId> listUnread = new ArrayList<TuitId>();
		List<TuitId> listAll = dataBase.get(this.login).getTuitStore();
		for(int i = 0;i < listAll.size();i++){
			TuitId tuit = listAll.get(i);
			if (!tuit.getRead()){
				listUnread.add(tuit);
				tuit.setRead(true);
			}
		}
		return listUnread;
	}
	private TuitListMsg RequestTuit(){
		List<TuitId> tuitList = getTuitUnread();
		TuitListMsg msg = new TuitListMsg(tuitList);
		return msg;
	}
	private List<String> getUsers(){
		Set<String> setUsers = dataBase.keySet();
		List<String> list = new ArrayList<String>();
		for(String name : setUsers){
			list.add(name);
		}
		return list;
	}
	public UsersListMsg Users (){
		List<String> list = new ArrayList<String>();
		list = getUsers();
		UsersListMsg msgUs = new UsersListMsg(list);
		return msgUs;
	}
	
	@Override
	public void run() {
		for(;;){
			Msg msg = skeleton.hear(idata);
			if (msg != null) {
				process(msg);
			} else {
				if (this.login != null) {
					System.err.println("Client '" + this.login + "' is disconnected...");
				}
				break;
			}
		}
	}
}