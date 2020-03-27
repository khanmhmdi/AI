package Game;

/**
 * Represents a game state in the board game.
 *
 */
public class GameState
{
    /**
     * Index for player 1's (south's) house.
     */
    public static final int HOUSE_S = 7; //6

    /**
     * Start index for player 1's (south's) ambos.
     */
    public static final int START_S = 1; //0

    /**
     * End index for player 1's (south's) ambos.
     */
    public static final int END_S = 6; //5

    /**
     * Index for player 2's (north's) house.
     */
    public static final int HOUSE_N = 0; //13

    /**
     * Start index for player 2's (north's) ambos.
     */
    public static final int START_N = 8; //7

    /**
     * End index for player 2's (north's) ambos.
     */
    public static final int END_N = 13; //12

    /**
     * Index for next player value in board string.
     */
    public static final int NEXT_PLAYER = 14;

    //Board representation
    private int[] board;

    //Next player to make a move
    private int nextPlayer = 1;

    /**
     * Creates a start game state for a new Game
     * game.
     */
    public GameState()
    {
        createBoard(6);
    }

    /**
     * Creates a game state from a board representation.
     *
     * @param board Board representation
     * @param nextPlayer Next player to make a move
     */
    public GameState(int[] board, int nextPlayer)
    {
        this.board = board;
        this.nextPlayer = nextPlayer;
    }

    /**
     * Creates a game state from a string board representation
     * received from the game server.
     *
     * @param boardStr Board string representation
     */
    public GameState(String boardStr)
    {
        try
        {
            board = new int[14];
            String[] tokens = boardStr.split(";");

            //House_S
            board[HOUSE_S] = Integer.parseInt(tokens[HOUSE_S]);

            //South (1) player ambos
            for (int i = START_S; i <= END_S; i++)
            {
                board[i] = Integer.parseInt(tokens[i]);
            }

            //House_N
            board[HOUSE_N] = Integer.parseInt(tokens[HOUSE_N]);

            //North (2) player ambos
            for (int i = START_N; i <= END_N; i++)
            {
                board[i] = Integer.parseInt(tokens[i]);
            }

            //Player to move
            nextPlayer = Integer.parseInt(tokens[NEXT_PLAYER]);
        }
        catch (Exception ex)
        {
            //Parsing failed. Create a default board.
            ex.printStackTrace();
            createBoard(6);
        }
    }

    /**
     * Creates a copy of this GameState object.
     *
     * @return The copy
     */
    public GameState clone()
    {
        //Make a copy of the board...
        int[] n_board = new int[14];
        for (int i = 0; i < 14; i++)
        {
            n_board[i] = board[i];
        }
        //... and return a new object
        return new GameState(n_board, nextPlayer);
    }

    /**
     * Creates a new Game start game state with the specified
     * number of seeds.
     *
     * @param seeds Number of seeds per ambo (6 is default)
     */
    private void createBoard(int seeds)
    {
        board = new int[14];

        //Player 1 - South
        for (int i = START_S; i <= END_S; i++)
        {
            board[i] = seeds;
        }

        //Player 2 - North
        for (int i = START_N; i <= END_N; i++)
        {
            board[i] = seeds;
        }
    }

    /**
     * Makes a move in the current game state.
     *
     * @param ambo The move to make (1-6)
     * @return True if the move was successful, false if not.
     */
    public boolean makeMove(int ambo)
    {
        //Internal ambo number of 0-5
        ambo--;

        int cMoveI;
        if (nextPlayer == 1)
        {
            cMoveI = START_S + ambo;
        }
        else
        {
            cMoveI = START_N + ambo;
        }

        //Check if legal move
        if (board[cMoveI] == 0)
        {
            //No legal move...
            return false;
        }

        //Pickup seeds
        int seeds = board[cMoveI];
        board[cMoveI] = 0;
        boolean lastIsHouse = false;

        //Sow seeds
        while (seeds > 0)
        {
            //Take a step
            cMoveI++;
            if (cMoveI >= 14) cMoveI = 0;

            if ( (nextPlayer == 1 && cMoveI == HOUSE_N) || (nextPlayer == 2 && cMoveI == HOUSE_S) )
            {
                //Don't sow in opponents house
            }
            else
            {
                //Sow a seed
                board[cMoveI]++;
                seeds--;
            }

            //Check special cases for last seed
            if (seeds == 0)
            {
                //Check for extra move
                if (nextPlayer == 1 && cMoveI == HOUSE_S) lastIsHouse = true;
                if (nextPlayer == 2 && cMoveI == HOUSE_N) lastIsHouse = true;

                //Check capture
                boolean capture = false;
                if (board[cMoveI] == 1)
                {
                    if (nextPlayer == 1)
                    {
                        if (cMoveI >= START_S && cMoveI <= END_S) capture = true;
                    }
                    if (nextPlayer == 2)
                    {
                        if (cMoveI >= START_N && cMoveI <= END_N) capture = true;
                    }
                }

                //Possible capture of opponent's seeds
                if (capture)
                {
                    int oi = getOppositeAmbo(cMoveI);
                    if (board[oi] > 0)
                    {
                        if (nextPlayer == 1)
                        {
                            board[HOUSE_S] += board[cMoveI] + board[oi];
                        }
                        else if (nextPlayer == 2)
                        {
                            board[HOUSE_N] += board[cMoveI] + board[oi];
                        }
                        board[cMoveI] = 0;
                        board[oi] = 0;
                    }
                }
            }
        }

        if (!lastIsHouse)
        {
            toggleNextPlayer();
        }

        //Call to update game state in
        //case any player won.
        gameEnded();

        return true;
    }

