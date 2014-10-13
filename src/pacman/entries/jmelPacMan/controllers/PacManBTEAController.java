package pacman.entries.jmelPacMan.controllers;

import pacman.entries.jmelPacMan.BT.*;
import pacman.entries.jmelPacMan.BTPacMan.PacManContext;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * A PacMan Controller using a behaviour tree with parameters evolved using an evolutionary algorithm.
 * 
 * @author Jakob Melnyk (jmel)
 */
public class PacManBTEAController extends PacManBTController
{
	/**
	 * Instantiates a new instance of the jmelBTEAPacManController class. Uses the parameters from the best of the candidates
	 * during experimentation.
	 */
	public PacManBTEAController()
	{
		this.context = new PacManContext(5, 36, 155);
		createBehaviourTree();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game, long timeDue)
	{
		this.context.setVariables(game);

		STATUS s = treeRoot.run(context);

		if (s != STATUS.SUCCESS)
			return MOVE.NEUTRAL;
		else
			return context.nextMove;
	}
}
