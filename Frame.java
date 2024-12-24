import java.util.ArrayList;
//Used to store the data for all of the objects in an individual frame

public class Frame 
{
    //ArrayList and Frame Number
    private ArrayList<Particle> frame;
    private int frameNumber;

    //Constructor
    public Frame(int frameNumber) //For the starting the ArrayList off
    {
        this.frameNumber = frameNumber;
        this.frame = new ArrayList<Particle>();
    }

    //Getter Methods

    public int getFrameNumber()
    {
        return this.frameNumber;
    }

    public ArrayList getObjects()
    {
        return this.frame;
    }

    //Setter Methods

    public void setObject(Particle ob)
    {
        frame.add(ob);
    }
}
