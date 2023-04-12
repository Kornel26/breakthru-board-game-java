/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2_v2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Defines a public class named Mouse that implements the MouseListener interface.
 * @author Kornel
 */
public class Mouse implements MouseListener {

    private final Game game; //Declares a private final variable named game of type Game.

    /**
     * Defines a public constructor for the Mouse class that accepts a Game object as a parameter and initializes the game variable with it.
     * @param game 
     */
    public Mouse(Game game) {
        this.game = game;
    }

    /**
     * Overrides the mouseClicked method of the MouseListener interface and defines an empty method body.
     * @param e 
     */
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Overrides the mousePressed method of the MouseListener interface and defines an empty method body.
     * @param e 
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Overrides the mouseReleased method of the MouseListener interface and calls the handleMouseClicked method of the Game object passed in the constructor, passing the MouseEvent object as a parameter.
     * @param e 
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        this.game.handleMouseClicked(e);
    }

    /**
     * Overrides the mouseEntered method of the MouseListener interface and defines an empty method body.
     * @param e 
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    
    /**
     * Overrides the mouseExited method of the MouseListener interface and defines an empty method body.
     * @param e 
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

}
