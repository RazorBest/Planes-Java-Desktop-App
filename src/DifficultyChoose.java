import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DifficultyChoose extends MouseAdapter{
	
	private Main main;
	private Preparation preparation;
	private Opponent opponent;
	
	private Button button1, button2, button3, button4, button5;
	
	private Color col1 = Color.black, col2 = Color.black, col3 = Color.black, col4 = Color.black;
	private Color gray = new Color(70, 80, 96);
	
	public DifficultyChoose(Main main, Preparation preparation, Opponent opponent){
		
		this.main = main;
		this.preparation = preparation;
		this.opponent = opponent;
		
		Font font = new Font("arial", 1, 35);
		Font font_back = new Font("arial", 1, 40);
		
		button1 = new Button(Main.WIDTH / 2 - 75, 130, 150, 50);
		button1.setText("Baby", font, 35, 38);
		
		button2 = new Button(Main.WIDTH / 2 - 75, 200, 150, 50);
		button2.setText("Easy", font, 35, 38);
		
		button3 = new Button(Main.WIDTH / 2 - 75, 270, 150, 50);
		button3.setText("Medium", font, 10, 38);
		
		button4 = new Button(Main.WIDTH / 2 - 75, 340, 150, 50);
		button4.setText("Hard", font, 35, 36);
		
		button5 = new Button(10, Main.HEIGHT - 100, 160, 45);
		button5.setText("< BACK", font_back, 5, 37);
	}
	
	public void mousePressed(MouseEvent e){
		
		int mx = e.getX();
		int my = e.getY();
		
		if(Button.mouseOver(mx, my, button1)){
			
			button1.clickAnimation();
			button1.click();
		}
		
		else if(Button.mouseOver(mx, my, button2)){
			
			button2.clickAnimation();
			button2.click();
		}
		
		else if(Button.mouseOver(mx, my, button3)){
			
			button3.clickAnimation();
			button3.click();
		}
		
		else if(Button.mouseOver(mx, my, button4)){
			
			button4.clickAnimation();
			button4.click();
		}
		else if(Button.mouseOver(mx, my, button5)){
			
			button5.click();
		}
	}
	
	public void mouseMoved(MouseEvent e){
		
		int mx = e.getX();
		int my = e.getY();
		
		if(Button.mouseOver(mx, my, button1))
			button1.hoverButton();
		else
			button1.unhoverButton();
		
		if(Button.mouseOver(mx, my, button2))
			button2.hoverButton();
		else
			button2.unhoverButton();
		
		if(Button.mouseOver(mx, my, button3))
			button3.hoverButton();
		else
			button3.unhoverButton();
		
		if(Button.mouseOver(mx, my, button4))
			button4.hoverButton();
		else 
			button4.unhoverButton();
		
		if(Button.mouseOver(mx, my, button5))
			button5.hoverButton();
		else 
			button5.unhoverButton();
	}
	
	private void setNextState(){
		
		main.state = Main.STATE.Preparation;
		
		main.removeMouseListener(this);
		main.removeMouseMotionListener(this);
		main.addMouseListener(preparation);
		main.addMouseMotionListener(preparation);
		
		Point p = main.getCursorRelativeToFrame();
		preparation.moveMouse(p.x, p.y);
	}
	
	public void tick(){
		
		button1.tick();
		button2.tick();
		button3.tick();
		button4.tick();
		
		if(button1.ready()){
			
			opponent.setDifficulty(Opponent.DIFFICULTY.Baby);
			setNextState();
		}
		else if(button2.ready()){
			
			opponent.setDifficulty(Opponent.DIFFICULTY.Easy);
			setNextState();
		}
		else if(button3.ready()){
	
			opponent.setDifficulty(Opponent.DIFFICULTY.Medium);
			setNextState();
		}
		else if(button4.ready()){
	
			opponent.setDifficulty(Opponent.DIFFICULTY.Hard);
			setNextState();
		}
		else if(button5.ready()){
			
			main.removeMouseListener(this);
			main.removeMouseMotionListener(this);
			main.goToMenu();
		}
	}
	
	public void render(Graphics g){
		
		Graphics2D g2d = (Graphics2D)g;
		
		Font font1 = new Font("arial", 1, 40);
		Font font2 = new Font("arial", 1, 35);
		
		g.setColor(Color.black);
		g.setFont(font1);
		
		g.drawString("Choose difficulty", 180, 90);
		
		g.setFont(font2);
		
		button1.render(g);
		button2.render(g);
		button3.render(g);
		button4.render(g);
		button5.render(g);
		
	}
}
