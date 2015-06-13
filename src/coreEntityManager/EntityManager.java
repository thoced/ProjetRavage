package coreEntityManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
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

import coreAI.Node;
import coreEntity.Knight;
import coreEntity.Unity;
import coreEntity.UnityNet;
import coreEvent.IEventCallBack;
import coreGUI.IRegionSelectedCallBack;
import coreGUIInterface.GuiManager;
import coreGUIInterface.panelFormation;
import coreLevel.LevelManager;
import coreNet.INetManagerCallBack;
import coreNet.NetAddUnity;
import coreNet.NetHeader;
import coreNet.NetHeader.TYPE;
import coreNet.NetHello;
import coreNet.NetManager;
import coreNet.NetMoveUnity;
import coreNet.NetSynchronize;
import corePhysic.PhysicWorldManager;
import ravage.FrameWork;
import ravage.IBaseRavage;


public class EntityManager implements IBaseRavage,IEventCallBack,IRegionSelectedCallBack,INetManagerCallBack
{
	
	// compteur d'id 
	private static int cptIdUnity = -1;
	// vecteur des unity du player
	private static List<Unity> vectorUnity;
	// vecteur des unity du joueur adverse (r�seau)
	//private static List<UnityNet> vectorUnityNet;
	
	private static Hashtable<Integer,UnityNet> vectorUnityNet;
	
	// test clock
	private Clock clock;
	private Time delta;
	// listdes unités selectionés
	private List<Unity> listUnitySelected;
	
	// instance du ChooseAngleFormationDrawable
	private ChooseAngleFormationDrawable arrow;
	// variable de vecteur de direction de formation
	private Vec2 dirFormation;
	// variable sur les coordonn�es de souris
	private Vector2f posMouseWorld;

	@Override
	public void init()
	{
		// TODO Auto-generated method stub
		vectorUnity = new ArrayList<Unity>();
		// liste des unités selectionnés
		listUnitySelected = new ArrayList<Unity>();
		// instance vectorunitynet
		//vectorUnityNet = new ArrayList<UnityNet>();
		vectorUnityNet = new Hashtable<Integer,UnityNet>();
		
		clock = new Clock();
		delta = Time.ZERO;
		
		// on s'accroche au NetManager
		NetManager.attachCallBack(this);
	}

