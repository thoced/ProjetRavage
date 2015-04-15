package coreEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;

import coreEntity.Unity;
import ravage.IBaseRavage;

public class EntityManager implements IBaseRavage 
{
	
	private static List<Unity> vectorUnity;
	// test clock
	private Clock clock;
	private Time delta;

	@Override
	public void init()
	{
		// TODO Auto-generated method stub
		vectorUnity = new ArrayList<Unity>();
		
		clock = new Clock();
		delta = Time.ZERO;
	}

	@Override
	public void update(Time deltaTime) 
	{
		for(Unity unity : vectorUnity)
		{
			unity.update(deltaTime);
		}
		
		/*Time ret = clock.restart();
		delta = Time.add(delta,ret);
		if(delta.asSeconds() > 3f)
		{
			delta = Time.ZERO;
			for(Unity unity : vectorUnity)
			{
				Random rand = new Random();
				float x = rand.nextFloat();
				float y = rand.nextFloat();
				//unity.setLinearVelocity(new Vec2(0f,0f));
				unity.setLinearVelocity(new Vec2(x *10,y*10));
			}
		}
	
		*/
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the vectorUnity
	 */
	public static List<Unity> getVectorUnity() {
		return vectorUnity;
	}

	/**
	 * @param vectorUnity the vectorUnity to set
	 */
	public static void setVectorUnity(List<Unity> vectorUnity) {
		EntityManager.vectorUnity = vectorUnity;
	}

}
