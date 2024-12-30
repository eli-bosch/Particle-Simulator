import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Iterator;

public class View extends JPanel
{ 
	//Variables for the View class
	private Model model;
	private int frameCount;
	private boolean playback;
	private String title;

	//Customizable options - currently set to the default
	private final String imageType = "png";
	private final String frameName = "frame_";

	//Constructor
	public View(Model m)
	{
		model = m;
		this.frameCount = 0;
		this.playback = false;
		this.title = "";
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
		if(!playback) { 
			this.simCreation(g);
		} else {
			this.playback(g);
		}
	}

	private void simCreation(Graphics g) //Initial creation of the simulation
	{
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, Simulator.FRAME_SIZE, Simulator.FRAME_SIZE);
			
		Iterator<Particle> iterator = model.getParticles().iterator();  

		while(iterator.hasNext())
		{
			Particle temp = iterator.next();

			if(temp.getMass() == 100) {
				g.setColor(new Color(0, 0, 255));
			} else if(g.getColor() != new Color(255, 0, 0)) {
				g.setColor(new Color(255, 0, 0));
			}	

			temp.draw(g);
		}
	}

	private void playback(Graphics g) //Playback of the previously created simulation
	{
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
