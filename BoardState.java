/**
 * @(#)BoardState.java
 * This class represents an individual board state, consisting of an array of tile positions. A BoardState is solvable when there is an even number of inversions present in the configuration. 
 * Conversely, setups with an odd number of inversions present are unsolvable. An inversion is defined as when a pair of tiles/blocks appear in a reverse order from their order within an 
 * 8-Block goal state. A BoardState is simply the state of the game board.
 *
 * @author Iain St. John
 * CSC 342 -- Artificial Intelligence -- Project One
 * @version 1.00 2016/2/13
 */

import java.lang.Math;

public class BoardState {
	//final int arrays to represent goal board states
	private static final int SOLVEDZEROSTART[] = {0,1,2,3,4,5,6,7,8}; //array representing a solved board. For this goal state, a given block's value should match its index in the array.
	private static final int SOLVEDZEROEND[] = {1,2,3,4,5,6,7,8,0}; //another solved board array, this one with the blank/0 at the end.
	private int tiles[]; //array of tiles in a board.
	private int closerToGoal; //int tracking which goal state a given initial board state is closer to. This will decide which goal to move towards in the search, and which manhattan distance to calculate.
	//0 = undecided, 1 = closer to SOLVEDZEROSTART, 2 = closer to SOLVEDZEROEND
	
   /**
    *Constructor method that takes an array of tiles, and sets itself accordingly.
    *
    *@param arr -- given an array of starting positions for our tiles
    *@return -- a BoardState with all tiles placed at specified positions
    */	
    public BoardState(int arr[]) {
    	tiles = new int[9]; //create array
    	for(int i = 0; i < 9; i++) {
    		tiles[i] = arr[i];
    	}
    }
    
    //accessor for the current boardstate's tile array
    public int[] getTiles() {
    	return tiles;
    }
    
     /**
    *Method that determines if a given BoardState is solvable or not. It uses the findInversions method to determine this.
    *
    *@return boolean -- true if solvable(even # inverions) false if unsolvable (odd #)
    */ 
    public boolean isSolvable() {
    	//acquire number of inversions
    	int numInversions = this.findInversions();
    	//if even return true
    	if(numInversions % 2 == 0) {
    		return true;
    	}
    	else { //otherwise, false
    		return false;
    	}
    }
    
   /**
    *Private helper function that counts the number of inversions within a given BoardState
    *
    *@return int -- number of inversions present in state's configuration.
    */ 
    private int findInversions() {
    	int numInversions = 0;
    	//use doubly nested for loops
    	for(int i = 0; i < 9; i++) {
    		for(int j = i + 1 ; j < 9; j++) {
    			//inversions don't take the blank into account.
    			if((tiles[i] > tiles[j]) && tiles[j] != 0) {
    				numInversions++;
    			}
    		}
    	}
    	return numInversions;
    }
    
    /**
    * Simple method that compares a board with our solved boards and returns true or false accordingly
    * Utilizes the custom equals method written for this class
    *
    *@return boolean -- true if it matches either solved board array, false otherwise
    */
    public boolean isSolved() {
    	//compare it to both solved configurations
    	BoardState solved1, solved2;
    	solved1 = new BoardState(SOLVEDZEROSTART);
    	solved2 = new BoardState(SOLVEDZEROEND);
    	if((this.equals(solved1)) || (this.equals(solved2))) {
    		return true;
    	}
    	else {
    		return false;
    	}		
    }
    
   /**
    *This method reads through the tiles of the calling BoardState, and makes sure there are no repeats.
    *
    *@return boolean -- true if there are no repeats, false if there are
    */
    public boolean noRepeats() {
    	for(int i = 0; i < 9; i++) {
    		for(int j=i+1; j < 9; j++) {
    			if(tiles[i] == tiles[j]) {
    				return false;
    			}
    		}
    	}
    	return true;
    }
    
    /**
    *This method reads through the tiles of the calling BoardState, and makes sure all the values lie within the range of 0 to 8.
    *
    *@return boolean -- true if there are no outliers, false if there are.
    */
    public boolean allInRange() {
    	for(int i = 0; i < 9; i++) {
    		if(tiles[i] < 0 || tiles[i] > 8) {
    			return false;
    		}
    	}
    	return true;
    }
    
   /**
    *Method that makes a move by swapping 2 tile positions, one numbered with one blank, and returns a new array with the updated tiles.
    *It's a private helper function used within the possibleBoards method. Legal moves are only defined in that method, and we want to ensure that only legal moves can be made.
    *
    *@param posTo -- the position/index a tile is being moved TO. This is the index of the blank tile.
    *@param posFrom -- the position/index a is being moved FROM. This is the index of the numbered tile.
    *@return int[] newTiles -- an array of tile tiles reflecting the move that was made
    */ 
    private int[] makeMove(int posTo, int posFrom) {
    	int newTiles[] = new int[9];
    	for(int i = 0; i < 9; i++) {
    		newTiles[i] = tiles[i];
    	}
    	//get our tile to move
    	int moving = tiles[posFrom]; 
    	//put that tile at its new position.
    	newTiles[posTo] = moving;
    	//we already know that it had to be the blank at posFrom, so we can just put a zero at tile's old spot.
    	newTiles[posFrom] = 0;
    	
    	return newTiles;
    }
    
