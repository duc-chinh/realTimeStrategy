package realTimeStrategy.gameUserInterface;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import realTimeStrategy.gameEngine.Game;
import realTimeStrategy.gameEngine.Player;
import realTimeStrategy.gameUserInterface.RtsEvent.ChangeType;
import realTimeStrategy.mapObjects.unitObject.Attacker;

public class AttackerAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	private Game game;
	private Player player;
	public AttackerAction(String str, Game game, Player player)
	{
		super(str);
		this.game = game;
		this.player = player;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if(player.getGoldPieces() >= 30 && player.getSelectedUnit() != null)
		{
			List<RtsEvent> events = new ArrayList<RtsEvent>();
			player.getSelectedUnit().setRole(new Attacker());
			events.add(new RtsEvent(player.getSelectedUnit().getPosition(), ChangeType.ACTION));
			game.notifyObserver(events);
		}
		return;
	}
}
