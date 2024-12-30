import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Model
{
	//Private variables for Model class
	private ArrayList<Particle> list;
	//private Particle buffer;
	private int cellSize;
	private static final double EPSILON = 1e-8;
	private static final double COLLISION_BUFFER = 0.85;
	

	//Constructor Method
	
	public Model()
	{
		Random ran = new Random();
		this.list = new ArrayList<Particle>();


		int radius = 3;


		this.cellSize = radius*2;

		for(int i = 0; i < 10000; i++)
		{
			Particle temp = new Particle(ran.nextDouble(5, 995), ran.nextDouble(5, 995), 0, 0, radius, 10, 0, 0);
			this.list.add(temp);
		}

		
	}

	// private void applyFriction(Particle p) 
	// {
		
	// }

	private double collisionBuffer(double velocity)
	{
		return (velocity * COLLISION_BUFFER);
	}

	private void handleWallCollision(Particle p)
	{
		//Wall Collision
		if(p.getY() + p.getRadius() > 1000) { //Bottom Wall
			p.setY(1000 - p.getRadius() - EPSILON);
			p.setYVelocity(this.collisionBuffer(p.getYVelocity() * -1));

		} 
		else if(p.getY() - p.getRadius() < 0) { //Top Wall
			p.setY(p.getRadius() + EPSILON);
			p.setYVelocity(this.collisionBuffer(p.getYVelocity() * -1));
		}

		if(p.getX() + p.getRadius() > 1000) { //Right Wall
			p.setX(1000 - p.getRadius() - EPSILON);
			p.setXVelocity(this.collisionBuffer(p.getXVelocity() * -1));
		} 
		else if(p.getX() - p.getRadius() < 0) { //Left Wall
			p.setX(p.getRadius() + EPSILON);
			p.setXVelocity(this.collisionBuffer(p.getXVelocity() * -1));
		}
	}

	private boolean checkCollision(Particle p1, Particle p2)
	{
		double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= p1.getRadius() + p2.getRadius() - EPSILON) {
			//System.out.println("Collision");
			return true;
		}

		return false;
	}

	private void resolveCollision(Particle p1, Particle p2) {
		// Calculate the distance between the two particles
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();
		double distance = Math.sqrt(dx * dx + dy * dy);
	
		// Avoid division by zero
		if (distance == 0) {
			distance = EPSILON; // Small value to prevent singularities
		}
	
		// Check if particles are overlapping
		double overlap = p1.getRadius() + p2.getRadius() - distance;
		if (overlap <= 0) {
			return; // No collision
		}
	
		// Normalize the collision vector
		double nx = dx / distance;
		double ny = dy / distance;
	
		// Separate particles to resolve overlap
		double totalMass = p1.getMass() + p2.getMass();
		double p1Shift = overlap * (p2.getMass() / totalMass);
		double p2Shift = overlap * (p1.getMass() / totalMass);
	
		p1.setX(p1.getX() - nx * p1Shift);
		p1.setY(p1.getY() - ny * p1Shift);
		p2.setX(p2.getX() + nx * p2Shift);
		p2.setY(p2.getY() + ny * p2Shift);
	
		// Compute relative velocity
		double rvx = p2.getXVelocity() - p1.getXVelocity();
		double rvy = p2.getYVelocity() - p1.getYVelocity();
	
		// Compute relative velocity along the collision normal
		double normalVelocity = rvx * nx + rvy * ny;
	
		// If particles are moving apart, no collision resolution needed
		if (normalVelocity > 0) {
			return;
		}
	
		// Compute restitution (elastic collision coefficient, 1 for perfectly elastic)
		double restitution = 1.0; // Adjust for inelastic collisions if needed
	
		// Impulse scalar
		double impulse = (-(1 + restitution) * normalVelocity) / (1 / p1.getMass() + 1 / p2.getMass());
	
		// Apply impulse to both particles
		double impulseX = impulse * nx;
		double impulseY = impulse * ny;
	
		p1.setXVelocity(p1.getXVelocity() - impulseX / p1.getMass());
		p1.setYVelocity(p1.getYVelocity() - impulseY / p1.getMass());
		p2.setXVelocity(p2.getXVelocity() + impulseX / p2.getMass());
		p2.setYVelocity(p2.getYVelocity() + impulseY / p2.getMass());
	}
	

	//Update Method called in Game

	public ArrayList<Particle> getParticles()
	{ 
		return this.list;
	}

	public void update() 
	{
		SpatialPartitioning partition = new SpatialPartitioning(this.cellSize);

		for(Particle p : this.list) {
			p.update();
		}

		for(Particle p : this.list) {
			this.handleWallCollision(p);
			partition.addParticle(p);
		}

		//Collision Check
		HashSet<Pair<Particle, Particle>> resolvedPairs = new HashSet<>(); //Makes sure two particles aren't checked twice

		for(Iterator<Particle> iterator = list.iterator(); iterator.hasNext();)
		{
			Particle p = iterator.next();
			ArrayList<Particle> nearbyParticles = partition.getNearby(p);
			
			for(Iterator<Particle> neighboring = nearbyParticles.iterator(); neighboring.hasNext();)
			{
				Particle neighbor = neighboring.next(); 

				if(neighbor != p && this.checkCollision(p, neighbor)) {
					Pair<Particle, Particle> pair = new Pair<>(p, neighbor);

					if(!resolvedPairs.contains(pair)) {
						this.resolveCollision(p, neighbor);
						resolvedPairs.add(pair);
					}
				}
					
			}
		}

	}
}