/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2022
 * Instructor: Prof. Brian King
 *
 * Name: Liv & Gang
 * Section: 02 - 11AM
 * Date: 4/8/22
 * Time: 11:12 AM
 *
 * Project: csci205_final_project
 * Package: main.controller
 * Class: Wordle Controller
 *
 * Description:
 *
 * ****************************************
 */
package main.controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.model.WordleModel;
import main.main.GuessEvaluator;
import main.main.WordleMain;
import main.view.EndMessageView;
import main.view.WordleView;
import java.io.IOException;

/**
 * This class takes care of event handling. Some examples are
 * mouse clicking on the virtual keyboard, typing on physical keyboard,
 * deleting letters, and entering guesses.
 */
public class WordleController {

    /** The model of our Wordle implementation */
    private WordleModel wordleModel;

    /** The view of our Wordle implementation */
    private WordleView wordleView;

    /** The scene, to take care of keyboard typing */
    private Scene scene;

    /** The state of our row, initially unchecked */
    private GuessState guessState;

    /** Evaluates the guess based on the secret word */
    private GuessEvaluator evaluator;

    /** The end message of the game once user is a winner or loser */
    private EndMessageView endMessage;

    /** Initial x-coordinate of node being dragged */
    private double initialX;

    /** Initial y-coordinate of node being dragged */
    private double initialY;

    /**
     * Simple constructor for our WorldFX game
     *
     * @param wordleView - The view of the game
     * @param wordleModel - The model of the game
     * @param scene - the scene to capture key events
     */
    public WordleController(WordleView wordleView, WordleModel wordleModel, Scene scene) {
        // Initialize view, model, and scene
        this.wordleView = wordleView;
        this.wordleModel = wordleModel;
        this.endMessage = new EndMessageView(this.wordleModel, this.wordleView);
        this.scene = scene;

        // Guess state starts out unchecked
        this.guessState = GuessState.UNCHECKED;

        // Initialize the guess evaluator and end message
        this.evaluator = new GuessEvaluator(this.wordleModel, this.wordleView, this.wordleModel.getSecretWord());

        initEventHandlers();
    }