	@Override
	public void update(Time deltaTime) 
	{
		// on parse les unité
		for(Unity unity : vectorUnity)
		{
			unity.update(deltaTime);
			// ajout test dans le netUnityDatagra
			
		}
		// on parse les unit�s adverses (r�seau)
		for(Unity unity :vectorUnityNet.values())
		{
			unity.update(deltaTime);
		}
		
		if(arrow!=null)
			arrow.update(deltaTime);
		
		
		// code test - envoie de la liste des unit�
		
	
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
	
	

	/*public static List<UnityNet> getVectorUnityNet() {
		return vectorUnityNet;
	}

	public static void setVectorUnityNet(List<UnityNet> vectorUnityNet) {
		EntityManager.vectorUnityNet = vectorUnityNet;
	}*/

	public static Hashtable<Integer, UnityNet> getVectorUnityNet() {
		return vectorUnityNet;
	}

	public static void setVectorUnityNet(Hashtable<Integer, UnityNet> vectorUnityNet) {
		EntityManager.vectorUnityNet = vectorUnityNet;
	}

	@Override
	public void onMousePressed(MouseButtonEvent mouseEvent) 
	{
		
		posMouseWorld = FrameWork.getWindow().mapPixelToCoords(mouseEvent.position);
		float pixels =  PhysicWorldManager.getRatioPixelMeter();
		
		// si c'est un click gauche
		
		if(mouseEvent.asMouseButtonEvent().button == Mouse.Button.LEFT)
		{
					
			// on vide la liste des objets selectionné
			this.listUnitySelected.clear();
			
			Vec2 mousePos = new Vec2(posMouseWorld.x / pixels,posMouseWorld.y / pixels ); 
			for(Unity unity : vectorUnity)
			{
				// si la souris est sur l'unité
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
			
			// cr�ation de la fleche de selection d'angle de formation
		    if(arrow == null)
		    	arrow  = new ChooseAngleFormationDrawable(posMouseWorld,posMouseWorld);
		
		}
		
		
	}

	@Override
	public void onKeyboard(KeyEvent keyboardEvent) 
	{
		if(keyboardEvent.key == Keyboard.Key.A )
		{
			// on ajoute une unit�
			Knight knight = new Knight();
			knight.init();
			knight.setPosition(NetManager.getPosxStartFlag(),NetManager.getPosyStartFlag());
			// r�ception de l'id unique pour l'unit�
			knight.setId(EntityManager.getNewIdUnity());
			EntityManager.getVectorUnity().add(knight);
			// on envoie sur le r�seau
			NetHeader header = new NetHeader();
			header.setTypeMessage(TYPE.ADD);
			NetAddUnity add = new NetAddUnity();
			add.setPosx(knight.getPositionMeterX());
			add.setPosy(knight.getPositionMeterY());
			add.setTypeUnity(knight.getIdType());
			add.setIdUnity(knight.getId());
			header.setMessage(add);
			try 
			{
				NetManager.PackMessage(header);
				
				//NetManager.SendMessage(header);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void onMouseMove(MouseEvent event) 
	{
		
	}
	
	
	@Override
	public void onMouseReleased(MouseButtonEvent event) 
	{
		// on relache le clic, on r�cup�re la dirction de formation et on lance la formation
		if(arrow != null)
		{
			// on r�cup�re le vecteur de direction pour la formation
			dirFormation = arrow.getVectorDirectionFormation();
			// on calcul la formation
			Vector2f pos = Vector2f.div(posMouseWorld, PhysicWorldManager.getRatioPixelMeter());
		    computeFormation(listUnitySelected,posMouseWorld.x,posMouseWorld.y,dirFormation);
		    
		   
		    // suppression de la fleche dans les callback
			arrow.destroy();
			arrow = null;
		}

	}

	@Override
	public void onRegionSelected(FloatRect region) 
	{
		
		// on vient de receptionné la region selectionné
		for(Unity unity : vectorUnity)
		{
			if(region.contains(new Vector2f(unity.getBody().getPosition().x * PhysicWorldManager.getRatioPixelMeter(),
					unity.getBody().getPosition().y * PhysicWorldManager.getRatioPixelMeter())))
					{
						unity.setSelected(true);
						this.listUnitySelected.add(unity);
						
						// on sort de la s�lection si on d�passe 32
						if(this.listUnitySelected.size() > 31)
							break;
						
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
		// cr�ation d'une unit� venant du r�seau
		UnityNet u= new UnityNet();
		u.init();
		u.setPosition(unity.getPosx(), unity.getPosy());;
		u.setId(unity.getIdUnity());
		// ajout dans le vecteur unity r�seau
		//vectorUnityNet.add(u);
		vectorUnityNet.put(u.getId(), u);
		
	}
	
	@Override
	public void onMoveUnity(NetMoveUnity unity)
	{
		// r�ception du unity move r�seau	
		// on r�cup�re le bon unity
				
		UnityNet un = vectorUnityNet.get(unity.getId());
		if(un != null)
		{
			// l'unit� est trouv�e
			Node n = new Node(unity.getNextPosx(),unity.getNextPosy(),true);
			un.setPosXYMeter(unity.getPosx(),unity.getPosy());
			un.setNext(n);
					
		}
		else
		{
			// l'unit� n'est pas trouv� suite � un probleme r�seau, il faut donc l'ajouter
			// cr�ation d'une unit� venant du r�seau
			UnityNet u= new UnityNet();
			u.init();
			u.setPosition(unity.getPosx(), unity.getPosy());;
			u.setId(unity.getId());
			// ajout dans le vecteur unity r�seau
			//vectorUnityNet.add(u);
			vectorUnityNet.put(u.getId(), u);
		}
		
	
	}

	public static int getNewIdUnity()
	{
		// g�n�ration d'un id unique pour les unit�s
		EntityManager.cptIdUnity++;
		return EntityManager.cptIdUnity;
	}

	@Override
	public void onSynchronize(NetSynchronize sync) 
	{
		UnityNet un = vectorUnityNet.get(sync.getIdUnity());
		if(un != null)
		{
			
			un.setPosXYMeter(sync.getPosx(),sync.getPosy());
			un.getBody().setTransform(un.getBody().getPosition(),sync.getRotation());
					
		}
	}

	@Override
	public void onMouse(MouseEvent buttonEvent) {
		// TODO Auto-generated method stub
		
	}

	public void computeFormation(List<Unity> listUnity,float px,float py,Vec2 dir)
	{
		
		// variable d�finissant le nombre maximal d'unit� sur une ligne
		int nbUnityPerLine = 0;
		int cptLine = 0;
		// on d�fini les positions
		float dx = px;
		
		Vector2f posInitial = new Vector2f(dx,py);
		Vector2f dep = posInitial;
	
		Vec2 skew = dir.skew();
		
		for(int i=0;i<listUnity.size();i++)
		{
		
			// on r�cup�re une unit�
			Unity u = listUnity.get(i);
			// on calcul la position ecran
			Vector2f pos = Vector2f.div(new Vector2f(dep.x,dep.y), PhysicWorldManager.getRatioPixelMeter());
			// on envoie l'unit� sur sa positoin
			if(u.setTargetPosition(dep.x,dep.y,(int)pos.x+1,(int)pos.y+1,dir) == false)
			{
				// aucune position possible pour l'unit�, on la place derri�re
				
				//dep = posInitial;
				cptLine++;
				dep = Vector2f.add(posInitial, Vector2f.mul(Vector2f.neg(new Vector2f(dir.x * 20  ,dir.y * 20)),cptLine));
				i--;
				nbUnityPerLine = 0;
				
			}
			else
			{
				// on a d�fini la position final de l'unit�, on incr�mente le nb d'unit� par ligne
				nbUnityPerLine++;
			}
			//dx = dx + 20;
			dep = Vector2f.add(dep, new Vector2f(skew.x * 20,skew.y * 20));
			
			if(nbUnityPerLine > 7)
			{
				//si un ligne est compl�te, on place en dessous
				nbUnityPerLine = 0;
				cptLine++;
				
				dep = Vector2f.add(posInitial, Vector2f.mul(Vector2f.neg(new Vector2f(dir.x * 20  ,dir.y * 20)),cptLine));
				//dep = Vector2f.add(dep, Vector2f.neg(new Vector2f(dir.x * 20,dir.y * 20)));

			}
			
			
			if(cptLine > 12)
				break;
			
		}
		
		/*
		for(int i=0;i<listUnity.size();i++)
		{
		
			// on r�cup�re une unit�
			Unity u = listUnity.get(i);
			// on calcul la position ecran
			Vector2f pos = Vector2f.div(new Vector2f(dx,py), PhysicWorldManager.getRatioPixelMeter());
			// on envoie l'unit� sur sa positoin
			if(u.setTargetPosition(dx,py,(int)pos.x+1,(int)pos.y+1) == false)
			{
				// aucune position possible pour l'unit�, on la place derri�re
				dx = px;
				py = py + 20;	
				i--;
				nbUnityPerLine = 0;
				
			}
			else
			{
				// on a d�fini la position final de l'unit�, on incr�mente le nb d'unit� par ligne
				nbUnityPerLine++;
			}
			dx = dx + 20;
			//dep = Vector2f.add(dep, new Vector2f(skew.x * 20,skew.y * 20));
			dx = dep.x;
			py = dep.y;
			
			if(nbUnityPerLine > 7)
			{
				//si un ligne est compl�te, on place en dessous
				nbUnityPerLine = 0;
				dx = px;
				py = py + 20;
				
			
				
				
			}
			
		}*/
		

	}

}
