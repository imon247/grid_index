import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getResults{
	public static ArrayList<DataPoint>[][] construct_index(String index_path, int n) throws IOException, FileNotFoundException {
		ArrayList<DataPoint>[][] index = new ArrayList[n][n];
		File inputFile = new File(index_path);
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String line;
		String cell_string;
		String[] dp_string;
		
		String cell_pattern = "Cell (?<gridx>\\d+), (?<gridy>\\d+)";
		String dp_pattern = "(?<id>.*)_(?<x>.*)_(?<y>.*)";
		
		Pattern r1 = Pattern.compile(cell_pattern);
		Pattern r2 = Pattern.compile(dp_pattern);
		
		int grid_x, grid_y;
		
		while((line = reader.readLine()) != null) {
			cell_string = line.split(":")[0];
			dp_string = line.split(":")[1].split(" ");
			Matcher m1 = r1.matcher(cell_string);
			Matcher m2;
			if(m1.find()) {
				grid_x = Integer.parseInt(m1.group("gridx"));
				grid_y = Integer.parseInt(m1.group("gridy"));
				index[grid_x][grid_y] = new ArrayList<DataPoint>();

				for(String s: dp_string) {
					m2 = r2.matcher(s);
					if(m2.find()) {
						int id = Integer.parseInt(m2.group("id"));
						double x = Double.parseDouble(m2.group("x"));
						double y = Double.parseDouble(m2.group("y"));
						DataPoint dp = new DataPoint(x, y, id);
						index[grid_x][grid_y].add(dp);
					}
				}
			}
		}
		
//		System.out.println(index[2][2].get(0));
		
		reader.close();
		return index;
	}
	
	
	/*
	 * get all neighbors of a cell
	 */
	public static ArrayList<Integer> get_neighbors(int layer, int grid_x, int grid_y, int n){
		ArrayList<Integer> result = new ArrayList<Integer>();
		if(grid_x-layer>=0 && grid_y-layer>=0 && grid_x+layer<n && grid_y+layer<n) {
			for(int i=grid_x-layer;i<grid_x+layer;i++) {
				result.add(i*n+grid_y-layer);
			}
			for(int j=grid_y-layer;j<grid_y+layer;j++) {
				result.add((grid_x+layer)*n + j);
			}
			for(int i=grid_x+layer;i>grid_x-layer;i--) {
				result.add(i*n + grid_y+layer);
			}
			for(int j=grid_y+layer;j>grid_y-layer;j--) {
				result.add((grid_x-layer)*n + j);
			}
		}
		else if(grid_x-layer>=0 && grid_y-layer>=0 && grid_y+layer<n) {
			for(int i=grid_x-layer;i<n;i++) {
				result.add(i*n + grid_y-layer);
				result.add(i*n + grid_y+layer);
			}
			for(int j=grid_y-layer+1;j<grid_y+layer;j++) {
				result.add((grid_x-layer)*n + j);
			}
		}
		else if(grid_x-layer>=0 && grid_y+layer<n && grid_x+layer<n) {
			for(int j=0;j<grid_y+layer;j++) {
				result.add((grid_x-layer)*n + j);
				result.add((grid_x+layer)*n + j);
			}
			for(int i=grid_x-layer;i<=grid_x+layer;i++) {
				result.add(i*n + grid_y+layer);
			}
		}
		else if(grid_x+layer<n && grid_y+layer<n && grid_y-layer>=0) {
			for(int i=0;i<grid_x+layer;i++) {
				result.add(i*n + grid_y-layer);
				result.add(i*n + grid_y+layer);
			}
			for(int j=grid_y-layer;j<=grid_y+layer;j++) {
				result.add((grid_x+layer)*n + j);
			}
		}
		else if(grid_x+layer<n && grid_y-layer>=0 && grid_x-layer>=0) {
			for(int j=grid_y-layer;j<n;j++) {
				result.add((grid_x-layer)*n + j);
				result.add((grid_x+layer)*n + j);
			}
			for(int i=grid_x-layer+1;i<grid_x+layer;i++) {
				result.add(i*n + grid_y-layer);
			}
		}
		
		else if(grid_x+layer<n && grid_y+layer<n) {
			for(int i=0;i<grid_x+layer;i++) {
				result.add(i*n + grid_y+layer);
			}
			for(int j=0;j<=grid_y+layer;j++) {
				result.add((grid_x+layer)*n + j);
			}
		}
		else if(grid_x+layer<n && grid_y-layer>=0) {
			for(int i=0;i<grid_x+layer;i++) {
				result.add(i*n + grid_y-layer);
			}
			for(int j=grid_y-layer;j<n;j++) {
				result.add((grid_x+layer)*n + j);
			}
		}
		else if(grid_x-layer>=0 && grid_y+layer<n) {
			for(int i=grid_x-layer;i<n;i++) {
				result.add(i*n + grid_y+layer);
			}
			for(int j=0;j<grid_y+layer;j++) {
				result.add((grid_x-layer)*n + j);
			}
		}
		else if(grid_x-layer>=0 && grid_y-layer>=0) {
			for(int i=grid_x-layer;i<n;i++) {
				result.add(i*n + grid_y-layer);
			}
			for(int j=grid_y-layer+1;j<n;j++) {
				result.add((grid_x-layer)*n + j);
			}
		}
		
		else if(grid_x+layer<n) {
			for(int j=0;j<n;j++) {
				result.add((grid_x+layer)*n + j);
			}
		}
		else if(grid_x-layer>=0) {
			for(int j=0;j<n;j++) {
				result.add((grid_x-layer)*n + j);
			}
		}
		else if(grid_y+layer<n) {
			for(int i=0;i<n;i++) {
				result.add(i*n + grid_y+layer);
			}
		}
		else if(grid_y-layer>=0) {
			for(int i=0;i<n;i++) {
				result.add(i*n + grid_y-layer);
			}
		}
		return result;
	}
	
	public static DataPoint find_largest(HashSet<DataPoint> knn, DataPoint target) {
		double largest = 0;
		DataPoint result = null;
		for(DataPoint dp : knn) {
			if(dp.distanceTo(target)>largest) {
				largest = dp.distanceTo(target);
				result = dp;
			}
		}
		return result;
	}
	
	public static double distance_p_cell(int i, DataPoint target, int n) {
//		double left = (i%n)*(353.8/n);
//		double right = left + 353.8/n;
//		double up = (i/n)*(180.0/n);
//		double low = up + 180.0/n;
		
		double left = (i%n)*(10/n);
		double right = left + 10/n;
		double up = (i/n)*(10/n);
		double low = up + 10/n;
		
//		System.out.println(left + " " + right + " " + up + " " + low);
		
		if(target.getX()>=up && target.getX()<low && target.getY()>=left && target.getY()<right) {
			return 0;
		}
		if(target.getX()>=up && target.getX()<low) {
			return Math.min(Math.abs(left-target.getY()), Math.abs(right-target.getY()));
		}
		if(target.getY()>=left && target.getY()<right) {
			return Math.min(Math.abs(up-target.getX()), Math.abs(low-target.getX()));
		}
		if(target.getX()<up && target.getY()<left) {
			return Math.sqrt(Math.pow(target.getX()-up, 2) + Math.pow(target.getY()-left, 2));
		}
		if(target.getX()>=low && target.getY()<left) {
			return Math.sqrt(Math.pow(target.getX()-low, 2) + Math.pow(target.getY()-left, 2));
		}
		if(target.getX()>=low && target.getY()>=right) {
			return Math.sqrt(Math.pow(target.getX()-low, 2) + Math.pow(target.getY()-right, 2));
		}
		return Math.sqrt(Math.pow((target.getX()-up), 2) + Math.pow(target.getY()-right, 2));
	}
	
	
	
	public static String knn_grid(double x, double y, String index_path, int k, int n) throws IOException, FileNotFoundException{
		// to get the k-NN result with the help of the grid index
		// Please store the k-NN results by a String of location ids, like "11, 789, 125, 2, 771"
		ArrayList<DataPoint>[][] index = construct_index(index_path, n);
//		System.out.println(index[0][0].get(0));
		
		ArrayList<Integer> search_queue = new ArrayList<Integer>();
		HashSet<DataPoint> knn = new HashSet<DataPoint>();
		double largest_distance = 0;
		DataPoint last = new DataPoint(0,0,0);
		int layer = 0;
		
		DataPoint target_dp = new DataPoint(x, y, -1); 
//		int grid_x = (int) Math.floor((x+90)/(180.0/n));
//		int grid_y = (int) Math.floor((y+176.30859376)/(353.8/n));
		int grid_x = (int) Math.floor(x/(10/n));
		int grid_y = (int) Math.floor(y/(10/n));
		if(grid_x>=n) grid_x = n;
		if(grid_y>=n) grid_y = n;
		
		int current_x = grid_x;
		int current_y = grid_y;
		
		search_queue.add(current_x*n+current_y);
//		System.out.println(search_queue.get(0));
		
		while(search_queue.size()>0) {
			int h = search_queue.remove(search_queue.size()-1);
			current_x = h/n;
			current_y = h%n;
			for(DataPoint dp : index[current_x][current_y]) {
				if(knn.size()<k) {
					knn.add(dp);
					if(target_dp.distanceTo(dp) > largest_distance) {
						largest_distance = target_dp.distanceTo(dp);
						last = dp;
					}
				}
				else if(target_dp.distanceTo(dp) < largest_distance){
					knn.remove(last);
					knn.add(dp);
					last = find_largest(knn, target_dp);
					largest_distance = target_dp.distanceTo(last);
				}
			}
			if(search_queue.size()==0) {
				layer++;
				ArrayList<Integer> neighbor_cells = get_neighbors(layer, grid_x, grid_y, n);
				for(int i : neighbor_cells) {
					if(distance_p_cell(i, target_dp, n) < largest_distance) {
						for(DataPoint p : index[i/n][i%n]) {
							search_queue.add(i);
						}
					}
				}
			}
		}
		
		String result = "";
		for(DataPoint dp : knn) {
			result += dp.getId() + " ";
		}
		return result;
	
	}


	public static String knn_linear_scan(double x, double y, String data_path_new, int k){
		// to get the k-NN result by linear scan
		// Please store the k-NN results by a String of location ids, like "11, 789, 125, 2, 771"

		return "";
	}

	public static void main(String args[]) throws IOException, FileNotFoundException {
//		String result = knn_grid(0, 0, "index.txt", 0, 10);
		/*
		ArrayList<Integer> result = get_neighbors(6, 4, 7, 8);
		for(Integer i : result) {
			System.out.println(i);
		}
		*/
		/*
		DataPoint dp = new DataPoint(0.5, 4.2, -1);
		System.out.println(distance_p_cell(9, dp, 10));
		*/
		String s = knn_grid(4.9, 4.8, "index.txt", 2, 10);
		System.out.println(s);
  	}
}