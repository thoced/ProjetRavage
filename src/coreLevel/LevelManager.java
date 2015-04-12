package coreLevel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import CoreLoader.LoaderTiled;
import CoreLoader.LoaderTiledException;
import CoreLoader.TiledLayerImages;
import CoreLoader.TiledLayerObjects;
import CoreLoader.TiledObjectBase;
import CoreLoader.TiledObjectPolyline;
import CoreTexturesManager.TexturesManager;
import ravage.IBaseRavage;

public class LevelManager implements IBaseRavage
{
	@Override
	public void init() 
	{
		// on charge un level de test
		//n test de chargement d'un fichier json	
		
	}

	@Override
	public void update(Time deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public Level loadLevel(String nameFile)
	{
		// création de l'objet Level
		Level level = new Level();
		// Chargement
		LoaderTiled tiled = new LoaderTiled();
		try 
		{
			// chargement
			tiled.Load(new FileInputStream("./bin/Maps/" + nameFile));
			
			
			// on récupère les images
			List<TiledLayerImages> listImages = tiled.getListLayersImages();
			
			for(TiledLayerImages l : listImages)
			{
				// pour chaque image, on crée un sprite que l'on ajoute dans l'objet level
				String[] paths = l.getPathImage().split("/");
				// on charge la texture et on cré un sprite
				Sprite sprite = new Sprite(TexturesManager.GetTextureByName(paths[paths.length - 1]));
				// on configure le sprite
				sprite.setOrigin(new Vector2f(0f,0f));
				sprite.setPosition(new Vector2f(l.getPosx(),l.getPosy()));
				// on ajoute dans le level
				level.getBackgrounds().add(sprite);
				
			}
			
			// on récupère les obstacles
			List<TiledLayerObjects> listObjects = tiled.getListLayersObjects();
			
			for(TiledLayerObjects obj : listObjects)
			{
				for(TiledObjectBase base : obj.getDataObjects())
				{
					if(base.getTypeObjects() == TiledObjectBase.Type.POLYLINE)
					{
						TiledObjectPolyline poly = (TiledObjectPolyline) base;
						level.InsertObstacle(poly.getListPoint(), poly.getX(), poly.getY(), poly.getType());
					}
				}
				
			}
					
		} catch (FileNotFoundException | LoaderTiledException e) {
					// TODO Auto-generated catch block
			System.out.println("Error : LevelManager : " + e.getMessage());
		}
		
		// retour du level
		return level;
	}

}
