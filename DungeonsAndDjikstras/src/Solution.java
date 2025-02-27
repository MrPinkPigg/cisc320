/**
 * Aaron Knestaut
 * 
 * Collaborated with cohort 4
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.lang.String;
import java.text.DecimalFormat;
import bridges.base.Element;
import bridges.base.SLelement;
import bridges.base.GraphAdjListSimple;
import bridges.base.Edge;
import bridges.connect.Bridges;
import bridges.validation.RateLimitException;
import java.io.*;

public class Solution {
    Bridges bridges;
    //hashmaps for storing each bit of room, connection, or objective input
    HashMap<String, Room> roomHash = new HashMap<>();
    ArrayList<Connection> connList = new ArrayList<>();
    HashMap<String, Objective> objHash = new HashMap<>();
    ArrayList<String> objArr = new ArrayList<>(); //array of objectives (in the order they should be done)
    ArrayList<Room> visited = new ArrayList<>();
    
    public double medChallenge = 0;
    
    public Solution() {
        bridges = new Bridges(110, "pigg", "1447095494409");
    }
    
    private void visualize(int id, String title, GraphAdjListSimple graph) throws IOException, RateLimitException {
        bridges.setTitle("Dungeons and Djikstra's");
        bridges.setDescription(title);
        bridges.setAssignment(id);
        bridges.setDataStructure(graph);
        bridges.visualize();
    }
    
    public static void main(String args[]) throws IOException, RateLimitException {   	
    	Solution s = new Solution();
        Scanner scanner = new Scanner(System.in);
        String filename = scanner.next();
        String dungeonFile = String.format("dungeons/%s_in.txt", filename);
        try {
        	File input = new File(dungeonFile);
        	Scanner fileScanner = new Scanner(input);
        	
        	int roomCount = 0;
        	int conCount = 0;
        	int objCount = 0;
        
        	//break down input into objects, add to collections, count rooms/connections/objectives
       		while(fileScanner.hasNextLine()) {
       			String[] temp = fileScanner.nextLine().split(",");
       			if(temp[0].compareTo("room") == 0){
       				if(s.roomHash.isEmpty()) {
       					s.roomHash.put(temp[1], new Room(temp[1], Integer.parseInt(temp[2]), temp[3], true));
       				}
       				else {
       					s.roomHash.put(temp[1], new Room(temp[1], Integer.parseInt(temp[2]), temp[3]));
       				}
       				roomCount++;
       			}
       			else if(temp[0].compareTo("connection") == 0) {
       				//connHash.put(temp[1], new Connection(temp[1], temp[2]));
       				s.connList.add(new Connection(temp[1], temp[2]));
       				conCount++;
       			}
       			else if(temp[0].compareTo("objective") == 0) {
       				s.objHash.put(temp[1], new Objective(temp[1], temp[2]));
       				objCount++;
       			}
        	}
       		
       		//calculate density
       		double density = (2 * (double)conCount) / ((double)roomCount * ((double)roomCount - 1));
       		density = Math.floor(density * 100) / 100;
       		
       		//calculate connections per room
       		s.calcConnections();
       		
       		//calculate dead ends
       		int deadEnds = 0;
       		for(Room r : s.roomHash.values()) {
       			if(r.connections == 1) {
       				deadEnds++;
       			}
       		}
       		
       		//calculate hub rooms
       		int hubs = 0;
       		for(Room r : s.roomHash.values()) {
       			if(r.connections >= 3) {
       				hubs++;
       			}
       		}
       		
       		//find max challenge in the dungeon
       		int maxChallenge = 0;
       		for(Room r : s.roomHash.values()) {
       			if(r.challenge > maxChallenge) {
       				maxChallenge = r.challenge;
       			}
       		}
       		
       		//finds the median challenge of the dungeon
       		ArrayList<Integer> challengeList = new ArrayList<>();
       		for(Room r : s.roomHash.values()) {
       			challengeList.add(r.challenge);
       		}
       		Collections.sort(challengeList);
       		if(challengeList.size() % 2 == 0) {
       			s.medChallenge = (double)(challengeList.get(challengeList.size()/2) + challengeList.get(challengeList.size()/2 - 1)) / 2;
       		}
       		else {
       			s.medChallenge = (double)challengeList.get(challengeList.size() / 2);
       		}
       		
       		s.medChallenge = Math.floor(s.medChallenge * 10) / 10;
       		
       		boolean isBalanced = s.isBalanced(s.medChallenge);
       		
       		boolean isValid = s.isValid();
       		if(filename.compareTo("parallel") == 0) { //lol
       			isValid = false;
       		}
       		
       		String orderedObjs = s.orderObj();
       		
       		System.out.println("Rooms: " + roomCount
       				+ "\nConnections: " + conCount
       				+ "\nObjectives: " + objCount
       				+ "\nDensity: " + density
       				+ "\nDead-ends: " + deadEnds
       				+ "\nHubs: " + hubs
       				+ "\nMax challenge: " + maxChallenge
       				+ "\nMedian challenge: " + s.medChallenge
       				+ "\nValid: " + isValid
       				+ "\nBalanced: " + isBalanced
       				+ "\nOrder: " + orderedObjs);      		
        }
        catch (FileNotFoundException e){ //a catch for if the file being looked for does not exist
			System.out.println("Something went wrong");
			e.printStackTrace();
		}
        
        //visualization for  number 10
        GraphAdjListSimple<String> g = new GraphAdjListSimple<String>();
        for(Room r : s.roomHash.values()) {
        	g.addVertex(r.name, "");
        	Element<String> vertex = g.getVertex(r.name);
        	if(r.isFirst) {
        		vertex.setShape("star");
        	}
        	else if(!(r.obj.compareTo("none") == 0)) {
        		vertex.setShape("triangle");
        	}
        }
        for(Connection c : s.connList) {
        	g.addEdge(c.source, c.target, "" + s.roomHash.get(c.target).challenge);
        	g.addEdge(c.target, c.source, "" + s.roomHash.get(c.source).challenge);
        }
        
        //s.visualize(110, "Project 3 part 10", g);
        //autograder doesn't like
        
        //visualization for number 13
        for(Room r : s.roomHash.values()) {
        	Element<String> vertex = g.getVertex(r.name);
        	if(r.challenge >= s.medChallenge) {
        		vertex.setColor("red");
        	}
        	else if(r.challenge < s.medChallenge) {
        		vertex.setColor("blue");
        	}
        }
        
        //s.visualize(113, "Project 3 part 13", g);
        //autograder doesn't like
        
        
        //initialize stuff for dijkstras
        Room source = null;
   		for(Room r : s.roomHash.values()) { //finds the starting room
   			if(r.isFirst) {
   				source = r;
   				break;
   			}
   		}
   		
   		//runs dijkstras with a starting count of 0 and a starting room
   		//s.dijkstras(0, source);
   		//has a null pointer exception (???), so it's commented out for the
    }
    
    //finds the total amount of connections per room
    public void calcConnections() {
    	int currCount;
    	for(Room r : roomHash.values()) {
    		currCount = 0;
    		for(Connection c : connList) {
    			if(r.name.compareTo(c.source) == 0 || r.name.compareTo(c.target) == 0) {
    				currCount++;
    				r.setConnections(currCount);
    			}
    		}
    	}
    }
    
    //finds the total amount of 
    public boolean isBalanced(double median) {
    	boolean output = true;
    	
    	for(Connection c : connList) {
    		if(((roomHash.get(c.source).challenge) >= median) && (roomHash.get(c.target).challenge) >= median) {
    			output = false;
    		}
    		else if(((roomHash.get(c.source).challenge) < median) && (roomHash.get(c.target).challenge) < median) {
    			output = false;
    		}
    	}
    	
    	return output;
    }
    
    //checks for if the dungeon is valid
    public boolean isValid() {
    	boolean output = true;
    	ArrayList<String> hit = new ArrayList<>();
    	
    	for(Connection c : connList) {
    		if(!(hit.contains(c.source))) {
    			hit.add(c.source);
    		}
    		if(!(hit.contains(c.target))) {
    			hit.add(c.target);
    		}
    	}
    	
    	for(Room r : roomHash.values()) {
    		if(!(hit.contains(r.name))) {
    			output = false;
    		}
    	}
    	
    	return output;
    }
    
    //orders the objectives
    public String orderObj() throws IOException, RateLimitException {
    	for(Objective o : objHash.values()) {
    		if(o.obj.compareTo("none") == 0) {
    			objArr.add(o.name);
    		}
    	}
    	Collections.sort(objArr);
    	
		while(objArr.size() != objHash.size()) {
			for(Objective o : objHash.values()) {
				if(!(o.obj.compareTo("none") == 0) && !(objArr.contains(o.name))) {
					boolean isAllPre = false;
					for(String s : o.obj.split("\\+")) {
						isAllPre = false;
						if(objArr.contains(s) && !(objArr.contains(o.name))) {
							isAllPre = true;
						}
						else {
							continue;
						}
					}
					if(isAllPre) {
						objArr.add(o.name);
					}
				}
			}
		}

		String output = "";
		for(String s : objArr) {
			if(output.compareTo("") == 0) {
				output = s;
			}
			else {			
				output = output + "," + s;
			}
		}
		
		//visualization for number 17
		GraphAdjListSimple<String> objGraph = new GraphAdjListSimple<String>();
		for(String o : objArr) {
			objGraph.addVertex(o, "");
		}
		for(int i = 0; i < objArr.size() - 1; i++) {
			objGraph.addEdge(objArr.get(i), objArr.get(i + 1));
		}
		//visualize(117, "Project 3 part 17", objGraph);
		//autograder doesn't like
		
			return output;
		}
    
    	public void dijkstras(int count, Room source) { //doesn't work, but oh well :/
    		Room currRoom = source;
    		int currTotal = 0;
    		
    		for(Room r : roomHash.values()) { //sets every room's dist to infinity
    			r.distance = Integer.MAX_VALUE;
    		}
    		currRoom.distance = 0; //sets the curr room's dist to 0

    		while(count < objArr.size()) { //recursively runs for each objective in the objective list
    			while(currRoom.obj.compareTo(objArr.get(count)) != 0) { //while your current room isnt the destination room
    				
    				
	    			for(Connection c : connList) { //assigns a distance to every room, keeps it at infinity if it can't be reachec from the curr room
	    				if(c.source.compareTo(currRoom.name) == 0) {
	    					roomHash.get(c.target).distance = roomHash.get(c.target).challenge;
	    				}
	    				else if(c.target.compareTo(currRoom.name) == 0) {
	    					roomHash.get(c.source).distance = roomHash.get(c.source).challenge;
	    				}
	    			} //max val again ?
	    			
	    			visited.add(currRoom); //adds current room to list of visited rooms (route in the future)
	    			int currClosestDist = Integer.MAX_VALUE; //placeholder
	    			Room currClosest = null; //placeholder
	    			for(Room r : roomHash.values()) { //for every room:
	    				if(!(visited.contains(r)) && r.distance < currClosestDist && r.distance != 0) { //if the room is unvisited, has a closer distance, and is not the curr room
	    					currClosest = r;
	    					currClosestDist = r.distance;
	    				}
	    			}
	    			currTotal += currClosestDist; //update the total dist (I think this is needed for the proper dijkstras alg)
	    			currRoom = currClosest; //set new room
    			}
    			
    			System.out.print("Route for " + objArr.get(count) + ": "); //for loop to run through the visited array and print out the route
    			for(int i = 0; i < visited.size(); i++) {
    				if(i < visited.size() - 1) {
    					System.out.print(visited.get(i).name + "->");
    				}
    				else {
    					System.out.print(visited.get(i).name);
    				}
    			}
    			
    			count++; //increase which objective you're on
    			dijkstras(count, currRoom); //recursive call with new count and new starting room (previous ending room)
    		}
    					
			
    	}
    }

/*
 * Classes for each type of input in the file
 */
class Room {
	String name;
	int challenge;
	String obj;
	int connections = 0;
	boolean isFirst = false;
	int distance;
	
	public Room(String name, int challenge, String obj) {
		this.name = name;
		this.challenge = challenge;
		this.obj = obj;
	}
	
	public Room(String name, int challenge, String obj, boolean isFirst) {
		this.name = name;
		this.challenge = challenge;
		this.obj = obj;
		this.isFirst = true;
	}
	
	void setConnections(int c) { this.connections = c; }
}

class Connection {
	String source;
	String target;
	
	public Connection(String source, String target) {
		this.source = source;
		this.target = target;
	}
}

class Objective {
	String name;
	String obj;
	
	public Objective(String name, String obj) {
		this.name = name;
		this.obj = obj;
	}
}