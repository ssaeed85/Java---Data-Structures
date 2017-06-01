package roadgraph;

import java.util.*;
import geography.GeographicPoint;

public class MapNode {
	private GeographicPoint loc;
	private List<MapEdge> adjList; //List of out-Neighbors
	
	public MapNode(GeographicPoint loc){
		this.loc = loc;
		this.adjList = new ArrayList<MapEdge>();
	}
	
	public GeographicPoint getLoc(){
		return loc;
	}
	
	public List<MapEdge> getNeighbors(){
		return adjList;
	}
	
	public int getNumEdges(){
		return adjList.size();
	}
	
//Following can be implemented in case adjacecy list is for a directed graph
	public int getNumOutEdges(){
		//Returns number of out bound Edges
		int numEdges=0;
		for(MapEdge e:adjList){
			//If distance between the start location of the edge and the location of this node is zero increment
			if(loc.distance(e.getStart()) != 0.0){
				numEdges++;
			}
		}
		return numEdges;
	}
	
	public int getNumInEdges(){
		//Returns number of in bound Edges
		int numEdges=0;
		for(MapEdge e:adjList){
			//If distance between the end location of the edge and the location of this node is zero increment
			if(loc.distance(e.getEnd()) != 0.0){
				numEdges++;
			}
		}
		return numEdges;
	}
	
	public void addOutPath(GeographicPoint to, String name, String type){
		//Adds a named map edge path from this node to another node (locations)
		adjList.add(new MapEdge(this.loc, to, name, type, this.loc.distance(to)));
	}
	public void addInPath(GeographicPoint from, String name, String type){
		//Adds a named map edge path from another node to this node (locations)
				adjList.add(new MapEdge(from, this.loc, name, type, from.distance(this.loc)));
	}
	
}
