package coreNet;

import org.jbox2d.common.Vec2;

public class NetSynchronize extends NetBase 
{
	private int idUnity;
	
	private float posx;
	
	private float posy;
	
	private Vec2 vectorDirFormation; // formation final

	
	
	public Vec2 getVectorDirFormation() {
		return vectorDirFormation;
	}

	public void setVectorDirFormation(Vec2 vectorDirFormation) {
		this.vectorDirFormation = vectorDirFormation;
	}

	public int getIdUnity() {
		return idUnity;
	}

	public void setIdUnity(int idUnity) {
		this.idUnity = idUnity;
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

	
	
}
