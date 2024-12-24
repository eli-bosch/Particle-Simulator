import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Iterator;

public class View extends JPanel
{
	/*Private variables in the View Class */

	private Model model;
	public int count;
	public boolean playback = false;
	public String folderPath;

	/*Constructor for View Class */

	public View(Model m)
	{
		model = m;
		count = 0;
	}

	// public static BufferedImage OPEN_IMAGE(String path)
	// {	
	// 	try
	// 	{
	// 		return ImageIO.read(new File(path)); 
	// 	} 
	// 	catch (Exception e)
	// 	{
	// 		e.printStackTrace(System.err);
    // 		System.exit(1);
	// 	}

	// 	return null;
	// }
	//FIX: Add Screenshot method
	public void captureFrame(String filename, String folderPath)
	{
		try {
			BufferedImage frame = new BufferedImage(Simulator.FRAME_SIZE, Simulator.FRAME_SIZE, BufferedImage.TYPE_INT_RGB);

			Graphics g = frame.getGraphics();
			this.paint(g);
			g.dispose();

			filename+=count;
			filename+=".png";

			File screenshotFile = new File(folderPath, filename);
			ImageIO.write(frame, "png", screenshotFile);
			count++;
			System.out.println("Frame saved as " + filename);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void createFolder(String folderPath)
	{
		File folder = new File(folderPath);

		if(!folder.exists()) {
			if(folder.mkdirs()) {
				System.out.println("Folder created: " + folderPath);
			} else {
				System.out.println("Failed to create folder: " + folderPath);
				return;
			}
		}
	}

	//FIX: Add Playback method
	public void playback(String folderPath, Graphics g) 
	{
		
	}

	/*Jframe Painting Method */

	public void paintComponent(Graphics g)
	{	
		if(!playback) {
			g.setColor(new Color(0,0,0));
			g.fillRect(0, 0, Simulator.FRAME_SIZE, Simulator.FRAME_SIZE);
			
			Iterator<Particle> iterator = model.getParticles().iterator();  

			while(iterator.hasNext())
			{
				Particle temp = iterator.next();
				temp.draw(g);
			}
		} else {
			try {
				BufferedImage frame = ImageIO.read(new File(folderPath + "//frame" + count + ".png"));
				g.drawImage(frame, 0, 0, Simulator.FRAME_SIZE, Simulator.FRAME_SIZE, null);
				count++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
}
