package pacman.entries.jmelPacMan.MCTSPacMan;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map.Entry;

import pacman.controllers.Controller;
import pacman.entries.jmelPacMan.MCTS.MCTS;
import pacman.entries.jmelPacMan.MCTS.TreeNode;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class MCTSPacMan extends Controller<MOVE>
{
	public static HashMap<Integer, Integer> nodesVisited = new HashMap<Integer, Integer>();

	MCTS mcts;

	public MCTSPacMan()
	{
		mcts = new MCTS();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game, long timeDue)
	{
		if (MCTS.pacManAtJunction(game))
		{
			nodesVisited = new HashMap<Integer, Integer>();
			TreeNode tn = mcts.search(game, 1000);
			MOVE move = tn == null ? MOVE.NEUTRAL : tn.getMoveTo();
			return move;
		}

		for (Entry<Integer, Integer> e : nodesVisited.entrySet())
		{
			int visits = (int) e.getValue();
			Color c = Color.red;
			if (visits < 6)
			{
				c = Color.red;
			}
			else if (visits < 12)
			{
				c = Color.yellow;
			}
			else
			{
				c = Color.GREEN;
			}

//			GameView.addPoints(MCTS.g, c, (int) e.getKey());
		}

		  MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		  return possibleMoves[0];
	}
}