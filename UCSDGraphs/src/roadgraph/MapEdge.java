package roadgraph;

public class MapEdge {
	private String streetName;
	private MapNode start;
	private MapNode end;
	private double length;
	
	public MapEdge(MapNode s, MapNode e, String n, double l){
		this.streetName = n;
		this.start = s;
		this.end = e;
		this.length = l;
	}
	
}
