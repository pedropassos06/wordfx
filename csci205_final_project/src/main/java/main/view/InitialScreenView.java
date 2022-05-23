/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2022
 * Instructor: Prof. Brian King
 *
 * Name: Liv & Gang
 * Section: 02 - 11AM
 * Date: 4/17/22
 * Time: 10:25 PM
 *
 * Project: csci205_final_project
 * Package: main.view
 * Class: InitialScreenView
 *
 * Description:
 *
 * ****************************************
 */
package main.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

/**
 * This class takes care of building the initial screen of the game
 */
public class InitialScreenView {

    /** Vertical node that will contain each button in our initial screen */
    private VBox root;

    /** Pane to set header, center of screen, and bottom of screen */
    private BorderPane borderPane;

    /** Each button contained in the initial screen */
    private Button threeLetterBtn, fourLetterBtn, fiveLetterBtn;

    /** Pane for the header of the initial screen */
    private HBox header;

    /**
     * @return Three-letter mode button on initial screen
     */
    public Button getThreeLetterBtn() { return threeLetterBtn; }

    /**
     * @return Four-letter mode button on initial screen
     */
    public Button getFourLetterBtn() { return fourLetterBtn; }

    /**
     * @return Five-letter mode button on initial screen
     */
    public Button getFiveLetterBtn() { return fiveLetterBtn; }

    /**
     * @return The BorderPane of our initial screen to create scene
     */
    public BorderPane getBorderPane() { return borderPane; }

    /**
     * Initialized the initial screen view
     */
    public InitialScreenView() {
        // Pane for the buttons
        root = new VBox();
        root.setId("button-pane");

        // Main pane
        borderPane = new BorderPane();

        initSceneGraph();
    }

    public void initSceneGraph() {

        // Set the background to be a drawing we created
        BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("/images/initial-screen.jpeg").toExternalForm()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        borderPane.setBackground(background);

        // Top Part
        createHeaderPortion();

        // Middle Part
        createInitialButtons();

        // Bottom Part
        createGroupNamePortion();
    }

    /**
     * Creates header portion, welcoming user and asking to choose
     * a game mode
     */
    private void createHeaderPortion() {
        Label initialQuestion = new Label("WELCOME TO WORDFX\nCHOOSE A MODE:");
        header = new HBox(initialQuestion);
        header.setId("header");
        initialQuestion.setId("question");
        borderPane.setTop(header);
    }

    /**
     * Creates the buttons on the initial screen, styles them,
     * and adds to the main Pane
     */
    private void createInitialButtons() {
        //Create three buttons
        threeLetterBtn = new Button("3 LETTER MODE\n(HARD)");
        fourLetterBtn = new Button("4 LETTER MODE\n(MEDIUM)");
        fiveLetterBtn = new Button("5 LETTER MODE\n(EASY)");

        // Style the three buttons
        threeLetterBtn.setId("three-letter-btn");
        fourLetterBtn.setId("four-letter-btn");
        fiveLetterBtn.setId("five-letter-btn");

        // Placing our buttons
        root.getChildren().addAll(threeLetterBtn, fourLetterBtn, fiveLetterBtn);
        borderPane.setCenter(root);
    }

    /**
     * Places the name of our group on the bottom
     */
    private void createGroupNamePortion() {
        Label groupName = new Label("by Liv & Gang ©");
        groupName.setId("group-name");
        header = new HBox(groupName);
        header.setAlignment(Pos.CENTER);
        borderPane.setBottom(header);
    }
}
