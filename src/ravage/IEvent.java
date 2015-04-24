package ravage;

import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;

public interface IEvent
{
	public void onMouse(MouseButtonEvent buttonEvent);
	public void onKeyboard(KeyEvent keyboardEvent);
}
