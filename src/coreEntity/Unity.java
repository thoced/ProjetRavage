package coreEntity;

import org.jsfml.system.Time;

import ravage.IBaseRavage;

public class Unity implements IBaseRavage 
{

	private float posx,posy;
	
	private int   nodex,nodey;
	
	private float rotation;
	
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
	
	

}
