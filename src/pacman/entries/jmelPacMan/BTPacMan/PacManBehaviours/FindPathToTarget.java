package pacman.entries.jmelPacMan.BTPacMan.PacManBehaviours;

import pacman.entries.jmelPacMan.BT.*;
import pacman.entries.jmelPacMan.BTPacMan.*;
import pacman.game.Constants.DM;
import pacman.game.Game;

/**
 * Action that finds the path to closest of PacMans targets.
 * 
 * @author Jakob Melnyk
 *
 */
public class FindPathToTarget extends PacManAction
{
	/**
	 * Instantiates a new instance of the FindPathToTarget class.
	 */
	public FindPathToTarget()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pacman.entries.jmelPacMan.jmelBTPacMan.BT.Behaviour#run(pacman.entries.jmelPacMan.jmelBTPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		this.context = (PacManContext) context;

		Game g = this.context.game;

		int nearestTarget = g.getClosestNodeIndexFromNodeIndex(
						this.context.currentPacManIndex, this.context.targetNodeIndices, DM.PATH);
		this.context.nextMove = g.getNextMoveTowardsTarget(this.context.currentPacManIndex, nearestTarget, DM.PATH);
		
		return STATUS.SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pacman.entries.jmelPacMan.jmelBTPacMan.BT.Behaviour#start(pacman.entries.jmelPacMan.jmelBTPacMan.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
		// Not necessary.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pacman.entries.jmelPacMan.jmelBTPacMan.BT.Behaviour#finish(pacman.entries.jmelPacMan.jmelBTPacMan.BT.Context)
	 */
	@Override
	public void finish(Context context)
	{
		// Not necessary.
	}

}
