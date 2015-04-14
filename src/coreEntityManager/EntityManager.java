package coreEntityManager;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.system.Time;

import coreEntity.Unity;
import ravage.IBaseRavage;

public class EntityManager implements IBaseRavage 
{
	
	private static List<Unity> vectorUnity;

	@Override
	public void init()
	{
		// TODO Auto-generated method stub
		vectorUnity = new ArrayList<Unity>();
		
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

}
