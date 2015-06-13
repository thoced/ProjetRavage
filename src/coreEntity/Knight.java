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

import corePhysic.PhysicWorldManager;

public class Knight extends Unity 
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
				shape.m_radius = 0.5f;
				
				FixtureDef fDef = new FixtureDef();
				fDef.shape = shape;
				fDef.density = 1.0f;
				
				fDef.friction = 0.0f;
				fDef.restitution = 0.0f;
			
				Fixture fix = body.createFixture(fDef);
				
				// instance du resetSearch
				resetSearchClock = new Clock();
				
				// anim sprite
				this.animSpriteRect = new IntRect[15];
				for(int i=0;i<15;i++)
					this.animSpriteRect[i] = new IntRect(0 + i * 32,0,32,32);
				
				this.currentAnim = this.animSpriteRect[0];
				this.indAnim = 0;
				
				// type d'unity
				this.idType = TYPEUNITY.KNIGHT;
	}

	@Override
	public void update(Time deltaTime) 
	{
		// appel au super constructeur
		super.update(deltaTime);
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
				indAnim = 0;
		}
		
	}
	
	
	
}
