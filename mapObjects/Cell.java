package realTimeStrategy.mapObjects;

import java.awt.Color;

public abstract class Cell
{
	private Coordinate position;
	private Color color;
	
	public Coordinate getPosition()
	{
		return position;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void setPosition(Coordinate position)
	{
		this.position = position;
		return;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
		return;
	}
	
	public Cell getObject()
	{
		return this;
	}
}
