import java.util.*; 
import java.io.*;

public class Squirrels {
	public static void main (String args[]){
		int sum = 0;
		Scanner readIn = new Scanner(System.in); //reads in the filename
		String fileName = readIn.nextLine(); //saves the filename as a string

		try {
			File squirrelFile = new File(fileName); //attempts to read the file
			Scanner squirrelScanner = new Scanner(squirrelFile); //sets up a scanner to read through the file

			int readLines = Integer.parseInt(squirrelScanner.nextLine()); //reads the first line of the file

			for(int i = 0; i < readLines; i++){ //loop that runs through each line in the file
				int curr = Integer.parseInt(squirrelScanner.nextLine());
				if(curr == -999){ //if the line = -999, stops the loop
					break;
				}
				else if(curr >= 0){ //if the line is not negative, adds it to the sum
					sum += curr;
				}
			}
            
            if(sum == 0){ //if the sum is still 0 by the end, prints "EMPTY"
                System.out.println("EMPTY");
            }
            else{ //prints the sum
                System.out.println(sum);
            }
		}
		catch (FileNotFoundException e){ //a catch for if the file being looked for does not exist
			System.out.println("Something went wrong");
			e.printStackTrace();
		}
	}
}