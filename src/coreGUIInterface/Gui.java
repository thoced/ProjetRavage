package coreGUIInterface;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;

import coreEvent.EventManager;
import coreEvent.IEventCallBack;

public class Gui implements Drawable {

	// title
	protected String title;
	// position x et y
	protected float x,y;
	// taille
	protected float width,height;
	// Texture et Sprite Background
	protected Texture textureBackground;
	// Rectangle shape pour la forme du gui
	protected RectangleShape shape;
	// Color Shape
	protected Color colorBackground = Color.WHITE;
	// epaisseur
	protected float tickness = 0f;
	// Color tickness
	protected Color colorTickness = Color.WHITE;
	
	public void Init()
	{
		
			// création du shape
			shape = new RectangleShape();
			shape.setSize(new Vector2f(width,height));
			shape.setPosition(new Vector2f(x,y));
			
			if(this.textureBackground != null)
			{
				/*this.spriteBackground = new Sprite(this.textureBackground);
				// placement du sprite
				this.spriteBackground.setPosition(new Vector2f(this.x,this.y));
				// création du bounds
				this.spriteBackground.setOrigin(new Vector2f(0f,0f));
				// TextureRect
				this.spriteBackground.setTextureRect(new IntRect(0,0,this.textureBackground.getSize().x,this.textureBackground.getSize().y));
				// taille
				this.spriteBackground.setScale(width/this.textureBackground.getSize().x,height/this.textureBackground.getSize().y);*/
				
				
				shape.setTexture(textureBackground);
				shape.setTextureRect(new IntRect(0,0,this.textureBackground.getSize().x,this.textureBackground.getSize().y));
			}
			shape.setFillColor(colorBackground);
			shape.setOutlineColor(colorTickness);
			shape.setOutlineThickness(tickness);
		
	}

	
	
	public float getTickness() {
		return tickness;
	}



	public void setTickness(float tickness) {
		this.tickness = tickness;
	}



	public Color getColorTickness() {
		return colorTickness;
	}



	public void setColorBackground(Color colorBackground) {
		this.colorBackground = colorBackground;
	}



	public void setColorTickness(Color colorTickness) {
		this.colorTickness = colorTickness;
	}



	public Color getColorBackground() {
		return colorBackground;
	}



	public Texture getTextureBackground() {
		return textureBackground;
	}

	public void setTextureBackground(Texture textureBackground) 
	{
		this.textureBackground = textureBackground;
		// création du spritebackground
		
	}
	

	
	public void setPosition(float x,float y)
	{
		this.x = x;
		this.y = y;
		// update de la position graphique
		if(this.shape != null)
		{
			this.shape.setPosition(new Vector2f(this.x,this.y));
		}
		
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
			
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Gui(String title)
	{
		this.title = title;
		
		
	}
	
	public Gui(String title,float x,float y,float width,float height)
	{
		this.title = title;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	
		
	}
	
	public boolean onMousePressed(Event event)
	{
		return false;
	}
	
	public boolean onMouseReleased(Event event)
	{
		return false;
	}
	
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		// TODO Auto-generated method stub
		
	}
	
	

}
