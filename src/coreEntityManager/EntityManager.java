package coreEntityManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.Mouse.Button;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;

import coreAI.Node;
import coreEntity.Knight;
import coreEntity.KnightNet;
import coreEntity.Unity;
import coreEntity.Unity.ANIMATE;
import coreEntity.Unity.TYPEUNITY;
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
import coreNet.NetKill;
import coreNet.NetManager;
import coreNet.NetMoveUnity;
import coreNet.NetStrike;
import coreNet.NetSynchronize;
import corePhysic.PhysicWorldManager;
import ravage.FrameWork;
import ravage.IBaseRavage;


public class EntityManager implements IBaseRavage,IEventCallBack,IRegionSelectedCallBack,INetManagerCallBack
{
	// enum des camps
	public static enum CAMP {BLUE,YELLOW};
	// camp du joueur
	public static CAMP campSelected;
	// compteur d'id 
	private static int cptIdUnity = -1;
	// vecteur des unity du player
	private static Hashtable<Integer,Unity> vectorUnity;
	// vecteur de killed
	private static List<Unity> vectorUnityKilled;
	// vecteur des unity du joueur adverse (réseau)
	//private static List<UnityNet> vectorUnityNet;
	
	private static Hashtable<Integer,UnityNet> vectorUnityNet;
	// vecteur net killed
	private static List<Unity> vectorUnityNetKilled;
	
	// test clock
	private Clock clock;
	private Time delta;
	// listdes unitÃ©s selectionÃ©s
	private List<Unity> listUnitySelected;
	
	// instance du ChooseAngleFormationDrawable
	private ChooseAngleFormationDrawable arrow;
	// variable de vecteur de direction de formation
	private Vec2 dirFormation;
	// variable sur les coordonnées de souris
	private Vector2f posMouseWorld;

	private static int idTestUnity = 0;
	
	
	
	public static CAMP getCampSelected() {
		return campSelected;
	}

	public static void setCampSelected(CAMP campSelected) {
		EntityManager.campSelected = campSelected;
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub
		vectorUnity = new Hashtable<Integer,Unity>();
		vectorUnityKilled = new ArrayList<Unity>();
		// liste des unitÃ©s selectionnÃ©s
		listUnitySelected = new ArrayList<Unity>();
		// instance vectorunitynet
		//vectorUnityNet = new ArrayList<UnityNet>();
		vectorUnityNet = new Hashtable<Integer,UnityNet>();
		vectorUnityNetKilled = new ArrayList<Unity>();
		
		clock = new Clock();
		delta = Time.ZERO;
		
		// on s'accroche au NetManager
		NetManager.attachCallBack(this);
	}

