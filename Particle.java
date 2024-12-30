import java.awt.Color;
import java.awt.Graphics;

public class Particle 
{
    //Private Variables
    private int radius;
    private double x, y, xVelocity, yVelocity, xAcceleration, yAcceleration, mass;

    private final double gravity = .5; //FIX: Move gravity to model

    //Constructor Method

    Particle(double x, double y, double xVelocity, double yVelocity, int radius, double mass, double xAcceleration, double yAcceleration)
    {
        //JFrames have the Y-Axis flipped
        this.x = x;
        this.y = y;

        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;

        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;

        this.radius = radius;
        this.mass = mass;

        //this.atRest = false;
    }

    //Getter Methods

    public double getX()
    {
        return this.x;
    }

    public double getY()
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

    public double getYVelocity()
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
    
    public int getRadius()
    {
        return this.radius;
    }

    public double getMass()
    {
        return this.mass;
    }

    //Setter Methods

    public void setX(double x)
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

    public void update()
    {
        this.handleGravity();
        this.handleMovement();
        //this.handleRest();
    }

    //Handle Methods

    private void handleMovement()
    {
        this.x += this.xVelocity;
        this.y += this.yVelocity; 
        //System.out.println("X-Velocity: " + this.xVelocity + ", " + "Y-Velocity: " + this.yVelocity + ", y = " + this.y + ", x = " + this.x);
    }

    private void handleGravity()
    {
       this.yVelocity += this.gravity;
    }

    //Draw Method
    public void draw(Graphics g)
    {
        g.fillOval((this.getRenderX() - this.radius), (this.getRenderY() - this.radius), this.radius*2, this.radius*2);
    }

    @Override
    public String toString()
    {
        return "Particle at (" + this.x + ", " + this.y + ") with speed " + this.xVelocity + ", " + this.yVelocity + " px per frame";
    }

}
