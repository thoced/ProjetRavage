package coreNet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jsfml.system.Time;

import ravage.IBaseRavage;

public class NetManager implements IBaseRavage
{
	private static Lock lock;
	
	private static List<NetHeader> listNetMessage;
	
	public NetManager()
	{
		// instance de listNetMessage
		listNetMessage = new ArrayList<NetHeader>();
		// instance du lock
		lock = new ReentrantLock();
	}

	public static void pushNetMessage(NetHeader header)
	{
		lock.lock();
		
		listNetMessage.add(header);
		
		lock.unlock();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Time deltaTime) 
	{
		NetHeader header = null; 
		
		// on vérifie si il n'y a pas qlq chose dans le listnetmessage
		lock.lock();
		
		if(listNetMessage.size() > 0)
		{
			// on récupère le premier placé
			NetHeader header = listNetMessage.get(0);
			// on supprime le premier placé
			listNetMessage.remove(0);
			
		}
		
		lock.unlock();
		
		if(header != null)
		{
			
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
