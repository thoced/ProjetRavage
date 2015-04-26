package coreAI;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsfml.system.Time;

import ravage.IBaseRavage;

public class Astar implements IBaseRavage 
{
	private List<Node> listOpen;
	
	private List<Node> listClosed;
	
	private int cx,cy; // current recherche x et y
	
	private int sizemapx,sizemapy;
	
	private Node[] tempmap;
	
	private Node currentNode;
	
	private List<Node> finalPath;
	
	/**
	 * Fonction de recherche
	 @param map est un tableau de Node représentant le monde
	 @param sizex est la taille en X du monde
	 
	 */
	
	public  ArrayList search(Node[] map,int sizex,int sizey, int xstart,int ystart,int xtarget,int ytarget)
	{
		// map size
		this.sizemapx = sizex;
		this.sizemapy = sizey;
		// map
		this.tempmap = map;
		
		listOpen =  new ArrayList<Node>();
		listClosed = new ArrayList<Node>();
		
		// création du node de fin
		//Node end = new Node(xtarget,ytarget,false);
		Node end = map[(375 * ytarget) + xtarget];
		end.reset(false);
		// on ajoute le point de départ dans la liste ouverte
		//Node start = new Node(xstart,ystart,false);
		Node start = map[(375 * ystart) + xstart];
		start.reset(false);
		listOpen.add(start);
		
		while(listOpen.size() > 0)
		{
			// on récupère le node ayant le plus petit f dans la liste ouverte
			currentNode = this.getNodeWithMinF();
			// on stoppe la boucle si le node current est le node de fin
			if(currentNode.getX() == end.getX() && currentNode.getY() == end.getY())
				break;
			
			// on bascule le node current dans la liste fermée
			this.listClosed.add(currentNode);
			this.listOpen.remove(currentNode);
			
			// on récupère les nodes adjacents
			List<Node> Neightboring = this.getNodeNeighboring(currentNode.getX(),currentNode.getY());
			
			// pour chaque noeud adjacents
			for(Node node : Neightboring)
			{
				// si le noeud est déja dans la liste fermé, on l'ignore
				if(this.isInClosedList(node))
					continue;
				
				// on calcul son G
				int newG;
				// on calcul son cout , si diagonal +14, si horinzontale ou verticale + 10
				if(node.isDiagonal())
					 newG = node.getParent().getG() + 14;  // on ajoute 14 si c'est un node diagonal
				else
					newG = node.getParent().getG() + 10; // on ajoute 10 si pas diagonal
				
				// on calcul son H
				int newH = (Math.abs(end.getX() - node.getX()) + Math.abs(end.getY() - node.getY())) * 10;
				
				// valeur F
				int newF = newH + newG;
				
				// si le node est  dans la liste ouverte
				if(this.isInOpenList(node))
				{
					if(newG < node.getG()) // si le nouveau G est plus petit que l'ancien
					{
						node.setParent(currentNode);
						
						if(node.isDiagonal())
							 newG = currentNode.getG() + 14;  // on ajoute 14 si c'est un node diagonal
						else
							newG = currentNode.getG() + 10; // on ajoute 10 si pas diagonal
						
						// on calcul son H
						 newH = (Math.abs(end.getX() - node.getX()) + Math.abs(end.getY() - node.getY())) * 10;
						
						// valeur F
						newF = newH + newG;
						node.setG(newG);
						node.setH(newH);
						node.setF(newF);
						this.listOpen.remove(node);
						this.listOpen.add(node);
						
					}
					
				}
				else
				{
					// le node n'est pas dans la liste ouverte
					if(node.isDiagonal())
						 newG = currentNode.getG() + 14;  // on ajoute 14 si c'est un node diagonal
					else
						newG = currentNode.getG() + 10; // on ajoute 10 si pas diagonal
					
					// on calcul son H
					 newH = (Math.abs(end.getX() - node.getX()) + Math.abs(end.getY() - node.getY())) * 10;
					
					// valeur F
					 newF = newH + newG;
					
					node.setParent(currentNode);
					node.setG(newG);
					node.setH(newH);
					node.setF(newF);
					this.listOpen.add(node);
					this.listClosed.remove(node);
					
				}
				
			}
			
			
		}
		
		finalPath = new ArrayList<Node>();
		
		
		// on est sorti de la boucle
		if(this.listOpen.size() == 0) // pas de solution
			return (ArrayList) finalPath;
		
		// Soit on maintenant on construit le chemin à rebours;
		// on construit le chemin en liant avec les parents en partant de la fin
		  Node lastNode = this.listClosed.get(this.listClosed.size() - 1 );
		  System.out.println(this.listClosed.size());
	      while( lastNode != start )
	      {
	        finalPath.add(lastNode);
	        lastNode = lastNode.getParent();
	      }
	 // on reverse le tout
		Collections.reverse(finalPath);
		// on return
		return (ArrayList) finalPath;
		
	}
	
