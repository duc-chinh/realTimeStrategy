package realTimeStrategy.mapObjects.unitObject;

import java.util.ArrayList;
import java.util.List;

import realTimeStrategy.gameEngine.Game;
import realTimeStrategy.gameEngine.Player;
import realTimeStrategy.mapObjects.Cell;
import realTimeStrategy.mapObjects.Crossable;
import realTimeStrategy.mapObjects.Coordinate;
import realTimeStrategy.mapObjects.Normal;

public class Unit extends Cell implements Crossable
{
	private Player master;
	private int healthPoints;
	private UnitState role;
	private Coordinate destination;
	private List<Coordinate> path;
	private boolean selected;
	private boolean alive;
	
	public Unit(Player master, int x, int y)
	{
		setPosition(new Coordinate(x, y));
		this.master = master;
		setColor(master.getColor());
		healthPoints = 5;
		role = new Jobless();
		destination = null;
		path = new ArrayList<Coordinate>();
		selected = false;
		alive = true;
	}
	
	public Player getMaster()
	{
		return master;
	}
	
	public int getHealthPoints()
	{
		return healthPoints;
	}
	
	public UnitState getRole()
	{
		return role;
	}
	
	public Coordinate getDestination()
	{
		return destination;
	}
	
	public void setDestination(Coordinate destination)
	{
		this.destination = destination;
		return;
	}
	
	public List<Coordinate> getPath()
	{
		return path;
	}
	
	@Override
	public Boolean isSelected()
	{
		return selected;
	}
	
	public boolean isAlive()
	{
		return alive;
	}
	
	@Override
	public int getSpeed()
	{
		return 300;
	}

	public void setRole(UnitState role)
	{
		this.role = role;
		return;
	}
	
	public void setPath(List<Coordinate> path)
	{
		this.path = path;
		return;
	}
	
	@Override
	public void setSelected(boolean selected)
	{
		if(selected) setColor(master.getSelectedColor());
		else setColor(master.getColor());
		this.selected = selected;
		return;
	}
	
	public void attacked()
	{
		healthPoints--;
		if(healthPoints == 0) dies();
		return;
	}
	
	public void dies()
	{
		alive = false;
		master.getArmy().remove(this);
		return;
	}
	
	public void action(Game game)
	{
		role.action(game, this);
		return;
	}
	
	public void move(Game game)
	{
		Unit unit = this;
		Thread thread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					while(unit.alive && !unit.getPath().isEmpty())
					{
						game.step(unit);
						unit.action(game);
						try
						{
							Thread.sleep(((Normal) game.getCell(unit.getPosition())).getSpeed());
						}
						catch(InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					if(!unit.alive) ((Normal) game.getCell(unit.getPosition())).remove(unit); 
					return;
				}
			}
		);
		thread.start();
		setSelected(false);
		return;
	}
}
