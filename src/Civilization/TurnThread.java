package Civilization;

public class TurnThread extends Thread{
	@Override
	public void run(){
		while(Main.main.cities.size()>0){
			Main.main.turn();
			Main.main.repaint();
			try{
				Thread.sleep(250);
			}catch(InterruptedException interrupt){
				interrupt.printStackTrace();
			}
		}
	}
}