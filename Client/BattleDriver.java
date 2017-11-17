/**
 * Creates the client with the command line arguements entered
 *
 * @author Logan Schmidt
 * @author Andrew McDaniels
 * @version 12/9/16
 */
package Client;
import java.io.IOException;
import java.util.InputMismatchException;

public class BattleDriver {

    /**
     * Creates the client at the specified port number and host
     *
     * @param args the command line arguements
     */
	public static void main(String[] args){
		BattleClient client=null;
		try{
			if(args.length==3)
				client = new BattleClient(args[0],
                                            Integer.parseInt(args[1]),args[2]);
		}catch(InputMismatchException e){
			System.err.println("java BattleDriver <host> <port> <name>");
		}catch(IOException e){
			System.err.println("Could not connect");
			e.printStackTrace();
		}
	}
}
