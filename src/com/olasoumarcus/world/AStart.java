package com.olasoumarcus.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AStart {
	public static double LastTime = System.currentTimeMillis();
	private static int POSITIONS = 9;
	private static int POSITION_IGNORED = 4;
	private static Comparator<Node> nodeSorter = new Comparator<Node>() {

		@Override
		public int compare(Node o1, Node o2) {
			if (o2.fcost < o1.fcost ) {
				return 1;
			}
			
			if (o2.fcost > o1.fcost) {
				return -1;
			}

			return 0;
		}
	};
	
	public static boolean clear() {
		return System.currentTimeMillis() - LastTime >= 1000;
	}
	
	
	// método principal para buscar caminho.
	public static List<Node> findPath(World world, Vector2I start, Vector2I end) {
		LastTime = System.currentTimeMillis();		
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		
		Node current = new Node(start, null,0,getDistance(start,end));
		openList.add(current);
		
		while (!openList.isEmpty()) {
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			
			if (current.equals(end)) {
				List<Node> path = new ArrayList<Node>();
				
				while (current.parent != null) {
					// adicionando item atual
					path.add(current);
					// o parent é o proximo current
					current = current.parent;
					// segue o fluxo.
				}
				
				openList.clear();
				closedList.clear();
				
				return path;
			}
			
			openList.remove(current);
			closedList.add(current);
			
			for (int i = 0; i < POSITIONS; i++) {
				 if (i == POSITION_IGNORED) continue;
				 
				 int x = current.tile.x;
				 int y = current.tile.y;
				 int xi = (i%3) - 1;
				 int yi = (i/3) - 1;
				 
				 // achar o tile na dimensão.
				 Tile tile = World.tiles[x+xi+((y+yi)*World.WIDTH)];
				 
				 if (tile == null) continue;
				 if (tile instanceof WallTile) continue;

				 if (i == 0) {
					 Tile test = World.tiles[x+xi+1+((y+yi)*World.WIDTH)];
					 Tile test2 = World.tiles[x+xi+1+((y+yi)*World.WIDTH)];
					 
					 if (test instanceof WallTile || test2 instanceof WallTile) {
						 continue;
					 }
				 }
				 else if (i == 2) {
					 Tile test = World.tiles[x+xi+1+((y+yi)*World.WIDTH)];
					 Tile test2 = World.tiles[x+xi+((y+yi)*World.WIDTH)];
					 
					 if (test instanceof WallTile || test2 instanceof WallTile) {
						 continue;
					 }
				 }
				 else if (i == 6) {
					 Tile test = World.tiles[x+xi+((y+yi-1)*World.WIDTH)];
					 Tile test2 = World.tiles[x+xi+1+((y+yi)*World.WIDTH)];
					 
					 if (test instanceof WallTile || test2 instanceof WallTile) {
						 continue;
					 }
				 }
				 else if (i == 8) {
					 Tile test = World.tiles[x+xi+((y+yi-1)*World.WIDTH)];
					 Tile test2 = World.tiles[x+xi-1+((y+yi)*World.WIDTH)];
					 
					 if (test instanceof WallTile || test2 instanceof WallTile) {
						 continue;
					 }
				 }
				 
				 Vector2I vector = new Vector2I(xi + x, yi + y);
				 double gCost = current.gcost + getDistance(current.tile, vector);
				 double hCost = getDistance(vector, end);
				 
				 Node node = new Node(vector, current, gCost, hCost);
				 
				 if (vectorInList(closedList, vector) && gCost >= current.gcost) continue;
				 
				 if (!vectorInList(openList, vector)) {
					 openList.add(node);	 
				 }
				 else if (gCost < current.gcost) {
					 openList.remove(current);
					 openList.add(node);
				 }
			}
		}

		closedList.clear();

		return null;
	}
	
	private static double getDistance(Vector2I tile, Vector2I target) {
		double dx = (tile.x - target.x) * (tile.x - target.x);
		double dy = (tile.y - target.y) * (tile.y - target.y);
		
		return Math.sqrt(dx+dy);
	}
	
	private static boolean vectorInList(List<Node> list, Vector2I vector) {
		for (Node node : list) {
			if (node.tile.equals(vector)) return true;
		}

		return false;
	}
}
