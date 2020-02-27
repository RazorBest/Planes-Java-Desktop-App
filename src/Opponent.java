import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Opponent {
	
	private Main main;
	
	private Shape shape;
	private ArrayList<ArrayList<State>> rows = new ArrayList<ArrayList<State>>();
	private ArrayList<ArrayList<State>> player_rows = new ArrayList<ArrayList<State>>();
	private ArrayList<ArrayList<State>> known_rows = new ArrayList<ArrayList<State>>();
	private ArrayList<ArrayList<IntBoxer>> frequency = new ArrayList<ArrayList<IntBoxer>>();      //Va retine de cate ori un avion este pe aceea celula, pentru toate posibilitatile 
	private ArrayList<ArrayList<IntBoxer>> frequency_head = new ArrayList<ArrayList<IntBoxer>>(); //Va retinr de cate ori un cap va fi pe aceea celula, pentru toate posibilitatile
	private final int width, height;
	private ArrayList<Positions> list = new ArrayList<Positions>();
	private ArrayList<Boolean> list_possible = new ArrayList<Boolean>();
	private Positions tempPos;
	private final Random rand = new Random();
	private int known_cells = 0;
	private int known_heads = 0;

	private int cnt = 0; //Temporary
	
	public static enum DIFFICULTY{
		
		Baby,
		Easy,
		Medium,
		Hard;
	}
	
	private DIFFICULTY difficulty;
	
	public Opponent(Main main, Shape shape, int width, int height){
		
		this.main = main;
		
		this.width = width;
		this.height = height;
		
		for(int i = 0; i < width; ++i){
			
			rows.add(new ArrayList<State>());
			known_rows.add(new ArrayList<State>());
			frequency.add(new ArrayList<IntBoxer>());
			frequency_head.add(new ArrayList<IntBoxer>());
			for(int j = 0; j < height; ++j){
				
				rows.get(i).add(new State());
				known_rows.get(i).add(new State());
				frequency.get(i).add(new IntBoxer(0));
				frequency_head.get(i).add(new IntBoxer(0));
			}
		}
		
		this.shape = shape;
		
		tempPos = new Positions(3, shape);
		init(3);
		System.out.println("List size: " + list.size());
	}
	
	private void init(int shapes){
		
		for(int i = 0; i < width; ++i){
				
			for(int j = 0; j < height; ++j){
				
				for(int rot = 1; rot <= 4; ++rot){
					
					tempPos.addShape(i, j, rot);
					
					if(tempPos.inBounds(width, height)){
						if(shapes > 1) init(shapes - 1);
						else{
						
							if(tempPos.collision() && tempPos.inBounds(width, height)){
								
								list.add(new Positions(tempPos));
								list_possible.add(true);
							}
						}
					}
					tempPos.removeLast();
				}
			}
		}
	}
	
	public ArrayList<ArrayList<State>> random(int shapes){
		
		int k = rand.nextInt(list.size());
		for(int i = 0; i < width; ++i){
			
			for(int j = 0; j < height; ++j){
				
				rows.get(i).get(j).setState(0);
				rows.get(i).get(j).removeMargin();
			}
		}
		for(int i = 0; i < 3; ++i){
			
			int y = list.get(k).getX(i);
			int x = list.get(k).getY(i);
			int rotation = list.get(k).getRotation(i);
			
			int len = shape.getLen();
			for(int j = 0; j < len; ++j){
				
				if(x + shape.getShapeX(j, rotation) >= 0 && x + shape.getShapeX(j, rotation) < width)
					if(y + shape.getShapeY(j, rotation) >= 0 && y + shape.getShapeY(j, rotation) < height)
						{
							int cellX = x + shape.getShapeX(j, rotation);
							int cellY = y + shape.getShapeY(j, rotation);
							State cell = rows.get(cellX).get(cellY);
							ArrayList<Boolean> margin = shape.getMargin(j, rotation);
							cell.setState(1);
							cell.setOrigin(x, y);
							cell.setRotation(rotation);
							cell.setMargin(margin);
						}
			}
		}
		cnt++;
		return rows;
	}
	
	public void setFrequency(){
		
		for(int i = 0; i < width; ++i){
			
			for(int j = 0; j < height; ++j){				
				
				IntBoxer freq = frequency.get(i).get(j);
				IntBoxer freqHead = frequency_head.get(i).get(j);
				freqHead.set(0);
				freq.set(0);
			}
		}
		
		
		int len = list.size();
		
		for(int i = 0; i < len; ++i){
			
			if(list_possible.get(i)){
			
				Positions tempPos = list.get(i);
				boolean possible = true;
				int reached = 0;
				int reached_heads = 0;
				
				for(int j = 0; j < 3; ++j){
				
					int y = tempPos.getX(j); //Sunt invers
					int x = tempPos.getY(j);
					int rotation = tempPos.getRotation(j);
					int n = shape.getLen();
					
					for(int k = 0; k < n; ++k){
						State cell = known_rows.get(x + shape.getShapeX(k, rotation)).get(y + shape.getShapeY(k, rotation));
						
						if(cell.getState() == 7)
							{possible = false; break;}
						else if(cell.getState() == 1)
							{
								//if()
								reached++;
							}
						else if(cell.getState() == 8)
							{
								if(k == 0)
									reached_heads++;
								else
									{
										possible = false;
										break;
									}
							}
					}
					if(!possible) break;
				}
				
				if(!possible || reached < known_cells || reached_heads < known_heads) list_possible.set(i, false);
				else{
					
					for(int j = 0; j < 3; ++j){
						
						int y = tempPos.getX(j); //Sunt invers
						int x = tempPos.getY(j);
						int rotation = tempPos.getRotation(j);
						int n = shape.getLen();
						
						for(int k = 0; k < n; ++k){
							
							//int cellState = known_rows.get(x + shape.getShapeX(k, rotation)).get(y + shape.getShapeY(k, rotation)).getState();
							
							IntBoxer cellFreq = frequency.get(x + shape.getShapeX(k, rotation)).get(y + shape.getShapeY(k, rotation));
							
							cellFreq.set(cellFreq.get() + 1);
							
							if(k == 0){
								
								IntBoxer cellFreqHead = frequency_head.get(x + shape.getShapeX(k, rotation)).get(y + shape.getShapeY(k, rotation));
								cellFreqHead.set(cellFreqHead.get() + 1);
							}
						}
					}
				}
			}
		}
	}
	
	public Point baby(){
		
		int min = 4000000;
		int min_heads = 4000000;
		int n_min = 0;
		int n_min_heads = 0;
		Random rnd = new Random();
		int n;
		
		for(int i = 0; i < width; ++i){
			
			for(int j = 0; j < height; ++j){	
				
				int freq = frequency.get(i).get(j).get();
				int freq_heads = frequency_head.get(i).get(j).get();
				
				if(known_rows.get(i).get(j).getState() == 0) {
					
					if(freq < min)
						{min = freq; n_min = 1;}
					else if(freq == min)
						n_min++;
					
					if(freq_heads < min_heads)
						{min_heads = freq_heads; n_min_heads = 1;}
					else if(freq_heads == min_heads)
						n_min_heads++;
				}    
			}
		}	
		
		n = rnd.nextInt(n_min_heads) + 1;
		
		System.out.println("Random number: " + n);
		
		//System.out.println("Max: " + max + "   Max Heads: " + max_heads);
		
		for(int i = 0; i < width /*n > 0*/; ++i){
			
			for(int j = 0; j < height/*n > 0*/; ++j){	
				
				int freq = frequency.get(i /*% width*/).get(j /*% height*/).get();
				int freq_head = frequency_head.get(i /*% width*/).get(j /*% height*/).get();
				
				if(freq_head == min_heads && known_rows.get(i).get(j).getState() == 0 /*&& max_heads * 20 > max known_heads > 0*/) {
					n--;
					System.out.println(n);
					if(n <= 0){
						System.out.println("MAX:" + min_heads);
						return new Point(i /*% width*/, j /*% height*/);
					}
				}
				/*else if(freq == max && known_rows.get(i).get(j).getState() == 0 && max >= max_heads * 20 /*known_heads == 0){
					
					/*System.out.println("Returned body");
					return new Point(i/*%width, j/*%height);
				}*/
			}
		}
		System.out.println("Returned null");
		return null;
	}
	
	public Point easy(){
		
		int max = -1;
		int max_heads = -1;
		int n_max = 0;
		int n_max_heads = 0;
		Random rnd = new Random();
		int n, m;
		
		for(int i = 0; i < width; ++i){
			
			for(int j = 0; j < height; ++j){	
				
				int freq = frequency.get(i).get(j).get();
				int freq_heads = frequency_head.get(i).get(j).get();
				
				if(known_rows.get(i).get(j).getState() == 0) {
					
					if(freq > max)
						{max = freq; n_max = 1;}
					else if(freq == max)
						n_max++;
					
					if(freq_heads > max_heads)
						{max_heads = freq_heads; n_max_heads = 1;}
					else if(freq_heads == max_heads)
						n_max_heads++;
				}    
			}
		}	
		
		n = rnd.nextInt(n_max_heads) + 1;
		m = rnd.nextInt(n_max) + 1;
		
		//System.out.println("Random number: " + n);
		
		System.out.println("Max: " + max + "   Max Heads: " + max_heads);
		
		for(int i = 0; i < width /*n > 0*/; ++i){
			
			for(int j = 0; j < height/*n > 0*/; ++j){	
				
				int freq = frequency.get(i /*% width*/).get(j /*% height*/).get();
				int freq_head = frequency_head.get(i /*% width*/).get(j /*% height*/).get();
				if(freq_head == max_heads && known_rows.get(i).get(j).getState() == 0 && (max_heads * 3 > max || known_heads > 2)) {
					n--;
					System.out.println(n);
					if(n <= 0){
						System.out.println("MAX:" + max_heads);
						return new Point(i /*% width*/, j /*% height*/);
					}
				}
				else if(freq == max && known_rows.get(i).get(j).getState() == 0 && max >= max_heads * 3 && known_heads <= 2){
					System.out.println("Pe aici");
					m--;
					if(m <= 0)
					{
						System.out.println("Returned body");
						return new Point(i , j);
					}
				}
			}
		}
		System.out.println("Returned null");
		return null;
	}
	
	public Point medium(){
		
		int max = -1;
		int max_heads = -1;
		int n_max = 0;
		int n_max_heads = 0;
		Random rnd = new Random();
		int n;
		
		for(int i = 0; i < width; ++i){
			
			for(int j = 0; j < height; ++j){	
				
				int freq = frequency.get(i).get(j).get();
				int freq_heads = frequency_head.get(i).get(j).get();
				
				if(known_rows.get(i).get(j).getState() == 0) {
					
					if(freq > max)
						{max = freq; n_max = 1;}
					else if(freq == max)
						n_max++;
					
					if(freq_heads > max_heads)
						{max_heads = freq_heads; n_max_heads = 1;}
					else if(freq_heads == max_heads)
						n_max_heads++;
				}    
			}
		}	
		
		if(n_max_heads != 0)
			n = rnd.nextInt(n_max_heads) + 1;
		else n = 1;
		
		System.out.println("Random number: " + n);
		
		//System.out.println("Max: " + max + "   Max Heads: " + max_heads);
		
		for(int i = 0; i < width /*n > 0*/; ++i){
			
			for(int j = 0; j < height/*n > 0*/; ++j){	
				
				int freq = frequency.get(i /*% width*/).get(j /*% height*/).get();
				int freq_head = frequency_head.get(i /*% width*/).get(j /*% height*/).get();
				
				if(freq_head == max_heads && known_rows.get(i).get(j).getState() == 0 /*&& max_heads * 20 > max known_heads > 0*/) {
					n--;
					System.out.println(n);
					if(n <= 0){
						System.out.println("MAX:" + max_heads);
						return new Point(i /*% width*/, j /*% height*/);
					}
				}
			}
		}
		System.out.println("Returned null");
		return null;
	}
	
	public Point hard(){
		
		Random rnd = new Random();
		boolean flag;
		int k = 2;
		
		while(k > 0){
			
			int n = rnd.nextInt(10);
			int m = rnd.nextInt(10);
			
			flag = false;
			
			for(int i = n; i < width * 2; ++i){
				
				for(int j = m; j < height * 2; ++j){
					
					State tempState = known_rows.get(i % width).get(j % height);
					
					if(tempState.getState() == 0){
						
						State cellState = player_rows.get(i % width).get(j % height);
						
						if(cellState.getState() == 1) {
							
							if(cellState.getOriginX() == i % width && cellState.getOriginY() == j % height)
								{/*tempState.setState(8); known_heads++;*/ System.out.println(i % width + " " + j % height + " is head");}
							else
								{tempState.setState(1); known_cells++; System.out.println(i % width + " " + j % height + " is plane body"); flag = true; break;}
						}
						else if(cellState.getState() == 0)
							{tempState.setState(7); System.out.println(i % width + " " + j % height + " is nothing"); flag = true; break;}
						
					}
				}
				if(flag) break;
			}
			
			k--;
		}
		
		setFrequency();
		
		return medium();
	}
	
	public Point Turn(){
		
		setFrequency();
		
		if(difficulty == DIFFICULTY.Baby)
			return baby();
		
		else if(difficulty == DIFFICULTY.Easy)
			return easy();
		
		else if(difficulty == DIFFICULTY.Medium)
			return medium();
		
		else if(difficulty == DIFFICULTY.Hard)
			return hard();
		
		
		return null;
	}
	
	public void addShape(Shape shape){
		
		this.shape = shape;
		
		init(3);
	}
	
	public void setCell(Point p, int state){
		
		int px = (int)p.getX();
		int py = (int)p.getY();
		
		if(state == 1) {
			
			if(known_rows.get(px).get(py).getState() == 0)  //Asta ar trebui sa se intaple intotdeauna
				known_cells++;
			
			known_rows.get(px).get(py).setState(state);
			
		}
		else if(state == 8) {
			
			if(known_rows.get(px).get(py).getState() == 0)  //Asta ar trebui sa se intample intotdeauna
				known_heads++;
			
			System.out.println("Head");
			
			known_rows.get(px).get(py).setState(state);
		}
		else known_rows.get(px).get(py).setState(state);
		
		
		
		//System.out.println(px + " " + py + " " + known_rows.get(px).get(py).getState());
	}
	
	public void reInit(){
		
		for(int i = 0; i < width; ++i){
			
			for(int j = 0; j < height; ++j){
				
				known_rows.get(i).set(j, new State());
				frequency.get(i).get(j).set(0);
				frequency_head.get(i).get(j).set(0);
			}
		}
		
		int len = list_possible.size();
		
		for(int i = 0; i < len; ++i)
			
			list_possible.set(i, true);
		
		known_cells = 0;
		known_heads = 0;
	}
	
	public void setDifficulty(DIFFICULTY difficulty){
		
		this.difficulty = difficulty;
	}
	
	public DIFFICULTY getDifficulty(){
		
		return difficulty;
	}
	
	public void givePlayerRows(ArrayList<ArrayList<State>> player_rows){
		
		this.player_rows = player_rows;
	}
}

