package realTimeStrategy.gameEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import realTimeStrategy.mapObjects.Cell;
import realTimeStrategy.mapObjects.Coordinate;

public class AStar
{
	private static int manhattan(Coordinate c1, Coordinate c2)
	{
		int x = c2.getX() - c1.getX();
		int y = c2.getY() - c1.getY();
		return (Math.abs(x) + Math.abs(y)) * 10;
	}
	
	private static Node getCurrent(List<Node> openedList)
	{
		int fMin = 1000000;
		Node current = null;
		for(int i = 0; i < openedList.size(); i++)
		{
			Node n = openedList.get(i);
			if(fMin > n.getF())
			{
				fMin = n.getF();
				current = n;
			}
		}
		return current;
	}
	
	private static Node isNeighbor(int x, int y, int i, Game game)
	{
		if(((i == 0 && y >= 0) || (i == 1 && y < Game.HEIGHT)
				|| (i == 2 && x >= 0) || (i == 3 && x < Game.WIDTH))
				&& game.getCell(x, y).getObject() != null)
			return new Node(x, y);
		return null;
	}
	
	private static List<Node> getNeighbors(Game game, Node current, Coordinate c2)
	{
		List<Node> neighbors = new ArrayList<Node>();
		for(int i = 0; i < 4; i++)
		{
			Node n = null;
			int x = current.getCoordinate().getX();
			int y = current.getCoordinate().getY();
			if(i == 0) y--;
			else if(i == 1) y++;
			else if(i == 2) x--;
			else x++;
			n = isNeighbor(x, y, i, game);
			if(n != null)
			{
				n.setFather(current);
				n.setG(current.getG() + 10);
				n.setH(manhattan(n.getCoordinate(), c2));
				n.setF(n.getG() + n.getH());
				neighbors.add(n);
			}
		}
		return neighbors;
	}
	
	private static Node contains(List<Node> list, Coordinate c)
	{
		for(int i = 0; i < list.size(); i++)
		{
			Node n = list.get(i);
			if(c.compare(n.getCoordinate())) return n;
		}
		return null;
	}
	
	private static Node findPath(Game game, Coordinate c1, Coordinate c2)
	{
		List<Node> openedList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		Node start = new Node(c1);
		start.setH(manhattan(c1, c2));
		start.setF(start.getH()); // start.g = 0
		openedList.add(start);
		while(!openedList.isEmpty())
		{
			if(contains(closedList, c2) != null) return contains(closedList, c2);
			Node current = getCurrent(openedList);
			openedList.remove(current);
			closedList.add(current);
			
			Cell currentCell = game.getCell(current.getCoordinate());
			if(!currentCell.getObject().getPosition().compare(currentCell.getPosition()))
			{
				currentCell = currentCell.getObject();
				Node teleporter = new Node(currentCell.getPosition());
				teleporter.setG(current.getG());
				teleporter.setH(manhattan(teleporter.getCoordinate(), c2));
				teleporter.setF(teleporter.getG() + teleporter.getH());
				teleporter.setFather(current);
				current = teleporter;
			}
			
			List<Node> neighbors = getNeighbors(game, current, c2);
			for(int i = 0; i < neighbors.size(); i++)
			{
				Node neighbor = neighbors.get(i);
				if(contains(closedList, neighbor.getCoordinate()) == null)
				{
					neighbor.setG(neighbor.getFather().getG() + 10);
					neighbor.setH(manhattan(neighbor.getCoordinate(), c2));
					neighbor.setF(neighbor.getG() + neighbor.getH());
					neighbor.setFather(current);
					Node exists = contains(openedList, neighbor.getCoordinate());
					if(exists == null) openedList.add(neighbor);
					else exists = neighbor;
				}
			}
		}
		return null;
	}
	
	public static List<Coordinate> path(Game game, Coordinate c1, Coordinate c2)
	{
		Node node = findPath(game, c1, c2);
		List<Coordinate> path = new ArrayList<Coordinate>();
		for(Node n = node; n != null; n = n.getFather()) path.add(n.getCoordinate());
		Collections.reverse(path);
		return path;
	}
}

class Node
{
	private Coordinate coordinate;
	private int f;
	private int g;
	private int h;
	private Node father;
	
	protected Node(int x, int y)
	{
		coordinate = new Coordinate(x, y);
		f = g = h = 0;
		father = null;
	}
	
	protected Node(Coordinate coordinate)
	{
		this.coordinate = coordinate;
		f = g = h = 0;
		father = null;
	}
	
	protected Node(Node node)
	{
		coordinate = node.coordinate;
		f = node.f;
		g = node.g;
		h = node.h;
		father = node.father;
	}
	
	public Coordinate getCoordinate()
	{
		return coordinate;
	}
	
	public int getF()
	{
		return f;
	}
	
	public int getG()
	{
		return g;
	}
	
	public int getH()
	{
		return h;
	}
	
	public Node getFather()
	{
		return father;
	}
	
	public void setF(int f)
	{
		this.f = f;
		return;
	}
	
	public void setG(int g)
	{
		this.g = g;
		return;
	}
	
	public void setH(int h)
	{
		this.h = h;
		return;
	}
	
	public void setFather(Node father)
	{
		this.father = father;
		return;
	}
}