package pacman.entries.jmelPacMan.MCTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pacman.entries.jmelPacMan.MCTSPacMan.MCTSPacMan;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class TreeNode
{
	public int index;
	public int visits;
	public TreeNode parent;
	public List<TreeNode> children;

	private Game gs;
	private MOVE moveTo;
	private Map<MOVE, Boolean> actions;

	private double pathLength;
	public double avgSPill;
	public double avgSSurvival;
	public double maxSPill;
	public double maxSSurvival;
	public double totSPill;
	public double totSSurvival;
	public double uctValue;

	public TreeNode(Game game, TreeNode parent, double pathLength)
	{
		index = game.getPacmanCurrentNodeIndex();
		this.visits = 0;
		this.parent = parent;
		this.children = new ArrayList<TreeNode>();

		this.gs = game.copy();
		this.moveTo = gs.getPacmanLastMoveMade();
		this.actions = new HashMap<MOVE, Boolean>();
		MOVE[] possibleActions = gs.getPossibleMoves(gs.getPacmanCurrentNodeIndex(), gs.getPacmanLastMoveMade());
		for (MOVE m : possibleActions)
		{
			actions.put(m, false);
		}

		this.pathLength = pathLength;
		this.avgSPill = 0.0;
		this.maxSPill = 0.0;
		this.totSPill = 0.0;
		this.avgSSurvival = 0.0;
		this.maxSSurvival = 0.0;
		this.totSSurvival = 0.0;
	}

	/**
	 * Gets the best child of the node.
	 */
	public TreeNode bestChild()
	{
		// If there are children that have not been visited at least
		// VISIT_THRESHOLD times, visit one of them.
		List<TreeNode> unvisitedChildren = new ArrayList<TreeNode>();
		for (TreeNode child : children)
			if (child.visits < MCTS.VISIT_THRESHOLD)
				unvisitedChildren.add(child);

		if (unvisitedChildren.size() > 0)
			return unvisitedChildren.get(MCTS.random.nextInt(unvisitedChildren.size()));

		// Find the best child based on UCT value.
		TreeNode bestChild = null;
		double bestValue = -10000;

		for (TreeNode child : children)
		{

			double log = Math.log(this.visits);
			double div = log / child.visits;
			double sqrt = Math.sqrt(div);
			double explorationValue = MCTS.EXPLORATION_CONSTANT * sqrt;
			child.uctValue = (child.getScore() + explorationValue);
			if (child.uctValue > bestValue)
			{
				bestChild = child;
				bestValue = child.uctValue;
			}
		}

		return bestChild;
	}

	/**
	 * Expands the node and returns the new child node created
	 */
	public TreeNode expand()
	{
		Game tempGame;
		MOVE[] untriedActions = this.getUntriedActions();
		TreeNode newNode = null;

		for (Map.Entry<MOVE, Boolean> entry : actions.entrySet())
		{
			tempGame = gs.copy();
			tempGame.advanceGame(entry.getKey(), MCTS.ghostStrategy.getMove(tempGame, MCTS.GHOST_MOVE_TIME));

			while (!MCTS.pacManAtJunction(tempGame))
			{
				if (tempGame.wasPacManEaten())
				{
					break;
				}

				tempGame.advanceGame(entry.getKey(), MCTS.ghostStrategy.getMove(tempGame, MCTS.GHOST_MOVE_TIME));
			}

			newNode = new TreeNode(tempGame, this, this.pathLength + 1);
			children.add(newNode);
		}

		return this.bestChild();
	}

	/**
	 * Gets the untried actions for this node.
	 */
	private MOVE[] getUntriedActions()
	{
		int untried = 0;
		MOVE[] untriedActions;

		for (Map.Entry<MOVE, Boolean> entry : actions.entrySet())
			if (entry.getValue() == false)
				untried++;

		untriedActions = new MOVE[untried];
		untried = 0;
		for (Map.Entry<MOVE, Boolean> entry : actions.entrySet())
		{
			if (entry.getValue() == false)
			{
				untriedActions[untried] = entry.getKey();
				untried++;
			}
		}

		return untriedActions;
	}

	/**
	 * Gets the score of the node.
	 */
	public double getScore()
	{
		if (MCTS.SURVIVAL)
		{
			return maxSSurvival;
		}
		else
		{
			if (maxSPill == 0.0)
			{
				return maxSSurvival;
			}
			return maxSSurvival * maxSPill;
		}
	}

	/**
	 * Gets the RSurvival value (see 4.4 in paper)
	 */
	public double getRewardSurvival()
	{
		if (!gs.wasPacManEaten())
		{
			return 1.0;
		}

		double min = 0;
		double max = MCTS.MAX_PATH_TO_ROOT;

		return (this.pathLength - min) / (max - min);
	}

	/**
	 * Gets the RPill value (see 4.4 in paper)
	 */
	public double getRewardPill()
	{
		if (gs.getMazeIndex() != MCTS.ROOT_MAZE_INDEX)
			return 1.0;

		double min = 0;
		double max = MCTS.PILLS_AT_ROOT;
		double eaten = MCTS.PILLS_AT_ROOT - gs.getNumberOfActivePills();

		if (min < 0)
		{
			min = 0;
		}
		if (eaten < 0)
		{
			eaten = MCTS.PILLS_AT_ROOT;
		}

		double normalized = (eaten - min) / (max - min);
		return normalized;
	}

	/**
	 * Updates the various score values (see 4.1 in paper)
	 */
	public void updateValues(double sPill, double sSurvival)
	{
		visits++;

		MCTSPacMan.nodesVisited.put(index, visits);

		totSPill += sPill;
		totSSurvival += sSurvival;

		double average = (children.size() == 0 ? 1 : children.size());

		double pillSum = 0;
		double survivalSum = 0;

		for (TreeNode c : children)
		{
			pillSum += c.maxSPill;
			survivalSum += c.maxSSurvival;
		}

		avgSPill = pillSum / average;
		avgSSurvival = survivalSum / average;

		// avgSPill = totSPill / average;
		// avgSSurvival = totSSurvival / average;

		if (sPill > maxSPill)
			maxSPill = sPill;
		if (sSurvival > maxSSurvival)
			maxSSurvival = sSurvival;
	}

	public double getPathLength()
	{
		return this.pathLength;
	}

	public MOVE getMoveTo()
	{
		return this.moveTo;
	}

	public Game getGameState()
	{
		return this.gs;
	}

	public boolean isTerminalNode()
	{
		return (gs.wasPacManEaten() || (pathLength >= MCTS.MAX_PATH_TO_ROOT) || (gs.getMazeIndex() != MCTS.ROOT_MAZE_INDEX));
	}

	public boolean isFullyExpanded()
	{
		return (children.size() == actions.size());
	}

	public boolean isLeafNode()
	{
		return (children.size() != actions.size());
	}
}