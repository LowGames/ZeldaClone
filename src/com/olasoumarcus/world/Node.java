package com.olasoumarcus.world;

public class Node {
	public Vector2I tile;
	public Node parent;
	public double fcost, gcost, hcost;
	
	
	public Node(Vector2I tile, Node parent, double gcost, double hcost) {
		this.tile = tile;
		this.parent = parent;
		this.gcost = gcost;
		this.hcost = hcost;
		this.fcost = gcost + hcost;
	}
}
