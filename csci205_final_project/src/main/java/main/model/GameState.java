package main.model;

/**
 * Simple enumeration to take care of the game state
 */
public enum GameState {
    NEW_GAME(true),
    GAME_IN_PROGRESS(true),
    GAME_PAUSED,
    GAME_WINNER,
    GAME_LOSER;

    /** True is game is playable, false otherwise */
    final boolean playable;

    /**
     * Make all states false by default
     */
    GameState() {
        this(false);
    }

    /**
     * Simple constructor that will take a parameters in case
     * game is in progress of if it is a new game
     * @param playable - true if playable, false otherwise
     */
    GameState(boolean playable) { this.playable = playable; }

    /**
     * @return true if game is playable, false otherwise
     */
    public boolean isPlayable() {
        return playable;
    }
}
