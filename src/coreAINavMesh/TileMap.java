package coreAINavMesh;

import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import coreAI.Node;

public class TileMap implements TileBasedMap 
{
	private Node[] map;
	
	private int testmap[] = {0,0,0,0,0,0,0,0,
							0,0,0,0,0,0,0,0,
							1,0,0,0,0,0,0,0,
							0,0,0,0,0,0,0,0,
							0,0,0,0,0,0,0,0,
							1,1,1,1,0,0,0,0,
							1,1,1,1,0,0,0,0,
							0,0,0,0,0,0,0,0,};
							
	
	
	public TileMap(Node[] map)
	{
		this.map = map;
		
		
	}

	@Override
	public boolean blocked(PathFindingContext arg0, int x, int y) {
		// TODO Auto-generated method stub
		if(this.map[(375 * y) + x ].getType() != 0)
			return true;
		else
			return false;
	}

	@Override
	public float getCost(PathFindingContext arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return  1.0f;
	}

	@Override
	public int getHeightInTiles() {
		// TODO Auto-generated method stub
		return 250;
	}

	@Override
	public int getWidthInTiles() {
		// TODO Auto-generated method stub
		return 375;
	}

	@Override
	public void pathFinderVisited(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
