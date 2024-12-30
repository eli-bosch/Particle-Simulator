import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpatialPartitioning 
{
    private int cellSize;
    private Map<Point, ArrayList<Particle>> grid;

    public SpatialPartitioning(int cellSize) 
    {
        this.cellSize = cellSize;
        this.grid = new HashMap<>();
    }

    public void addParticle(Particle particle) //Adds the particle to its correct cell
    {
        Point cell = this.getCell(particle);
        
        this.grid.putIfAbsent(cell, new ArrayList<>());
        this.grid.get(cell).add(particle);
    }

    public ArrayList<Particle> getNearby(Particle particle) 
    {
        ArrayList<Particle> neighbors = new ArrayList<>();
        Point cell = getCell(particle);

        //System.out.println(cell.toString());

        for(int x = -1; x <= 1; x++)
        {
            for(int y = -1; y <= 1; y++)
            {
                Point neighborCell = new Point(cell.x + x, cell.y + y);
                // System.out.println(this.grid.size());

                if(this.grid.containsKey(neighborCell)) {
                    neighbors.addAll(grid.get(neighborCell));
                }
            }
        }

        return neighbors;
    }

    public void clear() 
    {
        grid.clear();
    }

    private Point getCell(Particle particle) //Finds which cell the particle is in
    {
        int cellX = (int) (particle.getX() / this.cellSize);
        int cellY = (int) (particle.getY() / this.cellSize);

        //System.out.println(cellX + ", " + cellY);

        return (new Point(cellX, cellY));
    }
}
