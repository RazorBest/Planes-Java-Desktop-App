import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Main extends Canvas implements Runnable{

	private static final long serialVersionUID = -6800175946442857266L;
	
	public static final int WIDTH = 720, HEIGHT = WIDTH / 12 * 9;
	private boolean running = false;
	private Thread thread;
	private FileController file;
	private Handler handler;
	
	private Menu menu;
	private Help help;
	private DifficultyChoose difficultyChoose;
	private Preparation preparation;
	private Game game;
	private GameEnd gameEnd;
	private Opponent opponent;
	
	private Shape planeShape;
	private String planeShapePath = "res\\plane_shape.txt";
	
	private Point frameLocation;
	private int frameX, frameY;
	private int cursorX, cursorY;
	private ArrayList<ArrayList<State>> playerGrid = new ArrayList<ArrayList<State>>();
	
	public static enum STATE{
		
		Menu,
		Help,
		DifficultyChoose,
		Preparation,
		Game,
		GameEnd;
	}
	
	public STATE state;
	
	public Main(){
		
		file = new FileController();
		loadDefaultShape();
		handler = new Handler();
		state = STATE.Menu;
		opponent = new Opponent(this, planeShape, 10, 10);
		game = new Game(this, handler, opponent, planeShape);
		gameEnd = new GameEnd(this);
		preparation = new Preparation(this, handler, game, opponent, planeShape, 3);
		difficultyChoose = new DifficultyChoose(this, preparation, opponent);
		help = new Help(this);
		menu = new Menu(this, help, handler, difficultyChoose);
		this.addKeyListener(new KeyInput(this, preparation));
		new Window(WIDTH, HEIGHT, "Planes", this);
		this.addMouseListener(menu);
		this.addMouseMotionListener(menu);
	}
	
	public synchronized void start(){
		
		thread = new Thread(this);
		running = true;
		thread.start();
	}
	
	public synchronized void stop(){
		
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >=1)
			{
				tick();
				delta--;
			}
			if(running)
				render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	public void newGame(){
		
		System.out.println("NEW GAME");
		
		state = STATE.Game;
		opponent.givePlayerRows(playerGrid);
		game.initGrids();
		game.initOpponentTable();
		this.addMouseListener(game);
		this.addMouseMotionListener(game);
	}
	
	public void endGame(){
		
		System.out.println("Game Ended");
		
		opponent.reInit();
		game.reInit();
		preparation.initGrids();
		this.removeMouseListener(game);
		this.removeMouseMotionListener(game);
		
	}
	public void goToGameEnd(int win, int turns){
		
		state = STATE.GameEnd;
		gameEnd.setWin(win);
		gameEnd.setTurns(turns);
		this.addMouseListener(gameEnd);
		this.addMouseMotionListener(gameEnd);
	}
	public void goToMenu(){
		
		state = STATE.Menu;
		this.addMouseListener(menu);
		this.addMouseMotionListener(menu);
	}
	public void setPlayerGrid(ArrayList<ArrayList<State>> playerGrid){
		
		System.out.println("Size: " + playerGrid.size());
		
		this.playerGrid = new ArrayList<ArrayList<State>>(playerGrid);
	}
	
	public ArrayList<ArrayList<State>> getPlayerGrid(){
		
		return playerGrid;
	}
	private void loadDefaultShape(){
		
		StringBuilder sb = new StringBuilder(file.readFile(planeShapePath));
		int i = 0, k = 0;                    //Contorul i pentru fiecare numar, iar k pentru parcurgerea efectiva a sirului de caractere
		while(sb.charAt(k) < '0' || sb.charAt(k) > '9' && sb.charAt(k) != '-') k++;
		int n = nextNumber(sb.substring(k)); //Numarul de coordonate pentru forma
		int[][] shape = new int[500][2];
		
		for(i = 0; i < n; ++i){
			
			while(sb.charAt(k) >= '0' && sb.charAt(k) <= '9' || sb.charAt(k) == '-') k++;  //Trece peste urmatorul numar
			while((sb.charAt(k) < '0' || sb.charAt(k) > '9') && sb.charAt(k) != '-') k++;  //Ajunge la cel mai apropiat numar
			shape[i][0] = nextNumber(sb.substring(k));
			
			while(sb.charAt(k) >= '0' && sb.charAt(k) <= '9' || sb.charAt(k) == '-') k++;  //Trece peste urmatorul numar
			while((sb.charAt(k) < '0' || sb.charAt(k) > '9') && sb.charAt(k) != '-')  k++;   //Ajunge la cel mei apropiat numar
			shape[i][1] = nextNumber(sb.substring(k));
			
		}
		planeShape = new Shape(shape, n);
		
		for(i = 0; i < n; ++i){
			
			System.out.println(shape[i][0] + " " + shape[i][1]);
		}
	}
	private int nextNumber(String s){
		
		int n = 0, sign = 1;
		int i = 0;
		if(s.regionMatches(0, "-", 0, 1)) {sign = -1; i++;}
		while(s.charAt(i) >= '0' && s.charAt(i) <= '9'){
			n = n * 10 + Integer.parseInt(s.substring(i, i + 1)) * sign;
			++i;
		}
		return n;
	}
	
	public void tick(){
		
		frameLocation = this.getLocationOnScreen();
		frameX = (int)frameLocation.getX();
		frameY = (int)frameLocation.getY();
		PointerInfo mouseInfo = MouseInfo.getPointerInfo();
		Point mouseLocation = mouseInfo.getLocation();
		cursorX = (int)mouseLocation.getX();
		cursorY = (int)mouseLocation.getY();
		
		if(state == STATE.Menu){
			
			menu.tick();
		}
		else if(state == STATE.DifficultyChoose){
			
			difficultyChoose.tick();
		}
		else if(state == STATE.Preparation){
			
			preparation.tick();
		}
		else if(state == STATE.Game){
			
			game.tick();
		}
	}
	
	public void render(){
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		/////////////////////////////////////
		
		if(state == STATE.Menu) menu.render(g);
		
		else if(state == STATE.Help) help.render(g);
		
		else if(state == STATE.DifficultyChoose) difficultyChoose.render(g);
		
		else if(state == STATE.Preparation) preparation.render(g);
		
		else if(state == STATE.Game) game.render(g);
		
		else if(state == STATE.GameEnd) gameEnd.render(g);
		/////////////////////////////////////
		g.dispose();
		bs.show();
	}
	
	public Point getFrameLocation(){
		
		return frameLocation;
	}
	
	public Point getCursorRelativeToFrame(){
		
		int relativeX = cursorX - frameX; 
		int relativeY = cursorY - frameY;
		return new Point(relativeX, relativeY);
		
	}
	
	public static void main(String args[]){
		
		new Main();
	}
	
}
