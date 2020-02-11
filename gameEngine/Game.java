package realTimeStrategy.gameEngine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import realTimeStrategy.gameUserInterface.RtsEvent;
import realTimeStrategy.gameUserInterface.RtsObserver;
import realTimeStrategy.gameUserInterface.RtsEvent.ChangeType;
import realTimeStrategy.mapObjects.Cell;
import realTimeStrategy.mapObjects.Coordinate;
import realTimeStrategy.mapObjects.Crossable;
import realTimeStrategy.mapObjects.Gold;
import realTimeStrategy.mapObjects.House;
import realTimeStrategy.mapObjects.Mud;
import realTimeStrategy.mapObjects.Normal;
import realTimeStrategy.mapObjects.Teleporter;
import realTimeStrategy.mapObjects.Tree;
import realTimeStrategy.mapObjects.Uncrossable;
import realTimeStrategy.mapObjects.unitObject.Unit;

public class Game
{
	private Cell[][] map;
	private Player playerOne;
	private Player playerTwo;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public static final int SCALE = 10;
	private int goldTotal;
	private List<RtsObserver> observers;
	
	public Game()
	{
		map = new Cell[WIDTH][HEIGHT];
		for(int i = 0; i < WIDTH; i++)
		{
			for(int j = 0; j < HEIGHT; j++)
				map[i][j] = new Normal(i, j);
		}
		playerOne = new Player(Color.RED, Color.PINK, new Coordinate(0, 0));
		playerTwo = new Player(Color.BLUE, Color.CYAN, new Coordinate(WIDTH - SCALE, HEIGHT - SCALE));
		goldTotal = 0;
		observers = new ArrayList<RtsObserver>();
	}
	
	public void register(RtsObserver observer)
	{
		observers.add(observer);
		return;
	}
	
	public void unregister(RtsObserver observer)
	{
		observers.remove(observer);
		return;
	}
	
	public void notifyObserver(List<RtsEvent> events)
	{
		for(RtsObserver observer: observers)
			observer.notify(events);
		return;
	}
	
	public Cell getCell(int x, int y)
	{
		return map[x][y];
	}
	
	public Cell getCell(Coordinate c)
	{
		return map[c.getX()][c.getY()];
	}
	
	public Player getPlayerOne()
	{
		return playerOne;
	}
	
	public Player getPlayerTwo()
	{
		return playerTwo;
	}
	
	public int getGoldTotal()
	{
		return goldTotal;
	}
	
	public void setGoldTotal(int goldTotal)
	{
		this.goldTotal = goldTotal;
		return;
	}
	
	public void movePosition(Player player, int x, int y)
	{
		if((player.getPosition().getX() != 0 || x != -1) && (player.getPosition().getX() != WIDTH - SCALE || x != 1))
			player.getPosition().setX(player.getPosition().getX() + x);
		if((player.getPosition().getY() != 0 || y != -1) && (player.getPosition().getY() != HEIGHT - SCALE || y != 1))
			player.getPosition().setY(player.getPosition().getY() + y);
		List<RtsEvent> events = new ArrayList<RtsEvent>();
		events.add(new RtsEvent(player.getPosition(), ChangeType.MOVEMENT));
		notifyObserver(events);
		return;
	}
	
	public void setNormal(int x, int y)
	{
		if((x >= 0 && x < WIDTH) && (y >= 0 && y < HEIGHT))
			map[x][y] = new Normal(x, y);
		return;
	}
	
	public void setTree(int x, int y)
	{
		if((x >= 0 && x < WIDTH) && (y >= 0 && y < HEIGHT))
			map[x][y] = new Tree(x, y);
		return;
	}
	
	public void setGold(int x, int y, int quantity)
	{
		if((x >= 0 && x < WIDTH) && (y >= 0 && y < HEIGHT))
		{
			map[x][y] = new Gold(x, y, quantity);
			goldTotal += quantity;
		}
		return;
	}
	
