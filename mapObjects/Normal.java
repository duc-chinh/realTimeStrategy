package realTimeStrategy.mapObjects;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Normal extends Cell
{
	List<Crossable> objects;
	
	public Normal(int x, int y)
	{
		setPosition(new Coordinate(x, y));
		setColor(Color.DARK_GRAY);
		objects = new ArrayList<Crossable>();
	}
	
	@Override
	public Cell getObject()
	{
		if(!objects.isEmpty()) return ((Cell) objects.get(objects.size() - 1)).getObject();
		else return this;
	}
	
	public Cell getFirstObject()
	{
		if(!objects.isEmpty()) return (Cell) objects.get(0);
		else return this;
	}
	
	public void add(int i, Crossable object)
	{
		objects.add(i, object);
		return;
	}
	
	public void add(Crossable object)
	{
		objects.add(object);
		return;
	}
	
	public boolean remove(Crossable object)
	{
		return objects.remove(object);
	}
	
	public int getSpeed()
	{
		if(!objects.isEmpty()) return objects.get(objects.size() - 1).getSpeed();
		else return 300;
	}
}
