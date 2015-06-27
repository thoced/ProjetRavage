package coreNet;

import java.io.Serializable;

public class NetHeader implements Serializable
{
	public static enum TYPE {HELLO,ADD,MOVE,SYNC,STRIKE,KILL};
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
