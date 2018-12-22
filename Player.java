import java.util.Objects;
import java.util.Random;
import java.util.Scanner;


/**
 * The Player class represents a player in the Nim game, producing Moves as a response to a Board state.
 * Each player is initialized with a type, either human or one of several computer strategies, which
 * defines the move he produces when given a board in some state. The heuristic strategy of the player is
 * already implemented.  You are required to implement the rest of the player types according to the exercise
 * description.
 * @author OOP course staff
 */
public class Player {

	//Constants that represent the different players.
	/** The constant integer representing the Random player type. */
	public static final int RANDOM = 1;
	/** The constant integer representing the Heuristic player type. */
	public static final int HEURISTIC = 2;
	/** The constant integer representing the Smart player type. */
	public static final int SMART = 3;
	/** The constant integer representing the Human player type. */
	public static final int HUMAN = 4;
	/**Used by produceHeuristicMove() for binary representation of board rows. */
	private static final int BINARY_LENGTH = 4;
	
	private final int playerType;
	private final int playerId;
	private Scanner scanner;
	
	/**
	 * Initializes a new player of the given type and the given id, and an initialized scanner.
	 * @param type The type of the player to create.
	 * @param id The id of the player (either 1 or 2).
	 * @param inputScanner The Scanner object through which to get user input
	 * for the Human player type. 
	 */
	public Player(int type, int id, Scanner inputScanner){		
		// Check for legal player type (we will see better ways to do this in the future).
		if (type != RANDOM && type != HEURISTIC 
				&& type != SMART && type != HUMAN){
			System.out.println("Received an unknown player type as a parameter"
					+ " in Player constructor. Terminating.");
			System.exit(-1);
		}		
		playerType = type;	
		playerId = id;
		scanner = inputScanner;
	}
	
	/**
	 * @return an integer matching the player type.
	 */	
	public int getPlayerType(){
		return playerType;
	}
	
	/**
	 * @return the players id number.
	 */	
	public int getPlayerId(){
		return playerId;
	}
	
	/**
	 * @return a String matching the player type.
	 */
	public String getTypeName(){
		switch(playerType){
			
			case RANDOM:
				return "Random";			    
	
			case SMART: 
				return "Smart";	
				
			case HEURISTIC:
				return "Heuristic";
				
			case HUMAN:			
				return "Human";
		}
		//Because we checked for legal player types in the
		//constructor, this line shouldn't be reachable.
		return "UnknownPlayerType";
	}
	
	/**
	 * This method encapsulates all the reasoning of the player about the game. The player is given the 
	 * board object, and is required to return his next move on the board. The choice of the move depends
	 * on the type of the player: a human player chooses his move manually; the random player should 
	 * return some random move; the Smart player can represent any reasonable strategy; the Heuristic 
	 * player uses a strong heuristic to choose a move. 
	 * @param board - a Board object representing the current state of the game.
	 * @return a Move object representing the move that the current player will play according to
	 * his strategy
	 */
	public Move produceMove(Board board){
		
		switch(playerType){
		
			case RANDOM:
				return produceRandomMove(board);
				    
			case SMART:
				return produceSmartMove(board);
				
			case HEURISTIC:
				return produceHeuristicMove(board);
				
			case HUMAN:
				return produceHumanMove(board);

			//Because we checked for legal player types in the
			//constructor, this line shouldn't be reachable.
			default: 
				return null;			
		}
	}

	/*This method check row availability whether there are marked sticks or not
	* if there is still at least 1 unmark stick it will return true else,
	* if all row is marked false will be given*/
	boolean isRowAvailable(Board board, int row) {
		for (int i = 1; i <= board.getRowLength(row); i++) {
			if (board.isStickUnmarked(row, i))
				return true;
		}
		return false;
	}

	/*This method checks if the chosen sequence is all clear at ready to be marked or it
	* it not valid, which means there are marked sticks in sequence!*/
	boolean isSequenceAvailable(Board board, int row, int right, int left){
		for(int i = left; i <= right; i++) {
			if(!board.isStickUnmarked(row, i))
				return false;
		}
		return true;
	}

