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
	// list d'ibhet attacgés (temporary)
	private static List<IEventCallBack> listCallBackTemporary;
	// list de suppression des objet callback
	private static List<IEventCallBack> listCallBackRemove;
	
	// variable empechant d'appler en boucle lors des clic souris
	private boolean isMousePressed = false;

	
	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		// instance du listcallback
		listCallBack = new ArrayList<IEventCallBack>();
		listCallBackTemporary = new ArrayList<IEventCallBack>();
		listCallBackRemove = new ArrayList<IEventCallBack>();

	}

	@Override
	public void update(Time deltaTime) 
	{
		// TODO Auto-generated method stub
		
		// transferts des objets de la liste temporary à la liste call back
		listCallBack.addAll(listCallBackTemporary);
		listCallBackTemporary.clear();
		
		// suppression des objets 
		listCallBack.removeAll(listCallBackRemove);
		listCallBackRemove.clear();
	}
	
	public void catchEvent(Event event)
	{
		if(event.type == Event.Type.KEY_PRESSED)
			this.callOnKeyBoard(event);
		if(!this.isMousePressed && event.type == Event.Type.MOUSE_BUTTON_PRESSED)
		{
			this.callOnMousePressed(event);
			this.isMousePressed = true;
		}
		if(event.type == Event.Type.MOUSE_BUTTON_RELEASED)
		{
			this.callOnMouseReleased(event);
			this.isMousePressed = false;
		}
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
		// ajout dans la liste call back temporary
		listCallBackTemporary.add(i);
		
		
	}
	
	public static void removeCallBack(IEventCallBack r)
	{
		// ajout dans la liste des remove
		listCallBackRemove.add(r);
	}
	
	

}
