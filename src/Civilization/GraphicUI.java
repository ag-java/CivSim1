package Civilization;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

public class GraphicUI{
	static final int HEIGHT=150;
	private final int width=Main.dimension*Main.unitSize;
	private ArrayList<Data[]> histogram=new ArrayList<Data[]>();
	private Font font=new Font(Font.SERIF,Font.PLAIN,20);
	private int[] highestPop={-1,0};
	Object focus;
	public void draw(Graphics g){
		g.translate(0,width);
		g.setColor(Color.BLACK);
		g.fillRect(width/2,0, width/2,HEIGHT);
		if(focus!=null){
			drawFocus(g);
		}
		drawHistogram((Graphics2D)g);
		g.translate(0,-width);
	}
	private void drawFocus(Graphics g){
		final int margin=10;
		int x=(width/2)+margin;
		int y=margin+font.getSize();
		g.setFont(font);
		g.setColor(Color.WHITE);
		if(focus instanceof Population){
			Population pop=(Population)focus;
			g.drawString("Nation: "+Main.main.colors.getName(pop.color),x,y);
			g.drawString("Population: "+pop.pop,x,y+font.getSize());
			g.drawString("Food: "+pop.food,x,y+(font.getSize()*2));
			g.drawString("x: "+pop.x+", y: "+pop.y,x,y+(font.getSize()*3));
			g.drawString("Food (Land): "+Main.main.land.terrain[pop.x][pop.y],x,y+(font.getSize()*4));
		}else if(focus instanceof Integer){
			int food=(Integer)focus;
			g.drawString("Food (Land): "+food,x,y);
		}
	}
	private void drawHistogram(Graphics2D g){
		double lineWidth=(width/2)/(double)histogram.size();
		int maxHeight=getHighestPop();
		int x=0;
		g.scale(lineWidth,1);
		for(int a=0;a<histogram.size();a++){
			Data[] data=histogram.get(a);
			int pop=getPop(data);
			int lineHeight=(pop*HEIGHT)/maxHeight;
			int y=HEIGHT;
			for(int b=0;b<data.length;b++){
				int height=(data[b].num*lineHeight)/pop;
				g.setColor(data[b].c);
				g.fillRect(x,y-height,1,height);//lineWidth in place of 1
				y-=height;
			}
			x++;//=lineWidth
		}
		g.scale(1/lineWidth,1);
	}
	public void updateHistogram(){
		ArrayList<Data> temp=new ArrayList<Data>();
		for(int a=0;a<Main.main.cities.size();a++){
			int armies=Main.main.cities.get(a).armies.size();
			int cities=0;
			if(Main.main.cities.get(a).alive()){
				cities=1;
			}
			int b=1;
			while(a+b<Main.main.cities.size() && Main.main.cities.get(a+b).color==Main.main.cities.get(a).color){
				armies+=Main.main.cities.get(a+b).armies.size();
				if(Main.main.cities.get(a+b).alive()){
					cities++;
				}
				b++;
			}
			temp.add(new Data(Main.main.cities.get(a).color,cities+armies));
			a+=b-1;
		}
		histogram.add(temp.toArray(new Data[temp.size()]));
	}
	private int getHighestPop(){
		for(int a=highestPop[0]+1;a<histogram.size();a++) {
			int p=getPop(histogram.get(a));
			if(p>highestPop[1]){
				highestPop[0]=a;
				highestPop[1]=p;
			}
		}
		return highestPop[1];
		/*int pop=getPop(histogram.get(0));
		for(int a=1;a<histogram.size();a++){
			int p=getPop(histogram.get(a));
			if(p>pop){
				pop=p;
			}
		}
		return pop;*/
	}
	private int getPop(Data[] data){
		int pop=0;
		for(int a=0;a<data.length;a++){
			pop+=data[a].num;
		}
		return pop;
	}
	private class Data{
		Color c;
		int num;
		public Data(Color c,int num){
			this.c=c;
			this.num=num;
		}
	}
}