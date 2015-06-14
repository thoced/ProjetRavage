package coreEntityManager;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

public class ListBodyForOneRegion implements QueryCallback {

	// nb de body dans la liste
	private int nb;
	// list des body dans la région
	private List<Body> listBody;
	
	public ListBodyForOneRegion()
	{
		// instance à 0
		this.nb = 0;
		// instance de listbody
		this.listBody = new ArrayList<Body>();
	}
	
	@Override
	public boolean reportFixture(Fixture arg0) 
	{
		//on récupère le body
		Body b = arg0.getBody();
		// si le userdate provient de la class unity on incrément le nb
		if(b.getUserData() != null && b.getUserData().getClass() != String.class)
		{
			this.nb ++;
			// ajout dans la liste body
			this.listBody.add(b);
		}
		
		return true;
	}
	
	public int getNbBodyInList()
	{
		// return du nb de body
		return this.nb;
	}

	public List<Body> getListBody() {
		return listBody;
	}

	
	

}
