package ast.a.garciagomez3.tools;

public class Tuit {
	private String creater;
	private String text;
	private int retuits;
	
	public Tuit(){
		this.creater = "";
		this.text = "";
		this.retuits = 0;
	}
	
	public Tuit(String creater, String text, int retuits){
		this.creater = creater;
		this.text = text;
		this.retuits = retuits;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getRetuits() {
		return retuits;
	}

	public void newRetuit() {
		this.retuits += 1;
	}
}
