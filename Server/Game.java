/**
 * Starts the game of Battle Ship
 *
 * @author Logan Schmidt
 * @author Andrew McDaniels
 * @version 12/9/16
 */
package Server;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class Game {
	
    /**A map of all players grids*/
	HashMap<String,Grid> players;
    /**A queue of all players grids*/
	Queue<Grid> queue;
    /**If the game has started*/
	boolean start;
    /**Size of the grid*/
	int size;
    /**If the game has finished*/
	boolean gameOver;
	
    /**
     * Creates the game of a certain size grid
     *
     * @param size the size of the grid
     */
	public Game(int size){
		players= new HashMap<String, Grid>();
		queue= new LinkedList<Grid>();
		this.size=size;
		start=true;
		gameOver=false;
	}
	
    /**
     * Gets if game is over
     *
     * @return if game is over
     */
	public boolean getGameOver(){
		return gameOver;
	}
	
    /**
     * Gets the grid of a player
     *
     * @param name name of the player
     * @return the grid of the player
     */
	public Grid getGrid(String name){
		return players.get(name);
	}
	
    /**
     * Gets the current players name
     *
     * @return the name of the current player
     */
	public String getCurrentPlayer(){
		return queue.peek().getName();
	}
	
    /**
     * Gets if the game starts
     *
     * @return if the game has started
     */
	public boolean getStart(){
		return start;
	}
	
    /**
     * Adds a player to the game
     *
     * @param name the name of the new player
     * @return if the player is added to game
     */
	public boolean addPlayers(String name){
		if(players.containsKey(name))
			return false;
		players.put(name, new Grid(name,size));
		queue.add(players.get(name));
		return true;
	}
	
    /**
     * Removes the player from the game 
     *
     * @param name the name to be removed from the game
     * @param if the player was removed
     */
	public boolean removePlayers(String name){
		if(!players.containsKey(name))
			return false;
		players.remove(name);
		queue.remove(players.get(name));
		return true;
	}
	
    /**
     * Makes any of the possible commands of the player
     *
     * @param command the command being done
     * @param player the player being targeted
     * @param y the row of the attack
     * @param z the column of the attack
     * @return if move was valid
     */
	public boolean makeChoice(String command, String player,String y, String z){
		char x=' ';
		try{
            if(!players.containsKey(player))
                return false;
			if(command.equals("attack")){
				x=players.get(player).attack(Integer.parseInt(y)-1,
                                                 Integer.parseInt(z)-1);
			}else if(command.equals("show")&&queue.peek().name.equals(player)){
				players.get(player).show1();
				return false;
			}else if(command.equals("show")){
				players.get(player).show2();
				return false;
			}else if(command.equals("quit")&&queue.peek().name.equals(player)){
				queue.remove();
				removePlayers(player);
				return false;
			}else{
				return true;
			}
		}catch(NullPointerException e){
			return false;
		}
		if(x=='m'){
			return false;
		}
		if(x=='h'){
			return false;
		}
		return true;
	}
	
    /**
     * Cycles the players turns
     *
     * @return the next player
     */
	public String cycle(){
		String temp="";
		if(queue.peek().loss()){
			temp=queue.peek().name;
			queue.add(queue.remove());
			queue.remove();
			removePlayers(temp);
		}
		else if(queue.size()>1){
			queue.add(queue.remove());
		}
		return temp;
	}
}
