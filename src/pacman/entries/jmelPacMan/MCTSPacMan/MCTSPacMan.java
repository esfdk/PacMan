package pacman.entries.jmelPacMan.MCTSPacMan;

import java.util.HashMap;
import pacman.controllers.Controller;
import pacman.entries.jmelPacMan.MCTS.MCTS;
import pacman.entries.jmelPacMan.MCTS.TreeNode;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

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

		  MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		  return possibleMoves[0];
	}
}