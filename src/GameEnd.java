import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameEnd extends MouseAdapter{
	
	private Main main;
	private int win = 0;
	private int turns = 0;
	private Button button1;
	private Color gray = new Color(70, 80, 96);
	
	public GameEnd(Main main){
		
		this.main = main;
		
		Font font = new Font("arial", 1, 40);
		
		button1 = new Button(300, Main.HEIGHT / 2, 130, 50);
		button1.setText("Menu", font, 15, 40);
	}
	
	public void mousePressed(MouseEvent e){
		
		int mx = e.getX();
		int my = e.getY();
		
		if(Button.mouseOver(mx, my, button1))
		{
			button1.pressButton();
		}
	}
	public void mouseReleased(MouseEvent e){
		
		int mx = e.getX();
		int my = e.getY();
		
		if(Button.mouseOver(mx, my, button1) && button1.isPressed())
		{
			button1.unhoverButton();
			main.removeMouseListener(this);
			main.removeMouseMotionListener(this);
			main.goToMenu();
		}
		else
			moveMouse(mx, my);
		
		if(button1.isPressed()) button1.releaseButton();
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
	}
	
	public void tick(){}
	
	public void render(Graphics g){
		
		Graphics2D g2d = (Graphics2D)g;
		
		Font font1 = new Font("arial", 1, 50);
		Font font2 = new Font("arial", 1, 40);
		Font font3 = new Font("arial", 1, 45);
		
		g.setColor(Color.black);
		g.setFont(font1);
		
		if(win == 1){
			
			g.drawString("Congratulations!", Main.WIDTH / 2 - 190, 100);
			g.drawString("You won in " + turns + " turns", Main.WIDTH / 2 - 225, 170);
		}
		else if(win == 2){
			
			g.setFont(font3);
			
			g.drawString("You lost", Main.WIDTH / 2 - 90, 100);
			g.drawString("The computer has beaten you", 30, 170);
			g.drawString("in " + turns + " turns", 250, 240);
		}
		
		button1.render(g);
	}
	
	public void setWin(int win){
		
		this.win = win;
	}
	public void setTurns(int turns){
		
		this.turns = turns;
	}
}
