/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2022
 * Instructor: Prof. Brian King
 *
 * Name: Liv & Gang
 * Section: 02 - 11AM
 * Date: 4/6/22
 * Time: 11:35 PM
 *
 * Project: csci205_final_project
 * Package: main.view
 * Class: VirtualKeyboardView
 *
 * Description:
 *
 * ****************************************
 */
package main.view;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class creates the virtual keyboard, with all letter keys
 * as well and enter key and delete key.
 */
public class VirtualKeyboardView {

    /** Virtual keyboard node to later be added to scene */
    private VBox keyboard;

    /** The {@HBox} to be added as every row of the virtual keyboard */
    private HBox topPane;

    /** List with all the buttons representing virtual keyboard keys */
    private ArrayList<Button> keyboardKeys;

    /** List with all letters on the virtual keyboard */
    private ArrayList<String> keyboardLetters;

    /**
     * @return The {@link ArrayList} with all letters on the virtual keyboard
     */
    public ArrayList<String> getKeyboardLetters() { return keyboardLetters; }

    /**
     * @return the {@link ArrayList} with all the virtual keyboard keys (letters, enter, and delete)
     */
    public ArrayList<Button> getKeyboardKeys() { return keyboardKeys; }

    /**
     * @return the keyboard node to add to the scene in the view
     */
    public VBox getKeyboard() { return keyboard; }

    /**
     * Simple constructor to initialize the virtual keyboard, and the
     * topPane that will be used to create each row of the keyboard
     */
    public VirtualKeyboardView () {
        keyboard = new VBox();
        keyboard.setId("keyboard");

        topPane = new HBox();
        topPane.setId("top-pane");

        keyboardKeys = new ArrayList<>();
        keyboardLetters = new ArrayList<>();
    }

    /**
     * Creates a letter key on the virtual keyboard
     *
     * @param letter keyboard letter key to be created
     * @return The letter key as a button
     */
    public Button createKey(Character letter) {
        Button key = new Button(letter.toString());
        keyboardKeys.add(key);
        keyboardLetters.add(letter.toString().toLowerCase());
        key.getStyleClass().add("keyboard-letter");
        return key;
    }

    /**
     * Creates the enter key on the virtual keyboard
     *
     * @param enter "ENTER" string to be put on the keyboard key
     * @return The enter key as a button
     */
    public Button createEnterKey(String enter) {
        Button enterKey = new Button(enter);
        keyboardKeys.add(enterKey);
        enterKey.getStyleClass().add("keyboard-letter");
        enterKey.getStyleClass().add("enter");
        return enterKey;
    }

    /**
     * Creates the delete key on the virtual keyboard
     *
     * @return The delete key as a button
     */
    public Button createDeleteKey() {
        Button delKey = new Button("");
        keyboardKeys.add(delKey);
        delKey.getStyleClass().add("keyboard-letter");
        delKey.getStyleClass().add("delete");
        return delKey;
    }

    /**
     * Creates a new top pane object with its css id to avoid
     * adding duplicates to root
     */
    public void newTopPane() {
        topPane = new HBox();
        topPane.setId("top-pane");
    }

    /**
     * Creates the virtual keyboard by going through every single
     * key on the keyboard. Each key is a button.
     */
    public void createVirtualKeyboard() {
        // Each row of the keyboard
        ArrayList<Character> topKeyboard = new ArrayList<>(
                Arrays.asList('Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P')
        );
        ArrayList<Character> midKeyboard = new ArrayList<>(
                Arrays.asList('A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L')
        );
        ArrayList<Character> bottomKeyboard = new ArrayList<>(
                Arrays.asList('Z', 'X', 'C', 'V', 'B', 'N', 'M')
        );

        // Create top row of virtual keyboard
        createRowOfKeyboard(topKeyboard);

        // Create middle row of virtual keyboard
        newTopPane();
        createRowOfKeyboard(midKeyboard);

        // Create enter key, bottom row of virtual keyboard, and delete key
        // in this order.
        newTopPane();
        createRowOfKeyboard(bottomKeyboard, "ENTER");
    }

    /**
     * Creates rows of the virtual keyboard, more specifically top and
     * middle rows. Also adds them to keyboard node.
     *
     * @param keyboardRow - either top row, middle row, or bottom row
     *                    of keyboard
     */
    private void createRowOfKeyboard(ArrayList<Character> keyboardRow) {
        for (Character value : keyboardRow) {
            topPane.getChildren().add(createKey(value));
        }
        keyboard.getChildren().add(topPane);
    }

    /**
     * Creates the row of the keyboard that contains the enter and delete keys (bottom row).
     * Also adds row to the keyboard node
     *
     * @param bottomKeyboard - bottom keys of the keyboard, including enter and delete
     * @param enterKey - String associated with the enter key ("ENTER")
     */
    private void createRowOfKeyboard(ArrayList<Character> bottomKeyboard, String enterKey) {
        topPane.getChildren().add(createEnterKey(enterKey));
        for (Character character : bottomKeyboard) {
            topPane.getChildren().add(createKey(character));
        }
        topPane.getChildren().add(createDeleteKey());
        keyboard.getChildren().add(topPane);
    }
}