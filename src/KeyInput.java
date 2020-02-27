import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{
	
	private GridTable grid;
	private Main main;
	private Preparation preparation;
	
	public KeyInput(Main main, Preparation preparation){
		
		this.main = main;
		this.grid = grid;
		this.preparation = preparation;
	}
	
	public void keyPressed(KeyEvent e){
		
		int key = e.getKeyCode();
		if(main.state == Main.STATE.Preparation){
			
			if(key == e.VK_LEFT){
			
				preparation.rotateClock();
			}
			else if(key == e.VK_RIGHT){
			
				preparation.rotateCounterClock();
			}
		}
	}
}
