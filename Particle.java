import java.awt.Color;
import java.awt.Graphics;

public class Particle 
{
    //Private Variables
    private int x, y, radius;
    private double xVelocity, yVelocity, xAcceleration, yAcceleration, mass;
    private boolean atRest;

    private final int gravity = 1;
    private final double velocityBuffer = 0.9;
    private final double bufferSlowing = 0.8;
    private final double gravityBuffer = 0.9;

    //Constructor Method

    Particle(int x, int y, double xVelocity, double yVelocity, int radius, double mass, double xAcceleration, double yAcceleration)
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

        this.atRest = false;
    }

    //Getter Methods

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
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

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setXVeloctiy(double xVelocity)
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
        //this.handleVelocityBuffer();
        this.handleMovement();
        this.handleRest();
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
        if(this.atRest) {
            return;
        }

        if(this.yVelocity < 0) {
            this.yVelocity += this.gravity;
        } else if(this.yVelocity >= 0) {
            this.yVelocity += this.gravity * this.gravityBuffer;
        }
    }

    public void handleParticleCollision(Particle particle) {
        // Distance between the two particles
        double dx = particle.getX() - this.getX();
        double dy = particle.getY() - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
    
        // Check if the particles are colliding
        if (distance <= this.radius + particle.getRadius()) {
            // Unit normal and tangent vectors
            double nx = dx / distance; // Normalized collision vector
            double ny = dy / distance;
            double tx = -ny; // Tangential vector
            double ty = nx;
    
            // Velocity components along the normal and tangent
            double v1n = this.xVelocity * nx + this.yVelocity * ny;
            double v1t = this.xVelocity * tx + this.yVelocity * ty;
            double v2n = particle.getXVelocity() * nx + particle.getYVelocity() * ny;
            double v2t = particle.getXVelocity() * tx + particle.getYVelocity() * ty;
    
            // New normal velocities after collision (elastic collision)
            double v1nPrime = (v1n * (this.mass - particle.getMass()) + 2 * particle.getMass() * v2n) / (this.mass + particle.getMass());
            double v2nPrime = (v2n * (particle.getMass() - this.mass) + 2 * this.mass * v1n) / (this.mass + particle.getMass());
    
            // Tangential velocities remain unchanged
            double v1tPrime = v1t;
            double v2tPrime = v2t;
    
            // Convert scalar normal and tangential velocities to x and y components
            this.xVelocity = v1nPrime * nx + v1tPrime * tx;
            this.yVelocity = v1nPrime * ny + v1tPrime * ty;
            particle.setXVeloctiy(v2nPrime * nx + v2tPrime * tx);
            particle.setYVelocity(v2nPrime * ny + v2tPrime * ty);
    
            // Separate overlapping particles slightly to avoid sticking
            double overlap = this.radius + particle.getRadius() - distance;
            this.x -= (int) (overlap * nx / 2);
            this.y -= (int) (overlap * ny / 2);
            particle.setX(particle.getX() + (int) (overlap * nx / 2));
            particle.setY(particle.getY() + (int) (overlap * ny / 2));
            this.atRest = false;
            //System.out.println("Collision");
        }
    }
    

    public void handleWallCollision(String wall)
    {
        switch(wall) {
            case "top": {
                this.y = this.radius;
                this.yVelocity = (this.yVelocity < 0) ? this.yVelocity*-1 : this.yVelocity;
                this.handleCollisionBuffer(true);
                break;
            }
            case "right": {
                this.x = 1000 - this.radius;
                this.xVelocity = (this.xVelocity > 0) ? this.xVelocity*-1 : this.xVelocity;
                this.handleCollisionBuffer(false);
                break;
            }
            case "bottom": {
                this.y = 1000 - this.radius;
                this.yVelocity = (this.yVelocity > 0) ? this.yVelocity*-1 : this.yVelocity;
                this.handleCollisionBuffer(true);
                break;
            }
            case "left": {
                this.x = this.radius;
                this.xVelocity = (this.xVelocity < 0) ? this.xVelocity*-1 : this.xVelocity;
                this.handleCollisionBuffer(false);
                break;
            }
            default: {
                System.out.println("There is an error in wall type");
                break;
            }
        }
    }

    private void handleCollisionBuffer(Boolean verticle) //Simulates slight loss of energy every collsion
    {
        if(verticle) {
            this.yVelocity *= this.velocityBuffer;
        } else {
            this.xVelocity *= this.velocityBuffer;
        }
    }

    private void handleRest() //Since every collision in elastic the system would never come to rest
    {
        if(this.y + this.radius >= 990 && (this.yVelocity < 7.6 && this.yVelocity > -7.6)) {
            this.yVelocity *= this.bufferSlowing;

            if(this.yVelocity < 1.5 && this.yVelocity > -1.5) {
                this.yVelocity = 0;
                this.atRest = true;
            }
        }

        if(this.yVelocity == 0 && this.y == (1000 - this.radius)) {
            this.xVelocity *= (this.bufferSlowing + 0.175); //Simulates friction
            if(this.xVelocity < 0.01 && this.xVelocity > -0.01) { //FIX: Looks bad in simulation
                this.xVelocity = 0;
            }
        }

        
    }

    //Draw Method
    public void draw(Graphics g)
    {
        g.setColor(new Color(255, 0, 0));
        g.fillOval((this.x - this.radius), (this.y - this.radius), this.radius*2, this.radius*2);
    }

}
