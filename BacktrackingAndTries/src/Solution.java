import java.util.*;
import java.io.*;
import java.lang.String;

public class Solution {
	public HashMap<String, String> morseHash = new HashMap<>();
	public ArrayList<String> dictionary = new ArrayList<>();
    
    public static void handleSpacedLetters(String morsed, HashMap<String, String> morseHash, ArrayList<String> dictionary) {
    	String [] code = morsed.split(" ");
    	String output = "";
    	
    	//reads the morse code into a string of english
    	for(String s : code) {
    		output = output + morseHash.get(s);
    	}
    	
    	//binary search to see if the word exists
    	int exists = Collections.binarySearch(dictionary, output);
    	
    	if(exists >= 0) {
    		System.out.println(output);
    	}
    }
    
    public static ArrayList<String> handleWord(String morsed, HashMap<String, String> morseHash, ArrayList<String> dictionary, String word, int start, int end, ArrayList<String> output) {
        boolean outOfBounds = end - start < 0; //keeps track if you've reached the end of the morse string
    	
    	if(dictionary.contains(word) && !output.contains(word)) { //adds word to output list if it is in the dictionary and not already added
    		output.add(word);
    		word = "";
    	}
    	
    	/*
    	boolean wordStartsWith = false;
    	for(String d : dictionary) { //makes sure a word starts with the starting pieces of a string
    		if(d.startsWith(word)) {
    			wordStartsWith = true;
    			break;
    		}
    	}
    	*/
    	
    	if(!outOfBounds) {
    		String code = morsed.substring(end - start, end);
    		
    		if(morseHash.containsKey(code)) {
    			String updatedWord = word + morseHash.get(code);
    			handleWord(morsed, morseHash, dictionary, updatedWord, start, end, output);
    		}
    	}
    	
    	return output;
    }
    
    public static void handleSpacedWords(String morsed) {
        System.out.println("INCOMPLETE PART 3");
    }
    
    public static void handleSentence(String morsed) {
        System.out.println("INCOMPLETE PART 4");
    }

    public static void main(String[] args) throws FileNotFoundException {
    	Solution s = new Solution();
    	// Get input from stdin
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        // Parse the style and morsed code value
        String[] parts = command.split(":");
        String style = parts[0].trim();
        String morsed = parts[1].trim();
        
        //inserts the morse code into a hashmap
        //key is code, value is english letter
        File morseFile = new File("morse.txt");
        Scanner morseScanner = new Scanner(morseFile);
        while(morseScanner.hasNextLine()) {
        	String [] morsecode = morseScanner.nextLine().split(" ");
        	s.morseHash.put(morsecode[1], morsecode[0]);
        }
        
        //reads the dictionary into an arraylist
        File dictionary = new File("dictionary.txt");
        Scanner dictionaryScanner = new Scanner(dictionary);
        while(dictionaryScanner.hasNextLine()) {
        	s.dictionary.add(dictionaryScanner.nextLine());
        }
        
        switch (style) {
            case "Spaced Letters":
                handleSpacedLetters(morsed, s.morseHash, s.dictionary);
                break;
            case "Word":
            	ArrayList<String> output = new ArrayList<>();
            	String word = "";
                output = handleWord(morsed, s.morseHash, s.dictionary, word, 0, morsed.length() - 1, output);
                
                Collections.sort(output);
            	for(String o : output) {
            		System.out.println(o);
            	}
                break;
            case "Spaced Words":
                handleSpacedWords(morsed);
                break;
            case "Sentence":
                handleSentence(morsed);
                break;
        }  
    }

}