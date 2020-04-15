package ai;

/**
 * Contains some global constants to be used by the
 * minimax game tree.
 *
 */
public class Global
{
    private static boolean firstTaken = false;

    /**
     * Used by the GUI to find where to place a new
     * client window.
     *
     * @return X position for the new client window.
     */
    public static int getClientXpos()
    {
        if (!firstTaken)
        {
            firstTaken = true;
            return 0;
        }
        return 425;
    }
}
