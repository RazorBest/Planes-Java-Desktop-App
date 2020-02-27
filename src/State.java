import java.util.ArrayList;

public class State {
	
	private int state = 0;
	private int originX = -1, originY = -1;
	private int rotation;
	private boolean hidden = false;
	private boolean clickable = true;
	private ArrayList<Boolean> margin = new ArrayList<Boolean>(4);
	
	public State(){
		
		for(int i = 0; i < 4; ++i)
			margin.add(false);
	}
	
	public int getState(){
		
		return state;
	}
	public void setState(int state){
		
		/*
		 * 0 for white 
		 * 1 for green(plane on) 
		 * 2 for light green (reachable cell)
		 * 3 for red(unreachable cell)
		 * 4 for grey (when mouse over cell)
		 * 5 for white but plane on it(hidden)
		 * 6 for an x(incorrect clicked cell)
		 * 7 for incorrect(no color)
		 */
		
		this.state = state;
	}
	public void setOrigin(int originX, int originY){
		
		this.originX = originX;
		this.originY = originY;
	}
	public int getOriginX(){
		
		return originX;
	}
	
	public int getOriginY(){
		
		return originY;
	}
	public boolean originExist(){
		
		if(originX == -1 && originY == -1) return false;
		
		return true;
	}
	public void clearOrigin(){
		
		originX = -1;
		originY = -1;
	}
	public void setRotation(int rotation){
		
		this.rotation = rotation;
	}
	public int getRotation(){
		
		return rotation;
	}
	public void setMargin(ArrayList<Boolean> margin){
	
		for(int i = 0; i < 4; ++i){
			
			this.margin.set(i, margin.get(i));
		}
	}
	public ArrayList<Boolean> getMargin(){
		
		return margin;
	}
	public boolean getMargin(int i/*[0-3]*/){
		
		return margin.get(i);
	}
	public void removeMargin(){
		
		for(int i = 0; i < 4; ++i){
			
			margin.set(i, false);
		}
	}
	public void setHide(boolean hidden){
		
		this.hidden = hidden;
	}
	
	public boolean isHidden(){
		
		return hidden;
	}
	public void setClickable(boolean clickable){
		
		this.clickable = clickable;
	}
	
	public boolean isClickable(){
		
		return clickable;
	}
}
