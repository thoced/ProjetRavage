package ravage;



import java.util.List;

import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import CoreTexturesManager.TexturesManager;
import coreAI.Astar;
import coreAI.Node;
import coreCamera.CameraManager;
import coreDrawable.DrawableUnityManager;
import coreEntity.Unity;
import coreEntityManager.EntityManager;
import coreLevel.Level;
import coreLevel.LevelManager;

public class FrameWork
{
	// Managers
	private LevelManager levelManager;
	private TexturesManager texturesManager;
	private CameraManager cameraManager;
	private EntityManager entityManager;
	private DrawableUnityManager drawaUnityManager;
	// Clocks
	private Clock frameClock;
	// fps
	private Time fpsTime;
	private int fps;
	// Level
	private Level currentLevel;
	// RenderWindown
	private RenderWindow window;
	// RenderTarget
	private RenderTexture renderTexture;
	private Sprite	renderSprite;
	
	public void init() throws TextureCreationException 
	{
		// creation de l'environnemnet graphique jsfml
		window = new RenderWindow(new VideoMode(1366,768),"ProjetRavage");
		window.setFramerateLimit(60);
		// Instance des variables
		frameClock = new Clock();
		fpsTime = Time.ZERO;
		
		// Instance des managers
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
		
		// Chargement du niveau
		currentLevel  = levelManager.loadLevel("testlevel01.json");
		
		
		// création d'une première render texture
		renderTexture = new RenderTexture();
		renderTexture.create(window.getSize().x, window.getSize().y);
		renderSprite = new Sprite(renderTexture.getTexture());
		
		// on place une unity
		for(int y=0;y < 128;y++)
		{
			for(int x=0;x<128;x++)
			{
				Unity unity = new Unity();
				unity.setPosx(x * 32);
				unity.setPosy(y * 32);
				entityManager.getVectorUnity().add(unity);
			}
		}
		
		
		
	}

	public void run()
	{
		while(window.isOpen())
		{
			
			// Pool des evenements
			for(Event event : window.pollEvents())
			{
				if(event.type == Event.Type.CLOSED) 
				{
					window.close();
				}
				
				if(event.type == Event.Type.KEY_PRESSED)
				{
					if(event.asKeyEvent().key == Keyboard.Key.ESCAPE)
					{
						window.close();
					}
					
				}
			}
			
			// Créatin du deltaTime
			Time deltaTime = frameClock.restart();
			fpsTime = Time.add(fpsTime, deltaTime);
			
			if(fpsTime.asSeconds() > 1.0f)
			{
				System.out.println("fps : " + String.valueOf(fps));
				fps=0;
				fpsTime = Time.ZERO;
			}
			
			fps++;
			
			// Updates de composants
			currentLevel.update(deltaTime);
			cameraManager.update(deltaTime);
			
			// Draw des composants
			renderTexture.clear();
			renderTexture.setView(cameraManager.getView());
			// Draw du level
			currentLevel.draw(renderTexture, null);
			// Draw des unity
			drawaUnityManager.draw(renderTexture, null);
			renderTexture.display();
			// draw final
			window.clear();
			window.draw(renderSprite);
			window.display();
			
		}
		
		
	}

}
