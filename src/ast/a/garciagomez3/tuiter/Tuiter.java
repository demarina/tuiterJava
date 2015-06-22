package ast.a.garciagomez3.tuiter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ast.a.garciagomez3.Command.ComType;
import ast.a.garciagomez3.Command.Command;
import ast.a.garciagomez3.Command.FollowCom;
import ast.a.garciagomez3.Command.LoginCom;
import ast.a.garciagomez3.Command.RetuitCom;
import ast.a.garciagomez3.Command.TuitCom;
import ast.a.garciagomez3.Command.UnfollowCom;
import ast.a.garciagomez3.Command.UsersCom;
import ast.a.garciagomez3.tools.IlegalNumberArguments;

public class Tuiter {
	private Socket socket;
	private DataOutputStream odata;
	private DataInputStream idata;
	private BlockingQueue<Command> blocky;
	private BlockingQueue<Boolean> blockyLogin;
	
	public Tuiter (String args) throws IOException{
		try {
			String argument[] = args.split(":");
			socket = new Socket (argument[0], Integer.parseInt(argument[1]));
			odata = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			idata = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			blocky = new LinkedBlockingQueue<>();
			blockyLogin = new LinkedBlockingQueue<>();
		} catch (IOException e) {
			System.err.println("Error to get Socket");
			e.printStackTrace();
			throw e;
		}
	}
	
	private static void argumentControl(String args[]) throws IOException{
		try{
			if (args.length != 1)
				throw new IlegalNumberArguments();
			String argument[] = args[0].split(":");
			if (argument.length != 2){
				System.err.println("Argument error, must be host:port");
				throw new IOException();
			}
		} catch (IlegalNumberArguments na){
			System.err.println("Arguments error, only one argument (host:port).");
			na.printStackTrace();
			throw new IOException();
		} catch (NumberFormatException nf){
			System.err.println("Argument error, port must be number --> (host:port).");
			throw new IOException();
		}
	}
	private String[] parseCmd (String line){
		String cmd[] = line.split(" ");
		return cmd;
	}
	/**
	 * It says if the first line is a login command and if well done.
	 */
	private boolean firstCmd(String line) throws IOException{
		boolean valid = true;
		String cmd[] = parseCmd(line);
		if (!cmd[0].equals("login") || cmd.length != 2){
			System.err.println("First command must be \"login username\"");
			return valid = false;
		}
		return valid;
	}
	/**
	 * It says if the command is a candidate to valid command, don't tell me if it's okay or not used, not included login.
	 */
	private boolean isValidCmd(String cmd){
		return (cmd.equals("follow") || cmd.equals("users") || cmd.equals("unfollow")
				|| cmd.equals("retuit") || cmd.equals("tuit"));
	}
	/**
	 * It says me if a command it's okay or not used, not included login.
	 */
	private boolean goodUse (String cmd[]){
		boolean good = false;
		if (cmd[0].equals("follow") || cmd[0].equals("unfollow")  ){
			 good = (cmd.length == 2);
		}else if (cmd[0].equals("users")){
			good = (cmd.length == 1);
		}else if (cmd[0].equals("tuit")){
			good = (cmd.length > 1);
		}else if (cmd[0].equals("retuit")){
			if(cmd.length == 2){
				try{
					Integer.parseInt(cmd[1]);
					good = true;
				}catch (NumberFormatException nf){
					System.err.println("Error, the command \"retuit\" must be \"retuit <number of tuit>\"");
				}
			}
				
		}
		return good;
	}
	/**
	 * It says if a line is a valid command and is well used
	 */
	private boolean isCorrectCmd (String line){
		boolean valid = false;
		String cmd[] = parseCmd(line);
		if (isValidCmd(cmd[0])){
				valid = goodUse(cmd);
		}
		return valid;
	}
	/**
	 * It checks if the first command is "login name"
	 */
	private boolean doLogin(BufferedReader read) throws IOException, EOFException{
		boolean correctLogin = false;
		boolean acceptLogin = false;
		String firstline = "";
		while(!acceptLogin){
			while(!correctLogin){
				try{
					correctLogin = false;
					firstline = read.readLine();
					if (firstline == null)
						throw new EOFException();
					correctLogin = firstCmd(firstline);
				} catch (EOFException eo){
					throw eo;
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
			}
			correctLogin = false;
			String cmd[] = parseCmd(firstline);
			LoginCom LogCm = new LoginCom(cmd[1]);
			try {
				blocky.put(LogCm);
				acceptLogin = blockyLogin.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	private ComType decideType(String cmd){
		ComType type = null;
		switch (cmd){
		case "tuit":
			type = ComType.TuitCom;
			break;
		case "retuit":
			type = ComType.RetuitCom;
			break;
		case "follow":
			type = ComType.FollowCom;
			break;
		case "unfollow":
			type = ComType.UnfollowCom;
			break;
		case "users":
			type = ComType.UsersCom;
			break;
		default:
			break;
		}
		return type;
	}
	
	private void process (String line){
		String cmd[] = parseCmd(line);
		ComType type = decideType(cmd[0]);
		switch (type){
		case FollowCom:
			FollowCom comF = new FollowCom(cmd[1]);
			try{
				blocky.put(comF);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
			break;
		case RetuitCom:
			int id = Integer.parseInt(cmd[1]);
			RetuitCom comR = new RetuitCom(id);
			try{
				blocky.put(comR);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
			break;
		case TuitCom:
			TuitCom comT = new TuitCom(line.substring(line.indexOf(" ")));
			try {
				blocky.put(comT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case UnfollowCom:
			UnfollowCom comUf = new UnfollowCom(cmd[1]);
			try{
				blocky.put(comUf);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
			break;
		case UsersCom:
			UsersCom comUs = new UsersCom();
			try{
				blocky.put(comUs);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	
	private void readCmd(BufferedReader read) throws IOException, EOFException{
		try{	
			String line = read.readLine();
			if (line == null)
				throw new EOFException();
			if (isCorrectCmd(line)){
				process(line);
			}else{
				System.err.println("The command insert isn't valid");
			}
		}catch(IOException e){
			throw e;
		}
	}
	
	public void start(){
		System.err.println("Connect correctly.");
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		Thread tuiterManagerThread = null;
		Thread demandTuitsThread = null;
		boolean logged = false;
		try {
			tuiterManagerThread = new Thread (new TuiterManager(blocky, blockyLogin, odata, idata));
			tuiterManagerThread.start();
			
			logged = doLogin(read);
			
			demandTuitsThread = new Thread (new DemandTuits(blocky));
			demandTuitsThread.start();
			for(;;){
				readCmd(read);
			}
		}catch(Exception e){
			System.err.println("DISCONNECT!");
			
			try {
				this.idata.close();
				this.odata.close();
				this.socket.close();
			} catch (IOException e1) {
				System.err.println("Error: Closing connection...");
			}
			
			tuiterManagerThread.interrupt();
			if (logged) {
				demandTuitsThread.interrupt();
			}
			System.exit(0);
		}
	}

	public static void main (String args[]){
		Tuiter tuiter = null;
		try{
			argumentControl(args);
			tuiter = new Tuiter(args[0]);
			tuiter.start();
		}catch(IOException ie){
			System.err.println("Error to connect.");
			ie.printStackTrace();
		}
	}
}
