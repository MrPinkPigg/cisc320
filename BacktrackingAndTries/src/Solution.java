/**
 * Aaron Knestaut
 * work a little bit wth cohort 4
 */

import java.util.*;
import java.io.*;
import java.lang.String;

public class Solution {
	
	//Trie class
	class TrieNode {
		HashMap<String, TrieNode> children;
		ArrayList<String> words;

		public TrieNode() {
			children = new HashMap<>();
			words = new ArrayList<>();
		}
		
		public void addWord(TrieNode root, String input) {
			TrieNode curr = root;
			
			String mush = input.replaceAll(" ", "");
			String[] split = mush.split("");
			for(String s : split) {
				if(!curr.children.containsKey(s)) {
					curr.children.put(s, new TrieNode());
				}
				curr = curr.children.get(s);
			}
			curr.words.add(handleSpacedLetters(input));
			
		}

		public ArrayList<String> findWord(TrieNode root, String target) {
			TrieNode curr = root;
			String[] split = target.split("");
			for(String s : split) {
				if(curr.children.containsKey(s)) {
					curr = curr.children.get(s);
				}
			}
			
			return curr.words;
		}
	}
	
	static HashMap<String, String> morseHash = new HashMap<>();
	static HashMap<String, String> engHash = new HashMap<>();
	static ArrayList<String> dictionary = new ArrayList<>();
	static ArrayList<String> morseDictionary = new ArrayList<>();
    
	//********** PART 1 **********
    public static String handleSpacedLetters(String morsed) {
    	String [] code = morsed.split(" ");
    	String output = "";
    	
    	//reads the morse code into a string of english
    	for(String s : code) {
    		output = output + morseHash.get(s);
    	}
    	
    	//binary search to see if the word exists
    	int exists = Collections.binarySearch(dictionary, output);
    	
    	if(exists >= 0) {
    		return output;
    	}
    	else {
    		return "";
    	}
    }
    
    //*********** PART 2 **********
    public static ArrayList<String> handleWord(String morsed, String word, int start, int end, ArrayList<String> output) {
        boolean outOfBounds = start + end > morsed.length(); //keeps track if you've reached the end of the morse string
        
        if(dictionary.contains(word) && !output.contains(word) && correctLength(engHash, morsed, word)) { //adds word to output list if it is in the dictionary and not already added
    		output.add(word);
    	}
        
    	if(!outOfBounds) {
    		String code = morsed.substring(start, start + end);
    		
    		if(morseHash.containsKey(code)) {
    			String updatedWord = word + morseHash.get(code);
    			
    			handleWord(morsed, updatedWord, start + end, 1, output);
    			handleWord(morsed, word, start, end + 1, output);
    		}
    	}
    	
    	return output;
    }
    
    //helper function for handleWord
    public static boolean correctLength(HashMap<String, String> engHash, String morsed, String word) {
    	String[] splitWord = word.split("");
    	word = "";
    	for(String s : splitWord) {
    		word = word + engHash.get(s);
    	}
    	
    	return morsed.length() == word.length();
    }
    
    //********** PART 3 **********
    public static ArrayList<ArrayList<String>> handleSpacedWords(String morsed, TrieNode root) {
    	//puts all the words from the dictionary in the trie
    	for(String d : morseDictionary) {
    		root.addWord(root, d);
    	}
    	
    	//adds all found words to an arraylist
    	ArrayList<ArrayList<String>> output = new ArrayList<>();
    	String[] split = morsed.split(" ");
    	for(String s : split) {
    		output.add(root.findWord(root, s));
    	}
    	
    	return output;
    }
    
    //helper function for handleSpacedWords
    public static ArrayList<String> generatePermutations(ArrayList<ArrayList<String>> lists, ArrayList<String> result, int depth, String current) {
        if (depth == lists.size()) {
            result.add(current);
            return result;
        }

        for (int i = 0; i < lists.get(depth).size(); i++) {
            generatePermutations(lists, result, depth + 1, current + " " + lists.get(depth).get(i));
        }
        
        return result;
    }
    
    
    //********** PART 4 **********
    public static ArrayList<String> handleSentence(String morsed, TrieNode root) {
    	//puts all the words from the dictionary in the trie
    	for(String d : morseDictionary) {
    		root.addWord(root, d);
    	}
    	
    	ArrayList<String> sentences = findAllWords(morsed, root, new ArrayList<>());
    	ArrayList<String> sentence = new ArrayList<>();
    	ArrayList<String> sentenceList = new ArrayList<>(/*Arrays.asList("STAY", "SAID", " ",
														    			"STATE", "GIRL", " ",
														    			"STAY", "SAFE", " ",
														    			"SKY", "SAFE", " ",
														    			"SKY" , "SAID", " ",
														    			"AARON", "DYLAN", "CONNOR", "BRANDON", " ")*/);
    																//This stuff was used for testing the output sorting
    	
    	for(String s : sentences) {
    		if(s.equals("")) {
    			sentenceList.addAll(sentence);
    			sentence.clear();
    		}
    		else {
    			sentence.add(s);
    		}
    	}
    	
    	//finds min sentence length
    	int minLength = Integer.MAX_VALUE;
    	int temp = 0;
    	for(String o : sentenceList) {
    		if(o.equals(" ")) {
    			if(temp < minLength) {
    				minLength = temp;
    				temp = 0;
    				continue;
    			}
    		}
    		else {
    			temp++;
    		}
    	}
    	
    	//only keeps sentences at the min length or less
    	ArrayList<String> outputSentences = new ArrayList<>();
    	ArrayList<String> tempSentence = new ArrayList<>();
    	String tempString = "";
    	for(String a : sentenceList) {
    		if(a.equals(" ")) {
    			if(tempSentence.size() <= minLength) {
    				//roundabout way of making the sentences single strings
    				for(String w : tempSentence) {
    					if(tempString.equals("")) {
    						tempString = w;
    					}
    					else {
    						tempString = tempString + " " + w;
    					}
    				}    				
    				outputSentences.add(tempString);
    				tempSentence.clear();
    				tempString = "";
    			}
    		}
    		else {
    			tempSentence.add(a);
    		}
    	}
    	
    	//sorts sentences alphabetically
    	Collections.sort(outputSentences);
    	
    	return outputSentences;
    }
    
