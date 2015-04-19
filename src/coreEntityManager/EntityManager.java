package coreEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard.Key;

import coreEntity.Unity;
import ravage.IBaseRavage;
import ravage.IEvent;

public class EntityManager implements IBaseRavage,IEvent 
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

	@Override
	public void onMouse(Vector2f pos, int click) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyboard(Key k) {
		// TODO Auto-generated method stub
		
	}

}
