package coreEntity;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;

import coreEntity.Unity.ANIMATE;
import coreEntity.Unity.TYPEUNITY;
import coreEntityManager.BloodManager;
import coreEntityManager.EntityManager;
import corePhysic.PhysicWorldManager;

public class KnightNet extends UnityNet 
{

	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		super.init();
		
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
				shape.m_radius = 0.55f;
				
				FixtureDef fDef = new FixtureDef();
				fDef.shape = shape;
				fDef.density = 1.0f;
				
				fDef.friction = 0.0f;
				fDef.restitution = 0.0f;
			
				Fixture fix = body.createFixture(fDef);
				
				// instance du resetSearch
				resetSearchClock = new Clock();
				
				// anim sprite
				this.animSpriteRect = new IntRect[16];
				for(int i=0;i<16;i++)
					this.animSpriteRect[i] = new IntRect(0 + i * 32,0,32,32);
				
				this.currentAnim = this.animSpriteRect[0];
				this.indAnim = 0;
				
				// type d'unity
				this.idType = TYPEUNITY.KNIGHT;
	}

	@Override
	public void update(Time deltaTime) 
	{
		// TODO Auto-generated method stub
		super.update(deltaTime);
		
		this.animate(deltaTime);
	}

	@Override
	public void animate(Time deltaTime) 
	{
		// TODO Auto-generated method stub
		super.animate(deltaTime);
		
		if(this.animate == ANIMATE.KILL)
		{
			this.currentAnim = this.animSpriteRect[15]; // on joue le sprite de kill
		}
		
		if(this.animate == ANIMATE.STRIKE) // en mode je frappe comme un gros lourd !!!
		{
				// on récupère l'animation courante
				this.currentAnim = this.animSpriteRect[indAnim];
				// on additionne le temps écoulé
				this.timeElapsedAnim = Time.add(this.timeElapsedAnim, deltaTime);
				// si le temps écoulé est supérieur à ***  on incrémente l'indice d'animation
				if(this.timeElapsedAnim.asSeconds() > 0.03f)
				{
					this.timeElapsedAnim = Time.ZERO;
					indAnim++;
					if(indAnim > 14)
					{
						indAnim = 0;
						this.animate = ANIMATE.PAUSE;
					}
				}
			
		}
		
		if(this.animate == ANIMATE.PAUSE) // en mode je suis sur place et je ne fais rien !!!
		{
			// on récupère l'animation courante
			this.currentAnim = this.animSpriteRect[0];
		}
		
		if(this.animate == ANIMATE.WALK) // en mode je me déplace !!!
		{
			// on récupère l'animation courante
			this.currentAnim = this.animSpriteRect[0];
		}
	}

	@Override
	public void setKill() {
		// TODO Auto-generated method stub
		super.setKill();
		
		// animation
		this.setAnimate(ANIMATE.KILL);
		// on supprime l'objet du net
		EntityManager.IamKilledNet(this);
		// on lance un peu de sang
		BloodManager.addUnityKilled(this.getPosx(), this.getPosy(),this.getMyCamp());
		
	}
	
	
}
