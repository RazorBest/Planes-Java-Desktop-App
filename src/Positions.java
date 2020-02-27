import java.util.ArrayList;

public class Positions {
	
	private int shapes;
	private Shape shape;
	private ArrayList<Integer> x = new ArrayList<Integer>();
	private ArrayList<Integer> y = new ArrayList<Integer>();
	private ArrayList<Integer> rotation = new ArrayList<Integer>();
	
	private boolean print = true;
	
	public Positions(int shapes, Shape shape){
		
		this.shapes = shapes;
		this.shape = shape;
	}
	
	public Positions(Positions pos){
		
		this.shape = pos.getShape();
		int len = pos.getLen();
		for(int i = 0; i < len; ++i){
			
			x.add(pos.getX(i));
			y.add(pos.getY(i));
			rotation.add(pos.getRotation(i));
		}
	}
	
	public void addShape(int x, int y, int rotation){
		
		this.x.add(x);
		this.y.add(y);
		this.rotation.add(rotation);
	}
	
	public Shape getShape(){
		
		return shape;
	}
	public ArrayList<Integer> getListX(){
		
		return x;
	}
	public ArrayList<Integer> getListY(){
		
		return y;
	}
	public ArrayList<Integer> getListRotation(){
	
	return rotation;
}
	public int getX(int i){
		
		return x.get(i);
	}
	public int getY(int i){
		
		return y.get(i);
	}
	public int getRotation(int i){
	
	return rotation.get(i);
	}
	public int getLen(){
		
		return x.size();
	}
	public void remove(int i){
		
		x.remove(i);
		y.remove(i);
		rotation.remove(i);
	} 
	public void removeLast(){
		
		x.remove(x.size() - 1);
		y.remove(y.size() - 1);
		rotation.remove(rotation.size() - 1);
		
	}
	public boolean collision(){
		
		int len = x.size();
		int n = shape.getLen();
		boolean ok = false;
		
		for(int i = 0; i < len; ++i){
			
			for(int j = i + 1; j < len; ++j){
				
				for(int k1 = 0; k1 < n; ++k1){
					
					for(int k2 = 0; k2 < n; ++k2){
						if(x.get(i) + shape.getShapeY(k1, rotation.get(i)) == x.get(j) + shape.getShapeY(k2, rotation.get(j))){
							if(y.get(i) + shape.getShapeX(k1, rotation.get(i)) == y.get(j) + shape.getShapeX(k2, rotation.get(j)))
								return false;
						}
					}
				}
			}
		}
		return true;
	}
	public boolean inBounds(int width, int height){
		
		int len = x.size();
		int n = shape.getLen();
		for(int i = 0; i < len; ++i){
				
			for(int k = 0; k < n; ++k){
				
				int xs = x.get(i) + shape.getShapeY(k, rotation.get(i));    //A little problem here
				int ys = y.get(i) + shape.getShapeX(k, rotation.get(i));
						
				if(xs < 0 || xs >= width || ys < 0 || ys >= height){
							
						return false;
				}
			}
		}
		return true;
	}
}
