package pacman.entries.jmelPacMan.BTPacMan.PacManBehaviours;

import pacman.entries.jmelPacMan.BT.*;
import pacman.entries.jmelPacMan.BTPacMan.PacManContext;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

/**
 * Attempts to find an edible ghost and chase it down.
 * @author Jakob Melnyk (jmel)
 */
public class EatGhost extends PacManAction
{
	/**
	 * Instantiates a new instance of the EatGhost class.
	 */
	public EatGhost()
	{
	}

	/**
	 * @return SUCCESS if an edible ghost is nearby, FAILURE if not.
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#run(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		this.context = (PacManContext) context;

		STATUS result = STATUS.FAILURE;
		GHOST target = null;
		int ghostDistance, closestGhostDistance = Integer.MAX_VALUE;

		Game g = this.context.game;

		// Check for edible ghosts within acceptable distance and pick the closest, if any.
		for (GHOST ghost : GHOST.values())
		{
			if (g.getGhostEdibleTime(ghost) > 0)
			{
				ghostDistance = g.getShortestPathDistance(this.context.currentPacManIndex, g.getGhostCurrentNodeIndex(ghost));

				if (ghostDistance < this.context.EAT_GHOST_DISTANCE && ghostDistance < closestGhostDistance)
				{
					closestGhostDistance = ghostDistance;
					target = ghost;
				}
			}
		}
		
		// Find move towards target ghost, if any are edible.
		if(target != null)
		{
			this.context.nextMove = g.getNextMoveTowardsTarget(this.context.currentPacManIndex, g.getGhostCurrentNodeIndex(target), DM.PATH);
			result = STATUS.SUCCESS;
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see pacman.entries.jmelPacMan.jmelPacManBT.BT.Behaviour#start(pacman.entries.jmelPacMan.jmelPacManBT.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
		this.context = (PacManContext) context;
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
