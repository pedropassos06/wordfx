/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2022
 * Instructor: Prof. Brian King
 *
 * Name: Liv & Gang
 * Section: 02 - 11AM
 * Date: 4/8/22
 * Time: 8:24 AM
 *
 * Project: csci205_final_project
 * Package: main.view
 * Class: WordleView
 *
 * Description:
 *
 * ****************************************
 */
package main.view;

import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import main.model.GameState;
import main.model.WordleModel;
import java.util.ArrayList;
import java.util.Map;

/**
 * This is the class that will update the interface that the user
 * sees. It will take care of the typing showing on the screen, as
 * well as clicks on virtual keyboard, flipping tiles, shaking tiles
 * horizontally, showing end messages.
 */
public class WordleView {

    /** The root node containing all three nodes above */
    private BorderPane root;

    /** The model of the game */
    private WordleModel wordleModel;

    /** Stackpane to contain tiles and win screen */
    private StackPane tileStack;

    /** Play again button to keep playing */
    private Button playAgainBtn;

    /** Keeps track of when tiles are done flipping */
    private boolean isFlippingDone = true;

    /** Is the shaking animation is done? */
    private boolean isShakingDone = true;

    /**
     * @return true if flipping is done, false if not
     */
    public boolean isFlippingDone() { return isFlippingDone; }

    /**
     * @return The play again button
     */
    public Button getPlayAgainBtn() { return playAgainBtn; }

    /**
     * @return The tile stack where all guesses are placed
     */
    public StackPane getTileStack() { return tileStack; }

    /**
     * @return the root containing header, tiles, and keyboard, to create our scene
     */
    public BorderPane getRoot() { return root; }

    /**
     * Simple constructor to initialize the scene graph
     */
    public WordleView(WordleModel wordleModel) {
        this.wordleModel = wordleModel;

        // Initialize the root for our display
        this.root = new BorderPane();
        this.root.setId("background");

        this.playAgainBtn = new Button("Play again?");
        this.playAgainBtn.setId("play-again-btn");

        initSceneGraph();
    }

    /**
     * Initializes the scene graph containing header, tiles, and virtual keyboard
     */
    private void initSceneGraph() {
        // Set the scene accordingly
        this.tileStack = new StackPane(this.wordleModel.getTiles().getTiles()); //this must be a stack for final message
        this.root.setCenter(this.tileStack);
        this.root.setBottom(this.wordleModel.getVk().getKeyboard());
        this.root.setTop(this.wordleModel.getHeader().getHeaderSection());
    }

    /**
     * Will perform the action of flipping a tile on the screen for a
     * given duration
     *
     * @param tile - specific tile to be flipped
     * @param index - index of the tile
     * @param style - style to add to tile (exact, misplaced, wrong)
     */
    public void performFlip(Label tile, int index, String style, EndMessageView endMessage,
                            Map<Integer, ArrayList<String>> keyboardColors) {
        isFlippingDone = false;

        RotateTransition rotation = new RotateTransition(Duration.seconds(1), tile);
        rotation.setDelay(Duration.millis(index*500));
        rotation.setAxis(Rotate.X_AXIS);
        rotation.setToAngle(360);
        rotation.play();
        rotation.setOnFinished(event -> {
            changeTileColor(style, index);
            changeKeyboardLetterColor(keyboardColors, index);
            showEndMessage(index, endMessage);
        });
    }

    /**
     * Will perform the action of shaking a tile on the screen for a
     * given duration
     */
    public void horizontalShakeTiles() {
        // Wait until animation is done to start again.
        if (!isShakingDone) return;
        isShakingDone = false;

        for (int i = 0; i < this.wordleModel.getWordLength(); i++) {
            // Get the tiles.
            Label tile = this.wordleModel.getListOfGuesses().get(this.wordleModel.getRow()).get(i);

            /** Shift tiles to the left. */
            TranslateTransition shakeTileLeft = new TranslateTransition(Duration.millis(45), tile);
            shakeTileLeft.setFromX(0); // go back to center.
            shakeTileLeft.setCycleCount(4); // transition go back.
            shakeTileLeft.setByX(-7.5); // how much shift of tile.
            shakeTileLeft.setAutoReverse(true); // reverse back automatically.

            /** Shift tiles to the right. */
            TranslateTransition shakeTileRight = new TranslateTransition(Duration.millis(45), tile);
            shakeTileRight.setFromX(0);
            shakeTileRight.setCycleCount(4);
            shakeTileRight.setByX(7.5);
            shakeTileRight.setAutoReverse(true);

            /** Sequential order for horizontal shake */
            SequentialTransition sequentialTransition = new SequentialTransition(shakeTileLeft, shakeTileRight);

            // Animation can be rerun when done.
            sequentialTransition.setOnFinished(finish -> {
                isShakingDone = true;
            });

            // Play the animation.
            sequentialTransition.play();
        }
    }