	public List<Node> getNodeNeighboring(int x,int y)
	{
			List<Node> ret = new ArrayList<Node>(8);
			
			if(this.isFree(x + 1, y))   // EST
			{
				Node n = this.tempmap[(375 * y) + x + 1];
				//n.reset(false);
				n.setDiagonal(false);
				ret.add(n);
			}
			
			if(this.isFree(x, y + 1))   // SUD
			{
				Node n = this.tempmap[(375 * (y + 1)) + x];
				//n.reset(false);
				n.setDiagonal(false);
				ret.add(n);
			}
			
			if(this.isFree(x - 1, y))   // OUEST
			{
				Node n = this.tempmap[(375 * y) + x - 1];
				//n.reset(false);
				n.setDiagonal(false);
				ret.add(n);
			}
			
			if(this.isFree(x, y - 1))   // NORD
			{
				Node n = this.tempmap[(375 * (y - 1)) + x ];
				//n.reset(false);
				n.setDiagonal(false);
				ret.add(n);
			}
			
			if(this.isFree(x + 1, y + 1 ))   // SUD / EST
			{
				Node n = this.tempmap[(375 * (y + 1)) + x + 1];
				//n.reset(true);
				n.setDiagonal(true);
				ret.add(n);
			}
			
			if(this.isFree(x - 1, y + 1))   // SUD / OUEST
			{
				Node n = this.tempmap[(375 * (y + 1)) + x - 1];
				//n.reset(true);
				n.setDiagonal(true);
				ret.add(n);
			}
			
			if(this.isFree(x - 1, y - 1))   // NORD / OUEST
			{
				Node n = this.tempmap[(375 * (y - 1)) + x - 1];
				//n.reset(true);
				n.setDiagonal(true);
				ret.add(n);
			}
			
			if(this.isFree(x + 1, y - 1))   // NORD / EST
			{
				Node n = this.tempmap[(375 * (y - 1)) + x + 1];
				//n.reset(true);
				n.setDiagonal(true);
				ret.add(n);
			}
			
			
		
		return ret;
	}
	
	public Node getNodeWithMinF()
	{
		Node current = this.listOpen.get(0);
		for(Node n : this.listOpen)
		{
			if(n.getF() < current.getF())
				current = n;
			
		}
		return current;
	}
	
	public boolean isFree(int x,int y)
	{
		if(x < 0 || y < 0 || x > this.sizemapx || y > this.sizemapy)
			return false; // bord de la map
		
		if(this.tempmap[(this.sizemapx * y) + x].getType() == 0) // ground
			return true;
		else
			return false; // wall
		
	}
	
	public boolean isInClosedList(Node n)
	{
		for(Node c : this.listClosed)
		{
			//if(c.getX() == n.getX() && c.getY() == n.getY())
				//return true;
			if(n == c)
				return true;
		}
		
		return false;
	}
	
	public boolean isInOpenList(Node n)
	{
		for(Node c : this.listOpen)
		{
			//if(c.getX() == n.getX() && c.getY() == n.getY())
			//	return true;
			
			if(c == n)
				return true;
		}
		
		return false;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Time deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	

}
