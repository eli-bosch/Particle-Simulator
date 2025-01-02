/*
 * Eli Bosch, Junior at the University of Arkansas, Computer Science, 1/1/25
 */

import java.util.ArrayList;
import java.util.Random;

public class Model
{
	//ArrayList to store particles and future particles
	private ArrayList<Particle> list;
	private ArrayList<Particle> listBuffer;

	//Private class references
	private Threads threadManager;
	private SpatialPartitioning partition;

	//Private model variables
	private int frameCount;
	private int radius;
	private int frameAmt;
	private boolean isDone;

	//Static variables for all particles
	static final double EPSILON = 1e-8;
	static final double COLLISION_BUFFER = 0.7;
	static final double GRAVITY = 0.5;
	

	//Constructor Method
	public Model(Threads threadManager) //FIX: Layout of entire class
	{
		//Model ArrayList
		this.list = new ArrayList<Particle>();
		this.listBuffer = new ArrayList<Particle>();

		//Class references
		this.partition = new SpatialPartitioning(this.radius * 2);
		this.threadManager = threadManager;

		//Model variables
		this.frameCount = 0;
		this.radius = 1;
		this.frameAmt = 1000;
		this.isDone = false;

		// Random ran = new Random();

		// for(int i = 0; i < 1000000; i++)
		// {
		// 	Particle temp = new Particle(ran.nextDouble(5, 995), ran.nextDouble(5, 995),0, 0, radius, 1);
		// 	this.list.add(temp);
		// }
	}

	//Return Methods
	public boolean isDone()
	{
		return this.isDone;
	}

	public ArrayList<Particle> getParticles()
	{ 
		synchronized (this.list) {
			return new ArrayList<>(this.list);
		}
	}

	//Set Method
	public void setFrameAmount(int frameAmt) 
	{
		this.frameAmt = frameAmt;
	}

	private void spawner() //Used to generate particles during simulation
	{
		if (this.frameCount > 250)
			return;

		Random ran = new Random();

		for(int x = 0; x < 1000; x+=2) {
			this.listBuffer.add(new Particle(ran.nextDouble(1, 101), ran.nextDouble(1, 101), 25, 0, this.radius, 10));
		}
	}
	
	//Update Method
	public void update() 
	{	
		if(this.frameCount >= this.frameAmt) {
			System.out.println("Simulation is now complete, and is exiting");
			this.isDone = true;
			return;
		} else {
			this.frameCount++;
		}
		
		this.spawner(); //Can be used to create different simulations

		this.threadManager.collisionPreperation(list); //Updates multiple particles simultanously

		for(Particle particle : this.list) {
			partition.addParticle(particle);
		}

		//Multithread Collision
		this.threadManager.processCollisions(list, partition);
		this.partition.clear();

		synchronized (list) { //Synchronizes all calls for list
			this.list.addAll(this.listBuffer);
			this.listBuffer.clear();
		}
	}
}