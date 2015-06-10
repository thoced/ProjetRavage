package coreGUIInterface;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

public class ButtonRavage extends Gui
{

	// le bouton est presse
	private boolean isPressed = false;
	
	public ButtonRavage(String title) 
	{
		super(title);
		// TODO Auto-generated constructor stub
	}
	
	public ButtonRavage(String title,float x,float y, float width,float height)
	{
		super(title,x,y,width,height);
		
	}

	@Override
	public boolean onMousePressed(Event event)
	{
		if(this.shape != null)
		{
			Vector2f posMouse = new Vector2f(event.asMouseEvent().position.x,event.asMouseEvent().position.y);
			if(this.shape.getGlobalBounds().contains(posMouse))
			{
				
				this.shape.setFillColor(new Color(16,16,16));
				// le bouton est pressé
				this.isPressed = true;
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean onMouseReleased(Event event)
	{
		// TODO Auto-generated method stub
		if(this.shape != null && this.isPressed)
		{
				this.shape.setFillColor(new Color(128,32,32));
				this.isPressed = false;
				return true;
			
		}
		return false;
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		if(this.shape != null)
		{
			arg0.draw(this.shape);
		}
		
	}

	

	

	
}
