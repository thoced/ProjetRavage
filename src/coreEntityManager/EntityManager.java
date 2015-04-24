package coreEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;

import coreEntity.Unity;
import corePhysic.PhysicWorldManager;
import ravage.FrameWork;
import ravage.IBaseRavage;
import ravage.IEvent;

public class EntityManager implements IBaseRavage,IEvent 
{
	
	private static List<Unity> vectorUnity;
	// test clock
	private Clock clock;
	private Time delta;
	// unity selected
	private Unity unitySelected = null;

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
		// on parse les unité
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
	public void onMouse(MouseButtonEvent mouseEvent) 
	{
		
		Vector2f posMouseWorld = FrameWork.getWindow().mapPixelToCoords(mouseEvent.position);
		float pixels =  PhysicWorldManager.getRatioPixelMeter();
		
		// si c'est un click gauche
		
		if(mouseEvent.button == Mouse.Button.LEFT)
		{
		
			// si une unité est déja selectionné, on déselectionne avant l'unité
			if(unitySelected != null)
				unitySelected.setSelected(false);
			
			Vec2 mousePos = new Vec2(posMouseWorld.x / pixels,posMouseWorld.y / pixels ); 
			for(Unity unity : vectorUnity)
			{
				// si la souris est sur l'unité
				if(unity.getBody().getPosition().sub(mousePos).length() < .5f)
				{
					unity.setSelected(true);
					unitySelected = unity;
					break;
				}
				
			}
			
			
		}
		else if(mouseEvent.button == Mouse.Button.RIGHT)
		{
			if(unitySelected != null)
			{
				// on précise la node target
				Vector2f pos = Vector2f.div(posMouseWorld, pixels);
				unitySelected.setTargetPosition((int)pos.x+1,(int)pos.y+1);
			}
		}
		
		
	}

	@Override
	public void onKeyboard(KeyEvent keyboardEvent) {
		// TODO Auto-generated method stub
		
	}

	

}
