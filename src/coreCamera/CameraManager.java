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

import ravage.IBaseRavage;

public class CameraManager implements IBaseRavage 
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
		
		if(Mouse.getPosition().x > this.view.getSize().x - 64f)
			 this.center = Vector2f.add(this.center, Vector2f.mul(new Vector2f(1f,0f), mul));
		if(Mouse.getPosition().x < 64f)
			 this.center = Vector2f.sub(this.center, Vector2f.mul(new Vector2f(1f,0f), mul));
		if(Mouse.getPosition().y > this.view.getSize().y - 64f)
			this.center = Vector2f.add(this.center, Vector2f.mul(new Vector2f(0f,1f), mul));
		if(Mouse.getPosition().y < 64f)
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
	
	

}