    /**
     * Returns the opposite ambo index for a specified ambo.
     *
     * @param ambo The ambo
     * @return Opposite ambo index, or -1 if failed to find the opposite ambo.
     */
    private int getOppositeAmbo(int ambo)
    {
        if (ambo == START_S) return END_N;
        if (ambo == START_S+1) return END_N-1;
        if (ambo == START_S+2) return END_N-2;
        if (ambo == START_S+3) return END_N-3;
        if (ambo == START_S+4) return END_N-4;
        if (ambo == START_S+5) return END_N-5;
        if (ambo == START_N) return END_S;
        if (ambo == START_N+1) return END_S-1;
        if (ambo == START_N+2) return END_S-2;
        if (ambo == START_N+3) return END_S-3;
        if (ambo == START_N+4) return END_S-4;
        if (ambo == START_N+5) return END_S-5;

        return -1;
    }

    /**
     * Returns the next player to make a move.
     *
     * @return Next player to make a move (1 or 2)
     */
    public int getNextPlayer()
    {
        return nextPlayer;
    }

    /**
     * Toggles to next player.
     */
    private void toggleNextPlayer()
    {
        if (nextPlayer == 1) nextPlayer = 2;
        else nextPlayer = 1;
    }

    /**
     * Returns the number of seeds for the specified
     * ambo and player.
     *
     * @param ambo The ambo (1-6)
     * @param player The player (1-2)
     * @return Number of seeds, or -1 if an error occured
     */
    public int getSeeds(int ambo, int player)
    {
        //Internal ambo number of 0-5
        ambo--;
        if (ambo < 0 || ambo > 5) return -1;

        if (player == 1)
        {
            return board[START_S + ambo];
        }
        if (player == 2)
        {
            return board[START_N + ambo];
        }
        return -1;
    }

    /**
     * Checks if the game has ended. The game ends when one of the
     * players has 0 seeds in all own ambos.
     *
     * @return True if the game has ended, false if not.
     */
    public boolean gameEnded()
    {
        int seeds = 0;

        //Player 1 - South
        for (int i = START_S; i <= END_S; i++)
        {
            seeds += board[i];
        }
        if (seeds == 0)
        {
            //Gather opponents seeds (if any)
            //Rule 6
            for (int i = START_N; i <= END_N; i++)
            {
                if (board[i] > 0)
                {
                    board[HOUSE_N] += board[i];
                    board[i] = 0;
                }
            }
            return true;
        }

        //Player 2 - North
        seeds = 0;
        for (int i = START_N; i <= END_N; i++)
        {
            seeds += board[i];
        }
        if (seeds == 0)
        {
            //Gather opponents seeds (if any)
            //Rule 6
            for (int i = START_S; i <= END_S; i++)
            {
                if (board[i] > 0)
                {
                    board[HOUSE_S] += board[i];
                    board[i] = 0;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Returns the winner for this game.
     *
     * @return Winner (1 or 2), 0 if draw, and -1 if game is still running.
     */
    public int getWinner()
    {
        if (gameEnded())
        {
            int s1 = getScore(1);
            int s2 = getScore(2);

            if (s1 > s2)
            {
                return 1;
            }
            else if (s2 > s1)
            {
                return 2;
            }
            else
            {
                return 0;
            }
        }

        return -1;
    }

    /**
     * Returns the number of possible valid moves (non-empty
     * ambos) for the specified player in this game state.
     *
     * @param player The player to check
     * @return Number of possible moves for this player
     */
    public int getNoValidMoves(int player)
    {
        int cnt = 0;

        if (player == 1)
        {
            for (int i = START_S; i <= END_S; i++)
            {
                if (board[i] > 0) cnt++;
            }
        }
        else
        {
            for (int i = START_N; i <= END_N; i++)
            {
                if (board[i] > 0) cnt++;
            }
        }

        return cnt;
    }

    /**
     * Checks if a move is possible.
     *
     * @param ambo The move to make (1-6)
     * @return True if the move is possible (at least one seed in the ambo), false otherwise
     */
    public boolean moveIsPossible(int ambo)
    {
        //Internal ambo number of 0-5
        ambo--;
        if (nextPlayer == 1)
        {
            if (board[START_S + ambo] > 0) return true;
        }
        else
        {
            if (board[START_N + ambo] > 0) return true;
        }
        return false;
    }

    /**
     * Returns the score (number of seeds in the house) for a player.
     *
     * @param player The player
     * @return The score for the specified player
     */
    public int getScore(int player)
    {
        //In case we have a winner, this method
        //needs to be called to update the game state.
        gameEnded();

        if (player == 1)
        {
            return board[HOUSE_S];
        }
        else
        {
            return board[HOUSE_N];
        }
    }

    /**
     * Returns a board string representation for this game state.
     *
     * @return Board string representation
     */
    public String toString()
    {
        String str = "";

        for(int i = 0; i < 14; i++)
        {
            str += board[i] + ";";
        }
        str += nextPlayer;

        return str;
    }
}
