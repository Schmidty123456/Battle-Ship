/**
 * Creates the server and assigns it to a socket
 *
 * @author Logan Schmidt
 * @author Andrew McDaniels
 * @version 12/9/16
 */
package Server;
import Common.*;
import java.io.IOException;
import java.util.InputMismatchException;


public class BattleShipDriver {

    /**The server that is being set up*/
	BattleServer server;
	
	/**
     * Creates the server based off of commandline arguements
     *
     * @param args the command line arguements being passed in
     */
	public static void main(String[] args){
		BattleServer server=null;
		try{
			if(args.length==1)
				server = new BattleServer(Integer.parseInt(args[0]));
			else if(args.length==2)
				server = new BattleServer(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
			else if(args.length==0)
				server = new BattleServer();
			if(server!=null)
				server.listen();
		}catch(NumberFormatException e){
			System.err.println("java BattleShipDriver <port> <size>");
		}catch(IOException |InterruptedException e){
			System.err.println("java BattleShipDriver <port> <size of table>");
		}
	}
}
