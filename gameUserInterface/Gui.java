package realTimeStrategy.gameUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import realTimeStrategy.gameEngine.Game;
import realTimeStrategy.gameEngine.Player;
import realTimeStrategy.gameUserInterface.RtsEvent.ChangeType;
import realTimeStrategy.mapObjects.Cell;
import realTimeStrategy.mapObjects.Normal;
import realTimeStrategy.mapObjects.Uncrossable;
import realTimeStrategy.mapObjects.unitObject.Unit;

public class Gui implements RtsObserver
{
	private Game game;
	private Player player;
	private final int width = 500;
	private final int height = 500; 
	private JFrame frame = new JFrame("Real Time Strategy");
	private JComponent component = new JComponent()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			component.setBackground(Color.BLACK);
			drawMap(g);
			drawLines(g);
			showInformation(g);
			component.requestFocus();
			component.requestFocusInWindow();
			return;
		}
	};
	
	public Gui(Game game, Player player)
	{
		this.game = game;
		this.player = player;
		frame.setContentPane(component);
		frame.setSize(514, 637);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if(player.equals(game.getPlayerTwo()))
		{
			frame.setLocation(500, 0);
		}
		Box buttons = Box.createHorizontalBox();
		buttons.add(new JButton(new UnitAction("NEW UNIT", game, player)));
		buttons.add(new JButton(new MinerAction("MINER", game, player)));
		buttons.add(new JButton(new SentinelAction("SENTINEL", game, player)));
		buttons.add(new JButton(new AttackerAction("ATTACKER", game, player)));
		component.setLayout(new BorderLayout());
		component.add(buttons, BorderLayout.SOUTH);
		frame.setVisible(true);
		component.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent event)
			{
				int keyCode = event.getKeyCode();
				if(keyCode == KeyEvent.VK_UP) game.movePosition(player, 0, -1);
				else if(keyCode == KeyEvent.VK_DOWN) game.movePosition(player, 0, 1);
				else if(keyCode == KeyEvent.VK_LEFT) game.movePosition(player, -1, 0);
				else if(keyCode == KeyEvent.VK_RIGHT) game.movePosition(player, 1, 0);
				component.repaint();
				return;
			}
		});
		component.setFocusable(true);
		component.requestFocus();
		component.requestFocusInWindow();
		component.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent event)
			{
				int x = (event.getX() / (width / Game.SCALE)) + player.getPosition().getX();
				int y = (event.getY() / (height / Game.SCALE)) + player.getPosition().getY();
				game.selection(player, x, y);
				component.repaint();
				return;
			}
		});
	}
	
	private void drawLines(Graphics g)
	{
		g.setColor(Color.BLACK);
		for(int i = 1; i < Game.SCALE; i++)
		{
			g.drawLine(width * i / Game.SCALE, 0, width * i / Game.SCALE, height); // vertical
			g.drawLine(0, height * i / Game.SCALE, width, height * i / Game.SCALE); // horizontal
		}
		return;
	}
	
	private void drawMap(Graphics g)
	{
		int x = player.getPosition().getX();
		int y = player.getPosition().getY();
		g.setFont(new Font("Helvetica", Font.BOLD, 8));
		for(int i = x; i < x + Game.SCALE; i++)
		{
			for(int j = y; j < y + Game.SCALE; j++)
			{
				Cell cell = game.getCell(i, j);
				String str = null;
				if(cell instanceof Normal)
				{
					g.setColor(((Normal) cell).getFirstObject().getColor());
					g.fillRect(width * (i - player.getPosition().getX()) / Game.SCALE,
							height * (j - player.getPosition().getY()) / Game.SCALE,
							width / Game.SCALE, height / Game.SCALE);
					if(((Normal) cell).getFirstObject() instanceof Unit)
					{
						Unit unit = (Unit) ((Normal) cell).getFirstObject();
						if(unit.getRole() == null) str = "Unit";
						else str = unit.getRole().getClass().getSimpleName();
						str += " - " + unit.getHealthPoints();
					}
				}
				else
				{
					g.setColor(cell.getColor());
					str = "" + ((Uncrossable) cell).getValue();
				}
				g.fillRect(width * (i - player.getPosition().getX()) / Game.SCALE,
						height * (j - player.getPosition().getY()) / Game.SCALE,
						width / Game.SCALE, height / Game.SCALE);
				if(str != null && !str.equals("-1"))
				{
					g.setColor(Color.BLACK);
					g.drawString(str, width * (i - player.getPosition().getX()) / Game.SCALE + 2,
							height * (j - player.getPosition().getY() + 1) / Game.SCALE - 3);
				}
			}
		}
		return;
	}
	
	private void showInformation(Graphics g)
	{
		g.setFont(new Font("Helvetica", Font.BOLD, 20));
		String playerName = null;
		if(player.getColor() == Color.RED) playerName = "PLAYER 1";
		else playerName = "PLAYER 2";
		g.drawString(playerName, 10, 520);
		g.drawString("GOLD QUANTITY: " + player.getGoldPieces(), 10, 560);
		return;
	}
	
	private void showPopup()
	{
		String str = null;
		if(game.getPlayerOne().getGoldPieces() > game.getPlayerTwo().getGoldPieces())
			str = "Player 1 wins !";
		else if(game.getPlayerOne().getGoldPieces() < game.getPlayerTwo().getGoldPieces())
			str = "Player 2 wins !";
		else str = "Draw !";
		System.out.println(str);
		System.exit(0);
		return;
	}

	@Override
	public void notify(List<RtsEvent> events)
	{
		for(RtsEvent event: events)
		{
			if(event.getChangeType() == ChangeType.END) showPopup();
		}
		component.repaint();
		return;
	}
}
