package roadgraph;

import java.util.*;
import geography.GeographicPoint;

public class MapNode {
	private GeographicPoint loc;
	private HashMap<GeographicPoint,MapEdge> adjList; //List of out-Neighbors
	private double dist;
	
	public MapNode(GeographicPoint loc){
		this.loc = loc;
		this.adjList = new HashMap<GeographicPoint,MapEdge>();
		initDistance();
	}
	
	public void initDistance(){
		this.dist = Double.POSITIVE_INFINITY;
	}
	
	public GeographicPoint getLoc(){
		return loc;
	}
	
	public List<GeographicPoint> getNeighbors(){
		//Returns all neighbor locations that share a map edge using this node as a start point
		List<GeographicPoint> list = new ArrayList<GeographicPoint>();
		//System.out.println("Debugging: |\n" + adjList.keySet().toString() + "\n");
		list.addAll(adjList.keySet());
		return list;
	}
	
	public int getNumEdges(){
		return adjList.size();
	}
	
	public void setDist(double d){
		this.dist = d;
	}
	
	public double getDist(){
		return this.dist;
	}
	
	public double getDistanceTo(GeographicPoint g){
		return adjList.get(g).getLength();
	}
	
	public void addPath(GeographicPoint other, String name, String type){
		//Adds a named map edge connecting this node and another node
		MapEdge mE = new MapEdge(this.loc, other, name, type, this.loc.distance(other));
		adjList.put(other,mE);
		//System.out.println(mE.toString());
	}	
}
