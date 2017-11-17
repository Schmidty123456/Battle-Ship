/**
 * The interface that handles all messages to the listeners
 *
 * @author Logan Schmidt
 * @author Andrew McDaniels
 * @version 12/9/16
 */
package Common;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;



public class ConnectionInterface extends MessageSource implements Runnable{
    
    /**The socket to the listener*/
	Socket socket;
    /**The name of the player that is communication*/
	String name;
    /**The inputstream from the listener*/
	DataInputStream in;
    /**The outputstream from the listener*/
	DataOutputStream out;
	
    /**
     * Creates the connection interface
     *
     * @param socket the active socket to listener
     * @param server the listener that is listening
     */
	public ConnectionInterface(Socket socket, MessageListener server) {
		super();
		try{
			this.socket=socket;
			messageListeners.add(server);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		}catch(Exception e){
			
		}
	}
	
	@Override
    /**
     * Waits for a message to come in and sends to the listener
     */
	public void run() {
		Scanner input =new Scanner(in);
		while(input.hasNextLine()){
			messageListeners.get(0).messageReceived(input.nextLine(), this);
		}
        close();
	}
	
    /**
     * Passes data to the listener
     *
     * @param data the data being sent
     */
	public void pass(String data){
		try{
			out.writeBytes(data+"\n");
		}catch(IOException e){
			//nothing
		}
	}
	
    /**
     * The name of the player
     *
     * @return the name of the player
     */
    public String getName(){
        return name;
    }

    /**
     * Asks the user to send command
     */
	public void askCommand(){
		pass("Please enter a command");
	}
	
    /**
     * Sets the name of the connection
     *
     * @param name the name of the player
     */
    public void setName(String name){
        this.name=name;
    }

    /**
     * Closes the sockets
     */
	public void close(){
		try {
			socket.close();
		} catch (IOException e) {
		}
	}
	
}
