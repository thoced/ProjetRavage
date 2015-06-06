package coreDrawable;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Rot;
import org.jbox2d.common.Vec2;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import CoreTexturesManager.TexturesManager;
import coreEntity.Unity;
import coreEntityManager.EntityManager;
import ravage.IBaseRavage;

public class DrawableUnityManager implements IBaseRavage, Drawable
{

	// listCallBack des drawable
	private static List<Drawable> listCallBackDrawable;
	// listCallBack remove
	private static List<Drawable> listCallBackRemove;
	
	private VertexArray buffer;
	
	private RenderStates state = new RenderStates(TexturesManager.GetTextureByName("unity_sprite_01.png"));
	
	
	// test utilisation de sprite pour l'affichage des unitÈs
	private Sprite sprite_unite;
	

	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		// cr√©ation du VertexBuffer
	 buffer = new VertexArray(PrimitiveType.QUADS);
	 
	 // instance du listCallBackDrawable
	 listCallBackDrawable = new ArrayList<Drawable>();
	 listCallBackRemove = new ArrayList<Drawable>();
	 
	 // instance de sprite_unite
	 sprite_unite = new Sprite(TexturesManager.GetTextureByName("unity_sprite_01.png"));
	 sprite_unite.setOrigin(new Vector2f(16f,16f));
	}

	@Override
	public void update(Time deltaTime) 
	{
		// appel au callback update
	
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		// affichage des unity
		buffer.clear();
		// Vectoru direction des unitÈs
		Vec2 dir = new Vec2(1,0);
		// pour chaque unity 
		for(Unity unity : EntityManager.getVectorUnity() )
		{
			// on r√©cup√®re la position
			/*Vector2f pos = new Vector2f(unity.getPosx(),unity.getPosy());
			// on cr√©e les 4 vertex
			Vertex v1 = new Vertex(Vector2f.add(pos, new Vector2f(-16f,-16f)), this.getCoordText(1));
			Vertex v2 = new Vertex(Vector2f.add(pos, new Vector2f(16f,-16f)), this.getCoordText(2));
			Vertex v3 = new Vertex(Vector2f.add(pos,new Vector2f(16f,16f)), this.getCoordText(3));
			Vertex v4 = new Vertex(Vector2f.add(pos, new Vector2f(-16f,16f)), this.getCoordText(4));
			
			// on ajoute dans le buffer
			buffer.add(v1);
			buffer.add(v2);
			buffer.add(v3);
			buffer.add(v4);*/
			
			// test affichage sprite
			sprite_unite.setPosition(new Vector2f(unity.getPosx(),unity.getPosy()));
			// rotation
			
			// on spÈcifie la roation
			sprite_unite.setRotation((float)((unity.getBody().getAngle() * 180f) / Math.PI) % 360f);
			
			arg0.draw(sprite_unite);
			
		}
		
		// pour chaque unity rÈseau
		for(Unity unity : EntityManager.getVectorUnityNet().values() )
		{
			// on r√©cup√®re la position
		/*	Vector2f pos = new Vector2f(unity.getPosx(),unity.getPosy());
			// on cr√©e les 4 vertex
			Vertex v1 = new Vertex(Vector2f.add(pos, new Vector2f(-16f,-16f)), this.getCoordText(1));
			Vertex v2 = new Vertex(Vector2f.add(pos, new Vector2f(16f,-16f)), this.getCoordText(2));
			Vertex v3 = new Vertex(Vector2f.add(pos,new Vector2f(16f,16f)), this.getCoordText(3));
			Vertex v4 = new Vertex(Vector2f.add(pos, new Vector2f(-16f,16f)), this.getCoordText(4));
			
			// on ajoute dans le buffer
			buffer.add(v1);
			buffer.add(v2);
			buffer.add(v3);
			buffer.add(v4);*/
			
			
			// test affichage sprite
			sprite_unite.setPosition(new Vector2f(unity.getPosx(),unity.getPosy()));
			// rotation
		// on spÈcifie la roation
			sprite_unite.setRotation((float)((unity.getBody().getAngle() * 180f) / Math.PI) % 360f);
			
			arg0.draw(sprite_unite);
			
		}
		
		
		// affichage
		//arg0.draw(buffer,state);
		
		// appel des drawable call back
		this.CallBackDrawable(arg0,arg1);
	
		
		
		
	}
	
	public Vector2f getCoordText(int ind)
	{
		switch(ind)
		{
		case 1 : return new Vector2f(0,0);
		
		case 2 : return new Vector2f(32,0);
		
		case 3 : return new Vector2f(32,32);
		
		case 4 : return new Vector2f(0,32);
		
		default: return new Vector2f(0,0);
		}
	}
	
	// attachement
	public static void AddDrawable(Drawable d)
	{
		listCallBackDrawable.add(d);
	}
	
	public static void RemoveDrawable(Drawable d)
	{
		listCallBackRemove.add(d);
	}
	

	
	private void CallBackDrawable(RenderTarget render, RenderStates states)
	{
	
		for(Drawable d : listCallBackDrawable)
			d.draw(render, states);
		
		// suppresion des Èlements dans la liste remove
		listCallBackDrawable.removeAll(listCallBackRemove);
		listCallBackRemove.clear();
	}

	
}
