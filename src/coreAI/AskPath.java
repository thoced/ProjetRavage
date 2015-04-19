package coreAI;

import java.util.List;

public class AskPath 
{
	private ICallBackAStar caller;
	
	private int x;
	private int y;
	private int tx;
	private int ty;

	
	public AskPath(ICallBackAStar caller,int posx,int posy,int targetx,int targety)
	
	{
		this.caller = caller;
		this.x = posx;
		this.y = posy;
		this.tx = targetx;
		this.ty = targety;
		
	}

	


	/**
	 * @return the caller
	 */
	public ICallBackAStar getCaller() {
		return caller;
	}

	/**
	 * @param caller the caller to set
	 */
	public void setCaller(ICallBackAStar caller) {
		this.caller = caller;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the tx
	 */
	public int getTx() {
		return tx;
	}

	/**
	 * @param tx the tx to set
	 */
	public void setTx(int tx) {
		this.tx = tx;
	}

	/**
	 * @return the ty
	 */
	public int getTy() {
		return ty;
	}

	/**
	 * @param ty the ty to set
	 */
	public void setTy(int ty) {
		this.ty = ty;
	}
	
	
}