	@Override
	public void update(Time deltaTime) 
	{
		// on parse les unitÃ©
		for(Unity unity : vectorUnity.values())
		{
			unity.update(deltaTime);
			// ajout test dans le netUnityDatagra
			
		}
		// on parse les unités adverses (réseau)
		for(Unity unity :vectorUnityNet.values())
		{
			unity.update(deltaTime);
		}
		
		if(arrow!=null)
			arrow.update(deltaTime);
		
		
		// recherche des enemy dans les zones
		// pour chaque unity on lance la recherche
		
		for(Unity unity : this.vectorUnity.values())
		{
			if(unity.getEnemyAttribute() == null) // si aucun enemy n'est encore attribué.
			{
				// on lance la recherche
				List<UnityNet> listEnemy = this.searchEnemyZone(unity);
				if(listEnemy != null && listEnemy.size() > 0)
				{
					// on selectionne au hazard
					Random rand = new Random();
					int ind = rand.nextInt(listEnemy.size());
					// on récupère l'enemy
					UnityNet enemy = listEnemy.get(ind);
					// on attribue l'enemy
					
					unity.attributeEnemy(enemy);
					
				}
			}
		}
		
		
		// on regarde si il n'existe pas d'unité à supprimer dans le vecteur unitykilled
		if(this.vectorUnityKilled.size() > 0)
		{
			for(Unity u : this.vectorUnityKilled)
			{
				this.vectorUnity.remove(u.getId());
			}
		}
		this.vectorUnityKilled.clear();
		
		// on regarde si il n'existe pas d'unité à supprimer dans le vecteur unitykilled Net
			if(this.vectorUnityNetKilled.size() > 0)
			{
				for(Unity u : this.vectorUnityNetKilled)
				{
					this.vectorUnityNet.remove(u.getId());
				}
			}
		this.vectorUnityNetKilled.clear();
		
	
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	
	
	

	/*public static List<UnityNet> getVectorUnityNet() {
		return vectorUnityNet;
	}

	public static void setVectorUnityNet(List<UnityNet> vectorUnityNet) {
		EntityManager.vectorUnityNet = vectorUnityNet;
	}*/

	public static Hashtable<Integer, Unity> getVectorUnity() {
		return vectorUnity;
	}

	public static void setVectorUnity(Hashtable<Integer, Unity> vectorUnity) {
		EntityManager.vectorUnity = vectorUnity;
	}

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
					
			// on vide la liste des objets selectionnÃ©
			this.listUnitySelected.clear();
			
			Vec2 mousePos = new Vec2(posMouseWorld.x / pixels,posMouseWorld.y / pixels ); 
			for(Unity unity : vectorUnity.values())
			{
				// si la souris est sur l'unitÃ©
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
			Vector2f p = Vector2f.div(posMouseWorld, PhysicWorldManager.getRatioPixelMeter());
			// on teste si on ne clic pas sur un ennimi, si non en rentre dans le code de formation et de destination
			Unity unityPicked = this.getUnityPicked(p.x,p.y);
			if(unityPicked != null)
			{
			
				
					// pour toutes les unités selectionnées, on attribue l'ennemy
					for(Unity u : this.listUnitySelected)
						u.attributeEnemy(unityPicked);
					// pour toutes les unités, on crée leur position de formation pour attaquer
					this.computeFormationStrike(unityPicked, listUnitySelected, new Vec2(1,0));
				
			}
			else
			{
				// création de la fleche de selection d'angle de formation
			    if(arrow == null)
			    	arrow  = new ChooseAngleFormationDrawable(posMouseWorld,posMouseWorld);
			}
		}
		
		
	}

