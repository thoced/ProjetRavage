package coreEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.navmesh.NavPath;

import coreAI.AstarManager;
import coreAI.ICallBackAStar;
import coreAI.Node;
import coreLevel.LevelManager;
import coreNet.NetHeader;
import coreNet.NetHeader.TYPE;
import coreNet.NetManager;
import coreNet.NetMoveUnity;
import coreNet.NetSynchronize;
import corePhysic.PhysicWorldManager;
import ravage.IBaseRavage;

public class Unity implements IBaseRavage,ICallBackAStar
{
	protected final static float  TIME_BEFORE_TELEPORTATION = 0.5f;

	protected int id;
	
	protected float posx,posy;
	
	protected int tx,ty;
	
	protected float tfx,tfy;  // target en pixels
	
	protected int   nodex,nodey;
	
	protected float rotation;
	
	protected Body body;
	
	protected Vec2 targetPosition;
	
	protected Vec2 vecTarget;
	
	protected Vec2 vecDirFormation; // vecteur de formation finale
	
	// pathfinal pour le systeme classique
	protected List<Node> pathFinal;
	// pathfinal pour le systeme navmesh
	protected NavPath pathFinalNavMesh;
	protected Path pathFinalPath;
	protected int     indNavMesh = 0;
	protected int     cptNavMesh = 0;
	protected boolean isArrived = false;
	
	protected Clock resetSearchClock;
	protected Time  elapseSearchClock = Time.ZERO;

	protected float elapse = 0f;
	protected int ind = 0;
	
	protected Node next = null;
	
	// is selected
	protected boolean isSelected = false;
	
	protected boolean nextNode = true;
	protected int indexNode = 0;
	protected float mx =0;
	protected float my = 0;
	
	protected boolean isStop =false;
	
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
		bdef.userData = this;
	
		//bdef.gravityScale = 0.0f;
		
		// creation du body
		body = PhysicWorldManager.getWorld().createBody(bdef);
		
		Shape shape = new CircleShape();
		shape.m_radius = 0.45f;
		
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.density = 1.0f;
		
		fDef.friction = 0.0f;
		fDef.restitution = 0.0f;
	
		Fixture fix = body.createFixture(fDef);
		
