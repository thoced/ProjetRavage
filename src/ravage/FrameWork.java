package ravage;



import java.util.ArrayList;
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
import coreLevel.Level;
import coreLevel.LevelManager;
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
		astarManager = new AstarManager();
		astarManager.init();
		
		// Chargement du niveau
		currentLevel  = levelManager.loadLevel("testlevel01.json");
		
		
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
		Unity unity = new Unity();
		unity.init();
		unity.setPosition(new Vec2(32,32));
		entityManager.getVectorUnity().add(unity);
		
		unity.setTargetPosition(new Vec2(215,2));
		
		
		// test d'automate pour le systmee de chemin
		Body body;
		BodyDef bdef = new BodyDef();
		bdef.active = true;
		bdef.bullet = false;
		bdef.type = BodyType.DYNAMIC;
		bdef.fixedRotation = true;
		bdef.gravityScale = 0.0f;
		bdef.position = new Vec2(0.5f,0.5f);
		
		
		
		// creation du body
		body = PhysicWorldManager.getWorld().createBody(bdef);
		body.setUserData("NO");
		
		PolygonShape shape = new PolygonShape();
	
		shape.setAsBox(0.5f,0.5f);
		
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.density = 1.0f;
		fDef.friction =1.0f;
		fDef.restitution = 0f;
	
		Fixture fix = body.createFixture(fDef);
		
			

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
			physicWorld.update(deltaTime);
			currentLevel.update(deltaTime);
			cameraManager.update(deltaTime);
			entityManager.update(deltaTime);
			
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
