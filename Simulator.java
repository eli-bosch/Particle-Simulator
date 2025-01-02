/*
 * Eli Bosch, Junior at the University of Arkansas, Computer Science, 1/1/25
 */

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Simulator extends JFrame
{
	//Class references 
	Threads threadManager = new Threads();
	Model model = new Model(threadManager); 
	View view = new View(model);
	BufferedReader br;
	

	static final int FRAME_SIZE = 1000;

	public Simulator()
	{
		//creates the jframe and tells the view that controller is in charge of mouse and key clicks
		this.setTitle("Physics Simulator 2D");
		view.setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
		this.setFocusable(true);
		this.getContentPane().add(view);
		this.pack();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(false);
		this.br = new BufferedReader(new InputStreamReader(System.in));

		this.addWindowListener(new java.awt.event.WindowAdapter() { //Custom window close method for safe thread precedure
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				int choice = JOptionPane.showConfirmDialog(
                Simulator.this,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION
            );

				if (choice == JOptionPane.YES_OPTION) {
					Simulator.this.safeClose();
				}
			}
		});
	}

	//Main Method
	public static void main(String[] args)
	{
		Simulator s = new Simulator();
		s.UserInterface();
	}

	//Handles user options
	public void UserInterface()
	{
		String option = this.inputReader("Would you like to create a new simulation (\"N\"), or would you like to run a previously calculated simulation (\"P\")?");
		
		if(option.equals("N")) {
			this.runNew();
		} else if(option.equals("P")) {
			this.runPrevious();
		} else {
			System.out.println("The option, \"" + option + "\", is not available, please choose again");
			this.UserInterface();
		}

		//Other features that could be implemented: change fps, and actual UI outside of terminal
	}

	//Playback previous saved simulation
	private void runPrevious()
	{
		String title = this.inputReader("What is the title of the previously calculated simulation?");

		view.setOptions(true, title);
		this.setVisible(true);
		System.out.println("Running the previous simulation, \"" + title + "\"");

		while(!view.doneRendering()) {
			view.repaint();

			try {      
				Thread.sleep(15); //Framerate
			} catch (Exception e) {  
				e.printStackTrace();
				System.exit(1);
			}
		}

		this.safeClose();
	}

	//Create new simulation
	private void runNew()
	{
		String title = this.inputReader("What would you like to title the new simulation?");
		String numFrames = this.inputReader("How many ms would you like the simulation to be?");
		int num = 0;

		try {
			num = Integer.parseInt(numFrames);
		} catch (NumberFormatException e) {
			System.out.println("That is a word not a number!");
			this.safeClose();
		}

		view.setOptions(false, title);
		this.setVisible(true);
		System.out.println("The new simulation, \"" + title + "\", is now being calculated");

		while(!model.isDone()) {
			model.setFrameAmount(num);
			model.update();
			
			//Safe thread precedure for repaint method
			try {
            	SwingUtilities.invokeAndWait(() -> { 
                view.repaint();
                view.captureFrame();
            });
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		Toolkit.getDefaultToolkit().sync();

		this.safeClose();
	}

	private String inputReader(String prompt) //Used to get user input
	{
		System.out.println(prompt);
		String input = null;

			while(input == null || input == "") {
				try {
					input = this.br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		return input;
	}

	//Used to make sure all threads are closed
	private void safeClose() 
	{
		threadManager.shutdown();
		Simulator.this.dispose();
		System.exit(0);
	}
}
