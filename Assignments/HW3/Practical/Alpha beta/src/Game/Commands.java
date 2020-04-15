package Game;

/**
 * Command strings that can be sent to and from the game server.
 *
 */
public class Commands
{
    /**
     * Make a move.
     */
    public static final String MOVE = "MOVE";

    /**
     * Retrieves the player number for a client.
     */
    public static final String HELLO = "HELLO";

    /**
     * Retrieve the current board.
     */
    public static final String BOARD = "BOARD";

    /**
     * Retrieve the next player to make a move.
     */
    public static final String NEXT_PLAYER = "PLAYER";

    /**
     * Start a new game.
     */
    public static final String NEW_GAME = "NEW";

    /**
     * Retrieve the winner of this game.
     */
    public static final String WINNER = "WINNER";
}
