package realTimeStrategy.mapObjects;

import java.awt.Color;

public class Gold extends Cell implements Uncrossable
{
	private int pieces;
	
	public Gold(int x, int y, int pieces)
	{
		setPosition(new Coordinate(x, y));
		setColor(Color.YELLOW);
		this.pieces = pieces;
	}

	@Override
	public int getValue()
	{
		return pieces;
	}

	@Override
	public void setValue(int value)
	{
		pieces = value;
		return;
	}
	
	@Override
	public Cell getObject()
	{
		return null;
	}
}
