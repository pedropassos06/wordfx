/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2022
 * Instructor: Prof. Brian King
 *
 * Name: Liv & Gang
 * Section: 02 - 11AM
 * Date: 4/6/22
 * Time: 12:46 PM
 *
 * Project: csci205_final_project
 * Package: main.view
 * Class: TileView
 *
 * Description:
 *
 * ****************************************
 */
package main.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

/**
 * Tile class that creates all the tiles where the
 * guesses will be typed on
 */
public class TileView {

    /** Root node for the scene graph */
    private VBox tiles;

    /** Row of tiles representing each guess */
    private HBox topPane;

    /** List of labels where letters will be placed */
    private ArrayList<Label> listOfTilesInAGuess;

    /** List with each tile placed on the screen */
    private ArrayList<Label> listOfAllTiles;

    /** List of all guesses, where each guess is a list of labels (letters) */
    private ArrayList<ArrayList<Label>> guessList;

    /** Stack Pane with the tiles and win screen */
    private StackPane tileStackPane;

    /**
     * @return the {@ArrayList} of the guesses, containing each list of letters
     * contained in guesses
     */
    public ArrayList<ArrayList<Label>> getGuessList() {
        return guessList;
    }

    /**
     * @return the 30 tiles representing all the guesses
     */
    public VBox getTiles() {
        return tiles;
    }

    /**
     * @return the stack pane with tiles and win screen
     */
    public StackPane getTileStackPane() {
        return tileStackPane;
    }

    /**
     * Simple constructor for the Tile class. Initializes the tiles and
     * topPane, where the topPane represents each row of tiles.
     */
    public TileView (int wordLength) {

        // Set up the tile pane for our scene graph
        tiles = new VBox();
        tiles.setId("tile-pane");

        // Set up the first top pane node for our scene graph
        topPane = new HBox();
        topPane.setId("top-pane");

        // Initialize the two arraylists to store all the guesses and
        // letters respectively
        guessList = new ArrayList<ArrayList<Label>>();
        listOfTilesInAGuess = new ArrayList<>();
        listOfAllTiles = new ArrayList<>();

        // Create the stack pane for the win screen later on
        tileStackPane = new StackPane();

        createTilePane(wordLength);

    }

    /**
     * Creates the pane made up of the 30 tiles, meaning 6 guesses of 5-letter
     * words. Tiles (rectangles) are styled using css.
     */
    private void createTilePane(int wordLength) {
        // Loop through 6 rows (guesses) and length of word number of columns
        for (int i = 0; i < 6; ++i) {

            for (int j = 0; j < wordLength; ++j) {

                createNewTile();

            }
            // Add each guess to vertical box
            tiles.getChildren().add(topPane);

            introduceNewGuess();
        }

        // Add all tiles to the stack pane of tiles
        tileStackPane.getChildren().add(tiles);
    }

    /**
     * Creates each individual tile (label), styles it, and adds
     * to the topPane (each row).
     */
    private void createNewTile() {
        // Create new tile and add to top pane
        Label rect = new Label();
        rect.getStyleClass().add("tile");
        topPane.getChildren().add(rect);

        // Add label to list of letters
        listOfTilesInAGuess.add(rect);
        listOfAllTiles.add(rect);
    }

    /**
     * Introduces new guess. That is, move to the next row of tiles to
     * create them, add each tile as the letter of a guess, and all 5 tiles
     * to the {@link ArrayList} guessList as one of the six guesses.
     */
    private void introduceNewGuess() {
        // Add all 5 letters to guess list as a guess
        guessList.add(listOfTilesInAGuess);
        // Make sure to create new listOfTilesInAGuess (new guess)
        listOfTilesInAGuess = new ArrayList<>();

        // Create new top pane, meaning new guess on a new horizontal box
        topPane = new HBox();
        topPane.setId("top-pane");
    }
}