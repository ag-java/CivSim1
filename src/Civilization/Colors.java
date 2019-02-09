package Civilization;
import java.awt.Color;

public class Colors{
	private int colorIndex=0;
	private Colorname[] colors=new Colorname[16];
	public Colors(){
		colors[0]=new Colorname(Color.RED,"Red");
		colors[1]=new Colorname(Color.YELLOW,"Yellow");
		colors[2]=new Colorname(Color.ORANGE,"Orange");
		colors[3]=new Colorname(Color.WHITE,"White");
		//colors[4]=new Colorname(Color.BLACK,"Black");
		colors[4]=new Colorname(Color.CYAN,"Cyan");
		colors[5]=new Colorname(Color.MAGENTA,"Magenta");
		colors[6]=new Colorname(Color.GRAY,"Gray");
		colors[7]=new Colorname(Color.GREEN,"Green");
		colors[8]=new Colorname(new Color(255,192,203),"Pink");
		colors[9]=new Colorname(new Color(128,0,0),"Maroon");
		colors[10]=new Colorname(new Color(165,42,42),"Brown");
		colors[11]=new Colorname(new Color(128,0,128),"Purple");
		colors[12]=new Colorname(new Color(75,0,130),"Indigo");
		colors[13]=new Colorname(new Color(175,238,238),"Turquoise");
		colors[14]=new Colorname(new Color(127,255,212),"Aquamarine");
		colors[15]=new Colorname(new Color(34,139,34),"Forest Green");
	}
	public Color next(){
		if(Main.main==null){
			colorIndex++;
			return colors[colorIndex-1].color;
		}
		for(int a=0;a<colors.length;a++){
			boolean use=true;
			for(int b=0;b<Main.main.nations.size();b++){
				if(Main.main.nations.get(b).color==colors[a].color){
					use=false;
					break;
				}
			}
			if(use){
				return colors[a].color;
			}
		}
		return null;
	}
	public boolean colorsAvailable(){
		return (Main.main.nations.size()<colors.length);//(colorIndex<colors.length);
	}
	public String getName(Color color){
		for(int a=0;a<colors.length;a++){
			if(colors[a].color==color){
				return colors[a].name;
			}
		}
		return "Not a color";
	}
	private class Colorname{
		Color color;
		String name;
		public Colorname(Color color,String name){
			this.color=color;
			this.name=name;
		}
	}
}