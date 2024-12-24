::@echo off
javac Simulator.java View.java Controller.java Model.java Json.java Particle.java Frame.java Simulation.java

if %errorlevel% neq 0 (
	echo There was an error; exiting now.	
) else (
	echo Compiled correctly!  Running Simulator...
	java Simulator	
)
