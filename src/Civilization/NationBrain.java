package Civilization;
import java.awt.Color;

public class NationBrain{
	private static final int highLandFood=1000;
	private static final int highCityFood=250;
	private static final int highCityPop=500;
	private static final int highArmyFood=50;
	private static final int highArmyPop=100;
	static final int VISION=2;
	private int[][] synapses=new int[11][/*9*/8];
	Color color;
	private int outIndex;
	int result=0;
	public NationBrain(Color color){
		this.color=color;
		for(int a=0;a<synapses.length;a++){
			for(int b=0;b<synapses[a].length;b++){
				synapses[a][b]=newSynapse();
			}
		}
	}
	private int newSynapse(){
		return Main.random(-2,2);
	}
	public int leadNation(){
		int cities=0;
		for(int a=0;a<Main.main.cities.size();a++){
			City city=Main.main.cities.get(a);
			if(city.color==color){
				cities++;
				//if(cities<=600){
					if(city.pop>0 && city.food>0){
						leadCity(city);
					}
					for(int b=0;b<city.armies.size();b++){
						leadArmy(city.armies.get(b));
					}
				/*}else{
					city.food=0;
					city.pop=0;
				}*/
			}
		}
		return cities;
	}
	private void leadCity(City city){
		int[] connections=blank();
		calculateConnections(connections,city,highCityFood,highCityPop);
		if(connections[1]>=connections[0] && connections[1]>=connections[2]/* && connections[1]>=connections[8]*/){
			outIndex=1;
			result+=city.createArmy(100,100);
		}else if(connections[2]>=connections[0] && connections[2]>=connections[1]/* && connections[2]>=connections[8]*/){
			outIndex=2;
			result+=city.increasePop(10);
		}/*else if(connections[8]>=connections[0] && connections[8]>=connections[1] && connections[8]>=connections[2]){
			outIndex=8;
			Main.main.newCity(color);
			result++;
		}*/else{
			outIndex=0;
		}
		if(city.pop<100){
			result--;
		}
		if(city.food<highCityFood){
			result--;
		}
		resultsAnalysis();
	}
	private void leadArmy(Army army){
		int[] connections=blank();
		calculateConnections(connections,army,highArmyFood,highArmyPop);
		int h=3;
		for(int a=4;a<connections.length/*-1*/;a++){
			if(connections[a]>connections[h]){
				h=a;
			}
		}
		outIndex=h;
		if(h==3){
			result+=army.foundCity(army.x, army.y);
		}else if(h==4){
			result+=army.lookForFood();
		}else if(h==5){
			result+=army.lookFor(true, true);
		}else if(h==6){
			result+=army.lookFor(false, true);
		}else if(h==7){
			result+=army.lookFor(true, false);
		}
		if(army.food<=0 || army.pop<=0){
			result--;
		}
		resultsAnalysis();
	}
	private void resultsAnalysis(){
		if(result<=0){
			for(int a=0;a<synapses.length;a++){
				synapses[a][outIndex]=newSynapse();
			}
		}
		for(result=Math.abs(result);result>0;result--){
			synapses[Main.random(synapses.length)][Main.random(synapses[0].length)]=newSynapse();
		}
	}
	private void add(int[] array,int[] array1){
		for(int a=0;a<array.length;a++){
			array[a]+=array1[a];
		}
	}
	private void calculateConnections(int[] connections,Population pop,int highFood,int highPop){
		add(connections,higherCompare(Main.main.land.terrain[pop.x][pop.y],highLandFood,0));
		add(connections,lowerCompare(Main.main.land.terrain[pop.x][pop.y],highLandFood,1));
		add(connections,richLandNearby(pop.x,pop.y));
		add(connections,higherCompare(pop.food,highFood,3));
		add(connections,higherCompare(pop.pop,highPop,4));
		add(connections,lowerCompare(pop.food,highFood,5));
		add(connections,lowerCompare(pop.pop,highPop,6));
		add(connections,popNearby(pop,true,true,7));
		add(connections,popNearby(pop,true,false,8));
		add(connections,popNearby(pop,false,true,9));
		add(connections,popNearby(pop,false,false,10));
	}
	private int[] richLandNearby(int x,int y){
		for(int a=-VISION;a<=VISION;a++){
			for(int b=-VISION;b<=VISION;b++){
				if(!(a==0 && b==0)){
					try{
						if(Main.main.land.terrain[x+a][y+b]>highLandFood){
							return synapses[2];
						}
					}catch(ArrayIndexOutOfBoundsException e){}
				}
			}
		}
		return blank();
	}
	private int[] higherCompare(int num,int num1,int synapse){
		if(num>num1){
			return synapses[synapse];
		}
		return blank();
	}
	private int[] lowerCompare(int num,int num1,int synapse){
		if(num>num1){
			return synapses[synapse];
		}
		return blank();
	}
	private int[] popNearby(Population pop,boolean isCity,boolean friendly,int synapse){
		for(int a=-VISION;a<=VISION;a++){
			for(int b=-VISION;b<=VISION;b++){
				if(!(a==0 && b==0)){
					Population p=Main.main.popExists(pop.x+a,pop.y+b);
					if(p!=null && (p.getClass()==City.class)==isCity && pop.friendly(p)){
						return synapses[synapse];
					}
				}
			}
		}
		return blank();
	}
	private int[] blank(){
		return new int[]{0,0,0,0,0,0,0,0/*,0*/};
	}
}