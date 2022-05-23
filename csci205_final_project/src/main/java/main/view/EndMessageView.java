/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2022
 * Instructor: Prof. Brian King
 *
 * Name: Liv & Gang
 * Section: 02 - 11AM
 * Date: 4/17/22
 * Time: 4:34 PM
 *
 * Project: csci205_final_project
 * Package: main.view
 * Class: EndMessageFinal
 *
 * Description:
 *
 * ****************************************
 */
package main.view;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import main.model.WordleModel;

/**
 * Class that creates and displays the end message for loser or winner
 * user
 */
public class EndMessageView {

    /**  Header of end screen */
    private VBox endScreenHeader;

    /** The rectangle that shows up when you win */
    private Rectangle finalMessageRect;

    /** The label on the win screen */
    private Label finalMessageLabel;

    /** Stackpane for the win screen */
    private StackPane winStackPane;

    /** Border pane for the win screen */
    private BorderPane finalMessagePane;

    /** Label for the win screen */
    private Label groupNameLabel;

    /** The model of the game */
    private WordleModel wordleModel;

    /** The view of the game */
    private WordleView wordleView;

    /** Stack pane for printing message over tiles */
    private StackPane invalidWordStackPane;

    /**
     * Simple constructor for the end message
     *
     * @param wordleModel - model of the game
     * @param wordleView - view of the game
     */
    public EndMessageView(WordleModel wordleModel, WordleView wordleView) {
        // Initialize model and view
        this.wordleModel = wordleModel;
        this.wordleView = wordleView;

        this.finalMessageRect = new Rectangle(300, 200);
        this.winStackPane = new StackPane();
        this.invalidWordStackPane = new StackPane();
        this.finalMessageLabel = new Label();
        this.finalMessagePane = new BorderPane();
        this.groupNameLabel = new Label("A game by Liv & Gang");
        this.endScreenHeader = new VBox();
    }

    /**
     * Adds two labels and a button to a rectangle, then adds to tiles stackpane
     * to stack on top of tiles
     *
     * @param winOrLose the string of the game ending result.
     */
    public void showEndScreen(String winOrLose, String streakOrSecretWord) {

        settingStyles();

        // Create message with the winner/loser result displayed.
        createFinalMessageHeader(winOrLose, streakOrSecretWord);

        this.finalMessagePane.setAlignment(this.groupNameLabel, Pos.CENTER);

        // Placing sections of end message (header, play again button, group name)
        this.finalMessagePane.setTop(this.endScreenHeader);
        this.finalMessagePane.setCenter(this.wordleView.getPlayAgainBtn());
        this.finalMessagePane.setBottom(this.groupNameLabel);

        // Adding rectangle over tiles and text over rectangle
        placeEndGameMessage();

        // Animate the screen with fading
        animateEndScreen();
    }

    /**
     * Sets the styles of each aspect of our end screen message
     */
    private void settingStyles() {
        this.finalMessageRect.setId("final-message-rect");
        this.groupNameLabel.setId("group-name-label");
        this.endScreenHeader.setId("end-screen-header");
        this.finalMessageLabel.setId("final-message-label");
        this.finalMessagePane.setId("final-message-pane");
    }

    /**
     * Creates the header for our final message
     *
     * @param winOrLose - "You Won" or "You Lost"
     * @param streakOrSecretWord - Display streak if won,
     *                           display secret word if lost
     */
    private void createFinalMessageHeader(String winOrLose, String streakOrSecretWord) {
        /** Contains streak count if winner, secret word if loser */
        Label winOrLoseInfo = new Label(streakOrSecretWord);
        winOrLoseInfo.setId("win-or-lose");
        this.finalMessageLabel.setText(winOrLose);
        this.endScreenHeader.getChildren().addAll(this.finalMessageLabel, winOrLoseInfo);
    }

    /**
     * Simple places the message on top of the rectangles (guesses) on the
     * center of the screen
     */
    private void placeEndGameMessage() {
        this.winStackPane.getChildren().add(this.finalMessageRect);
        this.winStackPane.getChildren().add(this.finalMessagePane);
        this.wordleModel.getTileStackPane().getChildren().add(this.winStackPane);
        this.wordleView.getTileStack().getChildren().add(this.winStackPane);
        this.wordleView.getRoot().setCenter(this.wordleView.getTileStack());
    }

    /**
     * Makes the win screen fade in
     */
    private void animateEndScreen() {
        FadeTransition ft = new FadeTransition(Duration.millis(700), this.winStackPane);
        ft.setFromValue(0.1);
        ft.setToValue(1.0);
        ft.play();
    }

    /**
     * In case of trying short words, or words that do not exist,
     * the programs prints a message. Either "Invalid word" or
     * "Not enough letters"
     *
     * @param message - one of the two messages mentioned above
     */
    public void invalidInputScreen(String message) {
        Label badInputMessage = new Label(message);
        badInputMessage.setId("bad-input-message");
        placeInvalidWordMessage(badInputMessage);
        fadeAway(badInputMessage);
    }

    /**
     * Simply places the invalid word message on the tiles
     *
     * @param badInputMessage - message to be displayed
     */
    private void placeInvalidWordMessage(Label badInputMessage) {
        this.invalidWordStackPane.getChildren().add(badInputMessage);
        this.wordleModel.getTileStackPane().getChildren().add(this.invalidWordStackPane);
        this.wordleView.getTileStack().getChildren().add(this.invalidWordStackPane);
        this.wordleView.getRoot().setCenter(this.wordleView.getTileStack());
    }

    /**
     * Performs fading away animation
     *
     * @param badInputMessage - message to be faded away
     */
    private void fadeAway(Label badInputMessage) {
        FadeTransition ft = new FadeTransition(Duration.millis(1700), this.invalidWordStackPane);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.play();
        ft.setOnFinished(e -> this.invalidWordStackPane.getChildren().remove(badInputMessage));
    }

}
