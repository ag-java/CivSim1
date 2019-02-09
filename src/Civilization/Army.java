package Civilization;
import java.awt.Color;

public class Army extends Population{
	public Army(int x,int y,Color color,int pop,int food){
		this.x=x;
		this.y=y;
		this.color=color;
		this.pop=pop;
		this.food=food;
	}
	@Override
	public void nextFrame(){
		if(getFood(x,y)>Continent.WATER || Main.random(2)==0){
			food-=(pop/10);
		}
	}
	private int merge(City city){
		city.pop+=pop;
		city.food+=food;
		pop=0;
		food=0;
		return 1;
	}
	private int attack(Population p){
		/*if(p instanceof City){
			p.pop-=pop;
		}else{
			p.pop-=2;
		}*/
		p.pop-=pop;
		pop-=2;
		if(p.pop<=0){
			if(p instanceof City){
				return 1+foundCity(p.x,p.y);
			}
			return 1;
		}
		return 0;
	}
	public int foundCity(int x,int y){
		if(Main.main.land.terrain[x][y]<=0 || Main.main.popExists(x,y).getClass()==City.class){
			return 0;
		}
		City city=new City(x,y,color);
		if(Main.main.colors.colorsAvailable() && Main.random(1000)==0){
			Color c=Main.main.colors.next();
			Main.main.newNation(c);
			for(int a=-2;a<=2;a++){
				for(int b=-2;b<=2;b++){
					Population p=Main.main.popExists(x+a, y+b);
					if(p!=null){
						p.color=c;
						if(p instanceof City){
							Main.main.cities.remove(p);
							Main.main.addCity((City)p);
						}
					}
				}
			}
		}
		Main.main.addCity(city);
		return 1+merge(city);
	}
	public int lookForFood(){
		int[][] food=new int[8][2];
		int fIndex=0;
		for(int a=-1;a<=1;a++){
			for(int b=-1;b<=1;b++){
				try{
					int f=getFood(x+a,y+b);
					if(f>0 && f>getFood(x,y) && Main.main.popExists(x+a, y+b)==null){
						food[fIndex][0]=a;
						food[fIndex][1]=b;
						fIndex++;
					}
				}catch(ArrayIndexOutOfBoundsException e){}
			}
		}
		if(fIndex>0){
			int f=Main.random(fIndex);
			x+=food[f][0];
			y+=food[f][1];
			return 1;
		}
		int[][] water=new int[8][2];
		int wIndex=0;
		for(int a=-1;a<=1;a++){
			for(int b=-1;b<=1;b++){
				try{
					if((a!=0 || b!=0) && getFood(x+a,y+b)<=0 && Main.main.popExists(x+a, y+b)==null){
						water[wIndex][0]=a;
						water[wIndex][1]=b;
						wIndex++;
					}
				}catch(ArrayIndexOutOfBoundsException e){}
			}
		}
		if(wIndex>0){
			int r=Main.random(wIndex);
			x+=water[r][0];
			y+=water[r][1];
			return 1;
		}
		return 0;
	}
	private int getFood(int x,int y){
		return Main.main.land.terrain[x][y];
	}
	public int lookFor(boolean lookForCity,boolean attack){//never set to false,false
		for(int a=-NationBrain.VISION;a<=NationBrain.VISION;a++){
			for(int b=-NationBrain.VISION;b<=NationBrain.VISION;b++){
				if(!(a==0 && b==0)){
					Population p=Main.main.popExists(x, y);
					if(p!=null && (p.getClass()==City.class)==lookForCity && attack!=friendly(p)){
						if(isAdjacent(p)){
							if(attack){
								return attack(p);
							}else{
								return merge((City)p);
							}
						}else{
							return moveToward(p);
						}
					}
				}
			}
		}
		return 0;
	}
	private int moveToward(Population p){
		int x=0,y=0;
		if(p.x>this.x){
			x=1;
		}else if(p.x<this.x){
			x=-1;
		}
		if(p.y>this.y){
			y=1;
		}else if(p.y<this.y){
			y=-1;
		}
		if(Main.main.popExists(this.x+x,this.y+y)!=null){
			if(Main.main.popExists(this.x+x, this.y)==null){
				this.x+=x;
				return 1;
			}else if(Main.main.popExists(this.x, this.y+y)==null){
				this.y+=y;
				return 1;
			}
			return 0;
		}
		this.x+=x;
		this.y+=y;
		return 1;
	};
	private boolean isAdjacent(Population p){
		return Math.abs(x-p.x)==1 && Math.abs(y-p.y)==1;
	}
}