    /**
     * Initialize event handling such as typing on keyboard or
     * clicking on virtual keyboard
     */
    private void initEventHandlers() {

        // If virtual keyboard is clicked
        for (Button b : this.wordleModel.getVk().getKeyboardKeys()) {
            makeDraggable(b);
        }

        // If typed on physical keyboard
        this.scene.setOnKeyPressed(event -> {
            try {
                takeActionFromKeyPressed(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.wordleView.getPlayAgainBtn().setOnMouseClicked(this::restartGame);
    }

    /**
     * Starts a new game if user would like to continue. Ensures
     * that screen stays on the same size user was using it.
     *
     * @param event button handler
     */
    public void restartGame(Event event) {
        // Stops from implicitly exiting out the application before a
        // new stage is shown.
        Platform.setImplicitExit(false);

        // The main class or our implementation
        WordleMain wm = new WordleMain();

        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();

        // Close the scene a start a new one.
        stage.close();
        try {
            wm.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Set the streak of the game.
        wm.setStreak(this.wordleModel.getCurrentWinStreak());

        // Reapply last window position.
        stage.setX(stage.getX());
        stage.setY(stage.getY());

        // Reapply last window size.
        stage.setHeight(stage.getHeight());
        stage.setWidth(stage.getWidth());

        // Start the stage again.
        wm.start(stage);

        // Finish stage once closed
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Takes action from key pressed on virtual keyboard. Either type,
     * delete, or press enter.
     */
    private void takeActionFromVirtualKeyboard(Button b) {
        // If the game is not playable return to the call stack.
        if (!this.wordleModel.getGameState().isPlayable()) {
            return;
        }

        Text t = new Text(b.getText().toUpperCase());
        switch (b.getText()) {
            // If user wants to delete last letter checked
            case "": //"" represents the delete key which is an icon
                if (this.wordleModel.getColumn() >= 0) {
                    deleteFromTile();
                }
                break;
            // If user wants to check guess
            case "ENTER":
                checkInput();
                break;

            // If user wants to simply type from virtual keyboard
            default:
                if (this.wordleView.isFlippingDone()) {
                    typeToTile(t);
                }
                break;
        }
    }

    /**
     * Takes action such as typing, deleting, or pressing enter
     * on physical keyboard
     *
     * @param event - {@KeyEvent} representing key pressed on physical keyboard
     */
    private void takeActionFromKeyPressed(KeyEvent event) throws IOException {
        // If the game is not playable return to the call stack.
        if (!this.wordleModel.getGameState().isPlayable()) {
            return;
        }

        Text t = new Text(event.getText().toUpperCase());
        switch (event.getCode()) {
            // If user wants to delete a letter
            case BACK_SPACE:
                if (this.wordleModel.getColumn() >= 0) { deleteFromTile(); }
                break;

            // If user wants to check a guess
            case ENTER:
                checkInput();
                break;

            // If user types on keyboard
            default:
                if (event.getCode().isLetterKey() && this.wordleView.isFlippingDone()) {
                    typeToTile(t);
                }
                break;
        }
    }

    /**
     * Checks for validity of input. Checks if input has enough letters
     * and if it is in the word list.
     */
    private void checkInput() {
        String guess = getGuessFromTiles().toString().toLowerCase();
        // Ensure guess is valid by length and being in word list
        if (this.wordleModel.getColumn() == (this.wordleModel.getWordLength() - 1)) {
            if (this.wordleModel.getReader().isWordInSet(guess)) {
                // Evaluate guess, switch the guess state to checked, and jump to next guess
                this.evaluator.feedback(guess);
                this.guessState = GuessState.CHECKED;
                this.wordleModel.incrementRow();
                this.wordleModel.setColumn(0);
            }
            else {
                this.endMessage.invalidInputScreen("Invalid word");
                this.wordleView.horizontalShakeTiles();
            }
        }
        else {
            this.endMessage.invalidInputScreen("Not enough letters");
            this.wordleView.horizontalShakeTiles();
        }
    }

    /**
     * @return the guess from reading the labels aka tiles
     */
    private StringBuffer getGuessFromTiles() {
        StringBuffer s = new StringBuffer("");
        this.wordleModel.getListOfGuesses().get(this.wordleModel.getRow()).forEach(t -> s.append(t.getText()));
        return s;
    }

    /**
     * Deletes the letter from the latest tile that contains the letter
     */
    private void deleteFromTile() {
        this.wordleView.updateDelete(this.wordleModel.getRow(), this.wordleModel.getColumn());
        this.wordleModel.decrementColumn();
    }

    /**
     * Adds the letter to the next open tile. If on last tile of a guess,
     * must check guess before jumping to typing in the next row.
     *
     * @param letter - letter to be added
     */
    private void typeToTile(Text letter) {
        if ((this.guessState == GuessState.UNCHECKED) && (this.wordleModel.getColumn() < (this.wordleModel.getWordLength() - 1))) {
            this.wordleModel.incrementColumn();
            this.wordleView.updateTyping(letter, this.wordleModel.getRow(), this.wordleModel.getColumn());
        }
        else if (this.guessState == GuessState.CHECKED) {
            this.wordleModel.setColumn(0);
            this.guessState = GuessState.UNCHECKED;
            this.wordleView.updateTyping(letter, this.wordleModel.getRow(), this.wordleModel.getColumn());
        }
    }

    /**
     * Makes every key in the virtual keyboard draggable
     * @param key - virtual keyboard key
     */
    private void makeDraggable(Button key) {

        // Initial x and y coordinates of the key
        double startX = key.getTranslateY();
        double startY = key.getTranslateX();

        key.setOnMousePressed(event -> {
            // Obtain offset points
            initialX = event.getSceneX() - key.getTranslateX();
            initialY = event.getSceneY() - key.getTranslateY();
        });

        key.setOnMouseDragged(event -> {
            // Set the translations based on offset
            key.setTranslateX(event.getSceneX() - initialX);
            key.setTranslateY(event.getSceneY() - initialY);
        });

        key.setOnMouseReleased(event -> {
            checkDraggingConditions(key, event);

            // If letter has barely moved on screen, count it as a single click, not a drag
            if (Math.abs(key.getTranslateX() - startX) < 10) {
                key.setOnMouseClicked(e -> takeActionFromVirtualKeyboard(key));
            }

            // Send key back to its original position
            key.setTranslateX(startX);
            key.setTranslateY(startY);
        });
    }

    /**
     * This method checks if a key from the virtual keyboard is being dragged, and if
     * so, into what tile it is being dragged. There are many conditions, so follow
     * the comments if needed.
     *
     * @param key - Button b (key from virtual keyboard)
     * @param event - event of mouse being released
     */
    private void checkDraggingConditions(Button key, MouseEvent event) {
        // Iterate through the tiles present in the current guess
        for (int tileNum = 0; tileNum < this.wordleModel.getWordLength(); tileNum++) {
            // Get the x-coordinate of the tile and size of tile
            double tileXCoordinate = this.wordleModel.getLetter(tileNum).getLayoutX();
            double tileSize = this.wordleModel.getLetter(tileNum).getWidth();

            // Check if the x-position of the dragging event aligns with a given tile
            if ((event.getSceneX() >= tileXCoordinate) && (event.getSceneX() < (tileXCoordinate + tileSize))) {
                // Check if key has been dragged outside the virtual keyboard pane (avoid confusion with single click - no drag)
                if (event.getSceneY() < this.wordleView.getRoot().getBottom().getLayoutY()) {
                    // Letter can only be added by drag if tile is already in use
                    // or is the next to be filled
                    if (tileNum <= (this.wordleModel.getColumn() + 1)) {
                        if (tileNum == (this.wordleModel.getColumn() + 1)) { this.wordleModel.incrementColumn(); } // If next to be filled, increment column
                        this.wordleView.updateTyping(new Text(key.getText()), this.wordleModel.getRow(), tileNum); // Update typing
                        break;
                    }
                }
            }
        }
    }
}