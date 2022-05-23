/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2022
 * Instructor: Prof. Brian King
 *
 * Name: Liv & Gang
 * Section: 02 - 11AM
 * Date: 4/11/22
 * Time: 11:13 PM
 *
 * Project: csci205_final_project
 * Package: main.model
 * Class: WordleModel
 *
 * Description:
 *
 * ****************************************
 */
package main.model;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import main.main.ReadWordsFiles;
import main.view.Header;
import main.view.TileView;
import main.view.VirtualKeyboardView;
import java.util.ArrayList;

/**
 * Wordle model takes care of all the behind the scenes. Keeps track of the row
 * and column the next letter will be typed in, initializes header, virtual keyboard, and
 * tiles. Also keeps track of user win streak.
 */
public class WordleModel {
    /** Current row that we are on */
    private int row;

    /** Current column that we are on */
    private int column;

    /** Current guess number */
    private int currentGuessNumber;

    /** The 30 tiles representing all possible guesses */
    private TileView tiles;

    /** The virtual keyboard which user cna use to type */
    private VirtualKeyboardView vk;

    /** The "Wordle" header section */
    private Header header;

    /** List of all letters in the keyboard */
    private ArrayList<String> letterList;

    /** List of all 6 guesses in the game */
    private ArrayList<ArrayList<Label>> listOfGuesses;

    /** The list with the buttons on the virtual keyboard */
    private ArrayList<Button> keysList;

    /** Keeps track of how many games user has won */
    private int currentWinStreak;

    /** Reader object to create secret word/possible guess sets from txt files */
    private ReadWordsFiles reader;

    /** Secret word of current game */
    private String secretWord;

    /** Mode that user has chosen (3, 4, or 5-letter words) */
    private final int WORD_LENGTH;

    /**
     * @return length of words chosen by user
     */
    public int getWordLength() { return WORD_LENGTH; }

    /**
     * @return the secret word of the game
     */
    public String getSecretWord() { return secretWord; }

    /**
     * @return reader object
     */
    public ReadWordsFiles getReader() { return reader; }

    /**
     * @return the current win streak of the player
     */
    public int getCurrentWinStreak() { return currentWinStreak; }

    /**
     * Sets the win streak
     * @param currentWinStreak - current win streak of user
     */
    public void setStreak(int currentWinStreak) { this.currentWinStreak = currentWinStreak; }

    /**
     * @return List with all letters contained in last given guess
     */
    public ArrayList<String> getLetterList() { return letterList; }

    /**
     * @return List with all possible guesses (will be blank labels)
     */
    public ArrayList<ArrayList<Label>> getListOfGuesses() { return listOfGuesses; }

    /**
     * @return List with all buttons on the virtual keyboard
     */
    public ArrayList<Button> getKeysList() { return keysList; }

    /**
     * @return all 30 tiles
     */
    public TileView getTiles() { return tiles; }

    /**
     * @return the stackpane in the tile class
     */
    public StackPane getTileStackPane() { return this.tiles.getTileStackPane(); }

    /**
     * @return The VirtualKeyboard object that stores all keys on virtual keyboard
     */
    public VirtualKeyboardView getVk() { return vk; }

    /**
     * @return Header object that creates header section
     */
    public Header getHeader() { return header; }

    /** The current state of the game */
    private GameState gameState;

    /**
     * Sets the game state
     * @param gameState - a new state for the game (new game, in progress, paused, winner, loser)
     */
    public void setGameState(GameState gameState) { this.gameState = gameState; }

    /**
     * @return the state of the game
     */
    public GameState getGameState() { return gameState;}

    public int getCurrentGuessNumber() { return currentGuessNumber; }

    /**
     * Simple constructor of our model. Takes in the length of
     * word chosen by user, so it can shape the game accordingly.
     *
     * @param wordLength - length of word chosen by user
     */
    public WordleModel(int wordLength) {
        this.WORD_LENGTH = wordLength;

        // Three main components of interface
        this.header = new Header();
        this.tiles = new TileView(wordLength);
        this.vk = new VirtualKeyboardView();

        // Keep track of where the next letter is typed or deleted
        this.row = 0;
        this.column = -1;

        // state of game starts our with NEW_GAME
        this.gameState = GameState.NEW_GAME;
        this.currentWinStreak = 0;
        this.reader = new ReadWordsFiles();

        readFileOfWords(wordLength);
        initInterface();
    }

    /**
     * Reads file of words according to word length user has chosen
     *
     * @param wordLength - word length user has chosen (3, 4, or 5)
     */
    private void readFileOfWords(int wordLength) {
        switch (wordLength) {
            case 3:
                this.secretWord = this.reader.createRandomWord("src/main/resources/text-files/3words.txt");
                this.reader.createWordSet("src/main/resources/text-files/allowed3words.txt");
                break;
            case 4:
                this.secretWord = this.reader.createRandomWord("src/main/resources/text-files/4words.txt");
                this.reader.createWordSet("src/main/resources/text-files/allowed4words.txt");
                break;
            default:
                this.secretWord = this.reader.createRandomWord("src/main/resources/text-files/5words.txt");
                this.reader.createWordSet("src/main/resources/text-files/allowed5words.txt");
                break;

        }
    }

    /**
     * Initializes the interface with the header, tiles, and virtual keyboard
     */
    private void initInterface() {
        // Creating scene components
        this.header.createHeader();
        this.vk.createVirtualKeyboard();

        // Fill our array after creating the virtual keyboard
        this.letterList = this.vk.getKeyboardLetters();
        this.keysList = this.vk.getKeyboardKeys();
        this.listOfGuesses = this.tiles.getGuessList();
    }

    /** Increments row value by 1 -> going to a new guess after checking some guess */
    public void incrementRow() {
        this.row++;
    }

    /** Increments column value by 1 -> typing letter */
    public void incrementColumn() {
        this.column++;
    }

    /** Decreases column value by 1 -> deleting letter */
    public void decrementColumn() { this.column--; }

    /**
     * @return row value
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return column value
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Sets new column
     * @param col - new value for column
     */
    public void setColumn(int col) { this.column = col; }

    /**
     * Increments the win streak by one
     */
    public void incrementCurrentWinStreak() { this.currentWinStreak++; }

    /**
     * Increments the current guess
     */
    public void incrementCurrentGuessNumber() { this.currentGuessNumber++; }

    /**
     * @param index - index of guess tile
     * @return returns a given guess tile by index
     */
    public Label getLetter(int index) { return getListOfGuesses().get(getRow()).get(index); }
}
