/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;

import java.util.*;
import java.util.function.Consumer;
import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	private HashMap<GeographicPoint, MapNode> Map;		

	/** 
	 * Create a new empty MapGraph 
	 */	
	public MapGraph()
	{
		//Initialize hashMap
		Map = new HashMap<GeographicPoint, MapNode>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		//Returns size of graph map
		return Map.size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{	
		//Returns the keySet for the graph map i.e. the geographic points for the vertices
		return Map.keySet();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		//Returns total number of edges in graph. Accumulates number of out Edges for each node
		int numEdges = 0;	
		for(GeographicPoint key : Map.keySet()){
			numEdges += Map.get(key).getNumEdges();
		}
		if (numEdges == 0) 
			System.out.println("No edges in Map Graph");
		return numEdges;
	}
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		if(location == null){
			System.out.println("Error in location parameter");
		}
		else if(!Map.containsKey(location)){ 
			//Adds location to map if map doesn't already contain it
			Map.put(location,new MapNode(location));
			return true; //Returns true if added
		}	
		return false; //Returns false if null (with error msg) or if map already has location
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {
		//throw error if either locations are in map
		if (!Map.containsKey(from) || !Map.containsKey(to) || from == null || to == null) throw new IllegalArgumentException();
		//Add out bound edge to map node in graph at 'from' location
		Map.get(from).addPath(to, roadName, roadType);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		//error printout if either start or goal is null
		if (start == null || goal == null){
			System.out.println("Error: Start and/or Goal is 'null'. No path exists");
			return null;			
		}
		//ParentMap: key:value pair of node:parent 
		HashMap<GeographicPoint,GeographicPoint> parentMap = new HashMap<GeographicPoint,GeographicPoint>();

		if(!bfsSearch(start, goal, parentMap,nodeSearched)){
			System.out.println("No path found");
			return null;
		}
		else{
			return constructPath(start, goal, parentMap);
		}
	}
	
	/*
	 *  Actual BFS algorithm to find a path
	 */
	private boolean bfsSearch(GeographicPoint start, 
		     GeographicPoint goal, HashMap<GeographicPoint,GeographicPoint> parentMap, Consumer<GeographicPoint> nodeSearched){
		Set<GeographicPoint> vis = new HashSet<GeographicPoint>();
		Queue<GeographicPoint> q = new LinkedList<GeographicPoint>();
		boolean found = false;
		q.add(start);
		vis.add(start);
		
		while(!q.isEmpty()){
			GeographicPoint curr = q.remove();
			if(curr.distance(goal) == 0){ //if current node is the goal
				found = true;
				nodeSearched.accept(goal);
				break;
			}
			else{
				List<GeographicPoint> neighbors = Map.get(curr).getNeighbors();

				for(GeographicPoint n : neighbors){ //iterate through neighbors
					if(!vis.contains(n)){			//if visited doesn't contain neighbor
						vis.add(n);					//add to visited set
						q.add(n);					//enqueue
						parentMap.put(n, curr); 	//add to parentMap
						nodeSearched.accept(curr);
					}
				}
			}
		}
		return found;
	}
	
	/*
	 *  Returns shortest path found by any search algorithm
	 */
	private List<GeographicPoint> constructPath(GeographicPoint start, GeographicPoint goal, 
			HashMap<GeographicPoint, GeographicPoint> parentMap) {
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();
		GeographicPoint curr = goal;
		do {
			//System.out.println("Debugging: current NODE: " + curr.toString());
			path.addFirst(curr);			//adds node to top of list. Ensure path = start -> goal
			curr = parentMap.get(curr);
		} while (curr.distance(start)!=0);	//adds node to top of list. Ensure path = start -> goal
		path.addFirst(start);
		return path;
	}
	
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, 
										  Consumer<GeographicPoint> nodeSearched)
	{
		//error printout if either start or goal is null
		if (start == null || goal == null){
			System.out.println("Error: Start and/or Goal is 'null'. No path exists");
			return null;			
		}
		//ParentMap: key:value pair of node:parent 
		HashMap<GeographicPoint,GeographicPoint> parentMap = new HashMap<GeographicPoint,GeographicPoint>();

		if(!dijkstraSearch(start, goal, parentMap, nodeSearched)){
			System.out.println("No path found");
			return null;
		}
		else{
			return constructPath(start, goal, parentMap);
		}
	}
	
	/*
	 *  The actual dijkstra search algorithm
	 */
	public boolean dijkstraSearch(	GeographicPoint start, 
									GeographicPoint goal, 
									HashMap<GeographicPoint,GeographicPoint> parentMap, 
									Consumer<GeographicPoint> nodeSearched){
		Set<GeographicPoint> vis = new HashSet<GeographicPoint>();
		
		//Priority Queue compares distance from current geo point to other geographic point. Note edge has to exist
		PriorityQueue<GeographicPoint> q= new PriorityQueue<GeographicPoint>(10, new Comparator<GeographicPoint>(){
		    public int compare(GeographicPoint one, GeographicPoint two) {
		        if (Map.get(one).getDist() < Map.get(two).getDist()) return -1;
		        if (Map.get(one).getDist() > Map.get(two).getDist()) return 1;
		        return 0;
		    }
		});
		for(GeographicPoint key: Map.keySet())	Map.get(key).initDistance();		//Initialize all nodes to infinity
		boolean found = false;
		Map.get(start).setDist(0.0);
		q.add(start);
		vis.add(start);
		
		int numNodesVis = 0;				
		while(!q.isEmpty()){
			GeographicPoint curr = q.remove();
			numNodesVis++;
			//System.out.println("Dijkstra:  " +  numNodesVis +  ": " + curr.toString());
			if(curr.distance(goal) == 0){ //if current node is the goal
				found = true;
				nodeSearched.accept(goal);
				break;
			}
			else{
				dijkstraManageQandSet(curr,vis,q,parentMap,nodeSearched);
			}
		}
		System.out.println("Dijkstra| Nodes visited: "+numNodesVis);
		return found;	
	}
	
	/*
	 * Adds geographic points to priority queue and visited set and updates parent Map
	 */
	public void dijkstraManageQandSet(	GeographicPoint curr,
										Set<GeographicPoint> vis,
										PriorityQueue<GeographicPoint> q,
										HashMap<GeographicPoint,GeographicPoint> parentMap,
										Consumer<GeographicPoint> nodeSearched){
		List<GeographicPoint> neighbors = Map.get(curr).getNeighbors();
		vis.add(curr);
		for(GeographicPoint n : neighbors){ //iterate through neighbors
			if(!vis.contains(n)){			//if visited doesn't contain neighbor
				double d = Map.get(curr).getDist() + Map.get(curr).getDistanceTo(n);
				if (Map.get(n).getDist()>d){
					Map.get(n).setDist(d);	//set distance var in node to distance from curr to node
					q.add(n);					//enqueue
					parentMap.put(n, curr); 	//add to parentMap
				}
//				System.out.println("\t\t-----\n\t\tCumulated Distance:\t" + Map.get(curr).getDist() +
//						"\n\t\tDistance to next edge:\t" + Map.get(curr).getDistanceTo(n));
				
				nodeSearched.accept(curr);
			}
		}
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(
			GeographicPoint start, 
			GeographicPoint goal, 
			Consumer<GeographicPoint> nodeSearched)
	{
		
		if (start == null || goal == null){
			System.out.println("Error: Start and/or Goal is 'null'. No path exists");
			return null;			
		}
		//ParentMap: key:value pair of node:parent 
		HashMap<GeographicPoint,GeographicPoint> parentMap = new HashMap<GeographicPoint,GeographicPoint>();

		if(!aStarSearchAndManage(start, goal, parentMap, nodeSearched)){
			System.out.println("No path found");
			return null;
		}
		else{
			return constructPath(start, goal, parentMap);
		}
	}

	/*
	 *  Actual implementation of the aStar search algorithm
	 */
	public boolean aStarSearchAndManage(
			GeographicPoint start, 
		    GeographicPoint goal, 
		    HashMap<GeographicPoint,GeographicPoint> parentMap, 
		    Consumer<GeographicPoint> nodeSearched){
		Set<GeographicPoint> vis = new HashSet<GeographicPoint>();
		
		//Priority Queue compares distance from current geo point to other geographic point. Note edge has to exist
		PriorityQueue<GeographicPoint> q= new PriorityQueue<GeographicPoint>(10, new Comparator<GeographicPoint>(){
		    public int compare(GeographicPoint one, GeographicPoint two) {
		        if (Map.get(one).getDist()+one.distance(goal) < Map.get(two).getDist()+two.distance(goal)) return -1;
		        if (Map.get(one).getDist()+one.distance(goal) > Map.get(two).getDist()+two.distance(goal)) return 1;
		        return 0;
		    }
		});
		for(GeographicPoint key: Map.keySet())	Map.get(key).initDistance();		//Initialize all nodes to infinity
		boolean found = false;
		Map.get(start).setDist(0.0); //Set distance for start node to 0
		q.add(start);
		vis.add(start);
			
		int numNodesVis = 0;
		while(!q.isEmpty()){
			GeographicPoint curr = q.remove();
			numNodesVis++;
//			System.out.println("A*Star:  " +  numNodesVis +  ": " + curr.toString());
//			System.out.println("\tAct: " + Map.get(curr).getDist() + "\tPred: "+goal.distance(curr));
			if(curr.distance(goal) == 0){ //if current node is the goal
				found = true;
				nodeSearched.accept(goal);
				break;
			}
			else{
				vis.add(curr);
				aStarManageQandSet(goal,curr,vis,q,parentMap,nodeSearched);
			}
		}
		System.out.println("A*Star\t| Nodes visited: "+numNodesVis);		
		return found;	
	}
	
	/*
	 * Logic to manage the priority queue and visited set for the aStart search algorithm
	 */
	public void aStarManageQandSet(
		    GeographicPoint goal, 
			GeographicPoint curr,
			Set<GeographicPoint> vis,
			PriorityQueue<GeographicPoint> q,
			HashMap<GeographicPoint,GeographicPoint> parentMap, 
			Consumer<GeographicPoint> nodeSearched){
		
		List<GeographicPoint> neighbors = Map.get(curr).getNeighbors();
		for(GeographicPoint n : neighbors){ //iterate through neighbors
			if(!vis.contains(n)){			//if visited doesn't contain neighbor
				//vis.add(n);					//add to visited set
				double d = Map.get(curr).getDist() + Map.get(curr).getDistanceTo(n);
//				System.out.println("\t\t-----\n\t\tCumulated Distance:\t" + Map.get(curr).getDist() +
//									"\n\t\tDistance to next edge:\t" + Map.get(curr).getDistanceTo(n) + 
//									"\n\t\tDist to goal from next:\t" + goal.distance(n));
				if (Map.get(n).getDist()>d){
					Map.get(n).setDist(d);		//set distance var in node to distance from curr to node
					q.add(n);					//enqueue
					parentMap.put(n, curr); 	//add to parentMap
					nodeSearched.accept(curr);
				}				
			}
		}
	}	
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
//		MapGraph firstMap = new MapGraph();
//		System.out.print("DONE. \nLoading the map...");
//		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
//		System.out.println("DONE.");
		
		// You can use this method for testing.  
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */
		
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);

		//List<GeographicPoint> route = theMap.dijkstra(start,end);
		//List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
		 
	    MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		/*
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		
		/*
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("---Graph Loaded---\nTest 1 using simpletest: BFS");
		List<GeographicPoint> testroute = simpleTestMap.bfs(testStart,testEnd);
		System.out.println("Route: " +testroute);
		
		
		List<GeographicPoint> testroute2 = simpleTestMap.dijkstra(testStart,testEnd);
		System.out.println("Route: " +testroute2);
		

		List<GeographicPoint> testroute3 = simpleTestMap.aStarSearch(testStart,testEnd);
		System.out.println("Route: " +testroute3);
		
		/*
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		System.out.println("Route: " +testroute2);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		System.out.println("Route: " +testroute2);
		/*
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		*/
		
		
		/* Use this code in Week 3 End of Week Quiz */
		/*MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		*/
		
	}
	
}
