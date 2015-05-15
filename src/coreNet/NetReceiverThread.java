package coreNet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class NetReceiverThread extends Thread 
{
	// num port receiver
	private final int PORT = 1234;
	// size of buffer
	private final int SIZEBUFFER = 1024;
	// buffer de r�ception 
	private byte[] buffer;
	// Datagram Socket de r�ception
	private DatagramSocket  socketReceiver;

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		super.run();
		
		try
		{
			// instance du datagram receiver	
			socketReceiver = new DatagramSocket(PORT);
			// cr�ation du datagram packet 
			buffer = new byte[SIZEBUFFER];
			DatagramPacket datagram = new DatagramPacket(buffer,SIZEBUFFER);

			while(true)
			{
				// reception bloquante d'un datagram udp
				socketReceiver.receive(datagram);
				// on vient de r�ceptionner un datagram
				// on envoie le tout dans le NetManager
				
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
