import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpatialPartitioning 
{
    //Private partioning variables
    private int cellSize;
    private Map<Point, ArrayList<Particle>> grid;

    //Constructor
    public SpatialPartitioning(int cellSize) 
    {
        this.cellSize = cellSize;
        this.grid = new HashMap<>();
    }

    //Adds new particle based on cell
    public synchronized void addParticle(Particle particle)
    {
        Point cell = this.getCell(particle);
        
        this.grid.putIfAbsent(cell, new ArrayList<>());
        this.grid.get(cell).add(particle);
    }

    //Get Methods
    //Method for finding any other possible collisions of particles not in same cell
    public ArrayList<Particle> getNearby(Particle particle) 
    {
        ArrayList<Particle> neighbors = new ArrayList<>();
        Point cell = getCell(particle);

        for(int x = -1; x <= 1; x++)
        {
            for(int y = -1; y <= 1; y++)
            {
                Point neighborCell = new Point(cell.x + x, cell.y + y);

                if(this.grid.containsKey(neighborCell)) {
                    neighbors.addAll(grid.get(neighborCell));
                }
            }
        }

        return neighbors;
    }

    //Returns all cells
    public synchronized ArrayList<ArrayList<Particle>> getAllCells()
    {
        ArrayList<ArrayList<Particle>> cells = new ArrayList<>();

        for (Point key : this.grid.keySet()) {
            cells.add(this.grid.get(key));
        }

        return cells;
    }

    //Returns neighboring particles based on cell
    public ArrayList<ArrayList<Particle>> getNeighbors(int cellIndex) {
        ArrayList<ArrayList<Particle>> neighbors = new ArrayList<>();
        int numCellsPerRow = Simulator.FRAME_SIZE / this.cellSize;
    
        //Calculate row and column of the current cell
        int row = cellIndex / numCellsPerRow;
        int col = cellIndex % numCellsPerRow;
    
        //Iterate over neighboring cells
        for (int x = -1; x <= 1; x++) { // Rows
            for (int y = -1; y <= 1; y++) { // Columns
                int neighborRow = row + x;
                int neighborCol = col + y;
    
                //Check if the neighbor is within bounds
                if (this.isValidCell(neighborRow, neighborCol, numCellsPerRow)) {
                    Point neighborCell = new Point(neighborCol, neighborRow);
    
                    //Add particles from the neighboring cell if it exists in the grid
                    if (this.grid.containsKey(neighborCell)) {
                        neighbors.add(this.grid.get(neighborCell));
                    }
                }
            }
        }
    
        return neighbors;
    }

    private Point getCell(Particle particle) //Finds which cell the particle is in
    {
        int cellX = (int) (particle.x() / this.cellSize);
        int cellY = (int) (particle.y() / this.cellSize);

        return (new Point(cellX, cellY));
    }

    //Makes sure there are no out of bounds exceptions
    private boolean isValidCell(int row, int col, int numCellsPerRow) {
        return row >= 0 && row < numCellsPerRow && col >= 0 && col < numCellsPerRow;
    }

    //Clears grid
    public void clear() 
    {
        grid.clear();
    }
}
