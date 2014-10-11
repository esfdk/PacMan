package pacman.entries.jmelPacMan.MCTSPacMan;

import pacman.controllers.Controller;
import pacman.controllers.examples.Legacy2TheReckoning;
import pacman.entries.jmelPacMan.MCTS.MCTS;
import pacman.entries.jmelPacMan.MCTS.TreeNode;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * A controller for Ms PacMan that utilises Monte Carlo Tree Search to make decisions.
 * @author Jakob Melnyk (jmel)
 */
public class MCTSPacMan extends Controller<MOVE>
{
	MCTS mcts;

	/**
	 * Instantiates a new instance of the MCTSPacMan class.
	 */
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
			TreeNode tn = mcts.search(game, timeDue, -1, new Legacy2TheReckoning());
			MOVE move = tn == null ? MOVE.NEUTRAL : tn.gameState.getPacmanLastMoveMade();
			return move;
		}

		MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		return possibleMoves[0];
	}
}