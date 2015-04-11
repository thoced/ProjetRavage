package coreLevel;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.dynamics.Body;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;

import ravage.IBaseRavage;

public class Level implements IBaseRavage, Drawable 
{
	// Textures de background
	private List<Sprite> backgrounds;
	// Tableau des valeurs Tiled (pour la recherche de chemin)
	private int[][] nodes;
	// Obstacles box2d
	private Body obstacles;
	
	public Level()
	{
		// instances du background
		backgrounds = new ArrayList<Sprite>();
		// instance des nodes
		nodes = new int[256][256];
		
		
	}
	
	@Override
	public void draw(RenderTarget render, RenderStates state) 
	{
		// on affiche le background du level
		for(Sprite s : backgrounds)
			render.draw(s);
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Time deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the backgrounds
	 */
	public List<Sprite> getBackgrounds() {
		return backgrounds;
	}

	/**
	 * @param backgrounds the backgrounds to set
	 */
	public void setBackgrounds(List<Sprite> backgrounds) {
		this.backgrounds = backgrounds;
	}

	/**
	 * @return the nodes
	 */
	public int[][] getNodes() {
		return nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(int[][] nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the obstacles
	 */
	public Body getObstacles() {
		return obstacles;
	}

	/**
	 * @param obstacles the obstacles to set
	 */
	public void setObstacles(Body obstacles) {
		this.obstacles = obstacles;
	}
	
	

}