	public void setHouse(int x, int y, Player player)
	{
		if(player.getHouse() == null && (x >= 0 && x < WIDTH) && (y >= 0 && y < HEIGHT))
		{
			for(int i = 0; i < 2; i++)
			{
				for(int j = 0; j < 2; j++)
				{
					map[x + i][y + j] = new House(x + i, y + j, player.getColor());
				}
			}
			player.setHouse((House) map[x][y]);
		}
		return;
	}
	
	public void setTeleporter(int x1, int y1, int x2, int y2)
	{
		if((x1 >= 0 && x1 < WIDTH) && (y1 >= 0 && y1 < HEIGHT)
				&& (x2 >= 0 && x2 < WIDTH) && (y2 >= 0 && y2 < HEIGHT))
		{
			Teleporter gate1 = new Teleporter(x1, y1, null);
			Teleporter gate2 = new Teleporter(x2, y2, gate1);
			gate1.setGate(gate2);
			((Normal) map[x1][y1]).add(0, gate1);
			((Normal) map[x2][y2]).add(0, gate2);
		}
		return;
	}
	
	public void setMud(int x, int y)
	{
		if((x >= 0 && x < WIDTH) && (y >= 0 && y < HEIGHT))
		{
			((Normal) map[x][y]).add(0, new Mud(x, y));
		}
		return;
	}
	
	public void spawn(Player player)
	{
		int xMin = player.getHouse().getPosition().getX() - 1;
		int xMax = player.getHouse().getPosition().getX() + 3;
		int x = (int) (Math.random() * (xMax - xMin)) + xMin;
		int y;
		if(x > xMin && x < xMax - 1)
		{
			y = (int) (Math.random() * 2);
			if(y == 0) y = player.getHouse().getPosition().getY() - 1;
			else y = player.getHouse().getPosition().getY() + 2;
		}
		else
		{
			int yMin = player.getHouse().getPosition().getY() - 1;
			int yMax = player.getHouse().getPosition().getY() + 3;
			y = (int) (Math.random() * (yMax - yMin)) + yMin;
		}
		Unit unit = new Unit(player, x, y);
		((Normal) map[x][y]).add(0, unit);
		player.addUnit(unit);
		List<RtsEvent> events = new ArrayList<RtsEvent>();
		events.add(new RtsEvent(new Coordinate(x, y), ChangeType.ACTION));
		notifyObserver(events);
		return;
	}
	
	public void selection(Player player, int x, int y)
	{
		Unit selectedUnit = player.getSelectedUnit();
		if(selectedUnit == null)
		{
			if(map[x][y].getObject() != null)
			{
				Normal normal = (Normal) map[x][y];
				if(!normal.getFirstObject().equals(normal) && player.contains((Crossable) normal.getFirstObject()))
					((Crossable) ((Normal) map[x][y]).getFirstObject()).setSelected(true);
			}
		}
		else
		{
			if(map[x][y].getObject() != null)
			{
				Normal normal = (Normal) map[x][y];
				if(selectedUnit.equals(normal.getFirstObject()) && player.contains(selectedUnit))
					((Crossable) ((Normal) map[x][y]).getFirstObject()).setSelected(false);
				else selectedUnit.getRole().selection(this, selectedUnit, x, y);
			}
			else if(((Uncrossable) map[x][y]).getValue() != -1)
				selectedUnit.getRole().selection(this, selectedUnit, x, y);
		}
		return;
	}
	
	public void step(Unit unit)
	{
		List<RtsEvent> events = new ArrayList<RtsEvent>();
		events.add(new RtsEvent(unit.getPosition(), ChangeType.MOVEMENT));
		((Normal) map[unit.getPosition().getX()][unit.getPosition().getY()]).remove(unit);
		unit.getRole().move(unit);
		((Normal) map[unit.getPosition().getX()][unit.getPosition().getY()]).add(0, unit);
		events.add(new RtsEvent(unit.getPosition(), ChangeType.MOVEMENT));
		notifyObserver(events);
		return;
	}
}
