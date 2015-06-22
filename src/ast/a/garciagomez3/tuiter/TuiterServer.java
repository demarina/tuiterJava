package ast.a.garciagomez3.tuiter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import ast.a.garciagomez3.tools.IlegalNumberArguments;
import ast.a.garciagomez3.tools.Profile;

public class TuiterServer {
	private ServerSocket servsocket;
	private ConcurrentHashMap<String, Profile> dataBase = new ConcurrentHashMap<String, Profile>();
	
	public TuiterServer(String port_str) throws IOException{
			this.servsocket = new ServerSocket(Integer.parseInt(port_str));
	}
	private static void argumentControl(String args[]) throws IOException{
		try{
			if (args.length != 1)
				throw new IlegalNumberArguments();
			Integer.parseInt(args[0]);
		}catch (NumberFormatException nf){
			System.err.println("The port (argument) must be a number.");
			nf.printStackTrace();
			throw new IOException();
		}catch (IlegalNumberArguments e){
			System.err.println("Arguments error, only one argument (port number).");
			e.printStackTrace();
			throw new IOException();
		}
	}

	public void start(){
		System.err.println("Server bind correctly, WORKING!.");
		for(;;){
			try {
				Socket client = this.servsocket.accept();
				System.err.println("ACCEPTED");
				new Thread(new Laborer(client,dataBase)).start();
			} catch (Exception e) {
				System.err.println("Error to process client, NO PROCESS CLIENT!!");
			}
		}
	}

	public static void main (String args[]){
		TuiterServer server = null;
		try{
			argumentControl(args);
			server = new TuiterServer(args[0]);
			server.start();
		}catch (IOException e){
			System.err.println("Error to bind server, SERVER CAN'T START.");
			e.printStackTrace();
		}
	}
}
