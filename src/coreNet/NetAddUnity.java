package coreNet;

import java.io.Serializable;

import coreEntity.Unity.TYPEUNITY;
import coreEntityManager.EntityManager.CAMP;

public class NetAddUnity extends NetBase implements Serializable
{
	private CAMP  campUnity;
	
	private TYPEUNITY typeUnity;
	
	private int idUnity;
	
	private float posx;
	
	private float posy;
	
	private float rotation;
	
	
	
	public CAMP getCampUnity() {
		return campUnity;
	}

	public void setCampUnity(CAMP campUnity) {
		this.campUnity = campUnity;
	}

	public TYPEUNITY getTypeUnity() {
		return typeUnity;
	}

	public void setTypeUnity(TYPEUNITY typeUnity) {
		this.typeUnity = typeUnity;
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

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public int getIdUnity() {
		return idUnity;
	}

	public void setIdUnity(int idUnity) {
		this.idUnity = idUnity;
	}
	
	
	
}
