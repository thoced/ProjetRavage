package coreDrawable;

import java.lang.reflect.Field;
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
import coreEntity.Knight;
import coreEntity.Unity;
import coreEntityManager.EntityManager;
import coreEntityManager.EntityManager.CAMP;
import ravage.IBaseRavage;

public class DrawableUnityManager implements IBaseRavage, Drawable
{

	public static enum LAYERS {BACK,MIDDLE,FRONT};

	// listCallBack remove
	private static List<Drawable> listCallBackRemove;
	
	// listCallback des drawables layers back
	private static List<Drawable> listCallBackDrawableBACK;
	// listCallback des drawables layers middle
	private static List<Drawable> listCallBackDrawableMIDDLE;
	// listCallback des drawables layers middle
	private static List<Drawable> listCallBackDrawableFRONT;
	
	private VertexArray buffer;
	
	private RenderStates state = new RenderStates(TexturesManager.GetTextureByName("unity_sprite_01.png"));
	
	
	// test utilisation de sprite pour l'affichage des unités
	private Sprite sprite_unite;
	
	// KNIGHTS
	private Sprite sprite_knight_YELLOW;
	private Sprite sprite_knight_BLUE;
	
	// SPIRTE CURRENT
	private Sprite SpriteCurrent;
	private Sprite SpriteCurrentNet;
	

	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		// crÃ©ation du VertexBuffer
	 buffer = new VertexArray(PrimitiveType.QUADS);
	 
	 // instance du listCallBackDrawable
	 listCallBackDrawableBACK = new ArrayList<Drawable>();
	 listCallBackDrawableMIDDLE = new ArrayList<Drawable>();
	 listCallBackDrawableFRONT = new ArrayList<Drawable>();
	 listCallBackRemove = new ArrayList<Drawable>();
	 
	 // instance de sprite_unite
	 sprite_knight_YELLOW = new Sprite(TexturesManager.GetTextureByName("ANIM_KNIGHT_YELLOW.png"));
	 sprite_knight_YELLOW.setOrigin(new Vector2f(16f,16f));
	 sprite_knight_BLUE = new Sprite(TexturesManager.GetTextureByName("ANIM_KNIGHT_BLUE.png"));
	 sprite_knight_BLUE.setOrigin(new Vector2f(16f,16f));
	 
	 
	 
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
		// Vectoru direction des unités
		//Vec2 dir = new Vec2(1,0);
		// pour chaque unity 
		
		// on appel le BACK draw
		this.CallBackDrawableBACK(arg0, arg1);
		
		for(Unity unity : EntityManager.getVectorUnity().values() )
		{
			// on rÃ©cupÃ¨re la position
			/*Vector2f pos = new Vector2f(unity.getPosx(),unity.getPosy());
			// on crÃ©e les 4 vertex
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
			
		
			if(unity.getMyCamp() == CAMP.YELLOW)
			{
			
				switch(unity.getClass().getSimpleName())
				{
					case "Knight" :	sprite_knight_YELLOW.setPosition(new Vector2f(unity.getPosx(),unity.getPosy()));		
									// on spécifie la roation
									sprite_knight_YELLOW.setRotation((float)((unity.getBody().getAngle() * 180f) / Math.PI) % 360f);
									// on spécifie l'anim a jouer
									sprite_knight_YELLOW.setTextureRect(unity.getCurrentAnim());
									// on affiche
									arg0.draw(sprite_knight_YELLOW);
					break;
				}
			
			}
			else if(unity.getMyCamp() == CAMP.BLUE)
			{
			
				switch(unity.getClass().getSimpleName())
				{
					case "Knight" :	sprite_knight_BLUE.setPosition(new Vector2f(unity.getPosx(),unity.getPosy()));		
									// on spécifie la roation
									sprite_knight_BLUE.setRotation((float)((unity.getBody().getAngle() * 180f) / Math.PI) % 360f);
									// on spécifie l'anim a jouer
									sprite_knight_BLUE.setTextureRect(unity.getCurrentAnim());
									// on affiche
									arg0.draw(sprite_knight_BLUE);
					break;
				}
				
			}
				
			
			
			
		}
		
		// pour chaque unity réseau
		for(Unity unity : EntityManager.getVectorUnityNet().values() )
		{
		
			
			//arg0.draw(sprite_knight);
			
			if(unity.getMyCamp() == CAMP.YELLOW)
			{
				
				switch(unity.getClass().getSimpleName())
				{
					case "KnightNet" :	sprite_knight_YELLOW.setPosition(new Vector2f(unity.getPosx(),unity.getPosy()));		
									// on spécifie la roation
										sprite_knight_YELLOW.setRotation((float)((unity.getBody().getAngle() * 180f) / Math.PI) % 360f);
									// on spécifie l'anim a jouer
										sprite_knight_YELLOW.setTextureRect(unity.getCurrentAnim());
									// on affiche
										arg0.draw(sprite_knight_YELLOW);
					break;
				}
			
			}
			else if(unity.getMyCamp() == CAMP.BLUE)
			{
				switch(unity.getClass().getSimpleName())
				{
						case "KnightNet" :	sprite_knight_BLUE.setPosition(new Vector2f(unity.getPosx(),unity.getPosy()));		
									// on spécifie la roation
										sprite_knight_BLUE.setRotation((float)((unity.getBody().getAngle() * 180f) / Math.PI) % 360f);
									// on spécifie l'anim a jouer
										sprite_knight_BLUE.setTextureRect(unity.getCurrentAnim());
					// on affiche
										arg0.draw(sprite_knight_BLUE);
										break;
				}
			}
			
		}
		
		
		// affichage
		//arg0.draw(buffer,state);
		
		// on appel le middle
		this.CallBackDrawableMIDDLE(arg0, arg1);
		
		// on appel le front
		this.CallBackDrawableFRONT(arg0, arg1);	
		
		// on supprimer le list remove
		this.listCallBackRemove.clear();
	
		
		
		
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
	public static void AddDrawable(Drawable d,LAYERS layer )
	{
		switch(layer)
		{
		case BACK: listCallBackDrawableBACK.add(d);break;
			
		case MIDDLE: listCallBackDrawableMIDDLE.add(d);break;
		
		case FRONT: listCallBackDrawableFRONT.add(d);break;
			
		}
		
		
	}
	
	public static void RemoveDrawable(Drawable d)
	{
		listCallBackRemove.add(d);
	}
	

	
	private void CallBackDrawableBACK(RenderTarget render, RenderStates states)
	{
	
		for(Drawable d : listCallBackDrawableBACK)
			d.draw(render, states);
		
		// suppresion des élements dans la liste remove
		listCallBackDrawableBACK.removeAll(listCallBackRemove);
		
	}
	
	private void CallBackDrawableMIDDLE(RenderTarget render, RenderStates states)
	{
	
		for(Drawable d : listCallBackDrawableMIDDLE)
			d.draw(render, states);
		
		// suppresion des élements dans la liste remove
		listCallBackDrawableMIDDLE.removeAll(listCallBackRemove);
		
		
	}
	private void CallBackDrawableFRONT(RenderTarget render, RenderStates states)
	{
	
		for(Drawable d : listCallBackDrawableFRONT)
			d.draw(render, states);
		
		// suppresion des élements dans la liste remove
		listCallBackDrawableFRONT.removeAll(listCallBackRemove);

	}

	
}
