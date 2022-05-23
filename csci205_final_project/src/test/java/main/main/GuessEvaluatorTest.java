

package main.main;

import javafx.application.Application;
import main.model.GameState;
import main.model.WordleModel;
import main.view.WordleView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple JUnit test for guess analyzer. This is the class that analyzes
 * all guesses input by users. Notice that you must close the application to
 * see test results. Each method must be tested individually. Don't
 * run the entire test!
 */
class GuessEvaluatorTest {

    private GuessEvaluator evaluator3;
    private WordleModel wordleModel3;
    private WordleView wordleView3;

    private GuessEvaluator evaluator4;
    private WordleModel wordleModel4;
    private WordleView wordleView4;

    private GuessEvaluator evaluator5;
    private WordleModel wordleModel5;
    private WordleView wordleView5;

    @BeforeEach
    void setUp() {
        Application.launch(main.main.WordleMain.class);
        wordleModel3 = new WordleModel(3);
        wordleView3 = new WordleView(wordleModel3);
        evaluator3 = new GuessEvaluator(wordleModel3, wordleView3, "row");

        wordleModel4 = new WordleModel(4);
        wordleView4 = new WordleView(wordleModel4);
        evaluator4 = new GuessEvaluator(wordleModel4, wordleView4, "time");

        wordleModel5 = new WordleModel(5);
        wordleView5 = new WordleView(wordleModel5);
        evaluator5 = new GuessEvaluator(wordleModel5, wordleView5, "state");

    }

    void guessCorrect() {
        String analysis3 = evaluator3.analyzeGuess("row", 3);
        assertEquals(analysis3, "***");

        String analysis4 = evaluator4.analyzeGuess("time", 4);
        assertEquals(analysis4, "****");

        String analysis5 = evaluator5.analyzeGuess("state", 5);
        assertEquals(analysis5, "*****");
    }

    void guessWrong() {
        String analysis1 = evaluator3.analyzeGuess("tab", 3);
        assertEquals(analysis1, "---");
        String analysis2 = evaluator3.analyzeGuess("rap", 3);
        assertEquals(analysis2, "*--");
        String analysis3 = evaluator3.analyzeGuess("the", 3);
        assertEquals(analysis3, "---");
        String analysis4 = evaluator3.analyzeGuess("for", 3);
        assertEquals(analysis4, "-*+");
        String analysis5 = evaluator3.analyzeGuess("two", 3);
        assertEquals(analysis5, "-++");
        String analysis6 = evaluator3.analyzeGuess("win", 3);
        assertEquals(analysis6, "+--");
    }

    @Test
    void analyzeGuess() {
        guessCorrect();
    }

    @Test
    void loserUser() {
        guessWrong();
        evaluator3.loserUser();
        assertEquals(wordleModel3.getGameState(), GameState.GAME_LOSER);
        assertEquals(wordleModel3.getCurrentWinStreak(), 0);
    }

    @Test
    void winnerUser() {
        guessCorrect();
        evaluator3.winnerUser();
        evaluator4.winnerUser();
        evaluator5.winnerUser();
        assertEquals(wordleModel3.getGameState(), GameState.GAME_WINNER); //due to closing app (stays as game winner before closing)
        assertEquals(wordleModel3.getCurrentWinStreak(), 1);
        assertEquals(wordleModel4.getGameState(), GameState.GAME_WINNER); //due to closing app (stays as game winner before closing)
        assertEquals(wordleModel4.getCurrentWinStreak(), 1);
        assertEquals(wordleModel5.getGameState(), GameState.GAME_WINNER); //due to closing app (stays as game winner before closing)
        assertEquals(wordleModel5.getCurrentWinStreak(), 1);
    }
}