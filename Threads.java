/*
 * Eli Bosch, Junior at the University of Arkansas, Computer Science, 1/1/25
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Threads {

    //Private Threads variables
    private int numThreads;

    //Private class references
    private ExecutorService threadPool;

    // Constructor
    public Threads() {
        this.numThreads = Runtime.getRuntime().availableProcessors() - 2; // Reserve 2 threads for other tasks
        this.threadPool = Executors.newFixedThreadPool(this.numThreads);
    }

    //Batch updates the particle list
    public void collisionPreperation(ArrayList<Particle> particles)
    {
        ArrayList<Runnable> tasks = new ArrayList<>();

        int particleChunk = (int) Math.ceil((double) particles.size() / this.numThreads);

        for(int i = 0; i < this.numThreads; i++) {
            final int start = i * particleChunk;
            final int end = Math.min(start + particleChunk, particles.size());

            tasks.add(() -> {
                for(int p = start; p < end; p++) {
                    Particle particle = particles.get(p);
                    particle.update();
                }
            });
        }

        try {
            for (Runnable task : tasks) {
                threadPool.submit(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Processes particle collisions with threads
    public void processCollisions(ArrayList<Particle> particles, SpatialPartitioning partition) {
        ArrayList<Runnable> tasks = new ArrayList<>();
        ArrayList<ArrayList<Particle>> cells = partition.getAllCells();

        int chunkSize = (int) Math.ceil((double) cells.size() / this.numThreads); //Dynamic chunk allocation based on particle density would be better

        for (int i = 0; i < this.numThreads; i++) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, cells.size());

            //Processes the chunks
            tasks.add(() -> {
                for (int cellIndex = start; cellIndex < end; cellIndex++) {
                    ArrayList<Particle> cell = cells.get(cellIndex);
                    HashSet<Pair<Particle, Particle>> localResolvedPairs = new HashSet<>();

                    //Same cell collision
                    for (int j = 0; j < cell.size(); j++) {
                        Particle p1 = cell.get(j);
                        for (int k = j + 1; k < cell.size(); k++) {
                            Particle p2 = cell.get(k);
                            processPair(p1, p2, localResolvedPairs);
                        }
                    }

                    //Collision with neighboring cells
                    ArrayList<ArrayList<Particle>> neighbors = partition.getNeighbors(cellIndex);
                    for (ArrayList<Particle> neighborCell : neighbors) {
                        for (Particle p1 : cell) {
                            for (Particle p2 : neighborCell) {
                                processPair(p1, p2, localResolvedPairs);
                            }
                        }
                    }
                }
            });
        }

        //Submit tasks to thread pool
        try {
            for (Runnable task : tasks) {
                threadPool.submit(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Processes a pair of particles
    private void processPair(Particle p1, Particle p2, HashSet<Pair<Particle, Particle>> localResolvedPairs) {
        if (p1 == p2) return; //Avoid self-collision

        Pair<Particle, Particle> pair = new Pair<>(p1, p2);

            if (localResolvedPairs.contains(pair)) return; //Skip already resolved pairs
            localResolvedPairs.add(pair);

        //Check collision and resolve if necessary
        if (checkCollision(p1, p2)) {
            synchronized (p1) {
                synchronized (p2) {
                    resolveCollision(p1, p2);
                }
            }
        }
    }

    //Checks if two particles are colliding
    private boolean checkCollision(Particle p1, Particle p2) {
        double dx = p2.x() - p1.x();
        double dy = p2.y() - p1.y();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance <= p1.radius() + p2.radius() - Model.EPSILON;
    }

    //Resolves collision between two particles
    private void resolveCollision(Particle p1, Particle p2) {
        double dx = p2.x() - p1.x();
        double dy = p2.y() - p1.y();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) distance = Model.EPSILON;

        double overlap = p1.radius() + p2.radius() - distance;
        if (overlap <= 0) return;

        double nx = dx / distance;
        double ny = dy / distance;

        //Separate particles to resolve overlap
        double totalMass = p1.getMass() + p2.getMass();
        double p1Shift = overlap * (p2.getMass() / totalMass);
        double p2Shift = overlap * (p1.getMass() / totalMass);

        p1.x(p1.x() - nx * p1Shift);
        p1.setY(p1.y() - ny * p1Shift);
        p2.x(p2.x() + nx * p2Shift);
        p2.setY(p2.y() + ny * p2Shift);

        //Compute relative velocity
        double rvx = p2.getXVelocity() - p1.getXVelocity();
        double rvy = p2.yVelocity() - p1.yVelocity();

        //Compute relative velocity along the collision normal
        double normalVelocity = rvx * nx + rvy * ny;

        if (normalVelocity > 0) return;

        //Compute impulse
        double impulse = (-(1 + Model.COLLISION_BUFFER) * normalVelocity) / (1 / p1.getMass() + 1 / p2.getMass());

        double impulseX = impulse * nx;
        double impulseY = impulse * ny;

        //Apply impulse to both particles
        p1.setXVelocity(p1.getXVelocity() - impulseX / p1.getMass());
        p1.setYVelocity(p1.yVelocity() - impulseY / p1.getMass());
        p2.setXVelocity(p2.getXVelocity() + impulseX / p2.getMass());
        p2.setYVelocity(p2.yVelocity() + impulseY / p2.getMass());
    }

    //Shuts down the thread pool
    public void shutdown() {
        if (!threadPool.isShutdown()) {
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
