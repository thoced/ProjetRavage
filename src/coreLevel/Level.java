package coreLevel;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;

import coreAI.Node;
import coreCamera.CameraManager;
import corePhysic.PhysicWorldManager;
import CoreLoader.TiledObjectPolylinePoint;
import ravage.IBaseRavage;

public class Level implements IBaseRavage, Drawable 
{
	// Textures de background
	private List<Sprite> backgrounds;
	// Textures de foreground - Arbres et toits
	private List<Sprite> foregrounds;
	// Tableau des valeurs Tiled (pour la recherche de chemin)
	private Node[] nodes;
	// Obstacles box2d
	private Body obstacles;
	
	public Level()
	{
		// instances du background
		backgrounds = new ArrayList<Sprite>();
		// instance de foreground
		foregrounds = new ArrayList<Sprite>();
		// instance des nodes
		nodes = new Node[375*250];
		
		
	}
	
	public void drawBackground(RenderTarget render,RenderStates state)
	{
		// on affiche le background du level
		
				for(Sprite s : backgrounds)
				{
					FloatRect result = s.getGlobalBounds().intersection(CameraManager.getCameraBounds());
					if(result != null)
						render.draw(s);
				}
	}
	
	public void drawForeground(RenderTarget render,RenderStates state)
	{
		// on affiche le foreground
				for(Sprite f : foregrounds)
				{
					FloatRect result = f.getGlobalBounds().intersection(CameraManager.getCameraBounds());
					if(result != null)
						render.draw(f);
				}
	}
	
	@Override
	public void draw(RenderTarget render, RenderStates state) 
	{
		// on affiche le background du level
		
		for(Sprite s : backgrounds)
		{
			FloatRect result = s.getGlobalBounds().intersection(CameraManager.getCameraBounds());
			if(result != null)
				render.draw(s);
		}
		
		// on affiche le foreground
		for(Sprite f : foregrounds)
		{
			FloatRect result = f.getGlobalBounds().intersection(CameraManager.getCameraBounds());
			if(result != null)
				render.draw(f);
		}
		
		
	}
	
	public boolean isNodeObstacle(int x,int y)
	{
		Node n = this.nodes[(375 * y) + x];
		if(n.getType() != 0)
			return true;
		else
			return false;
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

	
	
	public List<Sprite> getForegrounds() {
		return foregrounds;
	}

	public void setForegrounds(List<Sprite> foregrounds) {
		this.foregrounds = foregrounds;
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
	public Node[] getNodes() {
		return nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(Node[] nodes) {
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
	
	
	
	public void InsertObstacle(List<TiledObjectPolylinePoint> listePoint,int x,int y,String typeobstacle) throws java.lang.RuntimeException
	{
		// ajout d'un obstacle de type polyline
		
		// on créer un bodydef
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.STATIC;
		bdef.bullet = false;
		// on determine 
		
		// on crée la chainshape
		ChainShape cs = new ChainShape();
		// on créer un vecteur de vec2 correspondant au nombre de point
		Vec2[] vectors = new Vec2[listePoint.size()];
		// on instance la liste des vecteurs
		for(int i=0;i<vectors.length;i++)
			vectors[i] = new Vec2();
		// on ajoute les vecteurs
		
		// on récupère la position initial de polyline
		float bx = x / PhysicWorldManager.getRatioPixelMeter();
		float by = y / PhysicWorldManager.getRatioPixelMeter();
		
		int ind = 0;
		for(Vec2 v : vectors)
		{
			// onrécupère les points de la listes
			float diffx = listePoint.get(ind).x / PhysicWorldManager.getRatioPixelMeter();
			float diffy = listePoint.get(ind).y / PhysicWorldManager.getRatioPixelMeter();
			// on ajoute la différence entre les coordonnées du pont initial et la liste des points
			v.set(bx + diffx,by + diffy);
			ind++;
			
		}
		// on ajoute le tout dans le chainshape
		try
		{
			cs.createChain(vectors, vectors.length);
			
			// création du body
			Body bodyObstacle = PhysicWorldManager.getWorld().createBody(bdef);
			bodyObstacle.setUserData(typeobstacle);
			
			// creation dufixture
			FixtureDef fixture = new FixtureDef();
			fixture.shape = cs;
			fixture.friction = 0.6f;
			fixture.density = 1f;
			fixture.restitution = 0.0f;
	
			// ajout dans le body
			bodyObstacle.createFixture(fixture);
		}
		catch(java.lang.RuntimeException e)
		{
			
		}
		
		
	}



}
