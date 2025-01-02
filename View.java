import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;

public class View extends JPanel
{ 
	//Private class references
	private Model model;

	//Private view variables
	private int frameCount;
	private boolean playback;
	private boolean doneRendering;
	private String title;

	//Customizable options - currently set to the default
	private final String imageType = "png";
	private final String frameName = "frame_";

	//Constructor
	public View(Model m)
	{
		//Class references
		model = m;

		//View variables
		this.frameCount = 0;
		this.playback = false;
		this.title = "";
		this.doneRendering = false;
	}

	//Used for playback
	public boolean doneRendering()
	{
		return this.doneRendering;
	}

	//Setter Methods
	public void setOptions(Boolean playback, String title) {
		this.playback = playback;
		this.title = title;;

		if(!this.playback) {
			this.setFolder();
		}
	}

	public void setFolder()
	{
		File folder = new File(this.title);

		//Checks if folder exists or not
		if(!folder.exists()) { 
			if(folder.mkdirs()) {
				System.out.println("Folder created: " + this.title);
			} else {
				System.out.println("Failed to create folder: " + this.title);
				return;
			}
		}
	}

	//Private Methods for Simulation Graphics
	public void captureFrame()
	{
		try {
			BufferedImage frame = new BufferedImage(Simulator.FRAME_SIZE, Simulator.FRAME_SIZE, BufferedImage.TYPE_INT_RGB);

			Graphics g = frame.getGraphics();
			this.paint(g);
			g.dispose();

			File screenshot= new File(this.title, this.frameName + this.frameCount + "." + this.imageType);
			ImageIO.write(frame, "png", screenshot);
			frameCount++;
			System.out.println("Frame saved as " + title + "//" + this.frameName + this.frameCount + "." + this.imageType);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//Jframe Painting Method
	public void paintComponent(Graphics g)
	{	
		//Handles which method to call
		if(!playback) { 

			synchronized (model.getParticles()) {
				this.simCreation(g);
			}
		} else {
			this.playback(g);
		}
	}

	//Captures screenshot for playback
	private void simCreation(Graphics g) 
	{
		g.setColor(new Color(255,255,255));
		g.fillRect(0, 0, Simulator.FRAME_SIZE, Simulator.FRAME_SIZE);
		g.setColor(Color.BLACK);

		ArrayList<Particle> particles;

		synchronized (model.getParticles()) {
			particles = new ArrayList<>(model.getParticles());
		}

		for(Particle p : particles) {
			p.draw(g);
		}
	}

	private void playback(Graphics g) //Playback of the previously created simulation
	{
		File file = new File(this.title + "//" + this.frameName + this.frameCount + "." + this.imageType);

		if(!file.exists()) { //Could run into issues if single frame is deleted in middle of simulation
			System.out.println("The simulation playback is complete, and will exit");
			this.doneRendering = true;
			return;
		}
			

		try 
			{
				BufferedImage frame = ImageIO.read(new File(this.title + "//" + this.frameName + this.frameCount + "." + this.imageType)); //Takes the screenshot and draws it onto screen
				g.drawImage(frame, 0, 0, Simulator.FRAME_SIZE, Simulator.FRAME_SIZE, null);
				System.out.println(this.title + "//" + this.frameName + this.frameCount + "." + this.imageType);

				frameCount++;
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
