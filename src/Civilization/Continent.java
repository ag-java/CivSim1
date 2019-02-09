package Civilization;
import java.awt.Graphics;
import java.awt.Color;

public class Continent{
	private static final int BLANK=-2;
	static final int WATER=-1;
	static final int foodMax=2000;
	int[][] terrain=new int[Main.dimension][Main.dimension];
	private int[][] seeds=new int[Main.dimension][3];
	public Continent(){
		setupTerrain();
	}
	private void setupTerrain(){
		for(int x=0;x<terrain.length;x++){
			for(int y=0;y<terrain[x].length;y++){
				terrain[x][y]=BLANK;
			}
		}
		setAltitudeSeeds();
		raiseContinent();
	}
	private void setAltitudeSeeds(){
		for(int a=0;a<Main.dimension;a++){
			int x=Main.random(Main.dimension);
			int y=Main.random(Main.dimension);
			if(Main.random(5)>=2){
				terrain[x][y]=Main.random(foodMax/2,(3*foodMax)/4);
			}else{
				terrain[x][y]=WATER;
			}
			seeds[a]=new int[]{x,y,terrain[x][y]};
		}
	}
	private void raiseContinent(){
		for(int x=0;x<terrain.length;x++){
			for(int y=0;y<terrain[x].length;y++){
				if(terrain[x][y]==BLANK){
					terrain[x][y]=getSeedValue(x,y);
				}
			}
		}
		seeds=null;
	}
	private int getSeedValue(int x,int y){
		double dis=distance(seeds[0][0],seeds[0][1],x,y);
		int value=seeds[0][2];
		for(int a=1;a<seeds.length;a++){
			double d=distance(seeds[a][0],seeds[a][1],x,y);
			if(d<dis){
				dis=d;
				value=seeds[a][2];
			}
		}
		if(value>WATER){
			value+=Math.round(dis*50);
			if(value>foodMax){
				return foodMax;
			}
		}
		return value;
	}
	public void draw(Graphics g){
		for(int x=0;x<terrain.length;x++){
			for(int y=0;y<terrain[x].length;y++){
				if(terrain[x][y]==WATER){
					square(g,x,y,Color.BLUE);
				}else if(terrain[x][y]>WATER){
					square(g,x,y,getGrassColor(terrain[x][y]));
				}
			}
		}
	}
	private Color getGrassColor(int food){
		return new Color(55,55+((200*food)/foodMax),55);
	}
	private double distance(int x,int y,int x1,int y1){
		return Math.sqrt(Math.pow(x-x1,2)+Math.pow(y-y1,2));
	}
	private void square(Graphics g,int x,int y,Color color){
		g.setColor(color);
		g.fillRect(x*Main.unitSize,y*Main.unitSize,Main.unitSize,Main.unitSize);
	}
}