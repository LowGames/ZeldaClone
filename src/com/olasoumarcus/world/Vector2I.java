package com.olasoumarcus.world;

public class Vector2I {
	public int x,y;
	
	public Vector2I(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object object) {
		Vector2I vector = (Vector2I) object;
		
		return vector.x == x && vector.y == y;
	}
}
