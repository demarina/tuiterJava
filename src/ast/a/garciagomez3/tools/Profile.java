package ast.a.garciagomez3.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Profile {
	private String name;
	private ArrayList<String> follows;
	private List<String> followers;
	private List<TuitId> tuitStore;
	private int id = 0;
	
	public Profile(String name){
		this.name = name;
		this.follows = new ArrayList<String>();
		this.followers = Collections.synchronizedList(new ArrayList<String>()); 
		this.tuitStore = Collections.synchronizedList(new ArrayList<TuitId>()); 
	}
	public Tuit getTuit(int id){
		Tuit tuit = new Tuit();
		for(int i = 0;i < tuitStore.size();i++){
			tuit = tuitStore.get(i).getTuit();
			if(tuitStore.get(i).getId() == id){
				break;
			}
		}
		return tuit;
	}
	public boolean exists (Tuit tuit){
		boolean exists = false;
		for(TuitId tid: tuitStore) {
			if(tid.getTuit() == (tuit)) {
				exists = true;
				break;
			}
		}
		return exists;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getFollows() {
		return follows;
	}
	public void setFollows(ArrayList<String> follows) {
		this.follows = follows;
	}
	public List<String> getFollowers() {
		return followers;
	}
	public void setFollowers(ArrayList<String> followers) {
		this.followers = followers;
	}
	public List<TuitId> getTuitStore() {
		return tuitStore;
	}
	public void setTuitStore(ArrayList<TuitId> tuitStore) {
		this.tuitStore = tuitStore;
	}
	public void tuitear(Tuit tuit){
		this.id += 1;
		TuitId newTuit = new TuitId(this.id, tuit, false);
		this.tuitStore.add(newTuit);
	}
}
