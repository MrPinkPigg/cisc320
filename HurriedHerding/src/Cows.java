import java.util.*; 
import java.io.*;

public class Cows {
	int id;
	String code;
	int weight;
	int milk;
	int temp;
	int time;
	int minWeight = Integer.MAX_VALUE;
	int avgMilk = 0;
	int totalMilk = 0;
	int milkCount = 0;
	static HashMap<Integer, Cows> herd = new HashMap<>();
	
	//cow constructor
	public Cows(int id, String code, int codeVal, int time) {
		this.id = id;
		
		if(code.compareTo("W") == 0) {
			this.weight = codeVal;
			this.minWeight = codeVal;
		}
		else if(code.compareTo("M") == 0) {
			this.milk = codeVal;
			this.avgMilk = codeVal;
			this.totalMilk = codeVal;
			this.milkCount = 1;
		}
		else {
			this.temp = codeVal;
		}
		
		this.time = time;
	}
	
	//getters
	public int getId() { return this.id; }
	public String getCode() { return this.code; }
	public int getWeight() { return this.weight; }
	public int getMilk() { return this.milk; }
	public int getTime() { return this.time; }
	public int getMinWeight() { return this.minWeight; }
	public int getAvgMilk() { return this.avgMilk; }
	public int getMilkCount() { return this.milkCount; }
	public int getTotalMilk() { return this.totalMilk; }
	
	//setters
	public void setWeight(int weight) { this.weight = weight; }
	public void setMilk(int milk) { this.milk = milk; }
	public void setTemp(int temp) { this.temp = temp; }
	public void setTime(int time) { this.time = time; }
	public void setMinWeight(int minWeight) { this.minWeight = minWeight; }
	public void setAvgMilk(int avgMilk) { this.avgMilk = avgMilk; }
	public void setTotalMilk(int totalMilk) { this.totalMilk = totalMilk; }
	public void setMilkCount(int milkCount) { this.milkCount = milkCount; }
	
	
	
	
	public static void main(String args[]) {
		//System.out.println("Enter the textfile name");
		Scanner readIn = new Scanner(System.in); //reads in the filename
		String fileName = readIn.nextLine(); //saves the filename as a string
		
		try {
			File cowFile = new File(fileName); //attempts to read the file
			Scanner cowScanner = new Scanner(cowFile); //sets up a scanner to read through the file

			int readLines = Integer.parseInt(cowScanner.nextLine()); //reads the first line of the file
			ArrayList<Integer> knownIds = new ArrayList<Integer>(); //keeps track of cows that already exist
			
			for(int i = 0; i < readLines; i++){ //loop that runs through each line in the file
				int id = Integer.parseInt(cowScanner.next());
				String code = cowScanner.next();
				int codeVal = Integer.parseInt(cowScanner.next());
				int time = Integer.parseInt(cowScanner.next());
				
				if(knownIds.contains(id)) { //if the cow exists already, just edit that cow
					editCow(herd.get(id), code, codeVal, time);
				}
				else { //if the cow does not exist, add it to the hashmap
					knownIds.add(id);
					herd.put(id, new Cows(id, code, codeVal, time));
				}
			}
            
			//had to put it in an arraylist for some reason
			ArrayList<Cows> cowList = new ArrayList<Cows>();
			for(Cows c : herd.values()) {
				cowList.add(c);
			}
			
			//made ANOTHER arraylist for what to remove
			ArrayList<Cows> cowListRemove = new ArrayList<Cows>();
			for(Cows c : cowList) {
				if(c.getMinWeight() == 0 || c.getWeight() == 0 || c.getAvgMilk() == 0 ) {
					cowListRemove.add(c);
				}
			}

			//removed cows with 0's
			for(Cows c : cowListRemove) {
			    cowList.remove(c);
			}
			
			//sorted the remaining cows using the comparator
			Collections.sort(cowList, new CowCompare());
			
			//output the sorted cow list
			for(Cows c : cowList) {
				System.out.println(c.getId() + " " + c.getMinWeight() + " " + c.getWeight() + " " + c.getAvgMilk());
			}
			
		}
		catch (FileNotFoundException e){ //a catch for if the file being looked for does not exist
			System.out.println("Something went wrong");
			e.printStackTrace();
		}
	}
	
	public static void editCow(Cows cow, String code, int codeVal, int time) {
		if(code.compareTo("W") == 0) { //edit weight info
			cow.setWeight(codeVal);
			if(cow.getMinWeight() > codeVal) {
				cow.setMinWeight(codeVal);
			}
		}
		else if(code.compareTo("M") == 0) { //edit milk info
			cow.setMilk(codeVal);
			
			int milkCount = cow.getMilkCount();
			milkCount++;
			cow.setMilkCount(milkCount);
			
			cow.setTotalMilk(cow.totalMilk + codeVal);
			
			cow.setAvgMilk(cow.totalMilk / cow.getMilkCount());
			
		}
		else { //edit temp info
			cow.setTemp(codeVal);
		}
		
		//edit timestamp
		cow.setTime(time);
	}
}


class CowCompare implements Comparator<Cows> {
	public int compare(Cows a, Cows b) {
		//compare minimum weights
		if(a.getMinWeight() < b.getMinWeight()) { return -1; }
		else if(a.getMinWeight() > b.getMinWeight()) { return 1; }
		else { 
			//compare current weight
			if(a.getWeight() < b.getWeight()) { return -1; }
			else if(a.getWeight() > b.getWeight()) { return 1; }
			else {
				//compare avg milk
				if(a.getAvgMilk() < b.getAvgMilk()) { return -1; }
				else if(a.getAvgMilk() > b.getAvgMilk()) { return 1; }
				else {
					//compare ids
					if(a.getId() < b.getId()) { return -1; }
					else { return 1; }
				}
			}
		}
	}
}


