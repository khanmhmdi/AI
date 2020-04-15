package client;

import ai.Global;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import Game.*;

/**
 * Bad playing client for the game server. The client always
 * makes the first possible move (first non-empty ambo).
 *
 */
public class BadClient implements Runnable
{
    private int player;
    private JTextArea text;

    private PrintWriter out;
    private BufferedReader in;
    private Thread thr;
    private Socket socket;
    private boolean running;

    /**
     * Creates a new bad playing client.
     */
    public BadClient()
    {
        player = -1;

        initGUI();

        try
        {
            addText("Connecting to localhost:" + Main.port);
            socket = new Socket("localhost", Main.port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            addText("Done");
        }
        catch (Exception ex)
        {
            addText("Unable to connect to server");
            return;
        }
    }

    /**
     * Starts the client thread.
     */
    public void start()
    {
        thr = new Thread(this);
        thr.start();
    }

    /**
     * Creates the GUI.
     */
    private void initGUI()
    {
        JFrame frame = new JFrame("Bad playing Client");
        frame.setLocation(Global.getClientXpos(), 445);
        frame.setSize(new Dimension(420,250));
        frame.getContentPane().setLayout(new FlowLayout());

        text = new JTextArea();
        JScrollPane pane = new JScrollPane(text);
        pane.setPreferredSize(new Dimension(400, 210));

        frame.getContentPane().add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }

    /**
     * Adds a text string to the GUI textarea.
     *
     * @param txt The text to add
     */
    public void addText(String txt)
    {
        text.append(txt + "\n");
        text.setCaretPosition(text.getDocument().getLength());
    }

    /**
     * Thread for server communication. Checks when it is this
     * client's turn to make a move, and then makes a random
     * move.
     */
    public void run()
    {
        String reply;
        running = true;

        try
        {
            while (running)
            {
                if (player == -1)
                {
                    out.println(Commands.HELLO);
                    reply = in.readLine();

                    String tokens[] = reply.split(" ");
                    player = Integer.parseInt(tokens[1]);

                    addText("I am player " + player);
                }

                //Check if game has ended
                out.println(Commands.WINNER);
                reply = in.readLine();
                if(reply.equals("1") || reply.equals("2") )
                {
                    int w = Integer.parseInt(reply);
                    if (w == player)
                    {
                        addText("I won!");
                    }
                    else
                    {
                        addText("I lost...");
                    }
                    running = false;
                }
                if(reply.equals("0"))
                {
                    addText("Even game!");
                    running = false;
                }

                //Check if it is my turn
                out.println(Commands.NEXT_PLAYER);
                reply = in.readLine();
                if (!reply.equals(Errors.GAME_NOT_FULL) && running)
                {
                    int nextPlayer = Integer.parseInt(reply);

                    if(nextPlayer == player)
                    {
                        out.println(Commands.BOARD);
                        reply = in.readLine();
                        int cnt = 1;
                        boolean validMove = false;
                        while (!validMove)
                        {
                            String cMove = "" + cnt;
                            out.println(Commands.MOVE + " " + cMove + " " + player);
                            reply = in.readLine();
                            if (reply.startsWith("ERROR"))
                            {
                                cnt++;
                            }
                            else
                            {
                                validMove = true;
                                addText("Made move " + cMove);
                            }
                        }
                    }
                }

                //Wait
                Thread.sleep(100);
            }
        }
        catch (Exception ex)
        {
            addText("An error was encountered: " + ex.getMessage());
            ex.printStackTrace();
            running = false;
        }

        try
        {
            socket.close();
            addText("Disconnected from server");
        }
        catch (Exception ex)
        {
            addText("Error closing connection: " + ex.getMessage());
        }
    }
}