    /**
     * Shows end message if user is winner or loser. Also keeps
     * track to see if flipping is done or not.
     *
     * @param index - index of the letter being flipped (checking for last letter to flip)
     * @param endMessage - End message either You Won, or You Lost
     */
    private void showEndMessage(int index, EndMessageView endMessage) {

        if (index == this.wordleModel.getWordLength() - 1) {
            if (this.wordleModel.getGameState() == GameState.GAME_WINNER) {
                String message = "Your streak: " + this.wordleModel.getCurrentWinStreak();
                endMessage.showEndScreen("You won!", message);
            }
            else if (this.wordleModel.getGameState() == GameState.GAME_LOSER) {
                String message = "Secret word was " + this.wordleModel.getSecretWord().toUpperCase();
                endMessage.showEndScreen("You Lost!", message);}
            else {
                isFlippingDone = true;
            }
        }
    }

    /**
     * Takes care of binding what is being typed to the labels on the screen
     *
     * @param letter - Letter being typed
     * @param guess - Current guess user is typing on
     * @param letterTile - Next available tile
     */
    public void updateTyping(Text letter, int guess, int letterTile) {
        this.wordleModel.getListOfGuesses().get(guess).get(letterTile).textProperty()
                .bind(letter.textProperty());
    }

    /**
     * Deletes letter from the screen by binding a white space character to the label
     *
     * @param guess - Current guess user is typing on
     * @param letterTile - Next available tile
     */
    public void updateDelete(int guess, int letterTile) {
        this.wordleModel.getListOfGuesses().get(guess).get(letterTile).textProperty()
                .bind(new Text(" ").textProperty());
    }

    /**
     * Changes the color of the tiles containing the guessed letters (forming a guess word) on the screen
     *
     * @param style - css style: exact for green, misplaced for yellow, wrong for dark grey
     * @param index - index in which the letter is located on the guess
     */
    public void changeTileColor(String style, int index) {
        this.wordleModel.getListOfGuesses().get(this.wordleModel.getRow() - 1).get(index).getStyleClass().add(style);
    }

    /**
     * Changes the color on the virtual keyboard following the same logic as
     * the color changes in the tiles where letters are typed
     *
     * @param keyboardColors - Map containing keyboard letters and styles to be applied to them
     * @param tileIndex - index of tile to be updated
     */
    public void changeKeyboardLetterColor(Map<Integer, ArrayList<String>> keyboardColors, int tileIndex) {
        //String style, String letter, int wordLength
        if (tileIndex == (this.wordleModel.getWordLength() - 1)) {
            for (int i = 0; i < this.wordleModel.getWordLength(); i++) {
                // Obtain the index of current letter in guess and change its style
                int index = this.wordleModel.getLetterList().indexOf(keyboardColors.get(i).get(1));

                // Only possibility to change colors is from yellow to green, nothing else
                if (this.wordleModel.getKeysList().get(index).getStyleClass().toString().contains("misplaced") && (keyboardColors.get(i).get(0).equals("exact"))) {
                    this.wordleModel.getKeysList().get(index).getStyleClass().remove("misplaced");
                    this.wordleModel.getKeysList().get(index).getStyleClass().add("exact");
                }
        if (index >= 19) { index++; } //skip enter key before bottom row

        // Only possibility to change colors is from yellow to green, nothing else
        if (this.wordleModel.getKeysList().get(index).getStyleClass().toString().contains("misplaced") && (keyboardColors.get(i).get(0).equals("exact"))) {
            this.wordleModel.getKeysList().get(index).getStyleClass().remove("misplaced");
            this.wordleModel.getKeysList().get(index).getStyleClass().add("exact");
        }

                // Only edit the color of the keyboard key if it is not colored yet. Colored remains the same
                else if (!this.wordleModel.getKeysList().get(index).getStyleClass().contains("misplaced") &&
                        !this.wordleModel.getKeysList().get(index).getStyleClass().contains("exact") &&
                        !this.wordleModel.getKeysList().get(index).getStyleClass().contains("wrong")) {
                    this.wordleModel.getKeysList().get(index).getStyleClass().add(keyboardColors.get(i).get(0));
                }
            }
        }
    }
}
