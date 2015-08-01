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
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import CoreTexturesManager.TexturesManager;
import coreAI.AstarManager;
import coreAI.Node;
import coreCamera.CameraManager;
import coreDrawable.DrawableUnityManager;
import coreEntity.Unity;
import coreEntityManager.BloodManager;
import coreEntityManager.EntityManager;
import coreEvent.EventManager;
import coreGUI.RectSelected;
import coreGUIInterface.ButtonRavage;
import coreGUIInterface.GuiManager;
import coreGUIInterface.PanelRavage;
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
	private GuiManager	guiManager;
	private BloodManager bloodManager;
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
	// Render pour le Gui
	private RenderTexture renderGui;
	private Sprite renderGuiSprite;
	
	private menuDialogRavage menu;
	
	public void init() throws TextureCreationException, InterruptedException 
	{
		
		// Instance du réseau
		netManager = new NetManager();
		netManager.init();
		
		// Lancement du menu
		
		window = new RenderWindow(new VideoMode(1024,768),"ProjetRavage",RenderWindow.DEFAULT);
		// Instance des variables
		frameClock = new Clock();
		fpsTime = Time.ZERO;
		
		menu = new menuDialogRavage(null, "Projet Ravage Menu", true,netManager);
		menu.setVisible(true);
		menu.dispose();
		
		// creation de l'environnemnet graphique jsfml
		window.close();
		if(menu.isFullScreen())
			window = new RenderWindow(new VideoMode(menu.getResolutionScreenXY()[0],menu.getResolutionScreenXY()[1]),"ProjetRavage",RenderWindow.FULLSCREEN);
		else
			window = new RenderWindow(new VideoMode(menu.getResolutionScreenXY()[0],menu.getResolutionScreenXY()[1]),"ProjetRavage",RenderWindow.DEFAULT);
		
		window.setFramerateLimit(60);
				
		
	
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
		guiManager = new GuiManager();
		guiManager.init();
		bloodManager = new BloodManager();
		bloodManager.init();
		
		
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
		
		// crÃ©ation d'une premiÃ¨re render texture
		renderTexture = new RenderTexture();
		renderTexture.create(window.getSize().x, window.getSize().y);
		renderSprite = new Sprite(renderTexture.getTexture());
		// création de la texture pour le render gui
		renderGui = new RenderTexture();
		renderGui.create(window.getSize().x, window.getSize().y);
		renderGuiSprite = new Sprite(renderGui.getTexture());
		
		// création des guis tests
		
		PanelRavage panel = new PanelRavage("My panel",0,window.getSize().y - 256,128,256);
		panel.setTextureBackground(TexturesManager.GetTextureByName("panel.png"));
		panel.setColorBackground(new Color(128,128,128));
		panel.setColorTickness(new Color(200,200,200));
		panel.setTickness(4f);
		panel.Init();
		GuiManager.addGui(panel);
		
		ButtonRavage button = new ButtonRavage("my button",32,32,64,32);
		button.setColorBackground(new Color(128,32,32));
		button.Init();
	    panel.addGui(button);
	    
	    ButtonRavage button2 = new ButtonRavage("my button 2",32,72,64,32);
		button2.setColorBackground(new Color(128,32,32));
		button2.Init();
	    panel.addGui(button2);

		
	}

	public void run()
	{
		while(window.isOpen())
		{
			
			// Pool des evenements
			for(Event event : window.pollEvents())
			{
				// catch des events
				if(guiManager.catchEvent(event) != true)  // si un evenement est attrapé par le guiManager, on ne passe pas l'evenement au Manager d'eventment du jeu
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
			
			// CrÃ©atin du deltaTime
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
			eventManager.update(deltaTime);
			netManager.update(deltaTime);
			bloodManager.update(deltaTime);
			
			// Draw des composants
			renderTexture.clear();
			renderTexture.setView(cameraManager.getView());
			// Draw du level
			currentLevel.drawBackground(renderTexture, null); // affichage du background du level
			// Draw des unity
			drawaUnityManager.draw(renderTexture, null);
			// draw du level foregrounds
			currentLevel.drawForeground(renderTexture, null); // affichage du foreground du level (arbre et toi
			renderTexture.display();
			// draw du guiManager
			renderGui.clear(Color.TRANSPARENT);
			guiManager.draw(renderGui, null);
			renderGui.display();
			
			// draw du rect
			rect.draw(renderTexture, null);
			renderTexture.display();
			// draw final
			window.clear();
			window.draw(renderSprite);
			window.draw(renderGuiSprite);
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
