// Eli Bosch, 3/12/2024, Controller for Assignment 5

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Controller implements MouseListener, KeyListener
{
	/*All Private variables and class references contained within the Controller Class */

	private Model model;
	private boolean addSegment, moveRight, moveDown, moveLeft, moveUp = false;
	
	/*Class methods contaioned within Controller */

	public Controller(Model m) // For Controller to reference the model class
	{
    	model = m;
	}

	/*All the Mouse Listner Methods */

	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) 
	{

	}

	public void mousePressed(MouseEvent e) //Sends the pixel cordinates of the mouse click to the model set destination 
	{
		
		
	}

	public void mouseReleased(MouseEvent e) //This completes the positioning of the wall
	{   
		
	}

	/*All of the  KeyListner Methods */

	public void keyTyped(KeyEvent e){	}

	public void keyPressed(KeyEvent e) // checks which keys are being clicked and sets them to true
	{ 
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_ESCAPE: System.exit(0); break;

			case KeyEvent.VK_RIGHT: moveRight = true; break;
			case KeyEvent.VK_DOWN: moveDown = true; break;
			case KeyEvent.VK_LEFT: moveLeft = true; break;
			case KeyEvent.VK_UP: moveUp = true; break;
		}
			
	}

	public void keyReleased(KeyEvent e) // once the keys are released it sets them to false
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: moveRight = false; break;
			case KeyEvent.VK_DOWN: moveDown = false; break;
			case KeyEvent.VK_LEFT: moveLeft = false; break;
			case KeyEvent.VK_UP: moveUp = false; break;
			case KeyEvent.VK_SPACE: addSegment = true; break;
		}
	}

	

	/*Constantly updated Controller Class which controls the movement of Pacman */

	public void update() 
	{
		
			
	}	
}
