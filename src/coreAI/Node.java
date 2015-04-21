package coreAI;

import org.jbox2d.common.Vec2;

public class Node 
{
	private int type; // wall, ground, ...
	private int x;
	private int y;
	private Node parent;
	private int g;
	private int h;
	private int f;
	private boolean isDiagonal;
	
	public Node(int x,int y,boolean isdiagonal)
	{
		this.x = x;
		this.y = y;
		this.g=0;
		this.h=0;
		this.f=0;
		this.isDiagonal = isdiagonal;
		this.parent = this;
	}
	
	public void reset(boolean diagonal)
	{
		this.g=0;
		this.h=0;
		this.f=0;
		this.isDiagonal = diagonal;
		this.parent = this;
	}

	public Vec2 getPositionVec2()
	{
		return new Vec2(this.x +0.5f,this.y + 0.5f);
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
	 * @return the parent
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * @return the g
	 */
	public int getG() {
		return g;
	}

	/**
	 * @param g the g to set
	 */
	public void setG(int g) {
		this.g = g;
	}

	/**
	 * @return the h
	 */
	public int getH() {
		return h;
	}

	/**
	 * @param h the h to set
	 */
	public void setH(int h) {
		this.h = h;
	}

	/**
	 * @return the f
	 */
	public int getF() {
		return f;
	}

	/**
	 * @param f the f to set
	 */
	public void setF(int f) {
		this.f = f;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the isDiagonal
	 */
	public boolean isDiagonal() {
		return isDiagonal;
	}

	/**
	 * @param isDiagonal the isDiagonal to set
	 */
	public void setDiagonal(boolean isDiagonal) {
		this.isDiagonal = isDiagonal;
	}
	
	
	
}
