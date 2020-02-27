import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Pause extends MouseAdapter{
	
	private Main main;
	private Game game;
	private Button button1, button2;
	
	public Pause(Main main, Game game){
		
		this.main = main;
		this.game = game;
		
		Font font = new Font("arial", 1, 30);
		
		button1 = new Button(220, Main.HEIGHT - 200, 100, 50);
		button1.setText("Yes", font, 24, 35);
		button2 = new Button(Main.WIDTH - 320, Main.HEIGHT - 200, 100, 50);
		button2.setText("No", font, 31, 35);
	}
	public void mousePressed(MouseEvent e){
		
		int mx = e.getX();
		int my = e.getY();
		
		if(Button.mouseOver(mx, my, button1))
			button1.pressButton();
		
		if(Button.mouseOver(mx, my, button2))
			button2.pressButton();
	}
	public void mouseReleased(MouseEvent e){
		
		int mx = e.getX();
		int my = e.getY();
		
		if(Button.mouseOver(mx, my, button1) && button1.isPressed())
			{
				button1.releaseButton();
				button1.unhoverButton();
				game.endGame();
				main.goToMenu();
			}
		
		else if(Button.mouseOver(mx, my, button2) && button2.isPressed()){
			
			button2.releaseButton();
			button2.unhoverButton();
			game.closePauseWindow();
		}
		else 
			moveMouse(mx ,my);
	}
	public void mouseMoved(MouseEvent e){
		
		int mx = e.getX();
		int my = e.getY();
		
		moveMouse(mx, my);
	}
	public void moveMouse(int mx, int my){
		
		if(Button.mouseOver(mx, my, button1))
			button1.hoverButton();
		else 
			button1.unhoverButton();
		
		if(Button.mouseOver(mx, my, button2))
			button2.hoverButton();
		else
			button2.unhoverButton();
	}
	public void render(Graphics g){
		
		Font font = new Font("arial", 1, 33);
		
		g.setColor(Color.white);
		g.fillRect(100, 100, Main.WIDTH - 200, Main.HEIGHT - 200);
		
		g.setColor(Color.black);
		g.drawRect(99, 99, Main.WIDTH - 198, Main.HEIGHT - 198);
		g.drawRect(104, 104, Main.WIDTH - 208, Main.HEIGHT - 208);
		g.drawRect(105, 105, Main.WIDTH - 210, Main.HEIGHT - 210);
		g.drawRect(106, 106, Main.WIDTH - 212, Main.HEIGHT - 212);
		g.drawRect(107, 107, Main.WIDTH - 214, Main.HEIGHT - 214);
		
		g.setFont(font);
		g.drawString("Are you sure", 270, 180);
		g.drawString("you want to go back to menu?", 130, 230);
		
		button1.render(g);
		button2.render(g);
	}
}
