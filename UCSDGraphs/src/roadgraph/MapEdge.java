package roadgraph;

import geography.GeographicPoint;

public class MapEdge {
	private String name;
	private String type;
	private GeographicPoint start;
	private GeographicPoint end;
	private double length;
	
	public MapEdge(GeographicPoint s, GeographicPoint e, String n, String t, double l){
		this.name = n;
		this.type = t;
		this.start = s;
		this.end = e;
		this.length = l;
	}
	
	public GeographicPoint getStart(){
		return start;
	}
	
	public GeographicPoint getEnd(){
		return end;
	}
	
	public double getLength(){
		return length;
	}
	
	public String toString(){
		return ("Map Ege Info | Name: " + name + "| Type: " + type + "| Start: " + start.toString() + 
				"| End: " + end.toString() + "| Length: " + length);
	}
}