package server;

import ai.AIClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Game.*;
import client.*;

/**
 * GUI for the server application.
 *
 */
public class ServerGUI implements ActionListener
{
    private JTextArea text;

    private JLabel[] labels_N;
    private JLabel[] labels_S;
    private JLabel house_N;
    private JLabel house_S;
    private JTextField portField;

    private static ServerGUI instance;

    /**
     * Singleton class.
     *
     * @return Class instance
     */
    public static ServerGUI getInstance()
    {
        if (instance == null)
        {
            instance = new ServerGUI();
        }
        return instance;
    }

    /**
     * Inits and creates the GUI.
     */
    private ServerGUI()
    {
        JFrame frame = new JFrame("Server");
        frame.setLocation(0, 30);
        frame.setSize(new Dimension(510,210+40+160));
        frame.getContentPane().setLayout(new FlowLayout());

        //Text area
        text = new JTextArea();
        JScrollPane pane = new JScrollPane(text);
        pane.setPreferredSize(new Dimension(380, 210));
        frame.getContentPane().add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Buttons
        JPanel bp = new JPanel();
        bp.setPreferredSize(new Dimension(100,210));
        frame.getContentPane().add(bp);
        JLabel l = new JLabel("Start clients:");
        bp.add(l);
        JButton b = new JButton("Random");
        b.setPreferredSize(new Dimension(80,20));
        b.addActionListener(this);
        bp.add(b);
        b = new JButton("Bad");
        b.setPreferredSize(new Dimension(80,20));
        b.addActionListener(this);
        bp.add(b);
        b = new JButton("Human");
        b.setPreferredSize(new Dimension(80,20));
        b.addActionListener(this);
        bp.add(b);
        b = new JButton("AI");
        b.setPreferredSize(new Dimension(80,20));
        b.addActionListener(this);
        bp.add(b);

        JPanel jp = new JPanel();
        jp.setPreferredSize(new Dimension(80,10));
        bp.add(jp);

        b = new JButton("Reconnect");
        b.setPreferredSize(new Dimension(80,20));
        b.addActionListener(this);
        bp.add(b);
        l = new JLabel("Port:");
        l.setPreferredSize(new Dimension(75,15));
        bp.add(l);
        portField = new JTextField("" + Main.port);
        portField.setPreferredSize(new Dimension(80,20));
        bp.add(portField);

        //Board panel
        JPanel boardPane = new JPanel();
        boardPane.setPreferredSize(new Dimension(475, 160));
        boardPane.setBackground(Color.white);
        boardPane.setLayout(null);

        //Add ambos for north player
        labels_N = new JLabel[6];
        for (int i = 0; i < 6; i++)
        {
            JLabel tmp = new JLabel("" + (i+1), SwingConstants.CENTER);
            tmp.setBounds(360-i*60,5,50,20);
            tmp.setForeground(Color.black);
            boardPane.add(tmp);

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

            JLabel tmp = new JLabel("" + (i+1), SwingConstants.CENTER);
            tmp.setBounds(60+i*60,135,50,20);
            tmp.setForeground(Color.black);
            boardPane.add(tmp);
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

        frame.getContentPane().add(boardPane);

        frame.setVisible(true);

        addText("Game Server " + Main.VERSION);
    }

    /**
     * Button handler.
     *
     * @param e Action event
     */
    public void actionPerformed(ActionEvent e)
    {
        String a = e.getActionCommand();

        if (a.equalsIgnoreCase("Random"))
        {
            RandomClient c = new RandomClient();
            c.start();
        }
        if (a.equalsIgnoreCase("Bad"))
        {
            BadClient c = new BadClient();
            c.start();
        }
        if (a.equalsIgnoreCase("Human"))
        {
            HumanClient c = new HumanClient();
            c.start();
        }
        if (a.equalsIgnoreCase("AI"))
        {
            AIClient c = new AIClient();
            c.start();
        }
        if (a.equalsIgnoreCase("Reconnect"))
        {
            try
            {
                Server s = Server.getInstance();
                s.stop();
                int p = Integer.parseInt(portField.getText());
                Main.port = p;
                s.start();
            }
            catch (Exception ex)
            {
                addText("Reconnect failed: " + ex.getMessage());
            }

        }
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
     * Updates the game state board panel.
     *
     * @param game Current game state.
     */
    public void updateBoard(GameState game)
    {
        String board = game.toString();
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
}
