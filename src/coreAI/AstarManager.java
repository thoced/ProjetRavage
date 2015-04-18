package coreAI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.jsfml.system.Time;

import coreLevel.LevelManager;
import ravage.IBaseRavage;

public class AstarManager extends Thread implements IBaseRavage 
{
	private static Astar star;
	
	private final int MAX_AVAILABLE = 1;

	private static List<AskPath> listAsk;
	
	private static Semaphore semaphore;
	
	@Override
	public void init() 
	{
		
		// instance du star
		star = new Astar();
		// instance de la liste des demandes
		this.listAsk = new ArrayList<AskPath>();
		// instance du semaphore
		semaphore = new Semaphore(MAX_AVAILABLE,true);
		// on lance le thread
		this.start();
		
	}

	@Override
	public void update(Time deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		super.run();
		
		//
		while(this.isAlive())
		{
			try 
			{
				// on prend le semaphore
				semaphore.acquire();
				// on récupère la demande dans liste
				if(this.listAsk.size() > 0)
				{
					AskPath ask = this.listAsk.get(0);
					
					// on lance une recherche de chemin
					List<Node> finalPath = star.search(LevelManager.getLevel().getNodes(), 375, 250, ask.getX(), ask.getY(), ask.getTx(), ask.getTy());
					
					// on appel le caller
					ask.getCaller().onCallSearchPath(finalPath);
					
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	public static void askPath(ICallBackAStar caller,int posx,int posy,int targetx,int targety) throws InterruptedException
	{
		// on créé une demande
		AskPath ask = new AskPath(caller,posx,posy,targetx,targety);
		
		// on place la demande dans la liste
		listAsk.add(ask);
		semaphore.release();
		
		
	}

	
	
	

}
