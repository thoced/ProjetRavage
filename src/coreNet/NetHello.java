package coreNet;

import java.io.Serializable;

public class NetHello extends NetBase implements Serializable
{
	private String nickName;
	
	private String message;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
