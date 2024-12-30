::@echo off
javac -d bin Simulator.java Model.java Particle.java SpatialPartitioning.java View.java Pair.java

if %errorlevel% neq 0 (
	echo There was an error; exiting now.	
) else (
	echo Compiled correctly!  Running Simulator...
	java -cp bin Simulator	
)
