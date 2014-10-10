package pacman.entries.jmelPacMan.BTPacMan.PacManBehaviours;

import java.util.ArrayList;

import pacman.entries.jmelPacMan.BT.Context;
import pacman.entries.jmelPacMan.BT.STATUS;
import pacman.entries.jmelPacMan.BTPacMan.PacManContext;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

/**
 * Action for finding PacMan's possible targets.
 * 
 * @author Jakob Melnyk
 */
public class FindPossibleTargets extends PacManAction
{
	/**
	 * Instantiates a new instance of the FindPossibleTargets class.
	 */
	public FindPossibleTargets()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#run(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		this.context = (PacManContext) context;
		Game g = this.context.game;

		ArrayList<Integer> targets = new ArrayList<Integer>();

		boolean ghostEdible = false;

		int ghostSpawnTimeTotal = 0;
		int[] ghostPositions = new int[4];
		int[] activePowerPills = this.context.activePowerPills;
		int[] activePills = this.context.activePills;

		// Check if a ghost is edible
		for (int ghost = 0; ghost < GHOST.values().length; ghost++)
		{
			GHOST currentGhost = GHOST.values()[ghost];
			ghostPositions[ghost] = g.getGhostCurrentNodeIndex(currentGhost);
			ghostSpawnTimeTotal += g.getGhostLairTime(currentGhost);
			if (g.getGhostEdibleTime(currentGhost) > 0 && g.getGhostLairTime(currentGhost) == 0)
			{
				ghostEdible = true;
			}
		}

		// If no ghosts are edible and no ghosts are in lair, add power pills to list of targets
		if (!ghostEdible && ghostSpawnTimeTotal == 0)
		{
			for (int i = 0; i < activePowerPills.length; i++)
			{
				int[] pathToPowerPill = g.getShortestPath(this.context.currentPacManIndex, activePowerPills[i]);
				if (!pathContainsAGhost(ghostPositions, pathToPowerPill))
				{
					targets.add(activePowerPills[i]);
				}
			}
		}

		// If there are no targets, add standard pills to list of targets.
		if (targets.size() == 0)
		{
			for (int i = 0; i < activePills.length; i++)
			{
				targets.add(activePills[i]);
			}
		}

		this.context.targetList = targets;
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
		this.context = (PacManContext) context;

		// Convert target list to array.
		ArrayList<Integer> newTargets = this.context.targetList;
		int numberOfNewTargets = newTargets.size();
		int[] newTargetNodeIndices = new int[numberOfNewTargets];
		for (int i = 0; i < numberOfNewTargets; i++)
		{
			newTargetNodeIndices[i] = newTargets.get(i);
		}

		// Update array of target node indices.
		this.context.targetNodeIndices = newTargetNodeIndices;
	}

	/**
	 * Checks if a ghost is currently on a specified path.
	 * 
	 * @param ghostPositions Current position of ghosts.
	 * @param path Path to check for ghosts.
	 * @return True if a ghost is on the path specified, false if not.
	 */
	private boolean pathContainsAGhost(int[] ghostPositions, int[] path)
	{
		for (int ghostPosition : ghostPositions)
		{
			for (int nodeOnPath : path)
			{
				if (ghostPosition == nodeOnPath)
				{
					return true;
				}
			}
		}

		return false;
	}
}
