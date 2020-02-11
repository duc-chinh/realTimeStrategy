package realTimeStrategy.mapObjects;

public class Coordinate
{
	private int x;
	private int y;
	
	public Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Coordinate(Coordinate c)
	{
		x = c.x;
		y = c.y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int x)
	{
		this.x = x;
		return;
	}
	
	public void setY(int y)
	{
		this.y = y;
		return;
	}
	
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
	
	public boolean compare(Coordinate coordinate)
	{
		if(x == coordinate.x && y == coordinate.y) return true;
		return false;
	}
}
