package server;

import java.io.*;
import java.net.*;
import Game.*;

/**
 * Creates and starts a game server.
 *
 */
public class Server implements Runnable
{
    private ServerGUI g;
    private static Server instance;

    private GameState game;

    private ServerSocket ssocket;
    private int nextClient;
    private ClientThread[] clients;
    private boolean running;

    public static Server getInstance()
    {
        if (instance == null)
        {
            instance = new Server();
        }
        return instance;
    }

    /**
     * Creates a new game server.
     */
    private Server()
    {

    }

    /**
     * Starts the game server on the specified network port.
     */
    public void start()
    {
        nextClient = 0;
        game = new GameState();
        g = ServerGUI.getInstance();

        try
        {
            g.addText("Starting server at port " + Main.port);
            ssocket = new ServerSocket(Main.port);
            g.addText("Server started successfully");
            clients = new ClientThread[2];
        }
        catch(Exception ex)
        {
            g.addText("ERROR: Could not start server on port " + Main.port + ": " + ex.getMessage());
            return;
        }

        //Start the client listener thread
        Thread thr = new Thread(this);
        thr.start();
    }

    /**
     * Stops the game server.
     */
    public void stop()
    {
        try
        {
            running = false;
            ssocket.close();

            if (clients[0] != null)
            {
                clients[0].stop();
                clients[0] = null;
            }
            if (clients[1] != null)
            {
                clients[1].stop();
                clients[1] = null;
            }
        }
        catch (Exception ex)
        {
            g.addText("Error closing game server: " + ex.getMessage());
            return;
        }
        g.addText("Game server stopped");
    }

    /**
     * Checks of both player have connect to this game.
     *
     * @return True if both players are connected, false otherwise.
     */
    public boolean gameIsFull()
    {
        if (clients[0] != null && clients[1] != null)
        {
            return true;
        }
        return false;
    }

    /**
     * Thread for listening to client connects. Once a client connects
     * to the server, a new client thread is started.
     */
    public void run()
    {
        running = true;

        while (running)
        {
            try
            {
                Socket mSocket = ssocket.accept();
                if (nextClient == 0)
                {
                    clients[0] = new ClientThread(mSocket, 1);
                }
                else if (nextClient == 1)
                {
                    clients[1] = new ClientThread(mSocket, 2);
                }
                else
                {
                    //No more players allowed. Close socket.
                    mSocket.close();
                }
                nextClient++;
            }
            catch (Exception ex)
            {
                g.addText("Error starting client " + (nextClient + 1) + ": " + ex.getMessage());
                running = false;
            }
        }
    }

    /**
     * Thread class for a client (player).
     */
    private class ClientThread implements Runnable
    {
        private Socket socket;
        private boolean running;
        private ServerGUI g;
        private int iAmPlayer;

        /**
         * Creates and starts a new client thread.
         *
         * @param socket Network socket
         * @param iAmPlayer Player number for this client (1 or 2)
         */
        public ClientThread(Socket socket, int iAmPlayer)
        {
            this.socket = socket;
            this.iAmPlayer = iAmPlayer;
            g = ServerGUI.getInstance();

            running = true;
            Thread thr = new Thread(this);
            thr.start();
        }

        /**
         * Stops this client thread.
         */
        public void stop()
        {
            try
            {
                running = false;
                socket.close();
            }
            catch (Exception ex)
            {
                g.addText("Error closing client " + iAmPlayer + ": " + ex.getMessage());
                return;
            }
            g.addText("Client " + iAmPlayer + " closed");
        }

        /**
         * Thread for listening to commands sent from the connected client.
         */
        public void run()
        {
            running = true;
            while (running)
            {
                try
                {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String cmd = in.readLine();
                    String reply;

                    while(cmd != null)
                    {
                        if (cmd.startsWith(Commands.HELLO))
                        {
                            reply = Commands.HELLO + " " + iAmPlayer;
                            g.addText("Client " + iAmPlayer + " connected");

                            if (iAmPlayer == 2)
                            {
                                //Both players connected. Update board.
                                g.updateBoard(game);
                            }
                        }
                        else if (cmd.startsWith(Commands.BOARD))
                        {
                            reply = game.toString();
                        }
                        else if (cmd.startsWith(Commands.MOVE))
                        {
                            if (!gameIsFull())
                            {
                                reply = Errors.GAME_NOT_FULL;
                            }
                            else
                            {
                                reply = makeMove(cmd);
                            }
                        }
                        else if (cmd.startsWith(Commands.NEXT_PLAYER))
                        {
                            if (!gameIsFull())
                            {
                                reply = Errors.GAME_NOT_FULL;
                            }
                            else
                            {
                                reply = "" + game.getNextPlayer();
                            }
                        }
                        else if (cmd.startsWith(Commands.NEW_GAME))
                        {
                            if (!gameIsFull())
                            {
                                reply = Errors.GAME_NOT_FULL;
                            }
                            else
                            {
                                g.addText("New game");
                                game = new GameState();
                                reply = game.toString();
                            }
                        }
                        else if (cmd.startsWith(Commands.WINNER))
                        {
                            if (!gameIsFull())
                            {
                                reply = Errors.GAME_NOT_FULL;
                            }
                            else
                            {
                                reply = "" + game.getWinner();
                            }
                        }
                        else
                        {
                            reply = Errors.CMD_NOT_FOUND;
                        }

                        out.println(reply);

                        //Read new line
                        cmd = in.readLine();
                    }
                }
                catch(Exception ex)
                {
                    g.addText("Connection error: " + ex.getMessage());
                }
            }

            running = false;
        }

        /**
         * Tries to make the move a requested from the client.
         *
         * @param cmd Move command string
         * @return Server reply
         */
        public String makeMove(String cmd)
        {
            String tokens[] = cmd.split(" ");
            int ambo;
            int player;

            if(tokens.length != 3)
            {
                return Errors.INVALID_PARAMS;
            }

            try
            {
                ambo = Integer.parseInt(tokens[1]);
                player = Integer.parseInt(tokens[2]);
            }
            catch(NumberFormatException ex)
            {
                return Errors.INVALID_PARAMS;
            }

            //Check if move is valid
            if (ambo < 1 || ambo > 6)
            {
                return Errors.INVALID_MOVE;
            }

            //Check if the correct player is
            //making the move
            if(player != game.getNextPlayer())
            {
                return Errors.WRONG_PLAYER;
            }

            //Check if the ambo is empty
            if(game.getSeeds(ambo, player) == 0)
            {
                return Errors.AMBO_EMPTY;
            }

            //Make the move!
            game.makeMove(ambo);
            g.addText("Move " + ambo + " by Player " + player);
            g.updateBoard(game);

            if(game.gameEnded())
            {
                g.addText("Player " + game.getWinner() + " won");
                g.updateBoard(game);
            }

            //Valid move
            return game.toString();
        }
    }
}

