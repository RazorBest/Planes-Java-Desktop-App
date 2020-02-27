import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Button {
	
	private Rectangle init_box;
	private Rectangle box;
	private Color col          = Color.black, 
				  col_init     = Color.black, 
				  col_inactive = new Color(201, 207, 216), 
				  col_hover    = new Color(70, 80, 96);
	private boolean active = true;
	private boolean hover = false;
	private String text = "";
	private Font font, font_init, font_hover;
	private int text_x, text_y;
	private int animation_time = 0;
	private boolean clicked = false;
	private boolean pressed = false;
	private boolean visible = true;
	
	public Button(int x, int y, int width, int height){
		
		init_box = new Rectangle(x, y, width, height);
		box      = new Rectangle(x, y, width, height);
		
		text_x = x; //Unnecesary
		text_y = y;
	}
	
	public void hoverButton(){
		
		if(active && !hover){
			
			box.x -= 3;
			box.y -= 3;
			box.width += 6;
			box.height += 6;
			
			col = col_hover;
			hover = true;
		}
	}
	public void unhoverButton(){
		
		if(animation_time == 0 && active){
			box.x = init_box.x;
			box.y = init_box.y;
			box.width = init_box.width;
			box.height = init_box.height;
		
			col = col_init;
			hover = false;
		}
	}
	public void pressButton(){
		
		pressed = true;
		box.x = init_box.x;
		box.y = init_box.y;
		box.width = init_box.width;
		box.height = init_box.height;
	}
	public void releaseButton(){
		
		if(pressed){
			box.x -= 3;
			box.y -= 3;
			box.width += 6;
			box.height += 6;
		}
		pressed = false;
	}
	public boolean isPressed(){
		
		return pressed;
	}
	public void makeActive(){
		
		active = true;
		col = col_init;
	}
	public void makeInactive(){
		
		System.out.println("Inactive button");
		
		active = false;
		col = col_inactive;
	}
	public boolean isActive(){
		
		return active;
	}
	public static boolean mouseOver(int x, int y, Button button){
		
		if(x >= button.getX() && x <= button.getX() + button.getWidth())
			if(y >= button.getY() && y <= button.getY() + button.getHeight()) return true;
		
		return false;
	}
	
	public void clickAnimation(){
		
		animation_time = 10;
	}
	
	public void makeInvisible(){
		
		visible = false;
	}
	public void makevisible(){
		
		visible = true;
	}
	public boolean isVisible(){
		
		return visible;
	}
	public void tick(){
		
		if(animation_time > 0){
			box.x -= 50;
			box.width += 100;
			animation_time--;
			if(animation_time == 0) unhoverButton();
		}
	}
	
	public void render(Graphics g){
		
		Graphics2D g2d 	= (Graphics2D)g;
		
		if(visible){
			g.setColor(col);
			g2d.draw(box);
			g.setFont(font);
			g.drawString(text, init_box.x + text_x, init_box.y + text_y);
		}
	}
	
	public void click(){
		
		clicked = true;
	}
	
	public void setText(String text, Font font, int text_x, int text_y){
		
		this.text = new String(text);
		this.font = font;
		this.font_init = font;
		this.font_hover = new Font(font.getFontName(), 1, font.getSize() + 2);
		this.text_x = text_x;
		this.text_y = text_y;
	}
	
	public int getX(){
		
		return box.x;
	}
	public int getY(){
		
		return box.y;
	}
	public int getWidth(){
		
		return (int)box.getWidth();
	}
	public int getHeight(){
		
		return (int)box.getHeight();
	}
	public boolean ready(){
		
		if(clicked && animation_time == 0)
			{clicked = false; return true;}
		
		return false;
	}
	
}
