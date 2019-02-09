package Civilization;

public class SSRNG{
	//private double current;
	public SSRNG(double seed){
		//current=seed;
	}
	public double next(){//0 to 1 (exclusive)
		/*current=(Math.pow(current,2)*1.5)+(current*0.5)-0.02+Math.sqrt(current);
		current=current%1;
		while(current<0){
			current++;
		}
		return current;*/
		return Math.random();//alpha rig
	}
}