package realTimeStrategy.gameEngine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import realTimeStrategy.mapObjects.Coordinate;
import realTimeStrategy.mapObjects.Crossable;
import realTimeStrategy.mapObjects.House;
import realTimeStrategy.mapObjects.unitObject.Unit;

public class Player
{
	private Color[] colors;
	private int goldPieces;
	private List<Unit> army;
	private House house;
	private Coordinate position;
	public Player(Color color1, Color color2, Coordinate position)
	{
		colors = new Color[2];
		colors[0] = color1;
		colors[1] = color2;
		goldPieces = 0;
		army = new ArrayList<Unit>();
		house = null;
		this.position = position;
	}
	
	public Color getColor()
	{
		return colors[0];
	}
	
	public Color getSelectedColor()
	{
		return colors[1];
	}
	
	public int getGoldPieces()
	{
		return goldPieces;
	}
	
	public List<Unit> getArmy()
	{
		return army;
	}
	
	public House getHouse()
	{
		return house;
	}
	
	public Coordinate getPosition()
	{
		return position;
	}
	
	public void setGoldPieces(int goldPieces)
	{
		this.goldPieces = goldPieces;
		return;
	}
	
	public void addUnit(Unit unit)
	{
		army.add(unit);
		return;
	}
	
	public boolean contains(Crossable object)
	{
		return army.contains(object);
	}
	
	public Unit getSelectedUnit()
	{
		for(int i = 0; i < army.size(); i++)
		{
			if(army.get(i).isSelected()) return army.get(i);
		}
		return null;
	}
	
	public void setHouse(House house)
	{
		this.house = house;
		return;
	}
	
	public void setPosition(Coordinate position)
	{
		this.position = position;
		return;
	}
}