    public static ArrayList<String> findAllWords(String morsed, TrieNode root, ArrayList<String> sentence){
    	int sentenceLength = 0;
    	int mcLength = morsed.length();
    	ArrayList<String> finalOutput = new ArrayList<>();
    	ArrayList<String> output = new ArrayList<>();
    	
    	for(String w : sentence) {
    		String currMorse = morseDictionary.get(dictionary.indexOf(w));
    		sentenceLength += currMorse.length();
    	}
    	
    	/*
    	if(sentence.size() > 5) { //hardcoded have to fix
    		return new ArrayList<>();
    	}*/
		
    	
    	if(sentenceLength >= mcLength) {
    		ArrayList<String> copy = sentence;
    		copy.add("");
    		return copy;
    	}
    	else {
    		ArrayList<String> possibleNext = checkRest(morsed.substring(sentenceLength), root);
    		
    		for(String n : possibleNext) {
    			sentence.add(n);
    			output = findAllWords(morsed, root, sentence);
    			if(!output.isEmpty()) {
    				finalOutput.addAll(output);
    			}
    			sentence.remove(sentence.size() - 1);
    		}
    	}
    	
    	return finalOutput;
    }
    
    public static ArrayList<String> checkRest(String morsed, TrieNode root){
    	ArrayList<String> finalOutput = new ArrayList<>();
    	int curr = morsed.length();
    	int length = morsed.length();
    	String word;
    	
    	while(curr > 0) {
    		if(curr == 0) {
    			word = morsed;
    		}
    		else {
    			word = morsed.substring(0, curr);
    		}
    		ArrayList<String> currList = root.findWord(root, word);
    		if(!currList.isEmpty()) {
    			finalOutput.addAll(currList);
    		}
    		curr--;
    	}
    	
    	
    	return finalOutput;
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
        //morseHash uses the morse code as the key
        //engHash uses english as the key
        File morseFile = new File("morse.txt");
        Scanner morseScanner = new Scanner(morseFile);
        while(morseScanner.hasNextLine()) {
        	String [] morsecode = morseScanner.nextLine().split(" ");
        	s.morseHash.put(morsecode[1], morsecode[0]);
        	s.engHash.put(morsecode[0], morsecode[1]);
        }
        
        //reads the dictionary into an arraylist
        File dictionary = new File("dictionary.txt");
        Scanner dictionaryScanner = new Scanner(dictionary);
        while(dictionaryScanner.hasNextLine()) {
        	s.dictionary.add(dictionaryScanner.nextLine());
        }
        
      //adds the entire dictionary to a new arraylist in morse code (no spaces)
    	for (int i = 0; i < s.dictionary.size(); i++) {
    		String [] split = s.dictionary.get(i).split("");
    		String currWord = "";
    		for (String e : split) {
    			if(currWord.equals("")){
    				currWord = engHash.get(e);
    				morseDictionary.add(currWord);
    			}
    			else{
    				currWord = currWord + " " + engHash.get(e);
    			}
    			morseDictionary.set(i, currWord);
    		}
    	}
        
        switch (style) {
            case "Spaced Letters": //PART 1
                System.out.println(handleSpacedLetters(morsed));
                break;
                
            case "Word": //PART 2
            	ArrayList<String> output2 = new ArrayList<>();
            	String word = "";
                output2 = handleWord(morsed, word, 0, 1, output2);

                Collections.sort(output2);
            	for(String o : output2) {
            		System.out.println(o);
            	}
                break;
                
            case "Spaced Words": //PART 3
            	TrieNode root3 = s.new TrieNode();
            	ArrayList<ArrayList<String>> output3 = new ArrayList<>();
                output3 = handleSpacedWords(morsed, root3);
                
                //outputs all permutations of the output arraylist
            	ArrayList<String> correctOutput = generatePermutations(output3, new ArrayList<String>(), 0, "");
            	for(String p : correctOutput) {
            		p = p.replaceFirst(" ", "");
            		System.out.println(p);
            	}
                break;
                
            case "Sentence": //PART 4
            	TrieNode root4 = s.new TrieNode();
            	ArrayList<String> output4 = new ArrayList<>();
            	
            	//output 4 doesnt output anything :/
                output4 = handleSentence(morsed, root4);
                
                //prints out sentences
                for(String h : output4) {
                	System.out.println(h);
                }
                
                break;
        }  
    }
}