	/*
	 * Produces a random move.
	 */
	private Move produceRandomMove(Board board) {

		Random myRandom = new Random();
		int row,left,right;

			/*Randomized row from the the current row size and checks if this row is not all marked!*/
			do{
				row = myRandom.nextInt(board.getNumberOfRows()) + 1;
			}
			while(!isRowAvailable(board,row));

			/*Randomized right and left after a valid row has been chosen*/
			do {
				do {
					/*Randomized a sequence according to the chosen legal row!*/
					left = myRandom.nextInt(board.getRowLength(row)) + 1;
					/*Randomized the right index according to the left one as minimum value*/
					right = myRandom.nextInt(board.getRowLength(row)) + 1;
				}
				while(right < left);
			}
			while(!isSequenceAvailable(board, row, right, left));
			/*If the move is finally legal we can surely produce the move and return it!*/
			Move RandMove = new Move(row, left, right);
			return RandMove;
		}
	
	/*
	 * Produce some intelligent strategy to produce a move
	 */
	private Move produceSmartMove(Board board) {
		/*First check the number of unmarked sticks in thr entire board*/
		int restSticks = board.getNumberOfUnmarkedSticks();
		String state = "even";
		int chosenRow = 0, chosenLeft = 0, chosenRight = 0;

		/*if the rest amount is even*/
		if (!(restSticks % 2 == 0))
			state = "odd";

		/*Check if we can make a win strategy at all*/
		if (restSticks > 1){
			/*start marking by making correct move*/
			overlook:
			for (int row = 1; row <= board.getNumberOfRows(); row++) {
				for (int stick = 1; stick <= board.getRowLength(row); stick++) {
					/*Check the first stick unmarked while the rest total amount is even,
					 so we can decrease the amount again by one, to an odd value*/
					if ((board.isStickUnmarked(row, stick)) && (Objects.equals(state, "even"))){
						chosenRow = row;
						chosenLeft = stick;
						chosenRight = stick;
						break overlook;
					}
					/*Check two first stick in sequence or not in a sequence - then decrease only 1 which are unmarked
					and rest total amount is odd, so we can decrease the amount again to an odd value*/
					if ((board.isStickUnmarked(row, stick)) && (Objects.equals(state, "odd"))){
						if(board.isStickUnmarked(row, stick+1)) {
							chosenRow = row;
							chosenLeft = stick;
							chosenRight = stick + 1;
							break overlook;
						}
						else{
							chosenRow = row;
							chosenLeft = stick;
							chosenRight = stick;
							break overlook;
						}
					}
				}
			}
		}
		/*this section deals with 1 rest stick that need to be mark
		meaning we lost - i hope we wont get here anytime*/
		else
		{
			doubleloop:
			for (int i = 1; i <= board.getNumberOfRows(); i++) {
				for (int j = 1; j <= board.getRowLength(i); j++) {
					if (board.isStickUnmarked(i, j)) {
						chosenRow = i;
						chosenLeft = j;
						chosenRight = j;
						break doubleloop;
					}
				}
			}
		}
		Move SmartMove = new Move(chosenRow,chosenLeft,chosenRight);
		return SmartMove;
	}

	

	/*Interact with the user to produce his move.*/

	private Move produceHumanMove(Board board){
		System.out.println("Press 1 to display the board. Press 2 to make a move:");
		int userInput = scanner.nextInt();
		switch (userInput) {
			case 1:
				System.out.println(board);
				return produceHumanMove(board);
			case 2:
				/*ask for user move and then actually make it happen*/
				System.out.println("Enter the row number:");
				int rowNumber = scanner.nextInt();
				System.out.println("Enter the index of the leftmost stick:");
				int leftNumber = scanner.nextInt();
				System.out.println("Enter the index of the rightmost stick:");
				int rightNumber = scanner.nextInt();

				/*make the move object!*/
				return new Move(rowNumber, leftNumber, rightNumber);
			default:
				System.out.println("Unsupported command");
				return produceHumanMove(board);
		}
	}
	
