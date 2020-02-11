package realTimeStrategy.mapObjects;

import java.awt.Color;

public class Mud extends Cell implements Crossable
{
	public Mud(int x, int y)
	{
		setPosition(new Coordinate(x, y));
		setColor(new Color(102, 51, 0));
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
		return 600;
	}
}
