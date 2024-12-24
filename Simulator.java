
// Eli Bosch, 3/12/24, Main java class for Assignment 5 (The og class \_|-o-|_/)
//Works but you have to adjust wdith and the tail set new

import javax.swing.JFrame;
import java.util.Scanner;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Simulator extends JFrame
{
	//Class references 
	Model model = new Model(); 
	View view = new View(model);
	Controller controller = new Controller(model);

	static final int FRAME_SIZE = 1000;

	public Simulator()
	{
		//creates the jframe and tells the view that controller is in charge of mouse and key clicks
		this.setTitle("Physics Simulator 2D");
		view.setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
		this.setFocusable(true);
		this.getContentPane().add(view);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		view.addMouseListener(controller);
		this.addKeyListener(controller);		
	}

	public static void main(String[] args) // Just the main method
	{
		Simulator s = new Simulator();
		s.run();
	}

	public void run() // a while loop that constantly runs in the main method 
	{
		view.createFolder("TestSim");
		Scanner scanner = new Scanner(System.in);
		System.out.println("'P' for playback or 'N' for new simulation");
		String type = scanner.nextLine();
		scanner.close();

		while(true)
		{
			if(type.equals("P")) {
				System.out.println("Playback");
				view.playback = true;
				view.folderPath = "TestSim";
				view.repaint();
			} else {
				//The updates is repeated waiting for inputs
				//controller.update();
				System.out.println("New Sim");
				view.playback = false;
				model.update();
				view.repaint(); // This will indirectly call View.paintComponent
				view.captureFrame("frame", "TestSim");
				Toolkit.getDefaultToolkit().sync(); // Updates screen
			}
			try
			{
				Thread.sleep(10); // sets the frame rate to 100 fps
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			
		}
	}
}