	@Override
	public void onKeyboard(KeyEvent keyboardEvent) 
	{
		
		
		if(keyboardEvent.key == Keyboard.Key.A )
		{
			// on ajoute une unité
			Knight knight = new Knight();
			knight.init();
			knight.setPosition(NetManager.getPosxStartFlag(),NetManager.getPosyStartFlag());
			// réception de l'id unique pour l'unité
			knight.setId(EntityManager.getNewIdUnity());
			knight.setMyCamp(EntityManager.getCampSelected());
			EntityManager.getVectorUnity().put(knight.getId(), knight);
			// on envoie sur le réseau
			NetHeader header = new NetHeader();
			header.setTypeMessage(TYPE.ADD);
			NetAddUnity add = new NetAddUnity();
			add.setPosx(knight.getPositionMeterX());
			add.setPosy(knight.getPositionMeterY());
			add.setTypeUnity(knight.getIdType());
			add.setIdUnity(knight.getId());
			add.setCampUnity(knight.getMyCamp());
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
		
		if(keyboardEvent.key == Keyboard.Key.B )
		{	
			// ajout d'un ennemi pour les test
			NetAddUnity unity = new NetAddUnity();
			unity.setIdUnity(idTestUnity);
			unity.setTypeUnity(TYPEUNITY.KNIGHT);
			unity.setPosx(48);
			unity.setPosy(28);
			unity.setRotation(0f);
			unity.setCampUnity(CAMP.BLUE);
			idTestUnity++;
			this.onAddUnity(unity);
			
			
		}
		
	}

	@Override
	public void onMouseMove(MouseEvent event) 
	{
		
	}
	
	
	@Override
	public void onMouseReleased(MouseButtonEvent event) 
	{
		
		if(event.button == Button.RIGHT)
		{
		
				// on relache le clic, on récupère la dirction de formation et on lance la formation
				if(arrow != null)
				{
					// on récupère le vecteur de direction pour la formation
					dirFormation = arrow.getVectorDirectionFormation();
					// on calcul la formation
					Vector2f pos = Vector2f.div(posMouseWorld, PhysicWorldManager.getRatioPixelMeter());
				    computeFormation(listUnitySelected,posMouseWorld.x,posMouseWorld.y,dirFormation);
				    
				   
				    // suppression de la fleche dans les callback
					arrow.destroy();
					arrow = null;
				}
			
		}

	}

	@Override
	public void onRegionSelected(FloatRect region) 
	{
		
		// on vient de receptionnÃ© la region selectionnÃ©
		for(Unity unity : vectorUnity.values())
		{
			if(region.contains(new Vector2f(unity.getBody().getPosition().x * PhysicWorldManager.getRatioPixelMeter(),
					unity.getBody().getPosition().y * PhysicWorldManager.getRatioPixelMeter())))
					{
						unity.setSelected(true);
						this.listUnitySelected.add(unity);
						
						// on sort de la sélection si on dépasse 32
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
		switch(unity.getTypeUnity())
		{
		case KNIGHT: KnightNet u = new KnightNet();
					u.init();
					u.setPosition(unity.getPosx(), unity.getPosy());;
					u.setId(unity.getIdUnity());
					u.setMyCamp(unity.getCampUnity());
					vectorUnityNet.put(u.getId(), u);
					break;
		}
		
		
	}
	
	@Override
	public void onMoveUnity(NetMoveUnity unity)
	{
		// réception du unity move réseau	
		// on récupère le bon unity
				
		UnityNet un = vectorUnityNet.get(unity.getId());
		if(un != null)
		{
			// l'unité est trouvée
			Node n = new Node(unity.getNextPosx(),unity.getNextPosy(),true);
			un.setPosXYMeter(unity.getPosx(),unity.getPosy());
			un.setNext(n);
			un.setVecDirFormation(unity.getVecDirFormation());
			un.setAnimate(ANIMATE.WALK); // on active l'animation de mouvement
					
		}
		else
		{
			// l'unité n'est pas trouvé suite à un probleme réseau, il faut donc l'ajouter
			// création d'une unité venant du réseau
			UnityNet u= new UnityNet();
			u.init();
			u.setPosition(unity.getPosx(), unity.getPosy());;
			u.setId(unity.getId());
			// ajout dans le vecteur unity réseau
			//vectorUnityNet.add(u);
			vectorUnityNet.put(u.getId(), u);
		}
		
	
	}
	
	@Override
	public void onStrike(NetStrike strike)
	{
		
		// on réceptionne un message de frappe sur notre unité.
		// on va récupéré l'unité en question
		Unity unity = this.vectorUnity.get(strike.getIdTarget());
		// on va la frapper
		if(unity != null)
			unity.setDamage(strike.getForce());
		
		// il faut récupérer l'unité réseau qui frappe pour lui jouer l'animation
		Unity unityNet = this.vectorUnityNet.get(strike.getIdStriker());
		if(unityNet != null)
			unityNet.setAnimate(ANIMATE.STRIKE);
	}
	
	@Override
	public void onKill(NetKill kill)
	{
		// on recoit le message de mort d'une unité adverse
		Unity unity = this.vectorUnityNet.get(kill.getId());
		if(unity != null)
			unity.setKill();
			
		
	}

	public static int getNewIdUnity()
	{
		// génération d'un id unique pour les unités
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
			
		//	un.getBody().setTransform(un.getBody().getPosition(),sync.getRotation());
					
		}
	}

	@Override
	public void onMouse(MouseEvent buttonEvent) {
		// TODO Auto-generated method stub
		
	}
	
	public float getRadian(float degre)
	{
		float radian = (float) (degre * (Math.PI / 180));
		return radian;
	}
	
	
	
	public void computeFormationStrike(Unity enemy,List<Unity> listUnity, Vec2 dir)
	{
		// en partant du centre (enemy), par couche, on fait un tour en utilisant l'objet Rot
		int couche = 0;
		int nbpercouche = 8;
		float pasDegre = 0f;
		float pasRadian = 0f;
		Vec2 vector;
		float distance = 1f;
		int indListUnity = 0;
		int maxboucle = 0;
		
		Rot rot = new Rot();
		rot.set(0f);
		
		// pour chaque couche on détermine le nombre de recherche
		while(indListUnity < listUnity.size() && maxboucle < 32)
		{
			Unity unity = listUnity.get(indListUnity);
			rot.set(this.getRadian(pasDegre));
			vector = new Vec2(rot.s,rot.c);
			
			// on recheche les emplacements
			Vec2 centre = enemy.getBody().getPosition();
			Vec2 pos = centre.add(vector.mul(distance));
			
			boolean obstacle = LevelManager.getLevel().isNodeObstacle((int)pos.x, (int)pos.y);
			if(!obstacle)
			{
				// on détermine la rotation finale
				Vec2 vFinal = new Vec2(pos.x,pos.y);
				Vec2 vEnemy = enemy.getBody().getPosition();
				// on détermine le vecteur de direction
				Vec2 vDir = vEnemy.sub(vFinal);
				vDir.normalize();
				
				unity.setTargetPosition(pos.x * PhysicWorldManager.getRatioPixelMeter(), pos.y * PhysicWorldManager.getRatioPixelMeter(), (int)pos.x, (int)pos.y, vDir);
				indListUnity++;
			}
			// augmentation du pas
			pasDegre += 45f;
			// max boucle pour la sécurité
			maxboucle++;
		}
		
	}

	public void computeFormation(List<Unity> listUnity,float px,float py,Vec2 dir)
	{
		
		// variable définissant le nombre maximal d'unité sur une ligne
		int nbUnityPerLine = 0;
		int cptLine = 0;
		// on défini les positions
		float dx = px;
		
		Vector2f posInitial = new Vector2f(dx,py);
		Vector2f dep = posInitial;
	
		Vec2 skew = dir.skew();
		
		for(int i=0;i<listUnity.size();i++)
		{
		
			// on récupère une unité
			Unity u = listUnity.get(i);
			// on calcul la position ecran
			Vector2f pos = Vector2f.div(new Vector2f(dep.x,dep.y), PhysicWorldManager.getRatioPixelMeter());
			// on envoie l'unité sur sa positoin
			if(u.setTargetPosition(dep.x,dep.y,(int)pos.x+1,(int)pos.y+1,dir) == false)
			{
				// aucune position possible pour l'unité, on la place derrière
				
				//dep = posInitial;
				cptLine++;
				dep = Vector2f.add(posInitial, Vector2f.mul(Vector2f.neg(new Vector2f(dir.x * 20  ,dir.y * 20)),cptLine));
				i--;
				nbUnityPerLine = 0;
				
			}
			else
			{
				// on a défini la position final de l'unité, on incrémente le nb d'unité par ligne
				nbUnityPerLine++;
			}
			//dx = dx + 20;
			dep = Vector2f.add(dep, new Vector2f(skew.x * 20,skew.y * 20));
			
			if(nbUnityPerLine > 7)
			{
				//si un ligne est complète, on place en dessous
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
		
			// on récupère une unité
			Unity u = listUnity.get(i);
			// on calcul la position ecran
			Vector2f pos = Vector2f.div(new Vector2f(dx,py), PhysicWorldManager.getRatioPixelMeter());
			// on envoie l'unité sur sa positoin
			if(u.setTargetPosition(dx,py,(int)pos.x+1,(int)pos.y+1) == false)
			{
				// aucune position possible pour l'unité, on la place derrière
				dx = px;
				py = py + 20;	
				i--;
				nbUnityPerLine = 0;
				
			}
			else
			{
				// on a défini la position final de l'unité, on incrémente le nb d'unité par ligne
				nbUnityPerLine++;
			}
			dx = dx + 20;
			//dep = Vector2f.add(dep, new Vector2f(skew.x * 20,skew.y * 20));
			dx = dep.x;
			py = dep.y;
			
			if(nbUnityPerLine > 7)
			{
				//si un ligne est complète, on place en dessous
				nbUnityPerLine = 0;
				dx = px;
				py = py + 20;
				
			
				
				
			}
			
		}*/
		

	}
	
	public Unity getUnityPicked(float x,float y)
	{
		   AABB tree = new AABB();
		   tree.lowerBound.set(new Vec2(x - 2f,y - 2f));
		   tree.upperBound.set(new Vec2(x + 2f,y + 2f));
		   ListBodyEnemyForOneRegion listRegion = new ListBodyEnemyForOneRegion();
		   PhysicWorldManager.getWorld().queryAABB(listRegion, tree);

		   // on récupère la liste des bodys dans la région
		   List<UnityNet> list = listRegion.getListEnemy();
		   if(list != null)
		   {
			   for(UnityNet unity : list) // on liste et on récupère l'objet picked
			   {
	
				   Vec2 diff = new Vec2(x,y).sub(unity.getBody().getPosition());
				   System.out.println("lenght : " + diff.length() );
				    
				   if(diff.length() < 0.5f)
				   {
					   // on est sur le body qui est sur la position de la souris
					   return unity;
				   }
			   }
		   }
		
		return null;
	}
	
	public static void IamKilled(Unity unity)
	{
		// l'unité est morte, elle demande à être détruite de la liste
		vectorUnityKilled.add(unity);
		
	}
	
	public static void IamKilledNet(Unity unity)
	{
		// l'unité est morte, elle demande à être détruite de la liste
				vectorUnityNetKilled.add(unity);
	}

	// methode qui recherche les enemys à proximité
	public List<UnityNet> searchEnemyZone(Unity unity)
	{
		// créatin du AABB
		AABB region = new AABB();
		region.lowerBound.set(unity.getBody().getPosition().sub(new Vec2(10,10)));
		region.upperBound.set(unity.getBody().getPosition().add(new Vec2(10,10)));
		
		// recherche
		ListBodyEnemyForOneRegion queryCallBack = new ListBodyEnemyForOneRegion();
		PhysicWorldManager.getWorld().queryAABB(queryCallBack, region);
		
		return queryCallBack.getListEnemy();
		
	}
	
	public static UnityNet searchEnemyZoneNear(Unity unity)
	{
		// créatin du AABB
		AABB region = new AABB();
		region.lowerBound.set(unity.getBody().getPosition().sub(new Vec2(10,10)));
		region.upperBound.set(unity.getBody().getPosition().add(new Vec2(10,10)));
		
		// recherche
		ListBodyEnemyForOneRegion queryCallBack = new ListBodyEnemyForOneRegion();
		PhysicWorldManager.getWorld().queryAABB(queryCallBack, region);
		
		List<UnityNet> list = queryCallBack.getListEnemy();
		// on regarde l'enemy le plus proche
		if(list.size() > 0)
		{
			UnityNet best = list.get(0); // on prend le premier
			for(UnityNet u : list)
			{
				if((best.getBody().getPosition().sub(unity.getBody().getPosition()).length() > (u.getBody().getPosition().sub(u.getBody().getPosition()).length())))
					{
						best = u;
					}
			}
			
			return best;
		}
		
		return null;
	}
	
	public static Vec2 searchPosition(Unity unity,Unity enemy) // recherche d'une place à coté d'un enemy
	{
		// on trace un vecteur
		Vec2 diff = enemy.getBody().getPosition().sub(unity.getBody().getPosition());
		diff.normalize();
		// on multiplie par 1 pour obtenir la distance 
		Vec2 pos = enemy.getBody().getPosition().add(diff.mul(1f).negate());
		
		return pos;
		
	}

	

}
