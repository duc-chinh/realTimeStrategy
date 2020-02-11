package realTimeStrategy.mapObjects;

import java.awt.Color;

public class House extends Cell implements Uncrossable
{

	public House(int x, int y, Color color)
	{
		setPosition(new Coordinate(x, y));
		setColor(color);
	}
	
	@Override
	public int getValue()
	{
		return -1;
	}

	@Override
	public void setValue(int value)
	{
		return;
	}
	
	@Override
	public Cell getObject()
	{
		return null;
	}
}
