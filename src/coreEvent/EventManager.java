package coreEvent;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import ravage.IBaseRavage;

public class EventManager implements IBaseRavage 
{

	// list d'objets attachés (callback)
	private static List<IEventCallBack> listCallBack;
	
	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		// instance du listcallback
		listCallBack = new ArrayList<IEventCallBack>();

	}

	@Override
	public void update(Time deltaTime) 
	{
		// TODO Auto-generated method stub

	}
	
	public void catchEvent(Event event)
	{
		if(event.type == Event.Type.KEY_PRESSED)
			this.callOnKeyBoard(event);
		if(event.type == Event.Type.MOUSE_BUTTON_PRESSED)
			this.callOnMousePressed(event);
		if(event.type == Event.Type.MOUSE_BUTTON_RELEASED)
			this.callOnMouseReleased(event);
		if(event.type == Event.Type.MOUSE_MOVED)
			this.callOnMouseMoved(event);
			
		
	}
	
	public void callOnKeyBoard(Event  event)
	{
		for(IEventCallBack i : listCallBack)
			i.onKeyboard(event.asKeyEvent());
	}
	
	public void callOnMousePressed(Event event)
	{
		for(IEventCallBack i : listCallBack)
			i.onMousePressed(event.asMouseButtonEvent());
	}	
	
	public void callOnMouseReleased(Event event)
	{
		for(IEventCallBack i : listCallBack)
			i.onMouseReleased(event.asMouseButtonEvent());
	}
	
	public void callOnMouseMoved(Event event)
	{
		for(IEventCallBack i : listCallBack)
			i.onMouseMove(event.asMouseEvent());
	}

	@Override
	public void destroy() 
	{
		// TODO Auto-generated method stub

	}
	
	public static void addCallBack(IEventCallBack i)
	{
		listCallBack.add(i);
	}
	
	

}
