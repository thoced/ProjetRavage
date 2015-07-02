package coreCamera;

import org.jsfml.graphics.ConstView;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.View;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;

import coreEvent.IEventCallBack;
import ravage.FrameWork;
import ravage.IBaseRavage;

public class CameraManager implements IBaseRavage, IEventCallBack
{

	// View
	private static View view;
	// rotation
	private float rot = 0f;
	// speed
	private  float speed = 512f;
	// Center
	private Vector2f center;
	
	public CameraManager(ConstView v)
	{
		// initialisation de la view
		this.setView((View)v);
	}
	
	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		this.view.setCenter(new Vector2f(1024f,1024f));
		
		
	}

	@Override
	public void update(Time deltaTime) 
	{
		
		// center
		this.center = this.view.getCenter();
		// création de la valeur de multiplication
		float mul = this.speed * deltaTime.asSeconds();
			
		if(Keyboard.isKeyPressed(Key.RIGHT))
			 this.center = Vector2f.add(this.center, Vector2f.mul(new Vector2f(1f,0f), mul));
		if(Keyboard.isKeyPressed(Key.LEFT))
			 this.center = Vector2f.sub(this.center, Vector2f.mul(new Vector2f(1f,0f), mul));
		if(Keyboard.isKeyPressed(Key.DOWN))
			 this.center = Vector2f.add(this.center, Vector2f.mul(new Vector2f(0f,1f), mul));
		if(Keyboard.isKeyPressed(Key.UP))
			 this.center = Vector2f.sub(this.center, Vector2f.mul(new Vector2f(0f,1f), mul));
		
		if(Keyboard.isKeyPressed(Key.SUBTRACT))
		{
			Vector2f size = this.view.getSize();
			size = Vector2f.mul(size, 1.1f);
			this.view.setSize(size);
		}
		
		if(Keyboard.isKeyPressed(Key.ADD))
		{
			Vector2f size = this.view.getSize();
			size = Vector2f.mul(size, 0.9f);
			this.view.setSize(size);
		}
		
		if(Mouse.getPosition(FrameWork.getWindow()).x > FrameWork.getWindow().getSize().x - 64f)
			 this.center = Vector2f.add(this.center, Vector2f.mul(new Vector2f(1f,0f), mul));
		if(Mouse.getPosition(FrameWork.getWindow()).x < 64f)
			 this.center = Vector2f.sub(this.center, Vector2f.mul(new Vector2f(1f,0f), mul));
		if(Mouse.getPosition(FrameWork.getWindow()).y > FrameWork.getWindow().getSize().y - 64f)
			this.center = Vector2f.add(this.center, Vector2f.mul(new Vector2f(0f,1f), mul));
		if(Mouse.getPosition(FrameWork.getWindow()).y < 64f)
			 this.center = Vector2f.sub(this.center, Vector2f.mul(new Vector2f(0f,1f), mul));
		
		// compute du View
		this.view.setCenter(this.center);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the view
	 */
	public View getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView( View view) {
		this.view = view;
	}
	
	public static FloatRect getCameraBounds()
	{
		Vector2f  size = CameraManager.view.getSize();
		Vector2f centre = CameraManager.view.getCenter();
		Vector2f source = Vector2f.sub(centre, Vector2f.div(size,2));
		return  new FloatRect(source,size);

	}

	

	@Override
	public void onKeyboard(KeyEvent keyboardEvent) 
	{
		// TODO Auto-generated method stub
		if(keyboardEvent.key == Keyboard.Key.C )
		{
			this.center = new Vector2f(640,640);
			// compute du View
			this.view.setCenter(this.center);
		}
		
	}

	@Override
	public void onMouseMove(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouse(MouseEvent buttonEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMousePressed(MouseButtonEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseReleased(MouseButtonEvent event) {
		// TODO Auto-generated method stub
		
	}


	

	
	

}
