package Game;

import server.*;

/**
 * Start point for the application.
 *
 */
public class Main
{

    /**
     * Version number.
     */
    public static String VERSION = "1.6";

    /**
     * Default port to start server at.
     */
    public static int port = 10101;

    /**
     * Starts the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Main main = new Main();
    }

    /**
     * Starts the GUI and server.
     */
    public Main()
    {
        try
        {
            Server server = Server.getInstance();
            server.start();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
