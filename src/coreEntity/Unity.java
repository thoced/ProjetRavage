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
	
	private int   nodex,nodey;
	
	private float rotation;
	
	private Body body;
	
	private Vec2 targetPosition;
	
	private List<Node> pathFinal;
	
	
	private float elapse = 0f;
	private int ind = 0;
	
	private Node next;
	
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
		shape.m_radius = 0.5f;
		
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.density = 1.0f;
		
		fDef.friction = 0.9f;
		fDef.restitution = 0f;
	
		Fixture fix = body.createFixture(fDef);
		
		// test de déplacement
		Random rand = new Random();
		float x = rand.nextFloat();
		float y = rand.nextFloat();
		

		//this.setLinearVelocity(new Vec2(x*10,y*10));
		
		
	
	}
	
	public void setTargetPosition(Vec2 tp)
	{
		this.targetPosition = tp;
		
		// on fait une demande au manager
		int posx = (int) this.getBody().getPosition().x;
		int posy = (int) this.getBody().getPosition().y;
		
		int targetx = (int) tp.x;
		int targety = (int) tp.y;
		
		try 
		{
			AstarManager.askPath(this, posx, posy, targetx, targety);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setLinearVelocity(Vec2 v)
	{
		body.setLinearVelocity(v);
		
	}
	
	public void setPosition(Vec2 pos)
	{
		body.setTransform(pos, 0f);
	}

	@Override
	public void update(Time deltaTime) 
	{
		// on positionne les coordonnées écran par rapport au coordonnée physique
		posx = body.getPosition().x * PhysicWorldManager.getRatioPixelMeter();
		posy = body.getPosition().y * PhysicWorldManager.getRatioPixelMeter();
		
		// 
		if(next == null && this.pathFinal != null && this.pathFinal.size() > 0)
		{
			// on récupère le node prochain
			next = this.pathFinal.get(0);
			// on supprme le noduer premier
			this.pathFinal.remove(0);
			
		}
		
		if(next != null) // il y a un node suivant
		{
			// on calcul le vecteur velocity de différence
			Vec2 n = new Vec2(next.getX(),next.getY());
			
			Vec2 diff = n.sub(this.body.getPosition());
			if(diff.length() < 0.2f)
				next = null;
			else
			{
				diff.normalize();
				this.body.setLinearVelocity(diff.mul(2f));
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
	 * @param nodey the nodey to set
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

	@Override
	public void onCallSearchPath(List<Node> finalPath) 
	{
		// TODO Auto-generated method stub
		this.pathFinal = finalPath;
		
	}
	
	

}
