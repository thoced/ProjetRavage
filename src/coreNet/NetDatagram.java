package coreNet;

import java.util.ArrayList;
import java.util.List;

public class NetDatagram extends NetBase 
{
	private List<NetHeader> listHeader;

	public NetDatagram()
	{
		// instance
		listHeader = new ArrayList<NetHeader>();
	}
	
	public void clear()
	{
		if(listHeader != null)
			listHeader.clear();
	}
	
	public List<NetHeader> getListHeader() {
		return listHeader;
	}

	public void setListHeader(List<NetHeader> listHeader) {
		this.listHeader = listHeader;
	}
	
	
}
