package client;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Game.*;

/**
 * Human player client for the game server.
 *
 */
public class HumanClient implements Runnable, ActionListener
{
    private int player;

    private JTextArea text;
    private JPanel boardPane;
    private JLabel[] labels_N;
    private JLabel[] labels_S;
    private JLabel house_N;
    private JLabel house_S;

    private PrintWriter out;
    private BufferedReader in;
    private Thread thr;
    private Socket socket;
    private boolean running;
    private boolean myTurn;
    private boolean connected;

    /**
     * Creates a new human client.
     */
    public HumanClient()
    {
        player = -1;
        myTurn = false;
        connected = false;

        initGUI();

        try
        {
            addText("Connecting to localhost:" + Main.port);
            socket = new Socket("localhost", Main.port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            addText("Done");

            out.println(Commands.HELLO);
            String reply = in.readLine();

            String parse[] = reply.split(" ");
            player = Integer.parseInt(parse[1]);

            addText("I am player " + player);
            connected = true;
        }
        catch (Exception ex)
        {
            addText("Unable to connect to server");
            return;
        }

        initBoardPanel();
    }

    /**
     * Starts the client thread.
     */
    public void start()
    {
        if (connected)
        {
            thr = new Thread(this);
            thr.start();
        }
    }

    /**
     * Creates the GUI.
     */
    private void initGUI()
    {
        JFrame frame = new JFrame("Human Client");
        frame.setLocation(515, 30);
        frame.setSize(new Dimension(485,210+40+160));
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        text = new JTextArea();
        JScrollPane pane = new JScrollPane(text);
        pane.setPreferredSize(new Dimension(400, 210));
        frame.getContentPane().add(pane);

        //Board panel
        boardPane = new JPanel();
        boardPane.setPreferredSize(new Dimension(475, 160));
        boardPane.setBackground(Color.white);
        boardPane.setLayout(null);
        frame.getContentPane().add(boardPane);

        frame.setVisible(true);
    }

    /**
     * Creates the GUI panel showing the Game
     * game board.
     */
    private void initBoardPanel()
    {
        //Add ambos for north player
        labels_N = new JLabel[6];
        for (int i = 0; i < 6; i++)
        {
            if (player == 2)
            {
                //Buttons
                JButton tmp = new JButton("" + (i+1));
                tmp.setActionCommand(Commands.MOVE + " " + (i+1));
                tmp.addActionListener(this);
                tmp.setBounds(360-i*60,5,50,20);
                tmp.setForeground(Color.black);
                tmp.setBackground(Color.lightGray);
                boardPane.add(tmp);
            }
            else
            {
                //Labels
                JLabel tmp = new JLabel("" + (i+1), SwingConstants.CENTER);
                tmp.setBounds(360-i*60,5,50,20);
                tmp.setForeground(Color.black);
                boardPane.add(tmp);
            }
            labels_N[i] = new JLabel(" ", SwingConstants.CENTER);
            labels_N[i].setBounds(360-i*60,30,50,30);
            labels_N[i].setBackground(Color.red);
            labels_N[i].setOpaque(true);
            labels_N[i].setForeground(Color.white);
            boardPane.add(labels_N[i]);
        }

        //Player N label
        JLabel n = new JLabel("2", SwingConstants.CENTER);
        n.setBounds(5, 30, 50, 30);
        n.setForeground(Color.red);
        boardPane.add(n);

        //House N
        house_N = new JLabel(" ", SwingConstants.CENTER);
        house_N.setBounds(5, 65, 50, 30);
        house_N.setBackground(Color.red);
        house_N.setForeground(Color.white);
        house_N.setOpaque(true);
        boardPane.add(house_N);

        //Add ambos for south player
        labels_S = new JLabel[6];
        for (int i = 0; i < 6; i++)
        {
            labels_S[i] = new JLabel(" ", SwingConstants.CENTER);
            labels_S[i].setBounds(60+i*60,100,50,30);
            labels_S[i].setBackground(Color.blue);
            labels_S[i].setOpaque(true);
            labels_S[i].setForeground(Color.white);
            boardPane.add(labels_S[i]);

            if (player == 1)
            {
                //Buttons
                JButton tmp = new JButton("" + (i+1));
                tmp.setActionCommand(Commands.MOVE + " " + (i+1));
                tmp.addActionListener(this);
                tmp.setBounds(60+i*60,135,50,20);
                tmp.setForeground(Color.black);
                tmp.setBackground(Color.lightGray);
                boardPane.add(tmp);
            }
            else
            {
                //Labels
                JLabel tmp = new JLabel("" + (i+1), SwingConstants.CENTER);
                tmp.setBounds(60+i*60,135,50,20);
                tmp.setForeground(Color.black);
                boardPane.add(tmp);
            }
        }

        //Player S label
        JLabel s = new JLabel("1", SwingConstants.CENTER);
        s.setBounds(420, 100, 50, 30);
        s.setForeground(Color.blue);
        boardPane.add(s);

        //House S
        house_S = new JLabel(" ", SwingConstants.CENTER);
        house_S.setBounds(420, 65, 50, 30);
        house_S.setBackground(Color.blue);
        house_S.setForeground(Color.white);
        house_S.setOpaque(true);
        boardPane.add(house_S);

        boardPane.updateUI();
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
     * Updates the GUI board panel to a new
     * game state received from the game server.
     *
     * @param board Board string representation
     */
    public void updateBoard(String board)
    {
        String[] tokens = board.split(";");

        //House_S
        house_S.setText(tokens[GameState.HOUSE_S]);

        //South player ambos
        for (int i = GameState.START_S; i <= GameState.END_S; i++)
        {
            labels_S[i - GameState.START_S].setText(tokens[i]);
        }

        //House_N
        house_N.setText(tokens[GameState.HOUSE_N]);

        //North player ambos
        for (int i = GameState.START_N; i <= GameState.END_N; i++)
        {
            labels_N[i - GameState.START_N].setText(tokens[i]);
        }
    }

    /**
     * Button event handler.
     *
     * @param e Action event
     */
    public void actionPerformed(ActionEvent e)
    {
        String a = e.getActionCommand();

        if (a.startsWith(Commands.MOVE) && running)
        {
            try
            {
                out.println(a + " " + player);
                String reply = in.readLine();
                if (reply.equals(Errors.WRONG_PLAYER))
                {
                    addText("Move failed: Not my turn");
                    myTurn = false;
                }
                else if (reply.equals(Errors.AMBO_EMPTY))
                {
                    addText("Move failed: Ambo is empty");
                }
                else
                {
                    updateBoard(reply);
                    myTurn = false;
                }
            }
            catch (Exception ex)
            {
                addText("Failed to send Move command: " + ex.getMessage());
            }
        }
    }

    /**
     * Thread for server communication. Checks when it is this
     * client's turn to make a move.
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

                //Check if it is my turn
                if (!myTurn)
                {
                    out.println(Commands.NEXT_PLAYER);
                    reply = in.readLine();
                    if (!reply.equals(Errors.GAME_NOT_FULL) && running)
                    {
                        int nextPlayer = Integer.parseInt(reply);

                        if(nextPlayer == player)
                        {
                            out.println(Commands.BOARD);
                            reply = in.readLine();
                            updateBoard(reply);
                            addText("Your move!");
                            myTurn = true;
                        }
                    }
                }

                //Check if game has ended
                out.println(Commands.WINNER);
                reply = in.readLine();
                if(reply.equals("1") || reply.equals("2"))
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

                //Wait
                Thread.sleep(500);
            }
        }
        catch (Exception ex)
        {
            addText("An error was encountered: " + ex.getMessage());
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