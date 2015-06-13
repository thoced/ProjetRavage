package coreGUIInterface;

import org.jsfml.graphics.Color;
import org.jsfml.window.Mouse;

import CoreTexturesManager.TexturesManager;
import ravage.FrameWork;

public class panelFormation extends PanelRavage {

	public panelFormation(String title) 
	{
		super(title);
		this.setX(Mouse.getPosition(FrameWork.getWindow()).x);
		this.setY(Mouse.getPosition(FrameWork.getWindow()).y);
		
		this.setWidth(128);
		this.setHeight(256);
		
		this.setColorBackground(Color.BLUE);
		this.setTickness(4f);
		this.setColorTickness(Color.CYAN);
		this.setTextureBackground(TexturesManager.GetTextureByName("panel.png"));
		
		this.Init();
		// TODO Auto-generated constructor stub
		// création des bouton
		ButtonRavage button01 = new ButtonRavage("button01",10,10,32,32);
		button01.setColorBackground(Color.RED);
		button01.Init();
		this.addGui(button01);
		
	
	}

}
