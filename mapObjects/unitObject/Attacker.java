package realTimeStrategy.mapObjects.unitObject;

import java.util.ArrayList;
import java.util.List;

import realTimeStrategy.gameEngine.AStar;
import realTimeStrategy.gameEngine.Game;
import realTimeStrategy.gameUserInterface.RtsEvent;
import realTimeStrategy.gameUserInterface.RtsEvent.ChangeType;
import realTimeStrategy.mapObjects.Coordinate;
import realTimeStrategy.mapObjects.Normal;

public class Attacker implements UnitState
{
	private Coordinate enemyCoordinate;
	
	public Attacker()
	{
		enemyCoordinate = null;
	}
	
	@Override
	public void move(Unit unit)
	{
		if(!unit.getPath().isEmpty())
		{
			unit.setPosition(unit.getPath().remove(0));
			if(unit.getPath().isEmpty()) unit.setDestination(null);
		}
		return;
	}

	@Override
	public void selection(Game game, Unit unit, int x, int y)
	{
		if(game.getCell(new Coordinate(x, y)).getObject() != null && unit.getDestination() == null)
		{
			unit.setDestination(new Coordinate(x, y));
			unit.setPath(AStar.path(game, unit.getPosition(), unit.getDestination()));
			unit.move(game);
		}
		return;
	}
	
	private Unit findEnemy(Game game, Unit unit)
	{
		for(int y = unit.getPosition().getY() - 3; y <= unit.getPosition().getY() + 3; y++)
		{
			int difference = Math.abs(y - unit.getPosition().getY());
			for(int x = unit.getPosition().getX() - 3 + difference; x <= unit.getPosition().getX() + 3 - difference; x++)
			{
				if(x > 0 && x < Game.WIDTH && y > 0 && y < Game.HEIGHT && !unit.getPosition().compare(new Coordinate(x, y))
						&& game.getCell(x, y).getObject() != null
						&& ((Normal) game.getCell(x, y)).getFirstObject() instanceof Unit)
				{
					Unit unitFound = (Unit) ((Normal) game.getCell(x, y)).getFirstObject();
					if(!unit.getMaster().getArmy().contains(unitFound)) return unitFound;
				}
			}
		}
		return null;
	}

	@Override
	public void action(Game game, Unit unit)
	{
		Unit enemy = findEnemy(game, unit);
		if(enemy != null)
		{
			List<RtsEvent> events = new ArrayList<RtsEvent>();
			enemyCoordinate = enemy.getPosition();
			unit.setPath(AStar.path(game, unit.getPosition(), enemyCoordinate));
			enemy.attacked();
			if(!enemy.isAlive())
			{
				((Normal) game.getCell(enemyCoordinate)).remove(enemy);
				enemyCoordinate = null;
				unit.setDestination(null);
				unit.setPath(null);
			}
			events.add(new RtsEvent(enemyCoordinate, ChangeType.ACTION));
			game.notifyObserver(events);
		}
		return;
	}
}
