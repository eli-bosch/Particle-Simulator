import java.util.ArrayList;
//Used to store the data for all of the objects in an individual frame

public class Simulation 
{
    //ArrayList and Frame Number
    private ArrayList<Frame> simulation;
    private String title;

    //Constructor
    public Simulation(String title) //For the starting the ArrayList off
    {
        this.title = title;
        this.simulation = new ArrayList<Frame>();
    }

    //Getter Methods

    public String getTitle()
    {
        return this.title;
    }

    public ArrayList getSimluation()
    {
        return this.simulation;
    }

    //Setter Methods

    public void setFrame(Frame frame)
    {
        simulation.add(frame);
    }
}