	/*
	 * Uses a winning heuristic for the Nim game to produce a move.
	 */
	private Move produceHeuristicMove(Board board){

		int numRows = board.getNumberOfRows();
		int[][] bins = new int[numRows][BINARY_LENGTH];
		int[] binarySum = new int[BINARY_LENGTH];
		int bitIndex,higherThenOne=0,totalOnes=0,lastRow=0,lastLeft=0,lastSize=0,lastOneRow=0,lastOneLeft=0;
		
		for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
			binarySum[bitIndex] = 0;
		}
		
		for(int k=0;k<numRows;k++){
			
			int curRowLength = board.getRowLength(k+1);
			int i = 0;
			int numOnes = 0;
			
			for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
				bins[k][bitIndex] = 0;
			}
			
			do {
				if(i<curRowLength && board.isStickUnmarked(k+1,i+1) ){
					numOnes++;
				} else {
					
					if(numOnes>0){
						
						String curNum = Integer.toBinaryString(numOnes);
						while(curNum.length()<BINARY_LENGTH){
							curNum = "0" + curNum;
						}
						for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
							bins[k][bitIndex] += curNum.charAt(bitIndex)-'0'; //Convert from char to int
						}
						
						if(numOnes>1){
							higherThenOne++;
							lastRow = k +1;
							lastLeft = i - numOnes + 1;
							lastSize = numOnes;
						} else {
							totalOnes++;
						}
						lastOneRow = k+1;
						lastOneLeft = i;
						
						numOnes = 0;
					}
				}
				i++;
			}while(i<=curRowLength);
			
			for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
				binarySum[bitIndex] = (binarySum[bitIndex]+bins[k][bitIndex])%2;
			}
		}
		
		
		//We only have single sticks
		if(higherThenOne==0){
			return new Move(lastOneRow,lastOneLeft,lastOneLeft);
		}
		
		//We are at a finishing state				
		if(higherThenOne<=1){
			
			if(totalOnes == 0){
				return new Move(lastRow,lastLeft,lastLeft+(lastSize-1) - 1);
			} else {
				return new Move(lastRow,lastLeft,lastLeft+(lastSize-1)-(1-totalOnes%2));
			}
			
		}
		
		for(bitIndex = 0;bitIndex<BINARY_LENGTH-1;bitIndex++){
			
			if(binarySum[bitIndex]>0){
				
				int finalSum = 0,eraseRow = 0,eraseSize = 0,numRemove = 0;
				for(int k=0;k<numRows;k++){
					
					if(bins[k][bitIndex]>0){
						eraseRow = k+1;
						eraseSize = (int)Math.pow(2,BINARY_LENGTH-bitIndex-1);
						
						for(int b2 = bitIndex+1;b2<BINARY_LENGTH;b2++){
							
							if(binarySum[b2]>0){
								
								if(bins[k][b2]==0){
									finalSum = finalSum + (int)Math.pow(2,BINARY_LENGTH-b2-1);
								} else {
									finalSum = finalSum - (int)Math.pow(2,BINARY_LENGTH-b2-1);
								}
								
							}
							
						}
						break;
					}
				}
				
				numRemove = eraseSize - finalSum;
				
				//Now we find that part and remove from it the required piece
				int numOnes=0,i=0;
				while(numOnes<eraseSize){

					if(board.isStickUnmarked(eraseRow,i+1)){
						numOnes++;
					} else {
						numOnes=0;
					}
					i++;
					
				}
				return new Move(eraseRow,i-numOnes+1,i-numOnes+numRemove);
			}
		}
		
		//If we reached here, and the board is not symmetric, then we only need to erase a single stick
		if(binarySum[BINARY_LENGTH-1]>0){
			return new Move(lastOneRow,lastOneLeft,lastOneLeft);
		}
		
		//If we reached here, it means that the board is already symmetric, and then we simply mark one stick
		// from the last sequence we saw:
		return new Move(lastRow,lastLeft,lastLeft);		
	}
	
	
}
