package coreNet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jsfml.system.Time;

import coreEntity.Unity;
import coreGUI.IRegionSelectedCallBack;
import coreGUI.RectSelected;
import coreNet.NetHeader.TYPE;
import ravage.IBaseRavage;

public class NetManager implements IBaseRavage
{
	private final static  int MAX_INFO_IN_DATAGRAM = 32; 
	
	private static Lock lock;
	
	private static List<NetHeader> listNetMessage;
	
	private static List<NetDatagram> listNetDatagram;
	
	// list des callback attachés au netmanager
	private static  List<INetManagerCallBack> listCallBack;
	
	// NetReceiverThread
	private NetReceiverThread netReceiver;
	// Datagramsocket emission
	private static DatagramSocket socketEmission;
	// Inetadress
	private static InetAddress inet;
	
	// Positiion flags start
	private static int posxStartFlag;
	private static int posyStartFlag;
	
	// NetMessage
	private static NetDatagram netDatagram;
			
	public NetManager()
	{
		// instance du listcallback
		listCallBack = new ArrayList<INetManagerCallBack>();
		// instance de listNetMessage
		listNetMessage = new ArrayList<NetHeader>();
		listNetDatagram = new ArrayList<NetDatagram>();
		// instance du lock
		lock = new ReentrantLock();
		// instance de socket emission
		// instance de NetDatagram
		//netDatagram = new NetDatagram();
		createNetDatagram();
		
	}
	
	public static void attachCallBack(INetManagerCallBack obj) 
	{
		// attach de callback
		listCallBack.add( obj);
	}
	
	public static void createNetDatagram()
	{
		netDatagram = new NetDatagram();
	}

	public static void pushNetMessage(NetHeader header)
	{
		lock.lock();
		
		listNetMessage.add(header);
		
		lock.unlock();
	}
	
	public static void pushNetDatagram(NetDatagram data)
	{
		lock.lock();
		
		listNetDatagram.add(data);
		
		lock.unlock();
	}
		
	
	@Override
	public void init()
	{
		// lancement du NetReceiverThread
		netReceiver = new NetReceiverThread();
		netReceiver.start();
		
	}
	
	public static void configureIp(String ip) throws UnknownHostException, SocketException
	{
		// configure l'inetadress du joueur adverse
		inet = InetAddress.getByName(ip);
		// configure la socket
		socketEmission = new DatagramSocket();
	}
	
	public static void SendMessage(NetHeader header) throws IOException
	{
		// création du message en buffer
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(header);
		// on récupère le buffer array
		byte[] buffer = new byte[baos.size()];
		buffer = baos.toByteArray();
		// création d'un datagramudp
		DatagramPacket datagram = new DatagramPacket(buffer,buffer.length);
		datagram.setPort(1234);
		datagram.setAddress(inet);
		// emission
		if(socketEmission != null)
		socketEmission.send(datagram);
		
		
	}
	
	public static void PackMessage(NetHeader header) throws IOException
	{
		if(netDatagram == null)
			createNetDatagram();
		
		if(netDatagram.getListHeader().size() < MAX_INFO_IN_DATAGRAM)
		{
			netDatagram.getListHeader().add(header);
		}
		else
		{
			netDatagram.getListHeader().add(header);
			// emission
			SendDatagram();
			// clear du netdatagram
			netDatagram.clear();
			// création d'un nouveau datagram
			createNetDatagram();
		}
		/*
		 * 
		 * 
		netDatagram.getListHeader().add(header);
		
		SendDatagram();
		
		netDatagram.clear();
*/
	}
	
	public static void SendDatagram() throws IOException
	{
		// création du message en buffer
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(netDatagram);
				// on récupère le buffer array
				byte[] buffer = new byte[baos.size()];
				buffer = baos.toByteArray();
				// création d'un datagramudp
				DatagramPacket datagram = new DatagramPacket(buffer,buffer.length);
				datagram.setPort(1234);
				datagram.setAddress(inet);
				// emission
				if(socketEmission != null)
				socketEmission.send(datagram);
				
			//	System.out.println("Buffer Size : " + String.valueOf(buffer.length));
	}

	
	
	@Override
	public void update(Time deltaTime) 
	{
		//NetHeader header = null; 
		
		// code d'envoie du netDatagram
				try 
				{
					// envoie
					if(netDatagram.getListHeader().size() > 0)
					{
						SendDatagram();
						// suppression
						netDatagram.clear();
						createNetDatagram();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		
		// on vérifie si il n'y a pas qlq chose dans le listnetmessage
		lock.lock();
		
	/*	while(listNetMessage.size() > 0)
		{
			// on récupère le premier placé
			header = listNetMessage.get(0);
			// on supprime le premier placé
			listNetMessage.remove(0);
			
			if(header != null)	// appel au dispatcher
				dispatcher(header);

		}*/
		
	
		
		
		
		// code de réception de données par le réseau
		while(listNetDatagram.size() > 0)
		{
			// on récupère le Netdatagram
			NetDatagram datagram = listNetDatagram.get(0);
			// on supprime le premier de la liste
			listNetDatagram.remove(0);
			
			// on décapsule l'ensemble des NetHeader présent dans le datagram
			while(datagram.getListHeader().size() > 0)
			{
				for(NetHeader header : datagram.getListHeader())
				{
					// on appel le dispatcher
					if(header != null)
						dispatcher(header);
				}
				
				// on supprime l'ensemble du datagram
				datagram.getListHeader().clear();
			}
			
			
		}
		
		// clear du listnetdatagram
			listNetDatagram.clear();
			listNetDatagram = new ArrayList<NetDatagram>();
		
		lock.unlock();
	

	}
	
	private void dispatcher(NetHeader header)
	{
		switch(header.getTypeMessage())
		{
			case HELLO: NetHello hello = (NetHello)header.getMessage();
						callBackHello(hello);
						break;
			
			case ADD: 	NetAddUnity unity = (NetAddUnity)header.getMessage();
					  	callBackAdd(unity);
					  	break;
			
			case MOVE: 	NetMoveUnity munity = (NetMoveUnity)header.getMessage();
					   	callBackMove(munity);
					   	break;
					   	
			case SYNC: 	NetSynchronize sync = (NetSynchronize)header.getMessage();
						callBackSync(sync);
						break;
						
			case STRIKE: NetStrike strike = (NetStrike)header.getMessage();
						callBackStrike(strike);
						break;
						
			case KILL: NetKill kill = (NetKill)header.getMessage();
						callBackKill(kill);
						break;
			
	
								   	
			default: break;
		}
	}
	
	private void callBackHello(NetHello hello)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onHello(hello);
		}
	}
	
	private void callBackAdd(NetAddUnity unity)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onAddUnity(unity);
		}
	}
	
	private void callBackMove(NetMoveUnity unity)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onMoveUnity(unity);
		}
	}
	
	private void callBackSync(NetSynchronize sync)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onSynchronize(sync);
		}
	}
	
	private void callBackStrike(NetStrike strike)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onStrike(strike);
		}
	}
	
	public void callBackKill(NetKill kill)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onKill(kill);
		}
	}
	
	public void close()
	{
		// fermeture de la connection et arret du thread
		netReceiver.closeConnection();
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		this.close();
		
	}

	public static int getPosxStartFlag() {
		return posxStartFlag;
	}

	public static void setPosxStartFlag(int posxStartFlag) {
		NetManager.posxStartFlag = posxStartFlag;
	}

	public static int getPosyStartFlag() {
		return posyStartFlag;
	}

	public static void setPosyStartFlag(int posyStartFlag) {
		NetManager.posyStartFlag = posyStartFlag;
	}
	
}
