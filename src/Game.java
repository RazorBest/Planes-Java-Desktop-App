import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Game extends MouseAdapter{
	
	private Main main;
	private Handler handler;
	private Opponent opponent;
	private Pause pause;
	
	private Button pause_button;
	private GridTable grid1; //Player
	private GridTable grid2; //Opponent
	private Shape shape;
	private Color color = Color.black;
	private int turn = 0; /*0 for player, 1 for opponent*/
	private int turn_count = 0;
	private int remained_planes = 3;
	private int found_heads = 0;
	private boolean pauseState = false;
	private boolean end = false;
	private int win = 0;
	
	private int animation_time = 0;
	private int delay = 120;
	private int delay_count = 0;
	
	public Game(Main main, Handler handler, Opponent opponent, Shape shape){
		
		this.main = main;
		this.handler = handler;
		this.opponent = opponent;
		pause = new Pause(main, this);
		this.shape = shape;
		pause_button = new Button(Main.WIDTH - 52, 15, 32, 24);
	}
	
	public void initGrids(){
		
		System.out.println("Init grids");
		
		grid1 = new GridTable(main, 10, 10, 40, 150, 27);
		grid1.addShape(shape);
		grid1.setRows(main.getPlayerGrid());
		handler.addObject(grid1);
		grid2 = new GridTable(main, 10, 10, 400, 150, 27);
		handler.addObject(grid2);
	}
	
	public void initOpponentTable(){

		grid2.setRows(opponent.random(3));
		grid2.hideRows();
	}
	public void endGame(){
		
		pauseState = false;
		handler.removeAll();
		main.endGame();
	}
	public void mousePressed(MouseEvent e){
		
		int mx = e.getX();
		int my = e.getY();
		
		if(pauseState){
			
			if((mx < 100 || mx > Main.WIDTH - 100) || (my < 100 || my > Main.HEIGHT - 100)){
				
				main.removeMouseListener(pause);
				main.removeMouseMotionListener(pause);
				pauseState = false;
			}
		}
		else{
		
			if(turn == 0){
				
				int type = grid2.isPlane(mx, my); //0 pentru incorect, 1 pentru "lovit", iar 2 pentru "cap"
				
				if(type != 0){
				
					if(type == 3) {
					
						found_heads++;
						if(found_heads == 3){
						
							animation_time = 191;
							win = 1;
							end = true;
							main.removeMouseListener(this);
							main.removeMouseMotionListener(this);
							return;
						}
					}
					turn = 1;
					grid2.refresh();
				}
			}
			if(Button.mouseOver(mx, my, pause_button)){
				
				main.addMouseListener(pause);
				main.addMouseMotionListener(pause);
				pauseState = true;
			}
		}
	}
	private void end(){
		
		main.goToGameEnd(win, turn_count);
		endGame();
	}
	private Point opponentTurn(){
		
		return opponent.Turn();
	}
	
	public void mouseMoved(MouseEvent e){
		
		int mx = e.getX();
		int my = e.getY();
		if(!pauseState){
			
			grid2.cellHover(mx ,my);
			if(Button.mouseOver(mx, my, pause_button))
				color = new Color(70, 80, 96);
			else
				color = Color.black;
		}
	}
	public void closePauseWindow(){
		
		pauseState = false;
		main.removeMouseListener(pause);
		main.removeMouseMotionListener(pause);
	}
	public void tick(){
		
		if(/*remained_planes > 0*/turn == 1){
			
			turn_count++;
			
			Point p = opponentTurn();
			int px = (int)p.getX();
			int py = (int)p.getY();
			State cellState = grid1.getRows().get(px).get(py);
			
			if(cellState.getState() == 1){
				
				if(cellState.getOriginX() == px && cellState.getOriginY() == py){
					
					opponent.setCell(p, 8); 
					cellState.setState(9);
					remained_planes--; 
					if(remained_planes == 0){
						
						animation_time = 192;
						win = 2;
						end = true;
						main.removeMouseListener(this);
						main.removeMouseMotionListener(this);
						return;
					}
				}
				else {
					
					opponent.setCell(p, 1);
					cellState.setState(8);
				}			
			}
			else{
				
				opponent.setCell(p, 7);
				cellState.setState(6);
			}
			turn = 0;
			delay_count = delay;
		}
		if(animation_time > 0){
			
			if(animation_time / 24 % 2 == 0){
				
				ArrayList<ArrayList<State>> rows = grid2.getRows();
				
				for(int i = 0; i < 10; ++i){
					
					for(int j = 0; j < 10; ++j){
						
						State tempState = rows.get(i).get(j);
						
						if(tempState.isHidden()){
							
							tempState.setState(10);
						}
					}
				}
			}
			else if(animation_time > 72){
				
				ArrayList<ArrayList<State>> rows = grid2.getRows();
				
					for(int i = 0; i < 10; ++i){
					
						for(int j = 0; j < 10; ++j){
						
							State tempState = rows.get(i).get(j);
							
							if(tempState.getState() == 10)
								tempState.setState(5);
						}
					}
			}
			animation_time--;
			if(animation_time == 0) end();
		}
	}
	
	public void render(Graphics g){
		
		g.setColor(Color.black);
		
		Font font = new Font("arial", 1, 30);
		g.setFont(font);
		
		g.drawString("Turn: " + turn_count, Main.WIDTH / 2 - 60, 40);
		
		handler.render(g);
		
		g.setColor(color);
		
		g.fillRect(pause_button.getX(), pause_button.getY(), pause_button.getWidth(), pause_button.getHeight() / 8 * 2);
		g.fillRect(pause_button.getX(), pause_button.getY() + pause_button.getHeight() / 8 * 3, pause_button.getWidth(), pause_button.getHeight() / 8 * 2);
		g.fillRect(pause_button.getX(), pause_button.getY() + pause_button.getHeight() / 8 * 6, pause_button.getWidth(), pause_button.getHeight() / 8 * 2);
		//This must be last
		if(pauseState)
			pause.render(g);
	}
	
	public void reInit(){
		
		turn = 0;
		turn_count = 0;
		remained_planes = 3;
		found_heads = 0;
	}
}
