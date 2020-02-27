import java.util.ArrayList;

public class Shape {
	
	private int[][] shape1; //Figura initiala
	private int[][] shape2; //Figura intoarsa cu 90 de grade in sens invers acelor de ceasornic
	private int[][] shape3; //Figura intoarsa cu 180 de grade
	private int[][] shape4; //Figura intoarsa cu 90 de grade in sensul acelor de ceasornic
	private ArrayList<ArrayList<Boolean>> margin = new ArrayList<ArrayList<Boolean>>();
	private int len;
	
	public Shape(int[][] shape, int len){
		
		shape1 = new int[len][2];   //Initializarea vectorilor
		shape2 = new int[len][2]; 
		shape3 = new int[len][2];
		shape4 = new int[len][2];
		this.len = len;
		copyArray(shape1, shape, len, 2);
		copyArray(shape2, shape, len, 2);
		copyArray(shape3, shape, len, 2);
		copyArray(shape4, shape, len, 2);
		
		System.out.println("SHAPE:");
		for(int i = 0; i < len; ++i){
			
			System.out.println(shape1[i][0] + " " + shape1[i][1]);
		}
		
		for(int i = 0; i < len; ++i){
			
			shape2[i][0] = shape1[i][1];
			shape2[i][1] = -shape1[i][0];
			shape3[i][0] = -shape1[i][0];
			shape3[i][1] = -shape1[i][1];
			shape4[i][0] = -shape2[i][0];
			shape4[i][1] = -shape2[i][1];
		}
		
		makeMargin(shape1);
		makeMargin(shape2);
		makeMargin(shape3);
		makeMargin(shape4);
	}
	
	private void makeMargin(int[][] shape){
		
		//Global: len
		for(int i = 0; i < len; ++i){
			
			int shapeX = shape[i][0];
			int shapeY = shape[i][1];
			margin.add(new ArrayList<Boolean>(4));
			ArrayList<Boolean> temp = margin.get(margin.size() - 1);
			for(int k = 0; k < 4; ++k) temp.add(true);
			for(int j = 0; j < len; ++j){
				
				if(shapeX == shape[j][0]){
					
					if(shapeY == shape[j][1] + 1) temp.set(0, false);
					else if(shapeY == shape[j][1] - 1) temp.set(1, false);
				}
				else if(shapeY == shape[j][1]){
					
					if(shapeX == shape[j][0] + 1) temp.set(2, false);
					else if(shapeX == shape[j][0] - 1) temp.set(3, false);
				}
			}
		}
	}
	
	public ArrayList<Boolean> getMargin(int i, int rotation/*[1,4]*/){
		
		return margin.get(i + (rotation - 1) * len);
	}
	
	private void copyArray(int[][] a, int[][] b, int len1, int len2){
		
		for(int i = 0; i < len1; ++i){
			for(int j = 0; j < len2; ++j){
				
				a[i][j] = b[i][j];
			}
		}
	}
	
	public int getLen(){
		
		return len;
	}
	
	public int getShapeX(int i, int rotation/*[1-4]*/){
		
		if(rotation == 1) return shape1[i][0];
		else if(rotation == 2) return shape2[i][0];
		else if(rotation == 3) return shape3[i][0];
		else if(rotation == 4) return shape4[i][0];
		
		return 0;
	}
	
	public int getShapeY(int i, int rotation/*[1-4]*/){
		
		if(rotation == 1) return shape1[i][1];
		else if(rotation == 2) return shape2[i][1];
		else if(rotation == 3) return shape3[i][1];
		else if(rotation == 4) return shape4[i][1];
		
		return 0;
	}
}
