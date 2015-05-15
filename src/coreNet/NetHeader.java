package coreNet;

public class NetHeader 
{
	private enum TYPE {HELLO,ADD,MOVE};
	// type de message
	private TYPE typeMessage;
	
	// message en question
	private NetBase Message;


	public NetBase getMessage() {
		return Message;
	}

	public void setMessage(NetBase message) {
		Message = message;
	}

	public TYPE getTypeMessage() {
		return typeMessage;
	}

	public void setTypeMessage(TYPE typeMessage) {
		this.typeMessage = typeMessage;
	}
	
	
}
