package Game;

/**
 * Error strings that can be returned from the game server.
 *
 */
public class Errors
{
    /**
     * Is returned when a client tries to connect to an already full game.
     */
    public static final String GAME_FULL = "ERROR GAME_FULL";

    /**
     * Is returned when a client tries to make a move but the game is missing player(s).
     */
    public static final String GAME_NOT_FULL = "ERROR GAME_NOT_FULL";

    /**
     * Is returned when an unknown command is sent from the client.
     */
    public static final String CMD_NOT_FOUND = "ERROR CMD_NOT_FOUND";

    /**
     * Is returned when the wrong parameters have been sent with a command.
     */
    public static final String INVALID_PARAMS = "ERROR INVALID_PARAMS";

    /**
     * Is returned when a client tries to make an invalid move. Valid moves are 1-6.
     */
    public static final String INVALID_MOVE = "ERROR INVALID_MOVE";

    /**
     * Is returned when the wrong player tries to make a move.
     */
    public static final String WRONG_PLAYER = "ERROR WRONG_PLAYER";

    /**
     * Is returned when a client tries to make a move from an empty ambo.
     */
    public static final String AMBO_EMPTY = "ERROR AMBO_EMPTY";
}
