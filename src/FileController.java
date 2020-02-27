
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileController {
	
	
	public void writeFile(String path, String content)
	{
		File file = new File(path);
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String readFile(String path)
	{
		File file = new File(path);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String content = "", line = "";
				do{
					content += line + "\n";
					line = br.readLine();
				}while(line != null);
			br.close();
			return content;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
}