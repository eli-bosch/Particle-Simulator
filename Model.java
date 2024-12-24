import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Model
{
	//Private variables for Model class
	
	private ArrayList<Particle> list;
	private Particle buffer;
	

	//Constructor Method
	
	public Model()
	{
		list = new ArrayList<Particle>();

		for(int i = 0; i < 40; i++) {
			Random ran = new Random();
			this.buffer = new Particle(ran.nextInt(35, 965), ran.nextInt(35, 965), 0, 0, 35, 10, 0, 0);
			list.add(this.buffer);
		}	

		// for(int i = 0; i < 100000; i++) {
		// 	Random ran = new Random();
		// 	this.buffer = new Particle(ran.nextInt(0, 950), ran.nextInt(0, 950), ran.nextInt(-300, 300), ran.nextInt(-300, 300), 2, 20, 0, 0);
		// 	list.add(this.buffer);
		// }

		// // this.buffer = new Particle(200, 950, 15, 0, 50, 10, 0, 0);
		// // list.add(this.buffer);
		// // this.buffer = new Particle(800, 950, -15, 0, 50, 10, 0, 0);
		// // list.add(this.buffer);
	}

	//Setter Methods

	// private void setNewSprite() //Utilizes buffer so that new Sprite can be added and adjusted without concurrency error
	// {
		
	// }

	//Wall Collision method
	private void checkCollision(Particle p) 
	{
		//Particle Collision
		ArrayList<Particle> copy = new ArrayList<Particle>(this.list);
		Iterator<Particle> iterator = copy.iterator();

		while(iterator.hasNext())
		{
			Particle temp = iterator.next();
			if(p != temp) {
				p.handleParticleCollision(temp);
			}
		}

		//Wall Collision
		if(p.getY() + p.getRadius() > 1000) { 
			p.handleWallCollision("bottom");
		}
		if(p.getY() - p.getRadius() < 0) {
			p.handleWallCollision("top");
		}
		if(p.getX() + p.getRadius() > 1000) {
			p.handleWallCollision("right");
		}
		if(p.getX() - p.getRadius() < 0) {
			p.handleWallCollision("left");
		}
	}

	//Update Method called in Game

	public ArrayList<Particle> getParticles()
	{
		return this.list;
	}

	public void update()
	{
		//update particles first	

		for(Iterator<Particle> iterator = list.iterator(); iterator.hasNext();)
		{
			Particle temp = iterator.next();
			temp.update();
			this.checkCollision(temp);
		}
	}
}