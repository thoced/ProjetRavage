package coreEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import CoreTexturesManager.TexturesManager;
import coreDrawable.DrawableUnityManager;
import coreDrawable.DrawableUnityManager.LAYERS;
import ravage.IBaseRavage;

public class BloodManager implements IBaseRavage,Drawable 
{
	// list des sprites blood
	private static List<Blood> listBlood;
	// liste destruct blood
	private static List<Blood> listDestroyBlood;
	// tableau de texturerect
	private static IntRect[] listIntRect;
	
	// texture blood
	private static Texture textureBlood;
	
	@Override
	public void init()
	{
		// on s'inscrit au DrawableUnityManager
		DrawableUnityManager.AddDrawable(this,LAYERS.BACK);
		// on créé les listes
		listBlood = new ArrayList<Blood>();
		listDestroyBlood = new ArrayList<Blood>();
		// chargement de la texture blood
		textureBlood = new Texture(TexturesManager.GetTextureByName("blood_splatt_01.png"));
		// instance des intrects
		listIntRect = new IntRect[15];
		int x = 0;
		for(int i=0;i<15;i++)
		{
			listIntRect[i] = new IntRect(x,0,32,32);
			x+=32;
		}

	}

	@Override
	public void update(Time deltaTime) 
	{
		for(Blood blood : listBlood)
		{
			Time elapse = blood.getElapsedTimeBlood();
			blood.setElapsedTimeBlood(Time.add(elapse, deltaTime));
			// si le sang est resté plus de 5 seconde
			if(blood.getElapsedTimeBlood().asSeconds() > 5f)
				listDestroyBlood.add(blood);
				
		}
		
		// on supprime de la liste
		listBlood.removeAll(listDestroyBlood);
		listDestroyBlood.clear();
	
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	
	public static void addBlood(float posx,float posy)
	{
		Sprite sprite = new Sprite(textureBlood);
		sprite.setPosition(new Vector2f(posx,posy));
		sprite.setOrigin(new Vector2f(16f,16f));
		// on cré un rand pour la selection aléatoire du sang
		Random rand = new Random();
		int randomNum = rand.nextInt(15);
		sprite.setTextureRect(listIntRect[randomNum]);
		// random de l'angle du sang
		float angleBlood = rand.nextFloat();
		angleBlood = angleBlood * 360f;
		sprite.setRotation(angleBlood);
		Blood blood = new Blood();
		blood.setSpriteBlood(sprite);
		
		// ajout dans la liste
		listBlood.add(blood);
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		for(Blood blood : listBlood)
		{
			arg0.draw(blood.getSpriteBlood());
		}
		
	}

}
