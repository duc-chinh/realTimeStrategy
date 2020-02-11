package realTimeStrategy.mapObjects.unitObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import realTimeStrategy.gameEngine.AStar;
import realTimeStrategy.gameEngine.Game;
import realTimeStrategy.gameUserInterface.RtsEvent;
import realTimeStrategy.gameUserInterface.RtsEvent.ChangeType;
import realTimeStrategy.mapObjects.Coordinate;
import realTimeStrategy.mapObjects.Gold;
import realTimeStrategy.mapObjects.Uncrossable;

public class Miner implements UnitState
{
	private int goldCarried;
	private Coordinate goldCoordinate;
	
	public Miner()
	{
		goldCarried = 0;
		goldCoordinate = null;
	}
	
	private Coordinate goldPath(Unit unit, int x, int y)
	{
		if(y - unit.getPosition().getY() > 0) return new Coordinate(x, y - 1);
		else return new Coordinate(x, y + 1);
	}
	
	@Override
	public void selection(Game game, Unit unit, int x, int y)
	{
		if(game.getCell(new Coordinate(x, y)).getObject() == null 
				&& ((Uncrossable) game.getCell(new Coordinate(x, y))).getValue() != -1
				&& unit.getDestination() == null)
		{
			goldCoordinate = new Coordinate(x, y);
			unit.setDestination(goldPath(unit, x, y));
			unit.setPath(AStar.path(game, unit.getPosition(), unit.getDestination()));
			unit.move(game);
		}
		return;
	}
	
	@Override
	public void move(Unit unit)
	{
		if(goldCoordinate != null) unit.getPath().add(unit.getPath().get(0));
		unit.setPosition(unit.getPath().remove(0));
		if(unit.getDestination() != null && unit.getPosition().compare(unit.getDestination()))
		{
			unit.setDestination(unit.getPath().get(0));
			List<Coordinate> newPath = new ArrayList<Coordinate>(unit.getPath());
			Collections.reverse(newPath);
			unit.setPath(newPath);
		}
		return;
	}

	@Override
	public void action(Game game, Unit unit)
	{
		if(unit.getDestination() != null && unit.getPath().get(unit.getPath().size() - 1).compare(unit.getDestination()))
		{
			List<RtsEvent> events = new ArrayList<RtsEvent>();
			if(game.getCell(goldCoordinate).getObject() == null && goldCarried == 0)
			{
				goldCarried = 10;
				Gold gold = (Gold) game.getCell(goldCoordinate);
				gold.setValue(gold.getValue() - goldCarried);
				game.setGoldTotal(game.getGoldTotal() - goldCarried);
				if(gold.getValue() == 0)
				{
					game.setNormal(goldCoordinate.getX(), goldCoordinate.getY());
					goldCoordinate = null;
					unit.setDestination(null);
				}
				events.add(new RtsEvent(goldCoordinate, ChangeType.ACTION));
				if(game.getGoldTotal() == 0) events.add(new RtsEvent(null, ChangeType.END));
			}
			else
			{
				unit.getMaster().setGoldPieces(unit.getMaster().getGoldPieces() + goldCarried);
				goldCarried = 0;
				events.add(new RtsEvent(unit.getPosition(), ChangeType.ACTION));
				if(game.getCell(goldCoordinate).getObject() != null)
				{
					goldCoordinate = null;
					unit.setDestination(null);
				}
			}
			game.notifyObserver(events);
		}
		return;
	}
}
