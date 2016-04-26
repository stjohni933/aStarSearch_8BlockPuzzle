/**
 * @(#)SlidingBlockPuzzle.java
 * This class will be what one uses to run a solution for an instance of a sliding block puzzle. User will enter the numbers included in a starting board state either at the command line, or at runtime
 * 
 * This class can be called on a command line in one of two ways:
 *	1. By entering a starting board state as a single string where each element is separated by a space. The empty tile must be specified as a 0.
 *		-Example: java SlidingBlockPuzzle 0,1,3,4,2,5,6,7,8
 *	2. With no additional parameters. Required data will be retrieved via input prompts at runtime.
 *
 * @author Iain St. John
 * CSC 342 -- Artificial Intelligence -- Project One
 * @version 1.00 2016/2/13 -- finished 2016/2/25
 */

import java.util.Scanner;

public class SlidingBlockPuzzle {
        
    public static void main(String[] args) {
    	SlidingBlockSolver sbs; //our a* searching problem solving object
    	String filename; //parameter filename, if there is one
    	int startTiles[] = new int[9]; //array of starting tile positions, if entered
    	
    	//there was nothing entered on the command line, user will enter a starting state for our solver via standard input.
        if(args.length == 0) {
        	//create an input Scanner
        	Scanner inputScan = new Scanner(System.in);
        	Scanner strScan; //String scanner
        	
        	//create our explanatory string
        	String info = "A starting configuration for the 8-Block puzzle is required.\n";
        	info += "Numbered blocks are represented by their corresponding integers, and the blank space is represented with a zero.\n";
        	info += "The blocks/tiles must be entered in 3 rows with space between each number. Like so...\n0 1 3\n4 2 5\n7 8 6";
        	System.out.println(info);
        	
        	boolean complete = false; //boolean driving input validation
        	
        	//prompt and parse until we get all valid input
        	while(!complete) {
        	
	        	//start prompting for input for each row. Make sure only 3 were entered on each line.
	        	System.out.print("Enter the first row ==>");
	        	int ctr = 0; //counter and index
	        	for(int i = 0; i < 3; i++) {
	        		startTiles[ctr] = inputScan.nextInt();
	        		ctr++;
	        	}
	        	
	        	if(ctr != 3) {
	        		System.out.println("Enter 3 integers between 0 and 8 on each line. No more, no less.");
	        		System.out.println("continue\n");
	        		continue;	
	        	}
	        	else {
	        		System.out.print("Enter the second row ==>");
	        		for(int i = 0; i < 3; i++) {
	        			startTiles[ctr] = inputScan.nextInt();
	        			ctr++;
	        		}
	        		
	        		if(ctr != 6) {
	        			System.out.println("Enter 3 integers between 0 and 8 on each line. No more, no less.");
	        			continue;
	        		}
	        		
	        		else {
	        			System.out.print("Enter the third row ==>");
	        			for(int i = 0; i < 3; i++) {
	        				startTiles[ctr] = inputScan.nextInt();
	        				ctr++;
	        			}
	        			
	        			if(ctr != 9) {
        					System.out.println("Enter 3 integers between 0 and 8 on each line. No more, no less.");
	        				continue;
        				}
	        		}
	        		
	        	}
	        	//perform a final check
	        	if(startTiles.length < 9) {
	        		System.out.println("Invalid number of starting tiles. The 8-Block puzzle requires 9 numbers (0 for the empty space).");
	        		continue;
	        	}
	        	
	        	//if we reach this point, we should be all set to exit this validation loop
	        	complete = true;
        	} //end while !complete	
        	
        }//end args.length == 0
        
        //the user entered something on the command line. We need to make sure it's valid
        else {
        	//ensure there enough tiles there
        	if(args.length == 9) {
        		for(int i = 0; i < 9; i++) {	
        			startTiles[i] = Integer.parseInt(args[i]);
        		}
        	}
        	else {
        		System.out.println("Invalid number of starting tiles. The 8-Block puzzle requires 9 numbers (0 for the empty space). Recall with correct number.");
        		return;
        	}
        }
        
        //we will have data within startTiles now, create the solver with an initial SlidingBlockState created from the acquired tile array.
        SlidingBlockState startState = new SlidingBlockState(startTiles);
        sbs = new SlidingBlockSolver(startState);
        
       	//make sure a valid starting state was passed by the user.
       	String check = sbs.checkStartValidity();
       	if(check.equals("solved")) {
       		System.out.println("There's no need to solve an 8-Block puzzle that is already solved. Please retry with a different initial configuration.");
       		return;
       	}
       	if(check.equals("unsolvable")) {
       		System.out.println("This 8-Block configuration is not solvable. Please retry with a different initial configuration.");
       		return;
       	}
       	if(check.equals("repeats")) {
       		System.out.println("There cannot be any repeating values on an 8-Block board. Please retry with a different initial configuration.");
       		return;
       	}
       	if(check.equals("outlier")) {
       		System.out.println("All tile values must be within the range of 0 to 8. Please retry with a different initial configuration.");
       		return;
       	}
       	//all valid if we're here. All that's left is to call the search function, and let it run.
        sbs.aStarSearch();
    }
}
