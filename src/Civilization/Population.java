package Civilization;
import java.awt.Color;

public abstract class Population{
	int food,pop,x,y;
	Color color;
	public boolean friendly(Population p){
		return (color==p.color);
	}
	public boolean alive(){
		return(food>0 && pop>0);
	}
	public abstract void nextFrame();
}
