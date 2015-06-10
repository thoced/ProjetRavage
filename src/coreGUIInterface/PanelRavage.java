package coreGUIInterface;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;


public class PanelRavage extends Gui
{
	// container de Gui
	protected List<Gui> containerGui;
	// isPressed
	protected boolean isPressed = false;
	
	public PanelRavage(String title)
	{
		super(title);
		// instance du container de Gui
		containerGui = new ArrayList<Gui>();
	}
	
	public PanelRavage(String title,float x,float y, float width,float height)
	{
		super(title,x,y,width,height);
		// instance du container de Gui
		containerGui = new ArrayList<Gui>();
	}

	public void addGui(Gui gui)
	{
		if(containerGui != null)
		{
			gui.shape.setPosition(Vector2f.add(gui.shape.getPosition(), this.shape.getPosition()));
			containerGui.add(gui);
		}
	}
	
	public void removeGui(Gui gui)
	{
		if(containerGui != null)
		{
			containerGui.remove(gui);
		}
	}
	
	
	
	@Override
	public boolean onMousePressed(Event event)
	{
		if(this.shape != null )
		{
			Vector2f posMouse = new Vector2f(event.asMouseEvent().position.x,event.asMouseEvent().position.y);
			if(this.shape.getGlobalBounds().contains(posMouse))
			{
				// un evenement de click est sur le panel, on passe l'evenement au gui enfants
				for(Gui gui : containerGui)
				{
					gui.onMousePressed(event);
						
				}
				//Is Pressed
				isPressed = true;
				// on return true car le panel a capté l'event
				return true;
			}
		}
			
		return false;
	}
	
	

	@Override
	public boolean onMouseReleased(Event event) 
	{
		// is released
		boolean isReleased = false;
		
		if(this.shape != null)
		{
			Vector2f posMouse = new Vector2f(event.asMouseEvent().position.x,event.asMouseEvent().position.y);
			if(this.shape.getGlobalBounds().contains(posMouse))
			{
				
				// on positionne le isReleased a true car on a déja released le panel
				isReleased = true;
			}
			
			// boucle dans les gui enfants même si le gui parent n'a pas été released, afin de débloquer un bouton si le clic est resté enfoncé
			
			if(isPressed) //  si au moins le panel avait été pressed 
			{
				for(Gui gui : containerGui)
				{
					gui.onMouseReleased(event);
						
				}
			}
			
			// false sur la variable ispressed
			isPressed = false;
		}
		
		return false;
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		// TODO Auto-generated method stub
		super.draw(arg0, arg1);
		
		// affichage du sprite si il existe
		if(this.shape!=null)
		{
			arg0.draw(shape);
		}
		
		// appel des affichages des gui enfants
		for(Gui gui : containerGui)
		{
			Vector2f relative = new Vector2f(x,y);
			gui.shape.setPosition(Vector2f.add(relative, new Vector2f(gui.getX(),gui.getY())));
			gui.draw(arg0, arg1);
		}
	}
	
	
}
