/**
 * @(#)SlidingBlockState.java
 * This class represents an individual state of the sliding block/8-block puzzle. It consists of a board state at step i, the previous state (NULL if at the first step), and the number of moves made to reach the current board state.	
 * This class implements the Comparator interface for integers, in order to support a logical ordering within the priority queue used in the SlidingBlockSolver. SlidingBlockStates are compared using their numMoves property.
 * A SlidingBlockState is an individual game state.
 *
 * @author Iain St. John
 * CSC 342 -- Artificial Intelligence -- Project One
 * @version 1.00 2016/2/13
 */
 
import java.util.*;
 
public class SlidingBlockState implements Comparable<SlidingBlockState>  {
	private BoardState currBoard; //B^i -- the current board state at step i
	private BoardState prevBoard; //B^i-1 -- the previous board state at step i -1
	private int numMoves; //M -- the number of moves to reach a particular state
    
   /**
    *Specific constructor that takes a specified starting BoardState, and starts previous at null and movesToReach at 0
    *
    *@param startState -- the BoardState at which we are starting
    *@return SlidingBlockState
    */
    public SlidingBlockState(BoardState startState) {
    	currBoard = startState;
    	prevBoard = null;
    	numMoves = 0;
    }
    
    /**
    *Another specific constructor that allows one to pass it a current and prior BoardState, as well as the number of moves to reach the current state.
    *
    *@param nowState -- the BoardState at which we are starting
    *@param moves -- number of moves to reach nowState
    *@param lastState -- prior BoardState
    *@return SlidingBlockState
    */
    public SlidingBlockState(BoardState nowState, int moves, BoardState lastState) {
    	currBoard = new BoardState(nowState.getTiles());
    	prevBoard = new BoardState(lastState.getTiles());
    	numMoves = moves;
    }
    
   /**
    *Specific constructor that takes an array of tiles at their positions, and uses it to create the corresponding BoardState, initializing everything else to a default.
    *
    *@param startTiles -- the array containing the starting positions for our tiles
    *@return SlidingBlockState
    */
    public SlidingBlockState(int[] startTiles) {
    	currBoard = new BoardState(startTiles);
    	prevBoard = null;
    	numMoves = 0;
    }
    
   /**
    *More specific constructor that takes an array of tiles at their positions for both the current and previous BoardStates, as well as the moves to reach the current state.
    *
    *@param startState -- the array containing the starting positions for our tiles
    *@param moves -- number of moves to build nowTiles
    *@param lastState -- prior array of tiles reflecting the last state of the board.
    *@return SlidingBlockState
    */
    public SlidingBlockState(int[] nowTiles, int moves, int[] lastTiles) {
    	currBoard = new BoardState(nowTiles);
    	prevBoard = new BoardState(lastTiles);
    	numMoves = moves;
    }
   /**
    *Constructor for an SBS that takes another SBS, for use in the aStarSearch method.
    *
    *@param  sbs -- the SlidingBlockState with which to create a new SlidingBlockState
    */ 
    public SlidingBlockState(SlidingBlockState sbs) {
    	currBoard = sbs.getCurrState();
    	if(sbs.getLastState() == null) {
    		prevBoard = null;
    	}
    	else {
    		prevBoard = sbs.getLastState();
    	}
    	numMoves = sbs.getMoves();
    }
    
    
    
    //"setter" method for numMoves
    public void setMoves(int n) {
    	numMoves = n;
    }
    
    
    //The following are our accessor/"getter" methods
    public BoardState getCurrState() {
    	return currBoard;
    }
    
    public BoardState getLastState() {
    	return prevBoard;
    }
    
    public int getMoves() {
    	return numMoves;
    }
    
      
   /**
    *Comparison method to compare two SlidingBlockStates, checking all SlidingBlockState properties
    *
    *@param other -- the SlidingBlockState we wish to compare to the calling one
    *@return boolean -- true is currBoards, prevBoards, and numMoves match in both SlidingBlockStates
    */ 
    public boolean equals(SlidingBlockState other) {
    	boolean ret; //return value
    	//compare current board, the last board, and the number of moves, M, to reach B_i. Two SlidingBlockStates are the exact same only when all three of these are the same.
    	ret = ((this.currBoard.equals(other.currBoard)) && (this.numMoves == other.numMoves) && (this.prevBoard.equals(other.prevBoard)));
    	return ret;
    }
    
   /*
    *Method that retrieves the result of the BoardState's calcManhattanDistance function for a given BoardState.
    *It is our heuristic function.
    *
    *@param closestGoal -- name of the goal we're approaching
    *@return int --  a boardstate's manhattan distance
    */ 
    public int getHeuristic() {
    	return currBoard.calcManhattanDistance();
    }
    
   /**
    *A method to create the frontier, or all reachable game states, from the current SlidingBlockState.
    *
    *@return SlidingBlockState[] -- an array of SlidingBlockStates. Each one has a different new currBoard, but all their prevBoard's are the currBoard of the calling SlidingBlockState.
    */ 
    public SlidingBlockState[] getFrontier() {
    	BoardState possibleBoards[]; //array of reachable board states
    	possibleBoards = this.currBoard.generatePossibleBoards(); //get all reachable BoardStates
    	SlidingBlockState frontier[] = new SlidingBlockState[possibleBoards.length]; //array of reachable SlidingBlockStates to return. Will have the same number of elements as possibleBoards.
    	//now loop through them and create a corresponding SlidingBlockState for each one.
    	//the BoardState at each iteration is the new currBoard, the new prevBoard is set to that of the calling SlidingBlockState.
    	//lastly, increment numMoves for the boards at the next step
    	for(int i = 0; i < possibleBoards.length; i++) {
    		frontier[i] = (new SlidingBlockState(possibleBoards[i],numMoves, this.currBoard));
    	}
    	return frontier;	
    }
    
    //To String method to print out an entire SlidingBlockState( a whole game state)
    public String toString() {
    	String str = (this.currBoard.toString() + "Reached in " + numMoves + " moves.");
    	return str;   
    }
    
    //alternate string for our goal state
    public String goalStateString() {
    	String str = ("Puzzle Solved! The goal configuration:\n" + this.currBoard.toString() + " was reached in " + this.numMoves + " moves.");
    	return str;
    }
    
    //comparable interface method override to support priority queue ordering
    @Override
    public int compareTo(SlidingBlockState other) {
    	return (this.getHeuristic() - other.getHeuristic());
    }
    
}