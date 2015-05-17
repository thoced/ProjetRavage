package coreEntity;

import org.jbox2d.common.Vec2;
import org.jsfml.system.Time;

import corePhysic.PhysicWorldManager;

public class UnityNet extends Unity 
{

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
				
				if(next != null) // il y a un node suivant
				{
					
					System.out.println("dans l'update");
					// on calcul le vecteur velocity de différence
					Vec2 n = next.getPositionVec2();
					
					Vec2 diff = n.sub(this.body.getPosition());
					if(diff.length() < 0.6f)
					{
						next = null;
						
					
					}
					else
					{
						diff.normalize();
						this.body.setLinearVelocity(diff.mul(6f));
					}	
				}
	}
	
}