    /**
    *This method is nearly identical to makeMove, however, instead of returning a new array reflecting the move, it simply changes the values within tiles[].
    *It's highly likely that only one of these methods is necessary, but I wrote both to allow myself to determine that later down the road. 
    *
    *@param posTo -- the position/index a tile is being moved TO. This is the index of the blank.
    *@param posFrom -- the position/index a is being moved FROM. This is the index of the numbered tile.
    */
    private void destructiveMove(int posTo, int posFrom) {
    	tiles[posTo] = tiles[posFrom]; //move the chosen tile into the blank space
    	tiles[posFrom] = 0; //put the space where the tile came from
    }
    
    /**
     *Method that calculates the manhattan distance for this board state.
     *Manhattan Distance is the sum of all the moves each tile must make to reach its goal position
     *This method serves as our heuristic function, a rough estimate of how far a given BoardState is from reaching a goal state of being solved.
     *There are two possible goal states, and two possible ending positions for each tile, and we only want to make moves that advance to our nearest goal.
     *
     *@param closestGoal -- string telling this board which goal state is closest, so it knows which manhattan distance to calculate.
     *@return int -- the closest manhattan distance of this board state.
     */
     public int calcManhattanDistance() {
     		//find distance to goal state with the zero at the beginning
	     	int total1 = 0; //the total sum for the board
	     	for(int i = 0; i < 9; i++) {
	     		int tile = tiles[i]; //current tile
	     		//the blank doesn't factor in to the calculation, skip it
	     		if(tile == 0) {
	     			continue;
	     		}
	     		int movesToGoal = 0; //the number of moves this tile is from its goal position
	     		//solved state for a given tile number is its index
	     		//if the tile isn't equal to its index
	     		if(tile != i) {
	     			movesToGoal = Math.abs(i - tile); 
	     		}
	     		total1 += movesToGoal;
	     	}
	     	//find distance to goal state with the zero at the end
	     	int total2 = 0; //the total sum for the board
	     	for(int i = 0; i < 9; i++) {
	     		int movesToGoal = 0; //the number of moves this tile is from its goal position
	     		//the blank doesn't factor in to the calculation, skip it
	     		if(tiles[i] == 0) {
	     			continue;
	     		}
				//for this goal state, a numbered block's final position should be located at the index one less than its own value.
	     		if(tiles[i] != (i + 1)) {
	     			movesToGoal = Math.abs((i+1) - tiles[i]);
	     		}
	     		total2 += movesToGoal;
	     	}
	     	
	     	if(total1 < total2)
	     		return total1;
	     	else
	     		return total2;
	     	
     }
     
