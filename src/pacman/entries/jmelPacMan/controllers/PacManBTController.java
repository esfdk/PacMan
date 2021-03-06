package pacman.entries.jmelPacMan.controllers;

import pacman.controllers.Controller;
import pacman.entries.jmelPacMan.BT.*;
import pacman.entries.jmelPacMan.BTPacMan.PacManContext;
import pacman.entries.jmelPacMan.BTPacMan.PacManBehaviours.*;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * A PacMan Controller using a behaviour tree.
 * @author Jakob Melnyk (jmel)
 */
public class PacManBTController extends Controller<MOVE>
{
	/**
	 * The root behaviour of the behaviour tree. 
	 */
	protected RootBehaviour treeRoot;

	/**
	 * The context used in the behaviour tree.
	 */
	protected PacManContext context;
	
	/**
	 * Instantiates a new instance of the jmelBTPacManController class.
	 */
	public PacManBTController()
	{
		this.context = new PacManContext();
		createBehaviourTree();
	}
	
	/**
	 * Instantiates a new instance of the jmelBTPacManController class.
	 * @param pmc The PacManContext used for the controller.
	 */
	public PacManBTController(PacManContext pmc)
	{
		this.context = pmc;
		createBehaviourTree();
	}

	/**
	 * Creates the behaviour tree.
	 */
	protected void createBehaviourTree()
	{
		Selector root = new Selector();
		
		Selector ghostBehaviour = new Selector();
		ghostBehaviour.addBehaviour(new Flee());
		ghostBehaviour.addBehaviour(new EatGhost());
		
		Sequence pillBehaviour = new Sequence();
		pillBehaviour.addBehaviour(new FindPossibleTargets());
		pillBehaviour.addBehaviour(new FindPathToTarget());
		
		root.addBehaviour(ghostBehaviour);
		root.addBehaviour(pillBehaviour);
		
		treeRoot = new RootBehaviour(this, context, root);
	}

	/*
	 * (non-Javadoc)
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
