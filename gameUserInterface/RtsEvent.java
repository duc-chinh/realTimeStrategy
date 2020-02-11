package realTimeStrategy.gameUserInterface;

import realTimeStrategy.mapObjects.Coordinate;

public class RtsEvent
{
	public static enum ChangeType
	{
		MOVEMENT, ACTION, END
	}
	
	private Coordinate coordinate;
	private ChangeType changeType;
	
	public RtsEvent(Coordinate coordinate, ChangeType changeType)
	{
		this.coordinate = coordinate;
		this.changeType = changeType;
	}
	
	public Coordinate getCoordinate()
	{
		return coordinate;
	}
	
	public ChangeType getChangeType()
	{
		return changeType;
	}
}
