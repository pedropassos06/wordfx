/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2022
 * Instructor: Prof. Brian King
 *
 * Name: Liv & Gang
 * Section: 02 - 11AM
 * Date: 4/12/22
 * Time: 4:32 PM
 *
 * Project: csci205_final_project
 * Package: main.controller
 * Class: GuessEvaluator
 *
 * Description:
 *
 * ****************************************
 */
package main.main;

import main.model.GameState;
import main.model.WordleModel;
import main.view.EndMessageView;
import main.view.WordleView;

import java.util.*;

/**
 * This is a simple class to evaluate guesses by users
 */
public class GuessEvaluator {

    /** Secret word to be guessed */
    private final String secretWord;

    /** The model of the game */
    private WordleModel wordleModel;

    /** The view of the game */
    private WordleView wordleView;

    /** End message of the game */
    private EndMessageView endMessage;

    /** Stores virtual keyboard letters and new styles for them after a given guess */
    private Map<Integer, ArrayList<String>> keyboardColors;

    /**
     * Simple GuessEvaluator constructor to define the secret word, current guess,
     * and a guess analysis
     */
    public GuessEvaluator(WordleModel wordleModel, WordleView wordleView, String secretWord) {
        this.wordleModel = wordleModel;
        this.wordleView = wordleView;
        this.secretWord = secretWord;
        this.endMessage = new EndMessageView(this.wordleModel, this.wordleView);
        this.keyboardColors = new HashMap<>();
    }

    /**
     * Creates a map of the keyboard tiles associated with their letter and style
     *
     * @param style - the style of the keyboard tile
     * @param letter - the letter of the keyboard tile
     * @param index - the index of the keyboard tile
     */
    private void setKeyboardLetterColor(String style, String letter, int index) {
        // exact = *
        // misplaced = +
        // wrong = -

        keyboardColors.put(index, new ArrayList<String>());
        keyboardColors.get(index).add(style);
        keyboardColors.get(index).add(letter);

    }

    /**
     * Creates an evaluator for a given guess. The evaluator will take care of
     * finding if a letter is in the correct position, misplaced, or not even
     * in the word. Analysis is displayed on screen
     *
     * @param guess - Given guess by user
     */
    public void feedback(String guess) {
        // Obtain result from analyzing guess
        String evaluation = analyzeGuess(guess, this.wordleModel.getWordLength());
        showAnalysis(evaluation, guess);

        // If the user gets the right word
        if (evaluation.equals("*".repeat(this.wordleModel.getWordLength()))) {
            winnerUser();
        }
        // If user runs out of guesses
        else if (this.wordleModel.getRow() >= 5) {
            loserUser();
        }

        this.wordleModel.incrementCurrentGuessNumber();
    }

    /**
     * Returns an encoded string with -, +, and * characters indicating
     * what letters are promising. First, letters in the correct position
     * will receive a * in the guessAnalysis. Those letters that receive a
     * asterisk will be 'removed' from both arrays by being replaced with
     * characters that are not used in the game. Then, take care of letters
     * that are yet to be checked by comparing to letters that haven't received a
     * asterisk and if present in the correctArray, letter will receive a +.
     *
     * @param currentGuess - user guess to be analyzed
     * @param wordLength - shows what mode user is playing
     * @return an encoded string with -, +, and * characters indicating
     * what letters are promising
     */
    public String analyzeGuess(String currentGuess, int wordLength) {

        /** This is the analysis of the given guess, including correct and incorrect letters */
        StringBuffer guessAnalysis = new StringBuffer("-".repeat(wordLength));

        /** Array that keeps track of letters contained in the secret word */
        ArrayList<Character> correctArray = new ArrayList<>();

        /** Array that keeps track of letters contained in the guess word */
        ArrayList<Character> guessArray = new ArrayList<>();

        // Fill one array with letters from secret word, and another array with letters from user guess
        for (int i = 0; i < wordLength; i++) { correctArray.add(i, this.secretWord.charAt(i)); }
        for (int i = 0; i < wordLength; i++) { guessArray.add(i, currentGuess.charAt(i)); }

        // Take care of letters that are in the correct positioning first
        for (int i = 0; i < wordLength; i++) {
            if (correctArray.get(i) == guessArray.get(i)) {
                guessAnalysis.setCharAt(i, '*');
                correctArray.set(i, '#');
                guessArray.set(i, '!');
            }
        }

        // Next, if a letter in the guess is still present in the array
        // of letters from secret word that didn't get a *, place a +
        for (int i = 0; i < wordLength; i++) {
            if (correctArray.contains(guessArray.get(i))) {
                guessAnalysis.setCharAt(i, '+');
                correctArray.remove(guessArray.get(i));
            }
        }
        return guessAnalysis.toString();
    }

    /**
     * Shows the analysis on the screen by performing the flip of letters
     *
     * @param evaluation - set of *, +, - representing green, yellow, and gray respectively
     * @param currentGuess - last guess given by user
     */
    public void showAnalysis(String evaluation, String currentGuess) {
        String style;
        for (int i = 0; i < wordleModel.getWordLength(); i++) {

            if (evaluation.charAt(i) == '*') style = "exact";
            else if (evaluation.charAt(i) == '+') style = "misplaced";
            else  style = "wrong";

            this.wordleView.performFlip(this.wordleModel.getLetter(i), i, style, this.endMessage, keyboardColors);
            setKeyboardLetterColor(style, Character.toString(currentGuess.charAt(i)), i);
        }
    }

    /**
     * In case user is a loser, we tell them the secret word
     * and print final message
     */
    public void loserUser() {
        this.wordleModel.setGameState(GameState.GAME_LOSER);
        this.wordleModel.setStreak(0);
    }

    /**
     * In case user is a winner, we keep track of wins and print final message
     */
    public void winnerUser() {
        this.wordleModel.setGameState(GameState.GAME_WINNER);
        this.wordleModel.incrementCurrentWinStreak();
    }
}
