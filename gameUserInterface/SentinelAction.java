package realTimeStrategy.gameUserInterface;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import realTimeStrategy.gameEngine.Game;
import realTimeStrategy.gameEngine.Player;
import realTimeStrategy.gameUserInterface.RtsEvent.ChangeType;
import realTimeStrategy.mapObjects.unitObject.Sentinel;

public class SentinelAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	private Game game;
	private Player player;
	public SentinelAction(String str, Game game, Player player)
	{
		super(str);
		this.game = game;
		this.player = player;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if(player.getGoldPieces() >= 20 && player.getSelectedUnit() != null)
		{
			List<RtsEvent> events = new ArrayList<RtsEvent>();
			player.getSelectedUnit().setRole(new Sentinel());
			events.add(new RtsEvent(player.getSelectedUnit().getPosition(), ChangeType.ACTION));
			game.notifyObserver(events);
		}
		return;
	}
}
