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

		// Main player cycle for loop
		for (int player = 1; player <= nPlayers; player++) {
			firstTurn();
			rollAllDice();
			nextTurn();
			rollSelectedDice();
			nextTurn();
			rollSelectedDice();
			finalTurn();
		}
	}
	
	/* Initialises the scorecard based on nPlauers
	 * and 'zeroes' all cells to UNSCORED_VALUE
	 */
	private void initScorecard() {
		scorecard = new int[nPlayers][N_CATEGORIES];
		for (int i = 0; i < nPlayers, i++) {
			
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
		int categoryIndex = display.waitForPlayerToSelectCategory();
		
		// do not allow update if user has already recorded a score for this category
		if (
				updateScorecard(categoryIndex, activePlayer);
	}
	
	/* Monstrous method that updates the scorecard based on
	 * current state of dice, category index and player index
	 * 
	 * @param categoryIndex - the player's chosen category
	 */
	private void updateScorecard(int categoryIndex) {
		int score = 0;
		
		// case for Ones, Twos.... Sixes
		if (categoryIndex <= 6) {
			for (int i = 0; i < N_DICE; i++) {
				if (dice[i] == categoryIndex) {
					score += categoryIndex;
				}
			}
		}

		// case for three of a kind and four of a kind
		if (categoryIndex == 9 || categoryIndex == 10) {
			score = sumDice();
		}
				
		// case for full house
		if (categoryIndex == 11) {
			score = 25;
		}
		
		// case for small straight
		if (categoryIndex == 12) {
			score = 30;
		}
		
		// case for large straight
		if (categoryIndex == 13) {
			score = 40;
		}
		
		// case for yahtzee
		if (categoryIndex == 14) {
			score = 50;
		}
		
		// case for chance
		if (categoryIndex == 15) {
			score = sumDice();
		}
		
		// update appropriate element of scorecard
		scorecard[activePlayer - 1][categoryIndex - 1] = score;

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
	// Array representing the scorecard. Each player has a column
	// and each row corresponds to a category on the yahtzee table
	private int[][] scorecard;
	// Only one player can be active at a time, so this should be a classwide variable
	private int activePlayer;
	
	// the value of a cell on the scoresheet that has not yet been scored by the player
	private static final int UNSCORED_VALUE = -1;
	
}
