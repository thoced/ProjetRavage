package ravage;

import java.util.ArrayList;

import org.newdawn.slick.geom.Path;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.navmesh.*;

import java.util.List;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import CoreTexturesManager.TexturesManager;
import coreAI.Astar;
import coreAI.AstarManager;
import coreAI.Node;
import coreCamera.CameraManager;
import coreDrawable.DrawableUnityManager;
import coreEntity.Unity;
import coreEntityManager.EntityManager;
import coreEvent.EventManager;
import coreGUI.RectSelected;
import coreGUISwing.menuDialogRavage;
import coreLevel.Level;
import coreLevel.LevelManager;
import coreNet.NetManager;
import corePhysic.PhysicWorldManager;

public class FrameWork
{
	// Managers
	private PhysicWorldManager physicWorld;
	private LevelManager levelManager;
	private TexturesManager texturesManager;
	private CameraManager cameraManager;
	private EntityManager entityManager;
	private DrawableUnityManager drawaUnityManager;
	private AstarManager astarManager;
	private RectSelected rect;
	private NetManager netManager;
	private EventManager eventManager;
	// Clocks
	private Clock frameClock;
	// fps
	private Time fpsTime;
	private int fps;
	// Level
	private Level currentLevel;
	// RenderWindown
	private static RenderWindow window;
	// RenderTarget
	private RenderTexture renderTexture;
	private Sprite	renderSprite;
	
	private menuDialogRavage menu;
	
	public void init() throws TextureCreationException, InterruptedException 
	{
		
		// Instance du r�seau
		netManager = new NetManager();
		netManager.init();
		
		// Lancement du menu
		

		// creation de l'environnemnet graphique jsfml
		window = new RenderWindow(new VideoMode(1024,768),"ProjetRavage");
		window.setFramerateLimit(60);
		// Instance des variables
		frameClock = new Clock();
		fpsTime = Time.ZERO;
		
		menu = new menuDialogRavage(null, "Projet Ravage Menu", true,netManager);
		menu.setVisible(true);
		menu.dispose();
				
	
		// Instance des managers
		physicWorld = new PhysicWorldManager();
		physicWorld.init();
		levelManager = new LevelManager();
		levelManager.init();
		texturesManager = new TexturesManager();
		texturesManager.init();
		cameraManager = new CameraManager(window.getView());
		cameraManager.init();
		entityManager = new EntityManager();
		entityManager.init();
		drawaUnityManager = new DrawableUnityManager();
		drawaUnityManager.init();
		rect = new RectSelected();
		rect.init();
		eventManager = new EventManager();
		eventManager.init();
		
		
		// attachement au call back
		RectSelected.attachCallBack(entityManager);
		eventManager.addCallBack(cameraManager);
		eventManager.addCallBack(entityManager);
		eventManager.addCallBack(rect);
		
		// Chargement du niveau
		currentLevel  = levelManager.loadLevel("testlevel01.json");
		// creatin du astarmanager
		astarManager = new AstarManager();
		astarManager.init();
		
		// création d'une première render texture
		renderTexture = new RenderTexture();
		renderTexture.create(window.getSize().x, window.getSize().y);
		renderSprite = new Sprite(renderTexture.getTexture());
		
		
		
		
		// on place une unity
		/*for(int y=0;y < 32;y++)
		{
			for(int x=0;x<32;x++)
			{
				Unity unity = new Unity();
				unity.init();
				unity.setPosition(new Vec2(x * 2, (y * 2) + 192 ));
				entityManager.getVectorUnity().add(unity);
			}
		}*/
	
	/*	for(int i=0;i<60;i+=2)
		{
			Unity unity = new Unity();
			unity.init();
			unity.setPosition(20 + i,12);
			entityManager.getVectorUnity().add(unity);
			
		//	unity.setTargetPosition(363,217);
		}
		*/
		
		
		
	//	NavPath paths = navmesh.findPath(1f	, 1f, 100, 110, true);
		
	/*	if(paths != null)
		{
			System.out.println(paths.length());
			
			for(int i=0;i<paths.length();i++)
			{
				float x = paths.getX(i);
				float y = paths.getY(i);
				System.out.println("X: " + x + " Y: " + y);
			}
		}*/
		
		
		
	}

	public void run()
	{
		while(window.isOpen())
		{
			
			// Pool des evenements
			for(Event event : window.pollEvents())
			{
				// catch des events
				eventManager.catchEvent(event);

				if(event.type == Event.Type.CLOSED) 
				{
					this.destroyFrameWork();
				}
				
				if(event.type == Event.Type.KEY_PRESSED)
				{
					if(event.asKeyEvent().key == Keyboard.Key.ESCAPE)
					{
						this.destroyFrameWork();
					}
				
					
				}

			}
			
			// Créatin du deltaTime
			Time deltaTime = frameClock.restart();
			fpsTime = Time.add(fpsTime, deltaTime);
			
			/*if(fpsTime.asSeconds() > 1.0f)
			{
				System.out.println("fps : " + String.valueOf(fps));
				fps=0;
				fpsTime = Time.ZERO;
			}
			
			fps++;*/
			
			// Updates de composants
			physicWorld.update(deltaTime);
			currentLevel.update(deltaTime);
			cameraManager.update(deltaTime);
			entityManager.update(deltaTime);
			astarManager.update(deltaTime);
			rect.update(deltaTime);
			netManager.update(deltaTime);
			
			// Draw des composants
			renderTexture.clear();
			renderTexture.setView(cameraManager.getView());
			// Draw du level
			currentLevel.draw(renderTexture, null);
			// Draw des unity
			drawaUnityManager.draw(renderTexture, null);
			renderTexture.display();
			
			// draw du rect
			rect.draw(renderTexture, null);
			renderTexture.display();
			// draw final
			window.clear();
			window.draw(renderSprite);
			window.display();
			
			
		}
		
	
	}

	public void destroyFrameWork()
	{
		// destruction du thread
		if(astarManager != null)
			astarManager.interrupt();
		// fermeture de la connection UDP et du theadd
		if(netManager != null)
			netManager.close();
		// fermeture de la fenetre
		window.close();
	}

	/**
	 * @return the window
	 */
	public static RenderWindow getWindow() {
		return window;
	}

	/**
	 * @param window the window to set
	 */
	public static void setWindow(RenderWindow window) {
		FrameWork.window = window;
	}
	
	
}
