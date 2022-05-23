package main.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controller.WordleController;
import main.model.WordleModel;
import main.view.InitialScreenView;
import main.view.WordleView;

/**
 * Main JavaFX class for our WordFX game. Creates the different stages
 * of the game, as well as handles events that could possibly switch
 * stages.
 */
public class WordleMain extends Application {

    /** Initial scene greeting user */
    private Scene firstScene;

    /** Second scene displaying the actual game */
    private Scene secondScene;

    /** This object takes care of the view of our initial scene */
    private InitialScreenView initialView;

    /** Stage object to be shown */
    private Stage window;

    /** Length of words chosen by user */
    private int wordLength;

    /** User streak from previous games */
    private int currentUserStreak;

    /** The model of our game */
    private WordleModel wordleModel;

    /** The view of our game */
    private WordleView wordleView;

    public static void main(String[] args) { launch(args); }

    @Override
    public void init() throws Exception {
        super.init();
        this.initialView = new InitialScreenView();
        wordLength = 0;
    }

    /**
     * Sets the streak from previous wins by user
     *
     * @param streak - Amount of games won in a row by user
     */
    public void setStreak(int streak) {
        this.currentUserStreak = streak;
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        // Create initial screen for user to choose mode
        firstScene = new Scene(this.initialView.getBorderPane(), 600, 850);

        firstScene.getStylesheets().add(
                getClass().getResource("initial-screen.css")
                        .toExternalForm());

        initEventHandlersForInitialScreen();

        // Add the scene graph to the stage
        window.setScene(firstScene);

        // Set the title for the main window
        window.setTitle("WordFX");

        // Display the scene
        window.show();
    }

    /**
     * Takes care of handling the clicks on the initial screen, where
     * user will click on the desired mode, which will output a new scene
     */
    private void initEventHandlersForInitialScreen() {
        this.initialView.getThreeLetterBtn().setOnMouseClicked(event -> {
            wordLength = 3;
            createSecondScene("style.css");
            window.setScene(secondScene);
        });
        this.initialView.getFourLetterBtn().setOnMouseClicked(event -> {
            wordLength = 4;
            createSecondScene("style.css");
            window.setScene(secondScene);
        });
        this.initialView.getFiveLetterBtn().setOnMouseClicked(event -> {
            wordLength = 5;
            createSecondScene("style.css");
            window.setScene(secondScene);
        });

    }

    /**
     * Creates the second screen with the model and view
     *
     * @param style - style of the css to be used on the second screen
     */
    public void createSecondScene(String style) {
        // Initialize model and view, and also set streak
        wordleModel = new WordleModel(wordLength);
        wordleModel.setStreak(currentUserStreak);
        wordleView = new WordleView(wordleModel);

        // Create new scene and use css resources from style.css
        secondScene = new Scene(wordleView.getRoot(), 600, 850);
        secondScene.getStylesheets().add(
                getClass().getResource(style)
                        .toExternalForm());

        //The virtual keyboard controller for handling events like typing
        WordleController keyboardController = new WordleController(wordleView, wordleModel, secondScene);


        initBackgroundEventHandlers();
    }

    /**
     * Takes care of handling the event of switching to dark or light
     * mode. Needs the secondScene to work.
     */
    private void initBackgroundEventHandlers() {
        final Boolean[] darkModeOn = {false};
        this.wordleModel.getHeader().getDarkModeButton().setOnMouseClicked(e -> {
            // If dark mode is off, change to dark-mode.css style
            if (!darkModeOn[0]) {
                switchModes("dark-mode.css");
                darkModeOn[0] = true;
            }
            else {
                switchModes("style.css");
                darkModeOn[0] = false;
            }
        });
    }

    /**
     * Sets the css style according to user preference.
     *
     * @param style - css style for light or dark mode
     */
    private void switchModes(String style) {
        if (style.equals("dark-mode.css")) {
            secondScene.getStylesheets().remove(
                    getClass().getResource("style.css")
                            .toExternalForm());
        }
        else {
            secondScene.getStylesheets().remove(
                    getClass().getResource("dark-mode.css")
                            .toExternalForm());
        }
        secondScene.getStylesheets().add(
                getClass().getResource(style)
                        .toExternalForm());
    }
}
