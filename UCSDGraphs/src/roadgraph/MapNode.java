package roadgraph;

import java.util.*;
import geography.GeographicPoint;

public class MapNode {
	private GeographicPoint loc;
	private List<MapEdge> adjList;
	
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
	
	public void addPath(MapNode to, String name){
		//Adds a named map edge path connecting another node to this node
		adjList.add(new MapEdge(this,to,name,this.loc.distance(to.getLoc())));
	}
	
}
