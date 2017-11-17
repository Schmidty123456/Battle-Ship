/**
 * Creates the server and sends the sockets to the threads to accept them
 *
 * @author Logan Schmidt 
 * @author Andrew McDaniels
 * @version 12/9/16
 */
package Server;
import Common.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BattleServer implements MessageListener {

    /**The socket that is trying to connect to clients*/	
	ServerSocket welcomeSocket;
    /**The game that is being started*/
	Game game;
    /***/
    int temp;
    /**The current connection interface*/
	ConnectionInterface connect;
    /**The message that is being recieved*/
	String message;
    /**The map of all connections by name*/
	HashMap<String,ConnectionInterface> connections;
    /**The list of threads that run the connections*/
	List<Thread> threads;
    /**The initial board size if nothing is entered*/
	public final static int BOARD_SIZE=10;
    /**The port number if none is given*/
	public final static int portnum=9998;
	
    /**
     * Creates the socket that welcomes connections
     *
     * @param port the port number the server is on
     * @param size the size of the board game
     */
	public BattleServer(int port, int size) throws IOException{
		welcomeSocket = new ServerSocket(port);
		game= new Game(size);
		threads = new LinkedList<Thread>();
		message="";
        temp=0;
		connections= new HashMap();
	}
	
    /**
     * Creates a socket with no board size
     *
     * @param port the port the server is listening to
     */
	public BattleServer(int port) throws IOException{
		this(port,BOARD_SIZE);
	}
	
    /**
     * Creates a socket when user enters no information
     */
	public BattleServer() throws IOException{
		this(portnum,BOARD_SIZE);
	}

    /**
     * Waits for clients to connect and when they connect add them to 
     * connections and game
     */ 
	public void listen() throws IOException, InterruptedException {
		boolean t=true;
        while(connections.size()!=2){
			ConnectionInterface connect = new ConnectionInterface(
                                                welcomeSocket.accept(), this);
			Thread thread = new Thread(connect);
            thread.start();
            while(message.equals("")){
                System.out.print("");
                }
            connect.setName(message);
            game.addPlayers(message);
			connections.put(message, connect);
            message="";
            temp++;
		}			
        while(connections.size()==2){
            toAll("It is "+game.getCurrentPlayer()+"'s turn");
            getMessage();
        }
        toAll("You have won");        
	}

    /**
     * Gets the messages coming in 
     */
    private void getMessage(){
        boolean b=true;
            while(b){
                b=false;
                while(message.equals(""))   
                    System.out.print("");
				try{
					String[] commands = message.split(" ");
                    if(!game.getCurrentPlayer().equals(connect.getName())||
                        game.makeChoice(commands[0], commands[1], commands[2],
                                         commands[3])){
						connect.pass("Invalid Move enter a new move");
				        b=true;
                    }else if(commands[0].equals("attack")&&
                        game.getCurrentPlayer().equals(connect.getName())){
                        toAll(game.getCurrentPlayer()+" attacked "+commands[1]);
                        game.cycle();
                    }
                    message="";
				}catch(ArrayIndexOutOfBoundsException e){
					String[] commands = message.split(" ");
                    try{
	                    if(commands[0].equals("quit")){
                            connections.get(commands[1]).close();
                            connections.remove(commands[1]);
                            b=false;
                        }else if(game.makeChoice(commands[0], commands[1],
                                                     "", "")){
    				    	connect.pass("Invalid Move enter a new move");
                        }else if(!commands[0].equals("quit")){
                            if(connect.getName().equals(commands[1]))
                                connect.pass(game.getGrid(commands[1]).show1());
                            else
                                connect.pass(game.getGrid(commands[1]).show2());
                            b=true;
                        }   
                        message="";
                    }catch(ArrayIndexOutOfBoundsException ex){
                        connect.pass("Invalid move enter a new move");
                        b=true;
                        message="";
                    }
				}
           }
    }
	
    /**
     * Passes message to specific client
     *
     * @param message the message that is being sent
     * @param source the connection that is being sent the message
     */
	public void toClient(String message, ConnectionInterface source){
		source.pass(message);
	}
	
    /**
     * Sends a message to all clients that are connected
     *
     * @param message the message being sent
     */
	public void toAll(String message){
		for(ConnectionInterface temp: connections.values()){
			temp.pass(message);
		}
	}
	
	@Override
    /**
     * When a message is recieved do the command
     *
     * @param message the command that the client has sent
     * @param source the connection that sent the command
     */
	public void messageReceived(String message, MessageSource source) {
        this.message=message;
		if(source instanceof ConnectionInterface){
			connect = (ConnectionInterface)source;
		}
	}

	@Override
    /**
     * Closes all connections that are still open
     *
     * @param source the connection that is closed
     */
	public void sourceClosed(MessageSource source) {
		if(source instanceof ConnectionInterface){
			ConnectionInterface temp = (ConnectionInterface)source;
			temp.close();
		}
	}
}
