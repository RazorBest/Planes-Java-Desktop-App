import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Preparation extends MouseAdapter /*implements MouseMotionListener*/{
	
	private Handler handler;
	private Main main;
	private Game game;
	private Opponent opponent;
	private GridTable grid;
	private ArrayList<GridTable> grids;
	private int lastRemainedShapes;
	private Button button1, button2, button3;
	private Color color = Color.black;
	private Shape shape;
	private int shapes;
	
	public Preparation(Main main, Handler handler, Game game, Opponent opponent, Shape shape, int shapes){
		
		this.main = main;
		this.handler = handler;
		this.game = game;
		this.opponent = opponent;
		
		this.shape = shape;
		this.shapes = shapes;
		
		initGrids();
		
		Font font1 = new Font("arial", 1, 40);
		Font font2 = new Font("arial", 1, 25);
		
		button1 = new Button(10, Main.HEIGHT - 100, 160, 45);
		button1.setText("< BACK", font1, 5, 37);
		
		button2 = new Button(Main.WIDTH - 180, Main.HEIGHT - 100, 160, 45);
		button2.setText("NEXT >", font1, 10, 37);
		button2.makeInactive();
		
		button3 = new Button(Main.WIDTH - 215, Main.HEIGHT - 170, 120, 30);
		button3.setText("RANDOM", font2, 4, 25);
		
		lastRemainedShapes = shapes;
	}
	
	public void initGrids(){
		
		grids = new ArrayList<GridTable>();
		grid = new GridTable(main, 10, 10, 200, 100, 30);
		grid.addShape(shape);
		handler.addObject(grid);
		grid.setNumberOfShapes(shapes);
		
		grids.add(new GridTable(main, 5, 5, 40, 50, 20));
		grids.add(new GridTable(main, 5, 5, 40, 150, 20));
		grids.add(new GridTable(main, 5, 5, 40, 250, 20));
		for(int i = 0; i < 3; ++i){
			
			GridTable gridTable = grids.get(i);
			
			gridTable.setColor(Color.white);
			gridTable.addShape(shape);
			gridTable.setNumberOfShapes(1);
			gridTable.putShape(2, 0, 1);
			handler.addObject(gridTable);
		}
	}
	
	public void mousePressed(MouseEvent e){
		
		int mx = e.getX();
		int my = e.getY();
		
		grid.mouseCreate(e);
		
		if(Button.mouseOver(mx, my, button2) && button2.isActive()){
			
			button2.pressButton();
		}
		else if(Button.mouseOver(mx, my, button1)){
			
			button1.pressButton();
		}
		else if(Button.mouseOver(mx, my, button3)){
			
			button3.pressButton();
		}
	}
	public void mouseReleased(MouseEvent e){
		
		int mx = e.getX();
		int my = e.getY();
		
		if(Button.mouseOver(mx, my, button2) && button2.isPressed() && button2.isActive()){
			
			main.removeMouseListener(this);
			
			System.out.println("Size: " + grid.getRows().size());
			
			main.setPlayerGrid(grid.getRows());
			
			button2.makeInactive();
			
			handler.removeAll();
			main.newGame(); 
		}
		else if(Button.mouseOver(mx, my, button1) && button1.isPressed()){
			
			main.goToMenu();
			main.removeMouseListener(this);
			
			handler.removeAll();
			initGrids();
		}
		else if(Button.mouseOver(mx, my, button3) && button3.isPressed()){
			
			grid.setRows(opponent.random(3));
			grid.setNumberOfShapes(0);
		}
		if(button1.isPressed()) button1.releaseButton();
		if(button2.isPressed()) button2.releaseButton();
		if(button3.isPressed()) button3.releaseButton();
		
		moveMouse(mx, my);
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
		
		if(Button.mouseOver(mx, my, button2) && button2.isActive())
			button2.hoverButton();
		else
			button2.unhoverButton();
		
		if(Button.mouseOver(mx, my, button3))
			button3.hoverButton();
		else
			button3.unhoverButton();
		
		grid.shadow(new Point(mx, my));
	}
	public void rotateClock(){
		
		grid.rotateClock();
	}
	public void rotateCounterClock(){
		
		grid.rotateCounterClock();
	}
	public void tick(){
		
		int remainedShapes = grid.getNumberOfShapes();
		
		if(remainedShapes > lastRemainedShapes){
			
			if(lastRemainedShapes == 0) button2.makeInactive();
			
			grids.get(lastRemainedShapes).putShape(2, 0, 1);
			lastRemainedShapes++;
		}
		else if(remainedShapes < lastRemainedShapes){
			
			grids.get(lastRemainedShapes - 1).removeShape(2, 0 ,1);
			lastRemainedShapes--;
			if(lastRemainedShapes == 0) button2.makeActive();
		}
	}
	public void render(Graphics g){
		
		Graphics2D g2d = (Graphics2D)g;
		
		Font font1 = new Font("arial", 1, 17);
		Font font2 = new Font("arial", 1, 40);
		Font font3 = new Font("arial", 1, 25);
		g.setFont(font1);
		g.setColor(Color.black);
		g.drawString("Remained Shapes: ", 15, 30);
		g.drawRect(10, 10, 160, 400);
		
		handler.render(g);
		button1.render(g);
		button2.render(g);
		button3.render(g);
	}
}
