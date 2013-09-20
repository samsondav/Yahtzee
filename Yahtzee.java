/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}

	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		initScorecard();
		playGame();
	}

	private void playGame() {
		/* You fill this in */

		/* MAIN PLAY LOOP
		 * 
		 * Note: This code uses a model that there can only be one active player
		 * at a time. This is switched using activePlayer. Unless otherwise stated,
		 * all methods only apply to the currently active player.
		 */
		int turn = 0;
		
		while (true) {
			// break if we just ended the final turn
			if (turn > N_TURNS) {
				break;
			}
			
			for (int player = 1; player <= nPlayers; player++) {
			activePlayer = player;
			firstTurn();
			rollAllDice();
			nextTurn();
			rollSelectedDice();
			nextTurn();
			rollSelectedDice();
			finalTurn();
			updateTotal();
			}
		}
			
		// update all scores, apply bonuses and announce the winner
		endGame();
		
		}
	}
	
	/* Initialises the scorecard based on nPlayers
	 * and 'zeroes' all cells to UNSCORED_VALUE
	 */
	private void initScorecard() {
		scorecard = new int[nPlayers][N_CATEGORIES];
		for (int i = 0; i < nPlayers; i++) {
			for (int j = 0; j < N_CATEGORIES; j++) {
				scorecard[i][j] = UNSCORED_VALUE;
			}
		}
	}
	
	/* Print welcome message and wait for player input */
	private void firstTurn() {
		display.printMessage(playerNames[activePlayer - 1] + "\'s turn! Click \"Roll Dice\" button to roll the dice.");
		display.waitForPlayerToClickRoll(activePlayer);
	}
	
	/* Print message and wait for user to select dice and roll again */
	private void nextTurn() {
		display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\"");
		display.waitForPlayerToSelectDice();
	}
	
	/* Print message, wait for user to select category and update scorecard */
	private void finalTurn() {
		display.printMessage("Select a category for this roll");
		int categoryIndex;
		
		// only allow writing to scorecard if user has NOT already recorded a score
		// for this category
		while(true) {
			categoryIndex = display.waitForPlayerToSelectCategory();
			if (scorecard[activePlayer - 1][categoryIndex - 1] == UNSCORED_VALUE) {
				writeScoreToScorecard(categoryIndex);
				break;
			} else {
				display.printMessage("You already picked that category. Please choose another category.");
			}
		}
		
		// Update display with the appropriate score
		display.updateScorecard(categoryIndex, activePlayer, scorecard[activePlayer - 1][categoryIndex - 1]);
	}

	/* NOTE: This method must be called at the end of every turn, and updates
	 * total for the active player ONLY.
	 * 
	 * It updates scorecard so that the total reflects the sum of the current scores
	 */
	private void updateTotal() {
		int total = 0;
		
		// sum every SCORED value in the column of the currently active player
		for (int i = 1; i <= N_CATEGORIES; i++) {
			if (i == TOTAL) {
				//DO NOTHING - do not count the total row itself
			} else if (scorecard[activePlayer - 1][i - 1] != UNSCORED_VALUE) {
				total+= scorecard[activePlayer - 1][i - 1];
			}
		}
		// write total to scorecard[][] and update display to reflect the new value
		scorecard[activePlayer - 1][TOTAL - 1] = total;
		display.updateScorecard(TOTAL, activePlayer, total);
	}
	
	/* return true if players have filled all categories i.e. game is over */
	private boolean isGameOver() {
		// TODO: fix this so it actually detects when the game is over
		return false;
	}
	
	/* update all scores, apply bonuses and announce the winner
	 * 
	 */
	private void endGame() {
		//TODO: write this method
	}
	
	/* Monstrous method that updates the scorecard based on
	 * current state of dice, category index and player index
	 * 
	 * WARNING: This method is 'dumb' and does not check if a score already exists in
	 * a certain category and will overwrite any existing values
	 * 
	 * Additionally it does NOT check to see if the current dice configuration is valid
	 * for this chosen category
	 * 
	 * @param categoryIndex - the player's chosen category
	 */
	private void writeScoreToScorecard(int categoryIndex) {
		int score = 0;
		
		// case for Ones, Twos.... Sixes
		if (categoryIndex <= SIXES) {
			for (int i = 0; i < N_DICE; i++) {
				if (dice[i] == categoryIndex) {
					score += categoryIndex;
				}
			}
		}

		/* NOTE: For the cases below, some additional logic must be used to determine
		 * if the current dice configuration is actually valid for the chosen category
		 * 
		 * This logic is engaged by calling the isRollValid() method
		 * 
		 * If the current roll is invalid for the chosen category, the score will be
		 * recorded as a 0
		 */
		if (isRollValid(categoryIndex)) {
			// case for three of a kind and four of a kind
			if (categoryIndex == THREE_OF_A_KIND || categoryIndex == FOUR_OF_A_KIND) {
				score = sumDice();
			} else if (categoryIndex == FULL_HOUSE) {
				score = 25;
			} else if (categoryIndex == SMALL_STRAIGHT) {
				score = 30;
			} else if (categoryIndex == LARGE_STRAIGHT) {
				score = 40;
			} else if (categoryIndex == YAHTZEE) {
				score = 50;
			}
		} else {
			// dice configuration not valid for chosen category option, score 0
			score = 0;
		}
			
		if (categoryIndex == CHANCE) {
			score = sumDice();
		}
		
		// update appropriate element of scorecard
		scorecard[activePlayer - 1][categoryIndex - 1] = score;
	}
	
	/* Determine whether dice roll is valid for the given category.
	 * 
	 * NOTE: This method only implements any interesting logic for the following categories:
	 * THREE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, SMALL_STRAIGHT, LARGE_STRAIGHT, YAHTZEE
	 * 
	 * For all other categories, any dice configuration is valid so we just return true
	 * 
	 * @param categoryIndex
	 * @return true if current state of dice matches supplied category. Otherwise
	 * return false
	 */
	private boolean isRollValid(int categoryIndex) {
		switch (categoryIndex) {
		case THREE_OF_A_KIND: return YahtzeeMagicStub.checkCategory(dice, categoryIndex);
		case FOUR_OF_A_KIND: return YahtzeeMagicStub.checkCategory(dice, categoryIndex);
		case FULL_HOUSE: return YahtzeeMagicStub.checkCategory(dice, categoryIndex);
		case SMALL_STRAIGHT: return YahtzeeMagicStub.checkCategory(dice, categoryIndex);
		case LARGE_STRAIGHT: return YahtzeeMagicStub.checkCategory(dice, categoryIndex);
		case YAHTZEE: return YahtzeeMagicStub.checkCategory(dice, categoryIndex);
		default: return true;
		}
	}
	
	/* Return sum of all values on the dice */
	private int sumDice() {
		int sum = 0;
		for (int i = 0; i < N_DICE; i++) {
			sum += dice[i];
		}
		return sum;
	}
	
	/* Roll dice and update display */
	private void rollAllDice() {
		for (int i = 0; i < N_DICE; i++) {
			dice[i] = rgen.nextInt(1, 6);
		}
		display.displayDice(dice);
	}
	
	/* Roll only dice selected by user */
	private void rollSelectedDice() {
		for (int i = 0; i < N_DICE; i++) {
			if (display.isDieSelected(i)){
				dice[i] = rgen.nextInt(1, 6);
			}
		}
		display.displayDice(dice);
	}
	
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	
	// The set of dice
	private int[] dice = new int[N_DICE];
	/*Array representing the scorecard. Each player has a column
	 * and each row corresponds to a category on the yahtzee table
	 * 
	 * NOTE: categoryIndex starts at 0 in scorecard and starts at 1 as described
	 * in YahtzeeConstants additionally, player index starts at 1, not at 0
	 * because the YahtzeeDisplay class expects this*/
	private int[][] scorecard;
	private int activePlayer;
	
	// the value of a cell on the scoresheet that has not yet been scored by the player
	private static final int UNSCORED_VALUE = -1;
	// the number of turns each player gets before the game is over
	private static final int N_TURNS = 13;
	
}
