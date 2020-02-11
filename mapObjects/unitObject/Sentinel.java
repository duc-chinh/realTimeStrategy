package realTimeStrategy.mapObjects.unitObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import realTimeStrategy.gameEngine.AStar;
import realTimeStrategy.gameEngine.Game;
import realTimeStrategy.mapObjects.Coordinate;
import realTimeStrategy.mapObjects.Normal;

public class Sentinel implements UnitState
{
	private Coordinate secondDestination;
	private List<Coordinate> singlePath;
	
	public Sentinel()
	{
		secondDestination = null;
		singlePath = null;
	}

	@Override
	public void move(Unit unit)
	{
		if(!singlePath.isEmpty() && singlePath.get(0).compare(unit.getPath().get(0)))
		{
			unit.setPosition(unit.getPath().remove(0));
			singlePath.remove(0);	
		}
		else
		{
			unit.getPath().add(unit.getPath().get(0));
			unit.setPosition(unit.getPath().remove(0));
			if(unit.getPosition().compare(secondDestination))
			{
				unit.setDestination(unit.getPath().get(unit.getPath().size() - 1));
				secondDestination = unit.getPath().get(0);
				List<Coordinate> newPath = new ArrayList<Coordinate>(unit.getPath());
				Collections.reverse(newPath);
				unit.setPath(newPath);
			}
		}
		return;
	}
	
	@Override
	public void selection(Game game, Unit unit, int x, int y)
	{
		if(game.getCell(new Coordinate(x, y)).getObject() != null && unit.getDestination() == null)
		{
			unit.setDestination(new Coordinate(x, y));
			singlePath = AStar.path(game, unit.getPosition(), unit.getDestination());
			singlePath.remove(singlePath.size() - 1);
			unit.setPath(singlePath);
		}
		else if(game.getCell(new Coordinate(x, y)).getObject() != null && secondDestination == null)
		{
			secondDestination = new Coordinate(x, y);
			List<Coordinate> newPath = new ArrayList<Coordinate>(singlePath);
			newPath.addAll(AStar.path(game, unit.getDestination(), secondDestination));
			unit.setPath(newPath);
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
		Unit enemy = null;
		while((enemy = findEnemy(game, unit)) != null)
		{
			enemy.attacked();
			try
			{
				Thread.sleep(300);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return;
	}
}
