package realTimeStrategy.gameUserInterface;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import realTimeStrategy.gameEngine.Game;
import realTimeStrategy.gameEngine.Player;

public class UnitAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	private Game game;
	private Player player;
	public UnitAction(String str, Game game, Player player)
	{
		super(str);
		this.game = game;
		this.player = player;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if(player.getGoldPieces() >= 50)
		{
			player.setGoldPieces(player.getGoldPieces() - 50);
			game.spawn(player);
		}
		return;
	}
}
