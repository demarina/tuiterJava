package ast.a.garciagomez3.tools;

public class TuitId {
	private int id;
	private Tuit tuit;
	private boolean read;
	
	public TuitId(){
		this.id = 0;
		this.tuit = null;
		this.read = false;
	}
	
	public TuitId(int id, Tuit tuit, boolean read){
		this.id = id;
		this.tuit = tuit;
		this.read = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Tuit getTuit() {
		return tuit;
	}

	public void setTuit(Tuit tuit) {
		this.tuit = tuit;
	}

	public boolean getRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
	
}
