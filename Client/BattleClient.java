/**
 * Creates a socket that connects to the connection interface
 *
 * @author Logan Schmidt
 * @author Andrew McDaniels
 * @version 12/9/16
 */
package Client;
import Common.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.net.InetAddress;

public class BattleClient implements MessageListener {

    /**The socket that is connected to the servers interface*/
	Socket clientSocket;
    /**The name of the player*/
	String name;
    /**The connection that is being made with server*/
	ConnectionInterface connection;
	
    /**
     * Creates the socket and starts the client
     *
     * @param host the name of the host
     * @param port the port number of the client
     * @param name the name of the client
     */
	public BattleClient(String host, int port, String name) throws IOException{
		clientSocket= new Socket(host,port);
		this.name=name;
        go();
	}
	
    /**
     * Creates the connection send to the interface and sends commands to 
     * server interface
     */
	public void go() throws IOException{
		connection= new ConnectionInterface(clientSocket, this);
		Thread thread = new Thread(connection);
		thread.start();
		sendMessage(name);
		Scanner in=new Scanner(System.in);
		while(clientSocket.isConnected()){
			while(in.hasNextLine())
				sendMessage(in.nextLine());
		}
	}
	
    /**
     * Sends a message to the server interface
     *
     * @param message the message being sent to server
     */
	public void sendMessage(String message){
		connection.pass(message);
	}

	@Override
    /**
     * A message is recieved
     *
     * @param message the message being recieved
     * @param source the connection interface that is communicating 
     *              with server
     */
	public void messageReceived(String message, MessageSource source) {
		System.out.println(message);
	}

	@Override
    /**
     * Closes this connection
     *
     * @param source the connection being closed
     */
	public void sourceClosed(MessageSource source) {
		source.closeMessageSource();
		
	}
}
