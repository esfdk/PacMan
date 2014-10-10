package pacman.entries.jmelPacMan.BTPacMan.PacManBehaviours;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pacman.entries.jmelPacMan.BT.*;
import pacman.entries.jmelPacMan.BTPacMan.PacManContext;
import pacman.game.Constants.*;
import pacman.game.Game;

/**
 * Attempts to find the best path away from nearby ghosts.
 * @author Jakob Melnyk (jmel)
 */
public class Flee extends PacManAction
{
	private int maxIterationsForPillSearch = 100;
	
	/**
	 * Instantiates a new instance of the Flee class.
	 */
	public Flee()
	{
	}

	/**
	 * @return SUCCESS if a ghost is nearby and PacMan is going to flee; FAILURE if not.
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#run(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		this.context = (PacManContext) context;

		boolean shouldFlee = false;
		int ghostDistance;
		STATUS result = STATUS.FAILURE;

		Game g = this.context.game;

		// Figure out if PacMan should flee.
		for (GHOST ghost : GHOST.values())
		{
			// If ghost is not edible and not in lair, find distance to it.
			if (g.getGhostEdibleTime(ghost) == 0 && g.getGhostLairTime(ghost) == 0)
			{
				ghostDistance = g.getShortestPathDistance(this.context.currentPacManIndex, g.getGhostCurrentNodeIndex(ghost));

				// If ghost is within minimum distance, ghost should flee.
				if (ghostDistance < this.context.MINIMUM_GHOST_DISTANCE)
				{
					shouldFlee = true;
					result = STATUS.SUCCESS;
					break;
				}
			}
		}

		// If PacMan shouldn't flee, stop searching.
		if (!shouldFlee)
		{
			return result;
		}

		MOVE[] possibleMoves = g.getPossibleMoves(this.context.currentPacManIndex);
		Double[] possibleMovesCount = new Double[possibleMoves.length];

		Arrays.fill(possibleMovesCount, 0.0);

		// Find move most likely to result in a successful escape.
		for (GHOST ghost : GHOST.values())
		{
			if (g.getGhostEdibleTime(ghost) == 0 && g.getGhostLairTime(ghost) == 0)
			{
				// Get shortest distance to ghost.
				ghostDistance = g.getShortestPathDistance(this.context.currentPacManIndex, g.getGhostCurrentNodeIndex(ghost));

				// If ghost is within search range
				if (ghostDistance < this.context.FLEE_SEARCH_RANGE)
				{
					MOVE moveToGhost = g.getNextMoveTowardsTarget(this.context.currentPacManIndex,
							g.getGhostCurrentNodeIndex(ghost), DM.PATH);

					// Increment the movement options that is not the move to this ghost.
					for (int move = 0; move < possibleMoves.length; move++)
					{
						if (possibleMoves[move] != moveToGhost)
						{
							possibleMovesCount[move] += this.context.FLEE_SEARCH_RANGE - ghostDistance;
						}
					}
				}
			}
		}

		List<MOVE> bestMoves = new ArrayList<MOVE>();
		Double bestMoveValue = 0.0;

		// Find the move with the highest success prediction.
		for (int move = 0; move < possibleMovesCount.length; move++)
		{
			if (possibleMovesCount[move] > bestMoveValue)
			{
				bestMoveValue = possibleMovesCount[move];
			}
		}

		// Add the moves that share the highest success prediction.
		for (int move = 0; move < possibleMoves.length; move++)
		{
			if (possibleMovesCount[move].equals(bestMoveValue))
			{
				bestMoves.add(possibleMoves[move]);
			}
		}

		// If there is only a single best move, choose that one.
		if (bestMoves.size() == 1)
		{
			this.context.nextMove = bestMoves.get(0);
		}
		// else find the move towards the closest pill where a ghost is not in the way.
		else
		{
			// Convert array of pills to list to make it more convenient to remove pills.
			List<Integer> pillList = new ArrayList<Integer>();
			int[] pills = g.getActivePillsIndices();
			for (int p : pills)
			{
				pillList.add(p);
			}
			
			// Iterate over pill list until a move is found or max iterations are reached
			for(int iterations = 0; iterations < maxIterationsForPillSearch; iterations++)
			{
				// Convert list to array for use with getClosestNodeIndexFromNodeIndex
				pills = new int[pillList.size()];
				for (int p = 0; p < pillList.size(); p++)
				{
					pills[p] = pillList.get(p);
				}
				
				// Find move leading to the closest pill.
				int closestPillIndex = g.getClosestNodeIndexFromNodeIndex(this.context.currentPacManIndex, pills, DM.PATH);
				MOVE moveToClosestPill = g.getNextMoveTowardsTarget(this.context.currentPacManIndex, closestPillIndex, DM.PATH);
				
				// If move is amongst best moves, stop searching and choose that move.
				if(bestMoves.contains(moveToClosestPill))
				{
					this.context.nextMove = moveToClosestPill;
					break;
				}
				
				// If found move is not amongst best moves, remove the pill from list and reiterate.
				pillList.remove((Integer) closestPillIndex); 
			}
		}

		return result;
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
