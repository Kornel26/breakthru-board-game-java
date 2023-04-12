/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2_v2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Defines a public class named GameFrame that extends JFrame and implements ActionListener.
 * @author Kornel
 */
public class GameFrame extends JFrame implements ActionListener {

    private Game game;
    private final int windowHeight = 600;
    private final int windowWidth = 600;
    private final JMenuItem newGameMenuItem = new JMenuItem("New game");

    /**
     * Defines a public constructor for the GameFrame class that initializes the game variable, creates a JMenuBar, sets the menuBar, sets the size of the JFrame, sets the default close operation, sets the location of the JFrame, adds the game to the JFrame, sets the JFrame to not be resizable, packs the JFrame, and sets it to visible.
     */
    public GameFrame() {
        this.game = new Game(8, this);
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        newGameMenuItem.addActionListener(this);

        gameMenu.add(newGameMenuItem);
        menuBar.add(gameMenu);
        this.setJMenuBar(menuBar);

        this.setSize(this.windowHeight + menuBar.getHeight(), this.windowWidth);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(game);
        this.setResizable(false);
        this.pack();

        this.setVisible(true);
    }

    /**
     * Overrides the actionPerformed method of the ActionListener interface and defines a method body to handle an action event.
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameMenuItem) {
            showResetGameDialog();
        }
    }

    /**
     * Defines a private method named showResetGameDialog that displays a dialog box to reset the game.
     */
    private void showResetGameDialog() {
        final String result = JOptionPane.showInputDialog(null, "How many squares you want to play with?", "Create a new game", JOptionPane.INFORMATION_MESSAGE);
        try {
            int n = Integer.parseInt(result);
            if (n < 1) {
                return;
            }
            this.resetGame(n);
        } catch (NumberFormatException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Defines a public method named resetGame that takes an integer parameter n and removes the game, creates a new Game object with n squares, adds the new Game object to the JFrame, updates the component tree, and displays the new game.
     * @param n 
     */
    public void resetGame(int n) {
        this.remove(game);
        game = new Game(n, this);
        this.add(game);
        SwingUtilities.updateComponentTreeUI(this);
    }
}
