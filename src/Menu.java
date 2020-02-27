import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Menu extends MouseAdapter implements MouseMotionListener{
	
	private Main main;
	private Help help;
	private Handler handler;
	private DifficultyChoose difficultyChoose;
	private Button button1, button2, button3;
	
	public Menu(Main main, Help help, Handler handler, DifficultyChoose difficultyChoose){
		
		this.main = main;
		this.help = help;
		this.handler = handler;
		this.difficultyChoose = difficultyChoose;
		
		Font font = new Font("arial", 1, 45);
		
		button1 = new Button(Main.WIDTH / 2 - 90, 150, 180, 50);
		button1.setText("Play", font, 40, 40);
		
		button2 = new Button(Main.WIDTH / 2 - 90, 250, 180, 50);
		button2.setText("Help", font, 40, 40);
		
		button3 = new Button(Main.WIDTH / 2 - 90, 350, 180, 50);
		button3.setText("Quit", font, 40, 40);
	}
	public void mousePressed(MouseEvent e){
		
		int mx = e.getX();
		int my = e.getY();
		
		System.out.println("Mouse pressed " + mx + " " + my);
		
		if(Button.mouseOver(mx, my, button1)){
			
			button1.clickAnimation();
			button1.click();
		}
		else if(Button.mouseOver(mx, my, button2)){
			
			button2.clickAnimation();
			button2.click();
		}
		else if(Button.mouseOver(mx, my, button3)){
			
			System.exit(1);
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
	}
	
	public void mouseReleased(MouseEvent e){
		
	}
	
	private void extendBox(Rectangle box){
		
		box.x = box.x - 3;
		box.y = box.y - 3;
		box.width = box.width + 6;
		box.height = box.height + 6;
	}
	
	private void resizeBox(Rectangle box){
		
		box.x = box.x + 3;
		box.y = box.y + 3;
		box.width = box.width - 6;
		box.height = box.height - 6;
	}
	
	public void tick(){
		
		button1.tick();
		
		if(button1.ready()){
			
			main.state = Main.STATE.DifficultyChoose;
			main.removeMouseListener(this);
			main.removeMouseMotionListener(this);
			main.addMouseListener(difficultyChoose);
			main.addMouseMotionListener(difficultyChoose);
		}
		if(button2.ready()){
			
			main.state = Main.STATE.Help;
			main.removeMouseListener(this);
			main.removeMouseMotionListener(this);
			main.addMouseListener(help);
			main.addMouseMotionListener(help);
		}
	}
	
	public void render(Graphics g){
		
		Graphics2D g2d = (Graphics2D)g;
		
		g.setColor(Color.black);
		
		Font font1 = new Font("arial", 1, 70);
		Font font2 = new Font("arial", 1, 45);
		
		g.setFont(font1);
		g.drawString("Planes", Main.WIDTH / 2 - 110, 100);
		
		button1.render(g);
		button2.render(g);
		button3.render(g);
		
		/*g.setFont(font2);
		
		g.setColor(col1);
		g2d.draw(box1);
		g.drawString("Play", (int)box1.getX() + 40, (int)box1.getY() + 40);
		
		g.setColor(col2);
		g2d.draw(box2);
		g.drawString("Help", (int)box2.getX() + 40, (int)box2.getY() + 40);
		
		g.setColor(col3);
		g2d.draw(box3);
		g.drawString("Quit", (int)box3.getX() + 40, (int)box3.getY() + 40);*/
	}
}
