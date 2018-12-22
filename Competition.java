import java.util.Scanner;

/**
 * The Competition class represents a Nim competition between two players, consisting of a given number of
 * rounds. It also keeps track of the number of victories of each player.
 */
public class Competition {

	static Player playerNum1, playerNum2;
	static boolean messageDisplay = false;
	static Scanner scanner = new Scanner(System.in);
	static int player1Score = 0;
	static int player2Score = 0;


	/*The constructor of the competition class which make a competition object with 3 fields*/
	Competition(Player player1, Player player2, boolean displayMessage) {
		playerNum1 = player1;
		playerNum2 = player2;
		messageDisplay = displayMessage;
	}

	/**
	 * The method runs a Nim competition between two players according to the three user-specified arguments.
	 * (1) The type of the first player, which is a positive integer between 1 and 4: 1 for a Random computer
	 * player, 2 for a Heuristic computer player, 3 for a Smart computer player and 4 for a human player.
	 * (2) The type of the second player, which is a positive integer between 1 and 4.
	 * (3) The number of rounds to be played in the competition.
	 *
	 * @param args an array of string representations of the three input arguments, as detailed above.
	 */
	public static void main(String[] args) {

		int p1Type = Integer.parseInt(args[0]);
		int p2Type = Integer.parseInt(args[1]);
		int numGames = Integer.parseInt(args[2]);


		/*Initialize players objects*/
		Player player1 = new Player(p1Type, 1, scanner);
		Player player2 = new Player(p2Type, 2, scanner);


		/*Display the general message of the game starting between two opponents,
		 which is always being displayed no matter what*/
		String player1type = player1.getTypeName();
		String player2type = player2.getTypeName();
		System.out.println("Starting a Nim competition of " + numGames + " rounds between a " + player1type +
				" player and a " + player2type + " player.");

		/*Check if message display should turn on or off - meaning in this case ,
		if we have human players or not*/
		if (p1Type == Player.HUMAN || p2Type == Player.HUMAN) {
			messageDisplay = true;
		}

		/*Create a competition object and call the playMultipleRounds method - start the game eventually*/
		Competition competition = new Competition(player1, player2, messageDisplay);
		competition.playMultipleRounds(numGames);
	}

	public void playMultipleRounds(int numberOfRounds) {
		Player currentPlayer;
		boolean valid = true;


		/*Play the game with the given number of rounds*/
		for (int round = 0; round < numberOfRounds; round++) {
			/*Initialize board*/
			Board board = new Board();

			/*Start game announcement!*/
			if(messageDisplay)
				System.out.println("Welcome to the sticks game!");

			/*A new player is initialized - player 1 is initialized always at start of a new game*/
			currentPlayer = playerNum1;

			/*Game round one after another*/
			while (board.getNumberOfUnmarkedSticks() != 0) {
				if((messageDisplay) && (valid))
					System.out.println("Player " + currentPlayer.getPlayerId() + ", it is now your turn!");
				/*produce the move - it will take care of the player type already*/
				/*try to mark the move if the move is a valid one*/
				Move player_move = currentPlayer.produceMove(board);
				/*if its not legal the program will skip rest code and evaluate again*/
				if(board.markStickSequence(player_move) != 0){
					System.out.println("Invalid move. Enter another:");
					valid = false;
					continue;
				}
				else {
					valid = true;
					/*Telling the exact move that has been made on about by last player*/
					if(messageDisplay)
						System.out.println("Player " + currentPlayer.getPlayerId() + " made the move: " + player_move);
					/*debug print - will delete it later on*/
					/*Turn is about to end Change the player respectively*/
					if(currentPlayer == playerNum1)
						currentPlayer = playerNum2;
					else
						currentPlayer = playerNum1;
				}
			}
			/*Print out the winner! - the player that have no more sticks to mark*/
			if(messageDisplay)
				System.out.println("Player " + currentPlayer.getPlayerId() + " won!");

			/*Distributes points/scores*/
			if (currentPlayer.getPlayerId() == 1)
				player1Score++;
			else
				player2Score++;
		}
			System.out.println("The results are "+ getPlayerScore(1) +":"+ getPlayerScore(2));
	}


/*If playerPosition = 1, the results of the first player is returned. If playerPosition = 2,
the result of the second player is returned. If playerPosition equals neiter, -1 is returned. */
	public int getPlayerScore(int playerPostion)
	{
		if(playerPostion == 1)
			return player1Score;
		else
			return player2Score;
	}
}







