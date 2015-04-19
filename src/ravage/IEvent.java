package ravage;

import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

public interface IEvent
{
	public void onMouse(Vector2f pos,int click);
	public void onKeyboard(Keyboard.Key k);
}
