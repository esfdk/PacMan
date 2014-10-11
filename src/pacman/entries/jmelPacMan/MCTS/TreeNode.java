package pacman.entries.jmelPacMan.MCTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * A node used to build the Monte Carlo Tree Search for Ms PacMan.
 * 
 * @author Jakob Melnyk (jmel)
 */
public class TreeNode
{
	/**
	 * Number of times this node has been visited.
	 */
	public int visits;

	/**
	 * The parent of this node.
	 */
	public TreeNode parent;

	/**
	 * The list of children of this node.
	 */
	public List<TreeNode> children;

	/**
	 * The average pill score of this node.
	 */
	public double avgPill;

	/**
	 * The average survival score of this node.
	 */
	public double avgSurvival;

	/**
	 * The maximum pill score of this node.
	 */
	public double maxPill;

	/**
	 * The maximum survival score of this node.
	 */
	public double maxSurvival;

	/**
	 * The UCT value of this node.
	 */
	public double uctValue;

	/**
	 * The game state of this node.
	 */
	public Game gameState;

	/**
	 * The amount of nodes between this node and the root node.
	 */
	public double pathLength;

	/**
	 * Actions that are possible from this node.
	 */
	private Map<MOVE, Boolean> actions;

	/**
	 * Instantiates a new instance of the TreeNode class.
	 * 
	 * @param game
	 *            The game state that the node should use.
	 * @param parent
	 *            The parent of this node.
	 * @param pathLength
	 *            How many nodes are between this node and the root node.
	 */
	public TreeNode(Game game, TreeNode parent, double pathLength)
	{
		this.visits = 0;
		this.parent = parent;
		this.children = new ArrayList<TreeNode>();

		this.gameState = game.copy();
		this.actions = new HashMap<MOVE, Boolean>();
		MOVE[] possibleActions = gameState.getPossibleMoves(gameState.getPacmanCurrentNodeIndex(),
				gameState.getPacmanLastMoveMade());
		for (MOVE m : possibleActions)
		{
			actions.put(m, false);
		}

		this.pathLength = pathLength;
		this.avgPill = 0.0;
		this.maxPill = 0.0;
		this.avgSurvival = 0.0;
		this.maxSurvival = 0.0;
	}

	/**
	 * Calculates which of this node's children would be best to explore.
	 * 
	 * @return The best child of this node to explore.
	 */
	public TreeNode bestChild()
	{
		// If there are any children visited less often that the threshold, visit one of them first.
		List<TreeNode> childrenBeneathThreshold = new ArrayList<TreeNode>();
		for (TreeNode child : children)
			if (child.visits < MCTS.VISIT_THRESHOLD)
				childrenBeneathThreshold.add(child);

		// Choose randomly between children with fewer visit than the threshold.
		if (childrenBeneathThreshold.size() > 0)
			return childrenBeneathThreshold.get(MCTS.random.nextInt(childrenBeneathThreshold.size()));

		TreeNode bestChild = null;
		double bestValue = -10000;

		// Calculate the UCT value of all this node's children and choose the best to return.
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
	 * Expands all children of this node and returns the best of these children.
	 * 
	 * @return The best child of this expanded node.
	 */
	public TreeNode expand()
	{
		Game game;
		TreeNode newNode = null;

		for (Map.Entry<MOVE, Boolean> entry : actions.entrySet())
		{
			game = gameState.copy();

			// Move once to get away from junction.
			game.advanceGame(entry.getKey(), MCTS.ghostStrategy.getMove(game, MCTS.GHOST_MOVE_TIME));

			// Until PacMan arrives at another junction or is eaten, continue moving in the same direction.
			while (!MCTS.pacManAtJunction(game))
			{
				if (game.wasPacManEaten())
				{
					break;
				}

				game.advanceGame(entry.getKey(), MCTS.ghostStrategy.getMove(game, MCTS.GHOST_MOVE_TIME));
			}

			// Add new junction node to children.
			newNode = new TreeNode(game, this, this.pathLength + 1);
			children.add(newNode);
		}

		return this.bestChild();
	}

	/**
	 * Gets the score of this node. If in survive node, this is simply the survival score. If not in survival mode, then the score
	 * is survival score times the pill score.
	 * 
	 * @return The score of this node.
	 */
	public double getScore()
	{
		if (MCTS.SURVIVAL)
		{
			return maxSurvival;
		}
		else
		{
			// If no pills were eaten, then simply return survival score.
			if (maxPill == 0.0)
			{
				return maxSurvival;
			}
			return maxSurvival * maxPill;
		}
	}

	/**
	 * Gets the normalised survival score of this node. The score is 1.0 if PacMan survived, or between 0 and 1.0 if PacMan
	 * survived depending on how far from the root this node is.
	 * 
	 * @return The normalised survival score.
	 */
	public double getSurvivalScore()
	{
		// If PacMan was not eaten, return max score.
		if (!gameState.wasPacManEaten())
		{
			return 1.0;
		}

		// Normalise score based on how far from root we are (dying later is better than dying early.
		double min = 0;
		double max = MCTS.MAX_PATH_TO_ROOT;
		return (this.pathLength - min) / (max - min);
	}

	/**
	 * Gets the normalised pill score of this node. The score is normalised between 0-1 (no pills eaten and all pills in current
	 * maze eaten).
	 * 
	 * @return The normalized pill score of this node.
	 */
	public double getPillScore()
	{
		// If the maze of this node is not the same as the root maze,
		// return value as if all pills were eaten.
		if (gameState.getMazeIndex() != MCTS.ROOT_MAZE_INDEX)
			return 1.0;

		double min = 0;
		double max = MCTS.PILLS_AT_ROOT;
		double eaten = MCTS.PILLS_AT_ROOT - gameState.getNumberOfActivePills();

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
	 * Increases the amount of visits by one and updates the average and max pill/survival scores of this node.
	 * 
	 * @param pillValue
	 *            The pillValue to use in the update.
	 * @param survivalValue
	 *            The survivalValue to use in the update.
	 */
	public void updateValues(double pillValue, double survivalValue)
	{
		// Increase visits
		visits++;

		// Average pill and survival scores over number of children.
		double average = (children.size() == 0 ? 1 : children.size());
		double pillSum = 0;
		double survivalSum = 0;
		for (TreeNode c : children)
		{
			pillSum += c.maxPill;
			survivalSum += c.maxSurvival;
		}
		avgPill = pillSum / average;
		avgSurvival = survivalSum / average;

		// Set new highest pill and/or survival scores.
		if (pillValue > maxPill)
			maxPill = pillValue;
		if (survivalValue > maxSurvival)
			maxSurvival = survivalValue;
	}

	/**
	 * Checks if this node is a terminal node.
	 * 
	 * @return True if PacMan was eaten, path length exceeds maximum, or the maze of the game state is this node is not the same
	 *         as in the root.
	 */
	public boolean isTerminalNode()
	{
		return (gameState.wasPacManEaten() || (pathLength >= MCTS.MAX_PATH_TO_ROOT) || (gameState.getMazeIndex() != MCTS.ROOT_MAZE_INDEX));
	}

	/**
	 * Checks if this node is a leaf.
	 * 
	 * @return True if there are any remaining actions to expand, false if not.
	 */
	public boolean isLeafNode()
	{
		return (children.size() != actions.size());
	}
}