package coreNet;

public class NetStrike extends NetBase 
{
	private int idStriker;
	
	private int idTarget;
	
	private int force;

	public int getIdStriker() {
		return idStriker;
	}

	public int getIdTarget() {
		return idTarget;
	}

	public int getForce() {
		return force;
	}

	public void setIdStriker(int idStriker) {
		this.idStriker = idStriker;
	}

	public void setIdTarget(int idTarget) {
		this.idTarget = idTarget;
	}

	public void setForce(int force) {
		this.force = force;
	}
	
	
}
