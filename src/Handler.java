import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {

	private LinkedList<GameObject> list = new LinkedList<GameObject>();
	
	public void addObject(GameObject object){
		
		list.add(object);
	}
	
	public void removeObject(GameObject object){
		
		int len = list.size();
		for(int i = 0; i < len; ++i)
			if(list.get(i) == object) list.remove(object);
	}
	public void removeAll(){
		
		while(list.size() > 0) list.removeLast();
	}
	public void tick(){
		
		for(int i = 0; i < list.size(); ++i){
			
			list.get(i).tick();
		}
	}
	
	public void render(Graphics g){
		
		for(int i = 0; i < list.size(); ++i)
			list.get(i).render(g);
	}
}	
