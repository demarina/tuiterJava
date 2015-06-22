package ast.a.garciagomez3.tuiter;

import java.util.concurrent.BlockingQueue;
import ast.a.garciagomez3.Command.Command;
import ast.a.garciagomez3.Command.RequestTuitCom;

class DemandTuits implements Runnable{
	private BlockingQueue<Command> blocky;
	
	public DemandTuits(BlockingQueue<Command> blocky){
		this.blocky = blocky;
	}

	@Override
	public void run() {
		RequestTuitCom comRT = new RequestTuitCom();
		for(;;){
			try {
				blocky.put(comRT);
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}
	}
}
