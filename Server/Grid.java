/**
 * This creates a grid for the Battle ship game to play off of.
 *
 * @author Logan Schmidt
 * @author Andrew McDaniels
 * @version 12/9/16
 *
 */
package Server;
import java.util.Random;
import java.util.Scanner;


public class Grid {

    /**The size of the board*/
	int size;
    /**The grid that is visible to the player*/
	String[][] grid;
    /**The gird that is visible to the opponent*/
	String[][] opponent;
    /**The name of the owner of the grid*/
	String name;
    /**The ship letters*/
	final String[]  SYMBOLS = {"H","T","C","D","E","F","G","Y"};;
	
    /**
     * Creates a grid and populates it with ships
     *
     * @param name the owner of the grid
     * @param size the size of the grid
     */
	public Grid(String name,int size){
		this.name=name;
		grid = new String[size][size];
		opponent = new String[size][size];
		populate();
		this.size=size;
		int[] t = {5,4,3,3,2};
		if(size<10&&size>2){
			t[0]=0;
			t[1]=0;
			t[2]=0;
			t[3]=0;
		}else if(size<2){
			size=10;
		}
		//int[] t = {1};
		setShips(t);
	}
	
    /**
     * Returns the owner of grid
     *
     * @return the owner name
     */
	public String getName(){
		return name;
	}
	
    /**
     * Returns if the owner has lost the game
     *
     * @return if owner lost
     */
	public boolean loss(){
		for(String[] temp: grid)
			for(String temp2: temp)
				for(String temp3:SYMBOLS)
					if(temp3.equals(temp2))
						return false;
		return true;
	}
	
    /**
     * Populates the grid with ships
     */
	private void populate(){
		for(int x=0;x<grid.length;x++){
			for(int y=0;y<grid.length;y++){
				grid[x][y]=" ";
				opponent[x][y]=" ";
			}
		}
	}
	
    /**
     * Shows your screen to you
     *
     * @return your screen with ships
     */
	public String show1(){
		String temp="    ";
		for(int x=0;x<grid.length;x++){
			if(x==0){
				for(int y=0;y<grid.length;y++)
					temp+= (y+1)+"   ";
				temp+="\n";
			}
			for(int y=0;y<grid.length;y++){
				if(y==0&&x!=size-1)
					temp+=x+1+"   ";
				else if(y==0)
					temp+=x+1+"  ";
				if(y!=size-1)
					temp+=grid[y][x]+" - ";
				else
					temp+=grid[y][x];
			}
			temp+="\n";
			for(int y=0;y<grid.length;y++){
				if(y==0)
					temp+="    ";
				if(x!=size-1)
					temp+="|   ";
			}
			temp+="\n";
		}
		return temp;
	}
	
    /**
     * Shows the grid that is visible to opponent
     *
     * @return grid visible to opponents
     */
	public String show2(){
		String temp="";
		for(int x=0;x<opponent.length;x++){
			if(x==0){
				for(int y=0;y<grid.length;y++)
					temp+= (y+1)+"   ";
				temp+="\n";
			}
			for(int y=0;y<opponent.length;y++){
				if(y==0&&x!=size)
					temp+=x+1+"   ";
				else if(y==0)
					temp+=x+1+"  ";
				if(y!=size-1)
					temp+=opponent[y][x]+" - ";
				else
					temp+=opponent[y][x];
			}
			temp+="\n";
			for(int y=0;y<opponent.length;y++){
				if(y==0)
					temp+="    ";
				if(x!=size-1)
					temp+="|   ";
			}
			temp+="\n";
		}
		return temp;
	}
	
    /**
     * Sets ths ships to the grid
     *
     * @param x the ships to be populated
     */
	private void setShips(int[] x){
		Random rnd = new Random();
		int count = 0;
		for(int temp : x){
			boolean goAgain = true;
			while(goAgain){
				int row = rnd.nextInt(size);
				int col = rnd.nextInt(size);
				int lay = rnd.nextInt(2);
				if(lay==0)
					goAgain=!setHor(temp,row,col,SYMBOLS[count]);
				else
					goAgain=!setVer(temp,row,col,SYMBOLS[count]);
			}
			count++;
		}
	}
	
    /**
     * Checks if the spot hasnt been taken horizontally
     *
     * @param x the size of the ship
     * @param row the row of the ship
     * @param col the column of the ship
     * @param sym the letter of the ship
     * @return if ship was set
     */
	private boolean setHor(int x, int row, int col,String sym){
		for(int count=0;count<x;count++){
			try{
				if(!grid[col][row+count].equals(" "))
					return false;
			}catch(IndexOutOfBoundsException e){
				return false;
			}
		}
		for(int count=0;count<x;count++){
			grid[col][row+count]=sym;
		}
		return true;
	}
	

    /**
     * Checks if the spot hasnt been taken vertically
     *
     * @param x the size of the ship
     * @param row the row of the ship
     * @param col the column of the ship
     * @param sym the letter of the ship
     * @return if ship was set
     */
	private boolean setVer(int x, int row, int col, String sym){
		for(int count=0;count<x;count++){
			try{
				if(!grid[col+count][row].equals(" "))
					return false;
			}catch(IndexOutOfBoundsException e){
				return false;
			}
		}
		for(int count=0;count<x;count++){
			grid[col+count][row]=sym;
		}
		return true;
	}
	
    /**
     * Attacks the grid
     *
     * @param row the row that is being attacked
     * @param col the column that is being attacked
     * @return if the hit was valid
     */
	public char attack(int row, int col){
		try{
			if(grid[col][row].equals("m"))
				return 'i';
			else if(grid[col][row].equals("@"))
				return '@';
			else if(grid[col][row].equals(" ")){
				grid[col][row]="m";
				opponent[col][row]="m";
                System.out.println("miss");
				return 'm';
			}else{
				grid[col][row]="@";
				opponent[col][row]="@";
				return 'h';
			}
		}catch(IndexOutOfBoundsException e){
			return 'e';
		}
	}
}
