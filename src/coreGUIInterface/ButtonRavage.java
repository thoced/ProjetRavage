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
		// création de l'epaisseur du bouton
		if(this.shape != null)
			this.shape.setOutlineThickness(1f);
		// TODO Auto-generated constructor stub
	}
	
	public ButtonRavage(String title,float x,float y, float width,float height)
	{
		super(title,x,y,width,height);
		// création de l'epaisseur du bouton
		if(this.shape != null)
			this.shape.setOutlineThickness(1f);
		
	}

	@Override
	public boolean onMousePressed(Event event)
	{
		if(this.shape != null)
		{
			Vector2f posMouse = new Vector2f(event.asMouseEvent().position.x,event.asMouseEvent().position.y);
			if(this.shape.getGlobalBounds().contains(posMouse))
			{
			
				this.shape.setOutlineColor(new Color(108,16,16));
				this.setPosition(this.getX() + 1, this.getY() + 1);
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
				
				this.setPosition(this.getX() - 1, this.getY() - 1);
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
