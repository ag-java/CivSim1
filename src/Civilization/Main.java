package Civilization;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;

public class Main extends JFrame implements MouseListener{
	static final long serialVersionUID=1;
	static final int dimension=65;
	static final int unitSize=10;
	private static final SSRNG rng=new SSRNG(Math.random());
	static Main main;
	private GraphicUI gui=new GraphicUI();
	private final BufferedImage image;
	ArrayList<NationBrain> nations=new ArrayList<NationBrain>();
	private int top;
	ArrayList<City> cities=new ArrayList<City>();
	static NationBrain currentBrain;
	Continent land=new Continent();
	Colors colors=new Colors();
	public static void main(String[] args){
		main=new Main();
	}
	public Main(){
		super("Alugo's Civilization Simulator");
		int simSize=dimension*unitSize;
		image=new BufferedImage(simSize,simSize+GraphicUI.HEIGHT+top,BufferedImage.TYPE_INT_RGB);
		top=getInsets().top+(unitSize*2);
		setSize(simSize,simSize+GraphicUI.HEIGHT+top);
		setVisible(true);
		setResizable(false);
		addMouseListener(this);
		for(int a=0;a<4;a++){
			java.awt.Color color=colors.next();
			newNation(color);
			newCity(color);
		}
		new TurnThread().start();
	}
	public void newNation(java.awt.Color color){
		nations.add(new NationBrain(color));
	}
	public void newCity(java.awt.Color color){
		int x=random(dimension);
		int y=random(dimension);
		while(land.terrain[x][y]==Continent.WATER || popExists(x,y)!=null){
			x=random(dimension);
			y=random(dimension);
		}
		addCity(new City(x,y,color));
	}
	public void addCity(City city){
		for(int a=0;a<cities.size();a++){
			if(cities.get(a).friendly(city)){
				//for(int b=a;b<cities.size();b++){
					//if(!cities.get(b).friendly(city)){
						cities.add(/*b-1*/a,city);
						return;
					//}
				//}
			}
		}
		cities.add(city);
	}
	public Population popExists(int x,int y){
		for(int a=0;a<cities.size();a++){
			City city=cities.get(a);
			if(city.x==x && city.y==y && city.alive()){
				return city;
			}
			for(int b=0;b<city.armies.size();b++){
				Army army=city.armies.get(b);
				if(army.x==x && army.y==y){
					return army;
				}
			}
		}
		return null;
	}
	
	//turn code
	public synchronized void turn(){
		for(int a=0;a<nations.size();a++){
			currentBrain=nations.get(a);
			if(currentBrain.leadNation()==0){
				nations.remove(a);
				a--;
			}
		}
		for(int a=0;a<cities.size();a++){
			City city=cities.get(a);
			city.nextFrame();
			if(!city.alive() && city.armies.size()==0){
				cities.remove(a);
				a--;
			}
		}
		/*if(turns>250 && turns<500){
			terraform();
		}*/
		gui.updateHistogram();
		updateImage();
		//turns++;
		//System.out.println(turns);
	}
	//private int turns=0;
	/*private void terraform(){
		int food=0;
		if(random(2)==0){
			food=Continent.foodMax;
		}
		final int dim=15;
		final int x=random(dimension-dim);
		final int y=random(dimension-dim);
		for(int a=0;a<dim;a++){
			for(int b=0;b<dim;b++){
				if(land.terrain[x+a][y+b]!=Continent.WATER){
					if((food==0)==(popExists(x+a,y+b) instanceof City)){
						land.terrain[x+a][y+b]=food;
					}
				}
			}
		}
	}*/
	
	//static methods
	public static int random(int max){
		return random(0,max);
	}
	public static int random(int min,int max){
		return (int)Math.floor(rng.next()*(max-min))+min;
	}
	
	//drawing
	private void updateImage() {
		Graphics g=image.createGraphics();
		g.clearRect(0,0,image.getWidth(),image.getHeight());
		g.translate(0,top);
		land.draw(g);
		for(int a=0;a<cities.size();a++){
			cities.get(a).draw(g);
		}
		gui.draw(g);
	}
	
	
	@Override
	public void paint(Graphics g){
		g.drawImage(image,0,0,null);
	}
	
	@Override
	public void mouseClicked(MouseEvent event){
		int eventY=event.getY()-top;
		if(eventY<=dimension*unitSize){
			int x=(int)Math.floor(event.getX()/unitSize);
			int y=(int)Math.floor(eventY/unitSize);
			gui.focus=null;
			for(int a=0;a<cities.size();a++){
				Object result=cities.get(a).checkForClick(x,y);
				if(result!=null){
					gui.focus=result;
					return;
				}
			}
			if(land.terrain[x][y]>Continent.WATER){
				gui.focus=new Integer(land.terrain[x][y]);
			}
			repaint();
		}
	}
	@Override
	public void mousePressed(MouseEvent event){}
	@Override
	public void mouseReleased(MouseEvent event){}
	@Override
	public void mouseEntered(MouseEvent event){}
	@Override
	public void mouseExited(MouseEvent event){}
}