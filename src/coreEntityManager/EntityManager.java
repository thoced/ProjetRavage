package coreEntityManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;

import coreEntity.Unity;
import coreGUI.IRegionSelectedCallBack;
import coreNet.INetManagerCallBack;
import coreNet.NetAddUnity;
import coreNet.NetHeader;
import coreNet.NetHeader.TYPE;
import coreNet.NetHello;
import coreNet.NetManager;
import corePhysic.PhysicWorldManager;
import ravage.FrameWork;
import ravage.IBaseRavage;
import ravage.IEvent;

public class EntityManager implements IBaseRavage,IEvent,IRegionSelectedCallBack,INetManagerCallBack
{
	
	// vecteur des unity du player
	private static List<Unity> vectorUnity;
	// vecteur des unity du joueur adverse (rÈseau)
	private static List<Unity> vectorUnityNet;
	// test clock
	private Clock clock;
	private Time delta;
	// listdes unit√©s selection√©s
	private List<Unity> listUnitySelected;

	@Override
	public void init()
	{
		// TODO Auto-generated method stub
		vectorUnity = new ArrayList<Unity>();
		// liste des unit√©s selectionn√©s
		listUnitySelected = new ArrayList<Unity>();
		// instance vectorunitynet
		vectorUnityNet = new ArrayList<Unity>();
		
		clock = new Clock();
		delta = Time.ZERO;
		
		// on s'accroche au NetManager
		NetManager.attachCallBack(this);
	}

	@Override
	public void update(Time deltaTime) 
	{
		// on parse les unit√©
		for(Unity unity : vectorUnity)
		{
			unity.update(deltaTime);
		}
		// on parse les unitÈs adverses (rÈseau)
		for(Unity unity :vectorUnityNet)
		{
			unity.update(deltaTime);
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the vectorUnity
	 */
	public static List<Unity> getVectorUnity() {
		return vectorUnity;
	}

	/**
	 * @param vectorUnity the vectorUnity to set
	 */
	public static void setVectorUnity(List<Unity> vectorUnity) {
		EntityManager.vectorUnity = vectorUnity;
	}
	
	

	public static List<Unity> getVectorUnityNet() {
		return vectorUnityNet;
	}

	public static void setVectorUnityNet(List<Unity> vectorUnityNet) {
		EntityManager.vectorUnityNet = vectorUnityNet;
	}

	@Override
	public void onMouse(MouseEvent mouseEvent) 
	{
		
		Vector2f posMouseWorld = FrameWork.getWindow().mapPixelToCoords(mouseEvent.position);
		float pixels =  PhysicWorldManager.getRatioPixelMeter();
		
		// si c'est un click gauche
		
		if(mouseEvent.asMouseButtonEvent().button == Mouse.Button.LEFT)
		{
					
			// on vide la liste des objets selectionn√©
			this.listUnitySelected.clear();
			
			Vec2 mousePos = new Vec2(posMouseWorld.x / pixels,posMouseWorld.y / pixels ); 
			for(Unity unity : vectorUnity)
			{
				// si la souris est sur l'unit√©
				if(unity.getBody().getPosition().sub(mousePos).length() < .5f)
				{
					unity.setSelected(true);
					this.listUnitySelected.add(unity);
					break;
				}
				
			}
			
			
		}
		else if(mouseEvent.asMouseButtonEvent().button == Mouse.Button.RIGHT)
		{
			
			Vector2f pos = Vector2f.div(posMouseWorld, pixels);
			
			for(Unity u : this.listUnitySelected)
			{
				// on pr√©cise la node target
				u.setTargetPosition((int)pos.x+1,(int)pos.y+1);
			}
		}
		
		
	}

	@Override
	public void onKeyboard(KeyEvent keyboardEvent) 
	{
		if(keyboardEvent.key == Keyboard.Key.A )
		{
			// on ajoute une unitÈ
			Unity unity = new Unity();
			unity.init();
			unity.setPosition(NetManager.getPosxStartFlag(),NetManager.getPosyStartFlag());
			EntityManager.getVectorUnity().add(unity);
			// on envoie sur le rÈseau
			NetHeader header = new NetHeader();
			header.setTypeMessage(TYPE.ADD);
			NetAddUnity add = new NetAddUnity();
			add.setPosx(unity.getPositionMeterX());
			add.setPosy(unity.getPositionMeterY());
			add.setTypeUnity(0);
			header.setMessage(add);
			try 
			{
				NetManager.SendMessage(header);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void onMouseMove(MouseEvent event) {
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

	@Override
	public void onRegionSelected(FloatRect region) 
	{
		
		// on vient de receptionn√© la region selectionn√©
		for(Unity unity : vectorUnity)
		{
			if(region.contains(new Vector2f(unity.getBody().getPosition().x * PhysicWorldManager.getRatioPixelMeter(),
					unity.getBody().getPosition().y * PhysicWorldManager.getRatioPixelMeter())))
					{
						unity.setSelected(true);
						this.listUnitySelected.add(unity);
					}
		}
		
	}

	@Override
	public void onHello(NetHello hello) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAddUnity(NetAddUnity unity)
	{
		// crÈation d'une unitÈ venant du rÈseau
		Unity u= new Unity();
		u.init();
		u.setPosition((int)unity.getPosx(), (int)unity.getPosy());;
		// ajout dans le vecteur unity rÈseau
		vectorUnityNet.add(u);
		
		
	}


	

}
