package realTimeStrategy.mapObjects.unitObject;

import realTimeStrategy.gameEngine.Game;

public class Jobless implements UnitState
{
	@Override
	public void move(Unit unit)
	{
		return;
	}
	
	@Override
	public void selection(Game game, Unit unit, int x, int y)
	{
		return;
	}

	@Override
	public void action(Game game, Unit unit)
	{
		return;
	}
}
