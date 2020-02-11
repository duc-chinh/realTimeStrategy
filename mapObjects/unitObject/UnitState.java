package realTimeStrategy.mapObjects.unitObject;

import realTimeStrategy.gameEngine.Game;

public interface UnitState
{
	public void move(Unit unit);
	public void selection(Game game, Unit unit, int x, int y);
	public void action(Game game, Unit unit);
}
