package testIdea;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestBIO {
	
	public static void main(String[] args) throws IOException{
		int port = 8080;
		if(args != null && args.length > 0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(NumberFormatException e){
				//use the default value
			}
		}
		ServerSocket server = null;
		try{
			server = new ServerSocket(port);
			System.out.println("The time server is start in port: " + port);
			Socket socket = null;
			while(true){
				socket = server.accept();
				new Thread(new TimeServerHandler(socket)).start();
				System.out.println("get an require");
			}
		}finally{
			if(server != null){
				System.out.println("The time servier close");
				server.close();
				server = null;
			}
			
		}
	}
}
