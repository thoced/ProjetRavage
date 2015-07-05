package coreEntity;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;

import coreAI.Node;
import coreEntity.Unity.ANIMATE;
import coreEntity.Unity.TYPEUNITY;
import coreNet.NetHeader;
import coreNet.NetMoveUnity;
import coreNet.NetHeader.TYPE;
import corePhysic.PhysicWorldManager;

public class UnityNet extends Unity 
{
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
	}

	@Override
	public void update(Time deltaTime) 
	{
		// -------------------------------------
				// Code de déplacement - mouvement
				// -------------------------------------
				
		// on positionne les coordonnées écran par rapport au coordonnée physique
				posx = body.getPosition().x * PhysicWorldManager.getRatioPixelMeter();
				posy = body.getPosition().y * PhysicWorldManager.getRatioPixelMeter();
		
				this.body.setLinearVelocity(new Vec2(0f,0f)); // il est arrivé à destination
				
				Vec2 n = null;
				if(next != null) // il y a un node suivant
				{
					
					System.out.println("dans l'update");
					// on calcul le vecteur velocity de différence
					n = next.getPositionVec2();
					
					
				}
			
				
				
				if(n!=null)
				{
						if(this.vecDirFormation == null)
							this.vecTarget = n.sub(this.body.getPosition());
						else
							this.vecTarget = this.vecDirFormation;  // on a recu le dernier message avec la formation finale
						
						if(this.vecTarget.length() < 0.4f)
						{
							next = null;
							this.body.setLinearVelocity(new Vec2(0f,0f));
		
						}
						else
						{
							this.vecTarget.normalize();
							// on calcul la rotation
							this.computeRotation(this.vecTarget);
							// on applique un vecteur de d�placement
							this.body.setLinearVelocity(this.vecTarget.mul(6f));
							
							
						}	
				}
				
				// appel de l'animate
				this.animate(deltaTime);
			
	}

	@Override
	public  void animate(Time deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDamage(int damage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setKill()
	{
		// TODO Auto-generated method stub
		this.setKilled(true);
	}

	@Override
	public void strikeNow() {
		// TODO Auto-generated method stub
		
	}

	

	



	

	
}
