package coreEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;

import coreAI.Astar;
import coreAI.AstarManager;
import coreAI.ICallBackAStar;
import coreAI.Node;
import coreLevel.LevelManager;
import corePhysic.PhysicWorldManager;
import ravage.IBaseRavage;

public class Unity implements IBaseRavage,ICallBackAStar
{

	private float posx,posy;
	
	private int tx,ty;
	
	private int   nodex,nodey;
	
	private float rotation;
	
	private Body body;
	
	private Vec2 targetPosition;
	
	private List<Node> pathFinal;
	
	private Clock resetSearchClock;
	private Time  elapseSearchClock = Time.ZERO;

	private float elapse = 0f;
	private int ind = 0;
	
	private Node next = null;
	
	// is selected
	private boolean isSelected = false;
	
	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		// intialisation du body
		BodyDef bdef = new BodyDef();
		bdef.active = true;
		bdef.bullet = false;
		bdef.type = BodyType.DYNAMIC;
		bdef.fixedRotation = false;
		//bdef.gravityScale = 0.0f;
		
		// creation du body
		body = PhysicWorldManager.getWorld().createBody(bdef);
		
		Shape shape = new CircleShape();
		shape.m_radius = 0.4f;
		
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.density = 1.0f;
		
		fDef.friction = 0.2f;
		fDef.restitution = 0.2f;
	
		Fixture fix = body.createFixture(fDef);
		
		// instance du resetSearch
		resetSearchClock = new Clock();
		
	
	
	}
	
	public void setTargetPosition(int tx,int ty)
	{
		//this.targetPosition = new Vec2((float)tx + 0.5f,(float)ty + 0.5f);
		this.tx = tx;
		this.ty = ty;
		// on fait une demande au manager
		int px =  (int) ((int)this.getBody().getPosition().x - 0.5f);
		int py =  (int) ((int)this.getBody().getPosition().y - 0.5f);
		
		try 
		{
			// si le target position est sur un node noir, on ne fait aucune recherche
			if(LevelManager.getLevel().getNodes()[(ty * 375) + tx].getType() == 0)
				AstarManager.askPath(this, px, py, tx, ty);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setLinearVelocity(Vec2 v)
	{
		body.setLinearVelocity(v);
		
	}
	
	public void setPosition(int x,int y)
	{
		body.setTransform(new Vec2((float)x + 0.5f,(float)y + 0.5f),0f);
		posx = body.getPosition().x * PhysicWorldManager.getRatioPixelMeter();
		posy = body.getPosition().y * PhysicWorldManager.getRatioPixelMeter();
		
	}

	@Override
	public void update(Time deltaTime) 
	{
		// on positionne les coordonnées écran par rapport au coordonnée physique
		posx = body.getPosition().x * PhysicWorldManager.getRatioPixelMeter();
		posy = body.getPosition().y * PhysicWorldManager.getRatioPixelMeter();
		
		
		// ------------------------------------
		// Code pour téléporter une unité bloqué
		// ------------------------------------
		if(next != null)
		{
			elapseSearchClock = Time.add(elapseSearchClock, deltaTime);
			if(elapseSearchClock.asSeconds() > 2f) // si bloqué plus de 2 secondes
			{
				elapseSearchClock = Time.ZERO;
				// on saute une node de recherche
				if(this.pathFinal.size() > 0)
				{
					Node node = this.pathFinal.get(0);
					this.pathFinal.remove(0);
					// on téléporte l'unité
					this.body.setTransform(node.getPositionVec2(), 0f);
					next = null;
				}
				

			}
		}
		
		// -------------------------------------
		// Code pour prendre le node suivant
		// -------------------------------------
		
		if(next == null && this.pathFinal != null && this.pathFinal.size() > 0)
		{
			// on récupère le node prochain
			next = this.pathFinal.get(0);
			// on supprme le noduer premier
			this.pathFinal.remove(0);
			// on lance le clock du resetSearchClock
			elapseSearchClock = resetSearchClock.restart();
			
		}
		else
			this.body.setLinearVelocity(new Vec2(0f,0f)); // il est arrivé à destination
		
		// -------------------------------------
		// Code de déplacement - mouvement
		// -------------------------------------
		
		if(next != null) // il y a un node suivant
		{
			// on calcul le vecteur velocity de différence
			Vec2 n = next.getPositionVec2();
			
			Vec2 diff = n.sub(this.body.getPosition());
			if(diff.length() < 0.4f)
				next = null;
			else
			{
				diff.normalize();
				this.body.setLinearVelocity(diff.mul(6f));
			}	
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the posx
	 */
	public float getPosx() {
		return posx;
	}

	/**
	 * @param posx the posx to set
	 */
	public void setPosx(float posx) {
		this.posx = posx;
	}

	/**
	 * @return the posy
	 */
	public float getPosy() {
		return posy;
	}

	/**
	 * @param posy the posy to set
	 */
	public void setPosy(float posy) {
		this.posy = posy;
	}

	/**
	 * @return the nodex
	 */
	public int getNodex() {
		return nodex;
	}

	/**
	 * @param nodex the nodex to set
	 */
	public void setNodex(int nodex) {
		this.nodex = nodex;
	}

	/**
	 * @return the nodey
	 */
	public int getNodey() {
		return nodey;
	}

	/**
	 * @param nodey the vnodey to set
	 */
	public void setNodey(int nodey) {
		this.nodey = nodey;
	}

	/**
	 * @return the rotation
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return the body
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(Body body) {
		this.body = body;
	}
	
	

	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) 
	{
		this.isSelected = isSelected;
		if(this.isSelected)
			System.out.println("Unité : " + this.toString() + " est sélectionné");
	}

	@Override
	public void onCallSearchPath(List<Node> finalPath) 
	{
		// TODO Auto-generated method stub
		this.pathFinal = finalPath;
		
		System.out.println(this.toString() +  " : " + finalPath.size());
		
	}
	
	

}