		// instance du resetSearch
		resetSearchClock = new Clock();
		
	
	
		
		
		
	
	
	
	}
	
	
	
	public Vec2 getVecTarget() {
		return vecTarget;
	}



	public void setVecTarget(Vec2 vecTarget) {
		this.vecTarget = vecTarget;
	}



	public void stop()
	{
		this.isStop = true;
	}
	
	public boolean setTargetPosition(float px,float py,int tx,int ty,Vec2 dir)
	{
		// instance de vecteur de formation finale
		this.vecDirFormation = dir;
		
		this.tfx = px;
		this.tfy = py;
		// une demande de chemin va être effectuée, on stoppe l'unité pour éviter le phénomène de rebond
		this.body.setLinearVelocity(new Vec2(0f,0f)); // il est arrivÃ© Ã  destination
		
		//this.targetPosition = new Vec2((float)tx + 0.5f,(float)ty + 0.5f);
		this.tx = tx;
		this.ty = ty;
		// on fait une demande au manager
		int pdx =  (int) ((int)this.getBody().getPosition().x );
		int pdy =  (int) ((int)this.getBody().getPosition().y );
		
		float fx = this.getBody().getPosition().x - 0.5f;
		float fy = this.getBody().getPosition().y - 0.5f;
		
		float ftx = (float)tx;
		float fty = (float)ty;
		
		try 
		{
			// si le target position est sur un node noir, on ne fait aucune recherche
			if(LevelManager.getLevel().getNodes()[(ty * 375) + tx].getType() == 0)
			{
				// on remet Ã  zero l'elapsed timer pour la tÃ©lÃ©portation
				elapseSearchClock = Time.ZERO;
				// on remet Ã  zero le pathfinal
			//	if(this.pathFinalPath != null)
					//this.pathFinalPath.clear();
				// Lancement recherche
				AstarManager.askPath(this, pdx, pdy, tx, ty); // classic
				//AstarManager.askPath(this, fx, fy, ftx, fty);
				return true;
			}
			else
			{
				return false; // return false car il n'y pas de destination possible
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  catch(ArrayIndexOutOfBoundsException ex)
		{
			  
		}
		return false;
	}
	
	public void setLinearVelocity(Vec2 v)
	{
		body.setLinearVelocity(v);
		
	}
	
	public void setPosition(float x,float y)
	{
		body.setTransform(new Vec2((float)x + 0.5f,(float)y + 0.5f),0f);
		posx = body.getPosition().x * PhysicWorldManager.getRatioPixelMeter();
		posy = body.getPosition().y * PhysicWorldManager.getRatioPixelMeter();
		this.tfx = posx;
		this.tfy = posy;
		this.isArrived = true;
		
	}
	
	public float getPositionMeterX()
	{
		return body.getPosition().x;
	}
	
	public float getPositionMeterY()
	{
		return body.getPosition().y;
	}
	
	protected void NetSend(float x,float y,float dx,float dy)
	{
		    NetHeader header = new NetHeader();
			header.setTypeMessage(TYPE.MOVE);
			NetMoveUnity move = new NetMoveUnity();
			move.setId(this.getId());
			move.setPosx(x);
			move.setPosy(y);
			move.setNextPosx(dx);
			move.setNextPosy(dy);
			header.setMessage(move);
			// émission
			try
			{
				NetManager.PackMessage(header);
				//NetManager.SendMessage(header);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
	}
	
	
	protected void NetSendSynchronise()
	{
		NetHeader header = new NetHeader();
		header.setTypeMessage(TYPE.SYNC);
		NetSynchronize sync = new NetSynchronize();
		sync.setIdUnity(this.getId());
		sync.setPosx(this.getPositionMeterX());
		sync.setPosy(this.getPositionMeterY());
		sync.setRotation(this.getBody().getAngle());
		header.setMessage(sync);
		// émission
		try
		{
			NetManager.PackMessage(header);
			//NetManager.SendMessage(header);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void computeRotation()
	{
		if(this.vecTarget != null)
		{
			Rot r = new Rot();
			r.s = this.vecTarget.y;
			r.c = this.vecTarget.x;
			float angle = r.getAngle(); 
			this.body.setTransform(this.getBody().getPosition(), angle);
		}
	}

	@Override
	public void update(Time deltaTime) 
	{
		// on positionne les coordonnÃ©es Ã©cran par rapport au coordonnÃ©e physique
		posx = body.getPosition().x * PhysicWorldManager.getRatioPixelMeter();
		posy = body.getPosition().y * PhysicWorldManager.getRatioPixelMeter();
		
		if(this.isStop)
			return;
		
		// -------------
		// téléportation
		// -------------
		
		
		if(!nextNode)
		{
			elapseSearchClock = Time.add(elapseSearchClock, deltaTime);
			if(elapseSearchClock.asSeconds() > TIME_BEFORE_TELEPORTATION) // si bloquÃ© plus de 2 secondes
			{
				elapseSearchClock = Time.ZERO;
				// on saute une node de recherche
				if(this.pathFinalPath != null && this.pathFinalPath.getLength() > 0 && this.indexNode < this.pathFinalPath.getLength())
				{
					int x = this.pathFinalPath.getX(this.indexNode);
					int y = this.pathFinalPath.getY(this.indexNode);
					//this.pathFinal.remove(0);
					// on tÃ©lÃ©porte l'unitÃ©
					this.body.setTransform(new Vec2(x,y), 0f);
					nextNode = true;
				}
				

			}
		}
		
		
		// ------------------------------------
		// récupération d'une nouvelle position
		// ------------------------------------
		
				if(nextNode && this.pathFinalPath != null && this.pathFinalPath.getLength() > 0 && this.indexNode  < this.pathFinalPath.getLength()  )
				{
					// on récupère la node suivante
				
					int nx = this.pathFinalPath.getX(this.indexNode);
					int ny = this.pathFinalPath.getY(this.indexNode);
					
					// on lance le clock du resetSearchClock
					elapseSearchClock = Time.ZERO;
					
					// transformation en postion meter
					
					 mx = (float)nx + 0.5f;
					 my = (float)ny +0.5f;
					
					 nextNode = false;
					 
					 this.indexNode++;
					 
					 // envoie des informations sur le réseau
					 this.NetSend(this.body.getPosition().x, this.body.getPosition().y, mx, my);
					 
				}
				else
				{
				// -------------------------------------
				// mouvement vers la destination finale
				// ------------------------------------
						
					Vec2 n =  new Vec2(this.tfx / PhysicWorldManager.getRatioPixelMeter(),this.tfy  / PhysicWorldManager.getRatioPixelMeter());
					this.vecTarget = n.sub(this.body.getPosition());
					
					if(this.vecTarget.length() < 0.2f )
						{
							this.body.setLinearVelocity(new Vec2(0f,0f));
							// envoie sur le réseau
									
							if(!this.isArrived)
							{
								this.NetSend(this.body.getPosition().x, this.body.getPosition().y,this.body.getPosition().x, this.body.getPosition().y);
								this.isArrived = true;
							
							}
							
							
							// il est arrivé, on tourne l'axe vers la vecteur fleche pointé
							this.vecTarget = this.vecDirFormation;
							// on détermine l'angle du sprite
							this.computeRotation();
							// envoie de la synchronisation finale
							this.NetSendSynchronise();
									
						}
						else
						{
							this.vecTarget.normalize();
							// on calcul la rotation
							this.computeRotation();
							// on applique un vecteur de déplacement
							this.body.setLinearVelocity(this.vecTarget.mul(6f));
							
							// envoie des informations sur le réseau
								this.NetSend(this.body.getPosition().x, this.body.getPosition().y, n.x, n.y);
								
								this.isArrived = false;
						}
					
					
				}
				
				
				if(!nextNode)
				{
				
					// -----------------------------------
					// Mouvement vers une destination centrale à un node
					// ------------------------------------------------
				
					Vec2 n =  new Vec2(mx,my);
					this.vecTarget = n.sub(this.body.getPosition());
				
					if(this.vecTarget.length() < 0.4f)
					{
						nextNode = true;
						this.body.setLinearVelocity(new Vec2(0f,0f));
					
					
					}
					else
					{
						this.vecTarget.normalize();
						// on calcul la rotation
						this.computeRotation();
						// on applique un vecteur de déplacement
						this.body.setLinearVelocity(this.vecTarget.mul(6f));
					}
					
				}
				
				
		// ------------------------------------
		// Code pour tÃ©lÃ©porter une unitÃ© bloquÃ©
		// ------------------------------------
			
	}
	
	public Vec2 getVectorTarget()
	{
		if(this.vecTarget == null)
			return new Vec2(1,0);
		else
			return this.vecTarget;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public float getPosXMeter()
	{
		return this.body.getPosition().x;
	}
	
	public float getPosYMeter()
	{
		return this.body.getPosition().y;
	}
	
	public void setPosXYMeter(float x,float y)
	{
		this.body.setTransform(new Vec2(x,y), 0f);
		posx = body.getPosition().x * PhysicWorldManager.getRatioPixelMeter();
		posy = body.getPosition().y * PhysicWorldManager.getRatioPixelMeter();
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
	
	

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
			System.out.println("UnitÃ© : " + this.toString() + " est sÃ©lectionnÃ©");
	}

	@Override
	public void onCallSearchPath(List<Node> finalPath) 
	{
		// TODO Auto-generated method stub
		this.pathFinal = finalPath;
		
		System.out.println(this.toString() +  " : " + finalPath.size());
		
	}

	@Override
	public void onCallSearchPath(NavPath finalPath) 
	{
		
		if(finalPath != null)
		{
			this.pathFinalNavMesh = finalPath;
			this.cptNavMesh = this.pathFinalNavMesh.length();
			this.indNavMesh = 1;
			elapseSearchClock = Time.ZERO;
			this.isArrived = false;
			
			
		
		}
		
		
	}

	@Override
	public void onCallsearchPath(Path finalPath) 
	{
	
		
		if(finalPath != null)
		{
			this.pathFinalPath = finalPath;
			this.cptNavMesh = this.pathFinalPath.getLength();
			this.indNavMesh = 1;
			elapseSearchClock = Time.ZERO;
		
			this.indexNode = 1;
			this.nextNode = true;
			this.isArrived = false;
		}
	}


}