    /**
      *Method that returns all reachable boardstates from the current one by making all possible/legal moves at this step.
      *A move is legal if and only if a given numbered tile swaps positions with the blank, represented by a 0 in this implementation.
      *
      *@return BoardState[] -- an array of different BoardStates each reflecting a different possible move.
      */
      public BoardState[] generatePossibleBoards() {
      	int blankPos = 0; //the blank's index in tiles
      	BoardState possibleBoards[]; //our resulting array holding all reachable BoardStates
      	
      	//legal moves are determined by the blank's position. Find the 0
      	for(int i = 0; i < 9; i++) {
      		if(tiles[i] == 0) {
      			blankPos = i; //remember that position
      			break;
      		}
      	}
     /**number of possible moves depends on whether 0 is at a corner, side, or the center of the board.
       *CORNERS: 0,2,6,8 = 2 moves -- SIDES: 1,3,5,7 = 3 moves -- CENTER: 4 = 4 moves -- use this info to declare an array of BoardStates of the correct size.
       *possibleBoards will hold our results
       */
       if(blankPos == 0 || blankPos == 2 || blankPos == 6 || blankPos == 8) {
       	 	possibleBoards = new BoardState[2];
       }
       else if(blankPos == 1 || blankPos == 3 || blankPos == 5 || blankPos == 7) {
       	  	possibleBoards = new BoardState[3];
       }
       else { //it's gotta be 4
       		possibleBoards = new BoardState[4];
       }
      	
      /**Legal moves if Zero @ [Index]: Use makeMove() method. posTo should be 0's index, posFrom should be one legal move given the blank position.
        *  Ind: From  To   From  To   From  To   From  To
      	*0@[0]: [1]<->[0]  [3]<->[0]
      	*0@[1]: [0]<->[1]  [2]<->[1]  [4]<->[1]
      	*0@[2]: [1]<->[2]  [5]<->[2]
      	*0@[3]: [0]<->[3]  [4]<->[3]  [6]<->[3]
      	*0@[4]: [1]<->[4]  [3]<->[4]  [5]<->[4]  [7]<->[4]
      	*0@[5]: [2]<->[5]  [4]<->[5]  [8]<->[5]
      	*0@[6]: [3]<->[6]  [7]<->[6]
      	*0@[7]: [4]<->[7]  [6]<->[7]  [8]<->[7]
      	*0@[8]: [5]<->[8]  [7]<->[8]
      	*/
      	if(blankPos == 0) { //corner 2
      		possibleBoards[0] = (new BoardState(this.makeMove(blankPos,1)));
      		possibleBoards[1] = (new BoardState(this.makeMove(blankPos,3)));
      	}
      	else if(blankPos == 1) {//side 3
      		possibleBoards[0] = (new BoardState(this.makeMove(blankPos,0)));
      		possibleBoards[1] = (new BoardState(this.makeMove(blankPos,2)));
      		possibleBoards[2] = (new BoardState(this.makeMove(blankPos,4)));
      	}
      	else if(blankPos == 2) { //corner 2
      		possibleBoards[0] = (new BoardState(this.makeMove(blankPos,1)));
      		possibleBoards[1] = (new BoardState(this.makeMove(blankPos,5)));
      	}
      	else if(blankPos == 3) { //side 3
      		possibleBoards[0] = (new BoardState(this.makeMove(blankPos,0)));
      		possibleBoards[1] = (new BoardState(this.makeMove(blankPos,4)));
      		possibleBoards[2] = (new BoardState(this.makeMove(blankPos,6)));	
      	}
      	else if(blankPos == 4) { //center 4
      		possibleBoards[0] = (new BoardState(this.makeMove(blankPos,1)));
      		possibleBoards[1] = (new BoardState(this.makeMove(blankPos,3)));
      		possibleBoards[2] = (new BoardState(this.makeMove(blankPos,5)));
      		possibleBoards[3] = (new BoardState(this.makeMove(blankPos,7)));
      	}
      	else if(blankPos == 5) { //side 3
      		possibleBoards[0] = (new BoardState(this.makeMove(blankPos,2)));
      		possibleBoards[1] = (new BoardState(this.makeMove(blankPos,4)));
      		possibleBoards[2] = (new BoardState(this.makeMove(blankPos,8)));
      	}
      	else if(blankPos == 6) { //corner 2
      		possibleBoards[0] = (new BoardState(this.makeMove(blankPos,3)));
      		possibleBoards[1] = (new BoardState(this.makeMove(blankPos,7)));
      	}
      	else if(blankPos == 7) { //side 3
      		possibleBoards[0] = (new BoardState(this.makeMove(blankPos,4)));
      		possibleBoards[1] = (new BoardState(this.makeMove(blankPos,6)));
      		possibleBoards[2] = (new BoardState(this.makeMove(blankPos,8)));
      	}
      	else { //position 8 -- corner 2
      		possibleBoards[0] = (new BoardState(this.makeMove(blankPos,5)));
      		possibleBoards[1] = (new BoardState(this.makeMove(blankPos,7)));
      	}
      	return possibleBoards;
      }
     
    /**
     *Method that compares calling boardstate to another boardstate, and returns if they have the same tile placements.
     *
     *@param other -- another boardstate with which we want a comparison
     *@return boolean -- true if two BoardStates's have identical tile arrays, false otherwise
     */ 
     public boolean equals(BoardState other) {
     	int same = 0; //the number of tiles that are found at the same locations
     	for(int i = 0; i < 9; i++) {
     		if(this.tiles[i] == other.tiles[i]) {
     			same++;
     		}
     	}
     	//if there are 9 tiles that are the same (including our blank), then they are the same
     	if(same == 9) {
     		return true;
     	}
     	else { //if just one is different, they aren't the same
     		return false;
     	}
     }
     
     //toString method to generate an easily printable string representation of a boardstate.
     public String toString() {
     	String str = "";
     	int i;
     	//first 3 (0,1,2) in first row
     	for(i = 0; i < 3; i++) {
     		str = str + " " + tiles[i];
     	}
     	str += "\n";
     	//(3,4,5) in second
     	for(i = 3; i < 6; i++) {
     		str = str + " " + tiles[i];
     	}
     	str += "\n";
     	//(6,7,8) in third
     	for(i = 6; i < 9; i++) {
     		str = str + " " + tiles[i];
     	}
     	str += "\n";
     	return str;
     }
    
}