package coreNet;

public interface INetManagerCallBack 
{
	public void onHello(NetHello hello);
	
	public void onAddUnity(NetAddUnity unity);
	
	public void onMoveUnity(NetMoveUnity unity);
		
}
