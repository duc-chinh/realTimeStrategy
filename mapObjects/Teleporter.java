package realTimeStrategy.mapObjects;

import java.awt.Color;

public class Teleporter extends Cell implements Crossable
{
	private Teleporter gate;
	
	public Teleporter(int x, int y, Teleporter gate)
	{
		setPosition(new Coordinate(x, y));
		setColor(Color.WHITE);
		this.gate = gate;
	}
	
	public Teleporter getGate()
	{
		return gate;
	}
	
	public void setGate(Teleporter gate)
	{
		this.gate = gate;
		return;
	}
	
	@Override
	public Cell getObject()
	{
		return (Cell) gate;
	}
	
	@Override
	public void setSelected(boolean selected)
	{
		return;
	}
	
	@Override
	public Boolean isSelected()
	{
		return null;
	}
	
	@Override
	public int getSpeed()
	{
		return 300;
	}
}
