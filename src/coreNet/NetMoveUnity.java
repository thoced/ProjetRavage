package coreNet;

import org.jbox2d.common.Vec2;

public class NetMoveUnity extends NetBase 
{
	private int id;
	
	private float posx;
	
	private float posy;
	
	private float nextPosx;
	
	private float nextPosy;
	
	private Vec2 vecDirFormation;

		
	public Vec2 getVecDirFormation() {
		return vecDirFormation;
	}

	public void setVecDirFormation(Vec2 vecDirFormation) {
		this.vecDirFormation = vecDirFormation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

	public float getPosx() {
		return posx;
	}

	public void setPosx(float posx) {
		this.posx = posx;
	}

	public float getPosy() {
		return posy;
	}

	public void setPosy(float posy) {
		this.posy = posy;
	}

	public float getNextPosx() {
		return nextPosx;
	}

	public void setNextPosx(float nextPosx) {
		this.nextPosx = nextPosx;
	}

	public float getNextPosy() {
		return nextPosy;
	}

	public void setNextPosy(float nextPosy) {
		this.nextPosy = nextPosy;
	}
	
	
}
