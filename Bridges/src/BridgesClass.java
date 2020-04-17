import java.util.ArrayList;
import java.lang.String;
import bridges.base.Element;
import bridges.base.SLelement;
import bridges.base.GraphAdjListSimple;
import bridges.base.Edge;
import bridges.connect.Bridges;
import bridges.connect.DataSource;

public class BridgesClass {

     public static void main(String[] args) throws Exception{

             //create the Bridges object

         Bridges bridges = new Bridges(12, "pigg", "1447095494409");
         
         bridges.setTitle("A graph showing people, and who knows each other");

         	// create an adjacency list based graph
 		GraphAdjListSimple<String> g = new GraphAdjListSimple<String>();

 			//10 vertices made from 10 people
 		String aaron_k = "Aaron Knestaut";
 		String oliva_k = "Olivia Knestaut";
 		String joey_h = "Joey Hackman";
 		String dylan_k = "Dylan Kirk";
 		String denise_k = "Denise Knestaut";
 		String cory_b = "Corey Bart";
 		String will_h = "Will Hackman";
 		String evan_b = "Evan Buss";
 		String ronnie_p = "Ronnie Probst";
 		String kyle_w = "Kyle Willump";
 		
 		g.addVertex(aaron_k, "");
 		g.addVertex(oliva_k, "");
 		g.addVertex(joey_h, "");
 		g.addVertex(dylan_k, "");
 		g.addVertex(denise_k, "");
 		g.addVertex(cory_b, "");
 		g.addVertex(will_h, "");
 		g.addVertex(evan_b, "");
 		g.addVertex("Ronnie Probst", "");
 		g.addVertex("Kyle Willump", "");
 		
 			//edges between people that know each other
 		g.addEdge(aaron_k, oliva_k);
 		g.addEdge(aaron_k, joey_h);
 		g.addEdge(aaron_k, dylan_k);
 		g.addEdge(aaron_k, denise_k);
 		g.addEdge(aaron_k, cory_b);
 		g.addEdge(aaron_k, will_h);
 		g.addEdge(aaron_k, evan_b);
 		g.addEdge(aaron_k, ronnie_p);
 		g.addEdge(aaron_k, kyle_w);
 		g.addEdge(oliva_k, joey_h);
 		g.addEdge(oliva_k, denise_k);
 		g.addEdge(joey_h, denise_k);
 		g.addEdge(joey_h, will_h);
 		g.addEdge(dylan_k, cory_b);
 		g.addEdge(evan_b, ronnie_p);
 		g.addEdge(evan_b, kyle_w);
 		g.addEdge(ronnie_p, kyle_w);
 		
             //pass the first element of the list
        bridges.setDataStructure(g);

             // visualize the list
         bridges.visualize();

        }
    }