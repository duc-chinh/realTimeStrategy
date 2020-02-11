package realTimeStrategy.mapObjects;

import java.awt.Color;

public class Tree extends Cell implements Uncrossable
{
	public Tree(int x, int y)
	{
		setPosition(new Coordinate(x, y));
		setColor(Color.GREEN);
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
