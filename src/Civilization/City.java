package Civilization;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class City extends Population{
	ArrayList<Army> armies=new ArrayList<Army>();
	Color outline=Color.BLACK;
	public City(int x,int y,Color color){
		this.x=x;
		this.y=y;
		this.color=color;
		pop=50;
		food=1000;
		if(color==outline) outline=Color.WHITE;
	}
	public void draw(Graphics g){
		if(food>0 && pop>0){
			g.setColor(color);
			g.fillRect(x*Main.unitSize,y*Main.unitSize,Main.unitSize,Main.unitSize);
			g.setColor(outline);
			g.drawRect(x*Main.unitSize,y*Main.unitSize,Main.unitSize,Main.unitSize);
		}
		for(int a=0;a<armies.size();a++){
			g.setColor(color);
			g.fillOval(armies.get(a).x*Main.unitSize,armies.get(a).y*Main.unitSize,Main.unitSize,Main.unitSize);
			g.setColor(outline);
			g.drawOval(armies.get(a).x*Main.unitSize,armies.get(a).y*Main.unitSize,Main.unitSize,Main.unitSize);
		}
		/*if(color==Color.BLACK){
			g.setColor(Color.WHITE);
		}else{
			g.setColor(Color.BLACK);
		}*/
		/*if(food>0 && pop>0){
			g.drawRect(x*Main.unitSize,y*Main.unitSize,Main.unitSize,Main.unitSize);
		}
		for(int a=0;a<armies.size();a++){
			g.drawOval(armies.get(a).x*Main.unitSize,armies.get(a).y*Main.unitSize,Main.unitSize,Main.unitSize);
		}*/
	}
	private void harvest(){
		if(Main.main.land.terrain[x][y]>0){
			int amount=(int)Math.floor(pop/10);
			food+=amount;
			Main.main.land.terrain[x][y]-=amount;
			if(Main.main.land.terrain[x][y]<=0){
				Main.main.land.terrain[x][y]=0;
			}
		}else{
			food-=100;//pop;
			if(food<0){
				food=0;
			}
		}
	}
	public int increasePop(int pop){
		if(food>pop*5){
			this.pop+=pop;
			useFood(pop*5);
			return 1;
		}
		return -1;
	}
	public int createArmy(int pop,int food){
		if(this.pop>pop && this.food>food && armies.size()<2){
			int[][] options=new int[8][2];
			int o=0;
			for(int a=-1;a<=1;a++){
				for(int b=-1;b<=1;b++){
					if(x+a>0 && x+a<Main.dimension && y+b>0 && y+b<Main.dimension){
						if(!(a==0 && b==0) && Main.main.popExists(x+a, y+b)==null){
							options[o][0]=a;
							options[o][1]=b;
							o++;
						}
					}
				}
			}
			if(o>0){
				int[] offset=options[Main.random(o)];
				armies.add(new Army(x+offset[0],y+offset[1],color,pop,food));
				this.pop-=pop;
				useFood(food);
				return 1;
			}
		}
		return -1;
	}
	private void useFood(int f){
		food-=f;
		if(food<0){
			food=0;
		}
	}
	public Object checkForClick(int x,int y){
		if(this.x==x && this.y==y && pop>0 && food>0){
			return this;
		}
		for(int a=0;a<armies.size();a++){
			if(armies.get(a).x==x && armies.get(a).y==y){
				return armies.get(a);
			}
		}
		return null;
	}
	@Override
	public void nextFrame(){
		if(food>0 && pop>0){
			harvest();
		}
		for(int a=0;a<armies.size();a++){
			Army army=armies.get(a);
			army.nextFrame();
			if(army.pop<=0 || army.food<=0){
				if(Main.main.land.terrain[army.x][army.y]>0){
					army.foundCity(army.x, army.y);
				}
				armies.remove(a);
				a--;
			}
		}
	}
}