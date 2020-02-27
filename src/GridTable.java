import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class GridTable implements GameObject{
	
	private Main main;
	private int width;
	private int height;
	private int len;
	private int x, y;
	private int cellX, cellY;
	private int rotation = 1;/*[1-4]*/
	private int remainedShapes = 0;
	private boolean clickable = true; // Arata daca poti pune un avion pe tabla, unde se afla cursorul
	private boolean removable = false; // Arata daca poti pune da click pe tabla, unde se afla cursorul, in ciuda celorlalte avioane puse
	private boolean inBounds = true;
	private Color color = Color.black;
	private Color light_green = new Color(204, 255, 204); 
	private Color gray = new Color(193, 196, 204);
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	
	private ArrayList<ArrayList<State>> rows = new ArrayList<ArrayList<State>>();
	
	public GridTable(Main main, int width, int height, int x, int y, int len){
		
		this.main = main;
		
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.len = len;
		
		for(int i = 0; i < height; ++i){
			
			rows.add(new ArrayList<State>());
			for(int j = 0; j < width; ++j){
						
				rows.get(i).add(new State());
			}
		}
	}
	
	public void mouseCreate(MouseEvent e){
		
		int mx = e.getX();
		int my = e.getY();
		
		int cellX = (mx - x) / len;
		int cellY = (my - y) / len;
		
		System.out.println("Mouse Pressed");
		
		if(cellX >= 0 && cellX < width){
			
			if(cellY >= 0 && cellY < height){
				
				State temp = rows.get(cellX).get(cellY);
				Shape shape = shapes.get(0);
				int n = shapes.get(0).getLen();
				
				if(temp.getState() == 1) {                  //Se va sterge un element de pe grid table
					
					State clickCell = rows.get(cellX).get(cellY);
					int originX = clickCell.getOriginX();
					int originY = clickCell.getOriginY();	
					cellX = originX;
					cellY = originY;
					int rotation = temp.getRotation();
							
					removeShape(cellX, cellY, rotation);
				}
				else if(clickable && inBounds) {
						
					putShape(cellX, cellY, rotation);
				}
			}
		}
	}
	
	public boolean isClickable(int mx, int my){
		int cellX;
		int cellY;
		
		if(mx - x >= 0) cellX = (mx - x) / len ;
		else cellX = -1;
		if(my - y >= 0) cellY = (my - y) / len ;
		else cellY = -1;
		if(cellX >= 0 && cellX < width){	
			if(cellY >= 0 && cellY < height){
				
				State temp = rows.get(cellX).get(cellY);
				if(temp.isClickable()) return true;
			}
		}
		
		return false;
	}
	public int cellIsPlane(int cellX, int cellY){
		
		return checkCell(cellX, cellY);
	}
	public int isPlane(int mx, int my){
		
		int cellX;
		int cellY;
		
		if(mx - x >= 0) cellX = (mx - x) / len ;
		else cellX = -1;
		if(my - y >= 0) cellY = (my - y) / len ;
		else cellY = -1;
		
		return checkCell(cellX, cellY);
	}
	private int checkCell(int cellX, int cellY){
		
		if(cellX >= 0 && cellX < width){	
			if(cellY >= 0 && cellY < height){
				
				State temp = rows.get(cellX).get(cellY);
				if(temp.isClickable()) {
					
					if(temp.isHidden()){
						
						temp.setState(1);
						temp.setHide(false);
						temp.setClickable(false); 
						
						if(cellX == temp.getOriginX() && cellY == temp.getOriginY())
							return 3;
						
						return 2;
					}
					else{
						
						temp.setState(6);
						temp.setHide(false);
						temp.setClickable(false); 
						return 1;
					}
				}
			}
		}
		return 0;
	}
	public void putShape(int cellX, int cellY, int rotation){
		
		int n = shapes.get(0).getLen();
		Shape shape = shapes.get(0);
		
		for(int i = 0; i < n; ++i){
			
			int x = shape.getShapeX(i, rotation);
			int y = shape.getShapeY(i, rotation);
			State tempState = rows.get(cellX + x).get(cellY + y);
			ArrayList<Boolean> margin = shape.getMargin(i, rotation);
			
			tempState.setState(1);
			tempState.setOrigin(cellX, cellY);
			tempState.setRotation(rotation);
			tempState.setMargin(margin);
		}
		remainedShapes--;
	}
	
	public void removeShape(int cellX, int cellY, int rotation){
		
		System.out.println("CellX: " + cellX + " CellY: " + cellY);
		
		int n = shapes.get(0).getLen();
		Shape shape = shapes.get(0);
		
		for(int i = 0; i < n; ++i){
			
			int x = shape.getShapeX(i, rotation);
			int y = shape.getShapeY(i, rotation);							
			State tempState = rows.get(cellX + x).get(cellY + y);
					
			tempState.setState(0);
			tempState.clearOrigin();
			tempState.removeMargin();
		}
		remainedShapes++;
		shadow(main.getCursorRelativeToFrame());
	}
	
	public void refresh()
	{
		for(int i = 0; i < height; ++i){
			
			for(int j = 0; j < width; ++j){
				
				State temp = rows.get(i).get(j);
				if(temp.getState() == 2 || temp.getState() == 3 || temp.getState() == 4) temp.setState(0);    //Restabilirea celulelor
				if(temp.isHidden()) temp.setState(5);
			}
		}
	}
	
	public void hideRows()
	{
		for(int i = 0; i < height; ++i){	
			for(int j = 0; j < width; ++j){
				
				State temp = rows.get(i).get(j);
				
				if(temp.getState() == 1) {
					
					temp.setState(5);
					temp.setHide(true);
					temp.removeMargin();
				}
			}
		}
	}
	
	public void cellHover(int mx, int my)
	{
		if(mx - x >= 0) cellX = (mx - x) / len ;
		else cellX = -1;
		if(my - y >= 0) cellY = (my - y) / len ;
		else cellY = -1;
		
		refresh();
		
		if(cellX >= 0 && cellX < width)
			
			if(cellY >= 0 && cellY < height){
				
				State temp = rows.get(cellX).get(cellY);
					if(temp.getState() != 1 && temp.getState() != 6) 
						temp.setState(4);
				}
	}
	
	public void shadow(Point point){
		
		int mx = (int)point.getX();
		int my = (int)point.getY();
		
		int cellX;
		int cellY;
		
		if(mx - x >= 0) cellX = (mx - x) / len ;
		else cellX = -1;
		if(my - y >= 0) cellY = (my - y) / len ;
		else cellY = -1;
		
		this.cellX = cellX;  //Pentru debugging
		this.cellY = cellY;
		
		clickable = true;
		removable = false;
		inBounds = false;
		
		refresh();
		
		if(cellX >= 0 && cellX < width){	
			if(cellY >= 0 && cellY < height){
				inBounds = true;
				Shape shape = shapes.get(0);
				int color = 2;              //Culoarea pe care o ia patratelul atunci cand forma este deasupra. Daca forma nu trece de table grid, atunci va ramane 2(verde)
				int n = shape.getLen();
				for(int i = 0; i < n; ++i){
					
					int x = shape.getShapeX(i, rotation);
					int y = shape.getShapeY(i, rotation);
					if(cellX + x < 0 || cellX + x >= width || cellY + y < 0 || cellY + y >= height){  //Daca ecista cel putin un patratel al formei care iese din table_grid, atunci toata forma va avea culoarea rosie
							
							//System.out.println("In afara");
							clickable = false;
							inBounds = false;
							removable = false;
					}
					else{
						
						//System.out.println(cellY + y + " Nu este in afara");
						State temp = rows.get(cellX + x).get(cellY + y);
						if(temp.getState() == 1){
							
							clickable = false;
							if(inBounds) removable = true;
						}
					}
				}
				
				if(clickable == false) color = 3;
				if(remainedShapes > 0){            //Daca nu mai sunt avioane, atunci nu le mai proiecta
					for(int i = 0; i < n; ++i){
					
						int x = shape.getShapeX(i, rotation);
						int y = shape.getShapeY(i, rotation);
					
						if(cellX + x >= 0 && cellX + x < width){
							if(cellY + y >= 0 && cellY + y < height){
							
								State temp = rows.get(cellX + x).get(cellY + y);
								if(temp.getState() != 1)temp.setState(color);
							}
						}
					}
				}
				else clickable = false;
				
			}
		}
	}
	
	public void tick(){}
	
	public void render(Graphics g){
		
		for(int i = 0; i < height; ++i){
			
			for(int j = 0; j < width; ++j){
				
				State temp = rows.get(i).get(j);
				g.setColor(color);
				g.drawRect(x + len * i, y + len * j, len, len);
				boolean x_shape = false;
				boolean circle_shape = false;
				
				if(temp.getState() == 0 || temp.getState() == 5) g.setColor(Color.white);
				else{
					
					if(temp.getState() == 1) {
						
						g.setColor(Color.GREEN);
						if(i == temp.getOriginX() && j == temp.getOriginY()) 
							circle_shape = true;
					}
					else if(temp.getState() == 2) g.setColor(light_green);
					else if(temp.getState() == 3) g.setColor(Color.pink);
					else if(temp.getState() == 4) g.setColor(gray);
					else if(temp.getState() == 6) {g.setColor(Color.white); x_shape = true;}
					else if(temp.getState() == 8) g.setColor(Color.blue);
					else if(temp.getState() == 9) g.setColor(Color.red);
					else if(temp.getState() == 10) {
						
						g.setColor(light_green);
						if(i == temp.getOriginX() && j == temp.getOriginY()) 
							circle_shape = true;
					}
					g.fillRect(x + len * i + 1, y + len * j + 1, len - 1, len - 1);
					
					if(x_shape){
						
						g.setColor(Color.red);
						g.drawLine(x + len * i + 1, y + len * j + 1, x + len * i + len, y + len * j + len);
						g.drawLine(x + len * i + len, y + len * j, x + len * i, y + len * j + len);
					}
					else if(circle_shape){
						
						int cornerX = x + len * i + 1;
						int cornerY = y + len * j + 1;
						
						g.setColor(Color.red);
						g.fillOval(cornerX + 6, cornerY + 6, len - 14, len - 14);
					}
					
					
					if(temp.getState() != 4 && temp.getState() != 6){
						
						g.setColor(Color.black);
				
						if(temp.getMargin(0)){
					
							g.fillRect(x + len * i, y + len * j + 1, len + 1, 2);
						}
						if(temp.getMargin(1)){
						
							g.fillRect(x + len * i, y + len * (j + 1) - 2, len + 1, 2);
						}
						if(temp.getMargin(2)){
						
							g.fillRect(x + len * i + 1, y + len * j, 2, len + 1);
						}
						if(temp.getMargin(3)){
						
							g.fillRect(x + len * (i + 1) - 2, y + len * j, 2, len + 1);
						}
					}
				}
			}
		}
	}
	
	public void addShape(Shape shape){
		
		shapes.add(shape);
	}
	
	public void removeShape(Shape shape){
		
		shapes.remove(shape);
	}
	
	public void setWidth(int width){
		
		this.width = width;
	}
	
	public void setHeight(int height){
		
		this.height = height;
	}
	public void setNumberOfShapes(int shapes){
		
		remainedShapes = shapes;
	}
	public int getNumberOfShapes(){
		
		return remainedShapes;
	}
	public void rotateClock(){
		
		rotation = rotation % 4 + 1;
		shadow(main.getCursorRelativeToFrame()); //Pentru refresh
	}
	public void rotateCounterClock(){
		
		rotation--;
		if(rotation == 0) rotation = 4;
		shadow(main.getCursorRelativeToFrame()); //Pentru refresh
	}
	public void setColor(Color color){
		
		this.color = color;
	}
	public ArrayList<ArrayList<State>> getRows(){
		
		return rows;
	}
	public boolean setRows(ArrayList<ArrayList<State>> rows){
		
		//remainedShapes = 0; //Temporary
		
		if(rows.size() == height){
			
			int len = rows.size();
			for(int i = 0; i < len; ++i){
				
				if(rows.get(i).size() != width) return false;
			}
			
			for(int i = 0; i < width; ++i)
				for(int j = 0; j < height; ++j){
					
					State globalRows = this.rows.get(i).get(j);
					State importRows  = rows.get(i).get(j);
					
					globalRows.setState(importRows.getState());
					globalRows.setOrigin(importRows.getOriginX(), importRows.getOriginY());
					globalRows.setRotation(importRows.getRotation());
					globalRows.setMargin(importRows.getMargin());
				}
				
			//this.rows = new ArrayList<ArrayList<State>>(rows);
			return true;
		}
		return false;
	}
}
