package coreEntity;

import java.util.Random;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.system.Time;

import corePhysic.PhysicWorldManager;
import ravage.IBaseRavage;

public class Unity implements IBaseRavage 
{

	private float posx,posy;
	
	private int   nodex,nodey;
	
	private float rotation;
	
	private Body body;
	
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
		
		fDef.friction = 0.5f;
		fDef.restitution = 0.0f;
	
		Fixture fix = body.createFixture(fDef);
		
		// test de déplacement
		Random rand = new Random();
		float x = rand.nextFloat();
		float y = rand.nextFloat();
		

		
		
		body.setLinearVelocity(new Vec2(x*100,y*100));

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
	
	

}
