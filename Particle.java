/*
 * Eli Bosch, Junior at the University of Arkansas, Computer Science, 1/1/25
 */

import java.awt.Graphics;

public class Particle 
{
    //Private Variables
    private int radius;
    private double x, y, xVelocity, yVelocity, mass, xAcceleration, yAcceleration;

    //Constructor Method

    Particle(double x, double y, double xVelocity, double yVelocity, int radius, double mass) //Particles store their own information
    {
        //JFrames have the Y-Axis flipped
        this.x = x;
        this.y = y;

        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;

        this.xAcceleration = 0; //Not utilized but easy to adapt
        this.yAcceleration = 0;

        this.radius = radius;
        this.mass = mass;

        /* Other features that can be adapted to this simulation: 
         * Temperature, Gravitational, Magnetism, and more */
    }

    //Getter Methods
    public double x()
    {
        return this.x;
    }

    public double y()
    {
        return this.y;
    }

    public int getRenderX()
    {
        return (int) Math.round(this.x);
    }

    public int getRenderY()
    {
        return (int) Math.round(this.y);
    }

    public double getXVelocity()
    {
        return this.xVelocity;
    }

    public double yVelocity()
    {
        return this.yVelocity;
    }

    public double getXAcceleration()
    {
        return this.xAcceleration;
    }

    public double getYAcceleration()
    {
        return this.yAcceleration;
    }
    
    public int radius()
    {
        return this.radius;
    }

    public double getMass()
    {
        return this.mass;
    }

    //Setter Methods
    public void x(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public void setXVelocity(double xVelocity)
    {
        this.xVelocity = xVelocity;
    }

    public void setYVelocity(double yVelocity)
    {
        this.yVelocity = yVelocity;
    }

    public void setXAcceleration(double xAcceleration)
    {
        this.xAcceleration = xAcceleration;
    }

    public void setYAcceleration(double yAcceleration)
    {
        this.yAcceleration = yAcceleration;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }

    public void setMass(double mass)
    {
        this.mass = mass;
    }

    //Update Method
    public void update() //Called every frame
    {
        this.handleGravity();
        this.handleMovement();
        this.handleWallCollision();
    }

    //Handle Methods
    private void handleMovement()
    {
        this.x += this.xVelocity;
        this.y += this.yVelocity; 
    }

    private void handleGravity()
    {
       this.yVelocity += Model.GRAVITY;
    }

    private void handleWallCollision()
    {
        if(this.y() + this.radius > 1000) { //Bottom Wall
			this.y = (1000 - this.radius - Model.EPSILON);
			this.yVelocity = (Model.COLLISION_BUFFER * (this.yVelocity * -1));

		} 
		else if(this.y() - this.radius < 0) { //Top Wall
			this.y = (this.radius + Model.EPSILON);
			this.yVelocity = (Model.COLLISION_BUFFER * (this.yVelocity * -1));
		}

		if(this.x() + this.radius > 1000) { //Right Wall
			this.x = (1000 - this.radius - Model.EPSILON);
			this.xVelocity = (Model.COLLISION_BUFFER * (this.xVelocity * -1));
		} 
		else if(this.x() - this.radius < 0) { //Left Wall
			this.x = (this.radius + Model.EPSILON);
			this.xVelocity = (Model.COLLISION_BUFFER * (this.xVelocity * -1));
        }
    }

    //Draw Method
    public void draw(Graphics g)
    {
        g.fillOval((this.getRenderX() - this.radius), (this.getRenderY() - this.radius), this.radius*2, this.radius*2);
    }

    //Debugging Method
    @Override
    public String toString()
    {
        return "Particle at (" + this.x + ", " + this.y + ") with speed " + this.xVelocity + ", " + this.yVelocity + " px per frame";
    }

}
