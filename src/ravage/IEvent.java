package ravage;

import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;

public interface IEvent
{
	public void onMouse(MouseEvent buttonEvent);
	public void onKeyboard(KeyEvent keyboardEvent);
	
	public void onMouseMove(MouseEvent event);
	public void onMousePressed(MouseButtonEvent event);
	public void onMouseReleased(MouseButtonEvent event);
}
