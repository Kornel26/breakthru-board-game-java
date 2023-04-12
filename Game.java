/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2_v2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * Game class
 * @author Kornel
 */
public class Game extends JPanel {

    private final int width = 600;
    private final int height = 600;
    private final Dimension size = new Dimension(this.width, this.height);
    private final int n;
    private final int tileWidth;
    private final Mouse mouse = new Mouse(this);
    private final GameFrame gameFrame;

    private char[][] board;
    private boolean whitesTurn = true;
    private boolean pawnClicked = false;
    private Point clickedPawn = null;
    private ArrayList<Point> validMoves = null;
    private boolean gameOver = false;
    private char whoWon;

    /**
     * Constructor that initializes the game with a given number of tiles and a GameFrame.
     * @param n
     * @param gameFrame 
     */
    public Game(int n, GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.n = n;
        this.tileWidth = this.width / this.n;
        this.initBoard();

        this.setMinimumSize(this.size);
        this.setPreferredSize(this.size);
        this.setMaximumSize(this.size);
        this.setLocation(0, 0);

        this.addMouseListener(mouse);

        this.repaint();
    }

    /**
     * Method that is called by the system whenever the component needs to be redrawn. It paints the board, pawns, and highlights the valid moves. If the game is over, it displays the winner and resets the game.
     * Credit https://stackoverflow.com/questions/1842734/how-to-asynchronously-call-a-method-in-java
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.draw(g);
        if (this.gameOver) {
            System.out.println("GAME OVER");
            final String winningColor = this.whoWon == 'w' ? "White" : (this.whoWon == 'b' ? "Black" : "");
            g.setColor(Color.DARK_GRAY);
            this.drawCenteredString(g, winningColor + " won!", new Rectangle(this.size.height, this.size.width), new Font("TimesRoman", Font.PLAIN, 100));
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.gameFrame.resetGame(n);
            }).start();
        }
    }

    /**
     * Draws a given string in the center of a rectangle with a specified font.
     * Credit: https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
     * @param g
     * @param text
     * @param rect
     * @param font 
     */
    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }

    /**
     * Draws the board, pawns, highlights the valid moves, and draws a yellow rectangle around the clicked pawn.
     * @param g 
     */
    private void draw(Graphics g) {
        for (int i = 0; i < n; i++) {
            boolean squareSwitch = i % 2 == 1;
            for (int j = 0; j < n; j++) {
                g.setColor(Color.decode(squareSwitch ? "#769656" : "#eeeed2"));
                squareSwitch = !squareSwitch;
                g.fillRect(i * tileWidth, j * tileWidth, tileWidth, tileWidth);
                //pawns
                if (this.board[i][j] == 'w' || this.board[i][j] == 'b') {
                    if (this.board[i][j] == 'w') {
                        g.setColor(Color.WHITE);
                    } else {
                        g.setColor(Color.BLACK);
                    }
                    g.fillOval(i * this.tileWidth + tileWidth / 4, j * tileWidth + tileWidth / 4, tileWidth / 2, tileWidth / 2);
                }
                //highlight
                if (this.validMoves != null) {
                    for (int k = 0; k < this.validMoves.size(); k++) {
                        g.setColor(Color.YELLOW);
                        g.fillOval(validMoves.get(k).x * tileWidth + tileWidth / 2 - tileWidth / 8, validMoves.get(k).y * tileWidth + tileWidth / 2 - tileWidth / 8, tileWidth / 4, tileWidth / 4);
                    }
                }
                if (this.clickedPawn != null && this.clickedPawn.x == i && this.clickedPawn.y == j) {
                    g.setColor(Color.YELLOW);
                    g.drawRect(i * this.tileWidth, j * this.tileWidth, this.tileWidth - 1, this.tileWidth - 1);
                }
            }
        }
    }

    /**
     * Initializes the board with empty squares and the pawns in their starting positions.
     */
    private void initBoard() {
        this.board = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.board[i][j] = 'e';
                if (j < 2) {
                    this.board[i][j] = 'b';
                }
                if (j >= n - 2) {
                    this.board[i][j] = 'w';
                }
            }
        }
    }

    /**
     * Prints the board's state to the console.
     */
    private void debug() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                //System.out.print("i: " + i + " j: " + j + " value: " + this.board[i][j]);
                System.out.print(this.board[j][i]);
            }
             System.out.println();
        }
    }

    /**
     * Method that handles the player's mouse click events.
     * @param e 
     */
    protected void handleMouseClicked(MouseEvent e) {
        if (this.gameOver) {
            return;
        }
        int i = e.getX() / this.tileWidth;
        int j = e.getY() / this.tileWidth;
        if (this.whitesTurn) {
            this.handleWhitesTurn(i, j);
        } else if (!this.whitesTurn) {
            this.handleBlacksTurn(i, j);
        }
        this.repaint();
    }

    /**
     * Method that handles the White player's turn.
     * @param i
     * @param j 
     */
    private void handleWhitesTurn(int i, int j) {
        if (this.pawnClicked && this.validMoves.size() > 0 && this.validMove(i, j)) {
            this.step(i, j, 'w');
            this.resetStepState();
        } else if (this.board[i][j] == 'w') {
            this.calculateWhitesValidSteps(i, j);
        } else {
            this.resetStepState();
        }
    }

    /**
     * Moves the pawn to the given position and updates the board.
     * @param i
     * @param j
     * @param c 
     */
    private void step(int i, int j, char c) {
        if (this.validMove(i, j)) {
            int dx = this.clickedPawn.x;
            int dy = this.clickedPawn.y;
            board[i][j] = c;
            board[dx][dy] = 'e';
            this.whitesTurn = !this.whitesTurn;
            this.gameOver = this.isGameOver();
        }
    }

    /**
     * Checks whether the game is over by examining the game board to see if either the white or black player has reached the opposite end of the board.
     * @return 
     */
    private boolean isGameOver() {
        for (int i = 0; i < n; i++) {
            if (this.board[i][0] == 'w') {
                this.whoWon = 'w';
                return true;
            }
            if (this.board[i][n - 1] == 'b') {
                this.whoWon = 'b';
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the valid moves for the White player.
     * @param i
     * @param j 
     */
    private void calculateWhitesValidSteps(int i, int j) {
        this.pawnClicked = true;
        this.clickedPawn = new Point(i, j);
        this.validMoves = new ArrayList();
        if (i - 1 >= 0 && (this.board[i - 1][j - 1] == 'e' || (this.board[i - 1][j - 1] != 'e' && this.board[i - 1][j - 1] != 'w'))) {
            this.validMoves.add(new Point(i - 1, j - 1));
        }
        if (this.board[i][j - 1] == 'e') {
            this.validMoves.add(new Point(i, j - 1));
        }
        if (i + 2 <= n && (this.board[i + 1][j - 1] == 'e' || (this.board[i + 1][j - 1] != 'e' && this.board[i + 1][j - 1] != 'w'))) {
            this.validMoves.add(new Point(i + 1, j - 1));
        }
    }

    /**
     * Method that handles the Black player's turn.
     * @param i
     * @param j 
     */
    private void handleBlacksTurn(int i, int j) {
        if (this.pawnClicked && this.validMoves.size() > 0 && this.validMove(i, j)) {
            this.step(i, j, 'b');
            this.resetStepState();
        } else if (this.board[i][j] == 'b') {
            this.calculateBlacksValidSteps(i, j);
        } else {
            this.resetStepState();
        }
    }

    /**
     * Calculates the valid moves for the Black player.
     * @param i
     * @param j 
     */
    private void calculateBlacksValidSteps(int i, int j) {
        this.pawnClicked = true;
        this.clickedPawn = new Point(i, j);
        this.validMoves = new ArrayList();
        if (i - 1 >= 0 && (this.board[i - 1][j + 1] == 'e' || (this.board[i - 1][j + 1] != 'e' && this.board[i - 1][j + 1] != 'b'))) {
            this.validMoves.add(new Point(i - 1, j + 1));
        }
        if (this.board[i][j + 1] == 'e') {
            this.validMoves.add(new Point(i, j + 1));
        }
        if (i + 2 <= n && (this.board[i + 1][j + 1] == 'e' || (this.board[i + 1][j + 1] != 'e' && this.board[i + 1][j + 1] != 'b'))) {
            this.validMoves.add(new Point(i + 1, j + 1));
        }
    }

    /**
     * Resets the game state after a step has been taken.
     */
    private void resetStepState() {
        this.pawnClicked = false;
        this.clickedPawn = null;
        this.validMoves = null;
    }

    /**
     * Checks if the given move is valid.
     * @param i
     * @param j
     * @return 
     */
    private boolean validMove(int i, int j) {
        for (int v = 0; v < this.validMoves.size(); v++) {
            if (this.validMoves.get(v).x == i && this.validMoves.get(v).y == j) {
                return true;
            }
        }
        return false;
    }
}
