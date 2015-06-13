package coreGUIInterface;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Time;
import org.jsfml.window.event.Event;

import coreDrawable.DrawableUnityManager;
import ravage.IBaseRavage;

public class GuiManager implements Drawable, IBaseRavage
{

	// container des Gui Principaux
	private static List<Gui> containerGuiMain;
	
	@Override
	public void init() 
	{
		// instance du containerGuiMain
		containerGuiMain = new ArrayList<Gui>();
		
	}
	
	public static void addGui(Gui gui)
	{
		if(containerGuiMain != null)
		{
			containerGuiMain.add(gui);
		}
	}
	
	public static void removeGui(Gui gui)
	{
		if(containerGuiMain != null)
		{
			containerGuiMain.remove(gui);
		}
	}
	
	public boolean catchEvent(Event event)
	{
		if(event.type == Event.Type.MOUSE_BUTTON_PRESSED)
		{
			for(Gui gui : containerGuiMain)
			{
				if(gui.onMousePressed(event) == true)
					return true;
			}
		}
		
		if(event.type == Event.Type.MOUSE_BUTTON_RELEASED)
		{
			for(Gui gui : containerGuiMain)
			{
				if(gui.onMouseReleased(event) == true)
					return false;
			}
		}
		
		return false;
	}

	@Override
	public void update(Time deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		// affichage des gui principaux
		for(Gui gui : containerGuiMain)
		{
			gui.draw(arg0, arg1);
		}

	}

}
