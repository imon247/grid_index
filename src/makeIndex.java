import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class makeIndex {
	public static void duplicate_elimination(String data_path, String data_path_new) throws FileNotFoundException, IOException{
		File dataFile = new File(data_path);
		File outputFile = new File(data_path_new);
		BufferedReader reader = new BufferedReader(new FileReader(dataFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		
		String line;
		HashMap<DataPoint, Integer> map = new HashMap<DataPoint, Integer>();
		
		
		String[] splitted = new String[5]; 
		
		while((line = reader.readLine())!=null) {
			splitted = line.split("\t");
			int current = Integer.parseInt(splitted[4]); 
			DataPoint dp = DataPoint.createPoint(Double.parseDouble(splitted[2]), Double.parseDouble(splitted[3]), current);
			if(map.keySet().contains(dp)) {
				int old = map.get(dp);
				if(old > current) {
					map.remove(dp);
					map.put(dp, current);
				}
			}
			else {
				map.put(dp, current);
			}
		}
		reader.close();
		for(DataPoint dp : map.keySet()) {
			writer.append(String.format("%s\t%s\t%d\n",dp.getX(), dp.getY(),map.get(dp)));
		}
		writer.close();
		System.out.println(String.format("Size after elimination: %d", map.size()));
	}
	public static void create_index(String data_path_new, String index_path, int n) throws IOException{
		// To create a grid index and save it to file on "index_path".
		// The output file should contain exactly n*n lines. If there is no point in the cell, just leave it empty after ":".
		ArrayList<DataPoint>[][] index = new ArrayList[n][n];
		
		File inputFile = new File(data_path_new);
		File outputFile = new File(index_path);
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
//		double x_interval = 180.0/n;
//		double y_interval = 353.8/n;
		double x_interval = 10.0/n;
		double y_interval = 10.0/n;
		
		String line;
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				index[i][j] = new ArrayList<DataPoint>();
			}
		}
		
		String[] splitted = new String[3];
		
		while((line = reader.readLine())!=null) {
			splitted = line.split("\t");
			DataPoint dp = DataPoint.createPoint(Double.parseDouble(splitted[0]), Double.parseDouble(splitted[1]), Integer.parseInt(splitted[2]));
//			int grid_x = (int) Math.floor((dp.getX()+90)/x_interval);
//			int grid_y = (int) Math.floor((dp.getY()+176.30859376)/y_interval);
			int grid_x = (int) Math.floor(dp.getX()/x_interval);
			int grid_y = (int) Math.floor(dp.getY()/y_interval);
			if(grid_x>=n) grid_x = n-1;
			if(grid_y>=n) grid_y = n-1;
			index[grid_x][grid_y].add(dp);
		}
		reader.close();
		
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				writer.append(String.format("Cell %d, %d: ", i, j));
				for(DataPoint dp : index[i][j]) {
					writer.append(String.format("%d_%s_%s ", dp.getId(), dp.getX(), dp.getY()));
				}
				writer.append("\n");
			}
		}
		writer.close();
	}
	public static void main(String[]args) throws FileNotFoundException, IOException{
  		if(args.length != 4){
  			System.out.println("Usage: java makeIndex DATA_PATH INDEX_PATH DATA_PATH_NEW N");
  			/*
			DATA_PATH(String): the file path of Gowalla_totalCheckins.txt
			INDEX_PATH(String): the output file path of the grid index
			DATA_PATH_NEW(String): the file path of the dataset without duplicates
  			N(integer): the grid index size
			*/
  			return;
  		}
		duplicate_elimination(args[0], args[2]);
		long s = System.currentTimeMillis();
  		create_index(args[2], args[1], Integer.parseInt(args[3]));
		long t = System.currentTimeMillis();
		System.out.println("Index construction time: "+(t-s));
  	}
}
