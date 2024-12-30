import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Simulator extends JFrame
{
	//Class references 
	Model model = new Model(); 
	View view = new View(model);
	//Controller controller = new Controller(model);
	private BufferedReader br;

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
		this.setVisible(false);
		// view.addMouseListener(controller);
		// this.addKeyListener(controller);		

		this.br = new BufferedReader(new InputStreamReader(System.in));
	}

	public static void main(String[] args) // Just the main method
	{
		Simulator s = new Simulator();
		s.UserInterface();
	}

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

	}

	private void runPrevious()
	{
		String title = this.inputReader("What is the title of the previously calculated simulation?");

		view.setOptions(true, title);
		this.setVisible(true);
		System.out.println("Running the previous simulation, \"" + title + "\"");

		while(true) {
			view.repaint();

			try {      
				Thread.sleep(15);
			} catch (Exception e) {  
				e.printStackTrace();
				System.exit(1);
			}
		}


	}

	private void runNew()
	{
		String title = this.inputReader("What would you like to title the new simulation?");

		view.setOptions(false, title);
		this.setVisible(true);
		System.out.println("The new simulation, \"" + title + "\", is now being calculated");

		while(true) {
			model.update();
			view.repaint();
			view.captureFrame();
			Toolkit.getDefaultToolkit().sync();

			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
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
}
