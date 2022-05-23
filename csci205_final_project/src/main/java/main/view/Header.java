/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2022
 * Instructor: Prof. Brian King
 *
 * Name: Pedro Carneiro Passos and Ramon Asuncion
 * Section: 02 - 11AM
 * Date: 4/8/22
 * Time: 8:13 AM
 *
 * Project: csci205_final_project
 * Package: main.view
 * Class: Header
 *
 * Description:
 *
 * ****************************************
 */
package main.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;

/**
 * Header class to create the header section, which includes the "WordFX" header
 * and the long separator line splitting the section.
 */
public class Header {

    /** WordFX header section */
    private final BorderPane headerSection;

    /** The word "WordFX". */
    private final Label title;

    /** Setting icon button. */
    private Button darkModeButton;

    /**
     * @return the headerSection including title and separator
     */
    public BorderPane getHeaderSection() { return headerSection; }

    /**
     * @return the button for the settings icon.
     */
    public Button getDarkModeButton() { return darkModeButton; }

    /**
     * Simple constructor to initialize the title and header section, and their
     * respective css id's
     */
    public Header() {
        // Initialize the title
        this.title = new Label("WordFX");
        this.title.getStyleClass().add("titleLabel");

        // Initialize header section
        this.headerSection = new BorderPane();
        this.headerSection.setId("header");
    }

    /**
     * Creates the header section using a {@BorderPane} as
     * the container and {@HBox} to horizontally line the buttons.
     */
    public void createHeader() {
        // Create all the button in the header.
        this.createDarkModeButton();

        // Organize items in the header.
        this.headerSection.setBottom(new Separator());

        // Adjust position of things on header
        this.headerSection.setRight(this.darkModeButton);
        this.headerSection.setCenter(this.title);
    }

    /**
     * Create a setting button to show the user the game options.
     */
    private void createDarkModeButton() {
        // Initialize a new button and add styling.
        darkModeButton = new Button();
        darkModeButton.getStyleClass().add("setting-button");
    }
}
