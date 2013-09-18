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
			firstTurn(player);
			rollAllDice();
			nextTurn();
			rollSelectedDice();
			nextTurn();
			rollSelectedDice();
			finalTurn();
		}
	}
	
	/* Initialises the scorecard based on the number of players */
	private void initScorecard() {
		scorecard = new int[nPlayers][N_CATEGORIES];
	}
	
	/* Print welcome message and wait for player input */
	private void firstTurn(int player) {
		display.printMessage(playerNames[player - 1] + "\'s turn! Click \"Roll Dice\" button to roll the dice.");
		display.waitForPlayerToClickRoll(player);
	}
	
	/* Print message and wait for user to select dice and roll again */
	private void nextTurn() {
		display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\"");
		display.waitForPlayerToSelectDice();
	}
	
	/* Print message and wait for user to select category */
	private void finalTurn() {
		display.printMessage("Select a category for this roll");
		int categoryIndex = display.waitForPlayerToSelectCategory();
		// update selected category on scorecard with appropriate score
		
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
	
}
