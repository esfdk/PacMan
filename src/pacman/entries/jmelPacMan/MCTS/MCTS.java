package pacman.entries.jmelPacMan.MCTS;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Monte Carlo tree search for Ms PacMan.
 * 
 * @author Jakob Melnyk (jmel)
 */
public class MCTS
{
	/**
	 * The maximum amount of iterations to make in a simulation.
	 */
	public static final int MAXIMUM_ITERATIONS = 1000;

	/**
	 * The exploration constant of the algorithm (the higher the constant, the more likely exploration is).
	 */
	public static final double EXPLORATION_CONSTANT = 0.5;

	/**
	 * The maximum amount of nodes to expand while searching the tree.
	 */
	public static final double MAX_PATH_TO_ROOT = 10;

	/**
	 * The minimum amount of times to visit a node.
	 */
	public static final int VISIT_THRESHOLD = 3;

	/**
	 * The survival threshold value (PacMan swaps to survival mode if survival rate is below this value).
	 */
	public static final double SURVIVAL_THRESHOLD = 0.7;

	/**
	 * The number of lives remaining at root.
	 */
	public static double LIVES_AT_ROOT;

	/**
	 * The index of the maze at the root node.
	 */
	public static int ROOT_MAZE_INDEX;

	/**
	 * The number of pills remaining at the root node.
	 */
	public static int PILLS_AT_ROOT;

	/**
	 * The number of power pills remaining at the root node.
	 */
	public static int POWER_PILLS_AT_ROOT;

	/**
	 * How much time the ghost controller has to make a decision.
	 */
	public static int GHOST_MOVE_TIME;

	/**
	 * Whether survival mode is enabled or not.
	 */
	public static boolean SURVIVAL;

	/**
	 * The random object used to make decisions requiring randomness.
	 */
	public static Random random;

	/**
	 * The ghost strategy used in prediction.
	 */
	public static Controller<EnumMap<GHOST, MOVE>> ghostStrategy;

	/**
	 * The root of the tree.
	 */
	private TreeNode root;

	/**
	 * The current iteration.
	 */
	private int currIteration;

	/**
	 * Instantiates a new instance of the MCTS class.
	 */
	public MCTS()
	{
		random = new Random();
	}

	/**
	 * Uses Monte Carlo Tree Search to decide on which of the possible actions PacMan can take is best.
	 * 
	 * @param game
	 *            The game state to perform the MCTS algorithm on.
	 * @param timeDue
	 *            How long the MCTS has to search.
	 * @param ghostMoveTime
	 *            How much time the ghosts have to move.
	 * @param gs
	 *            The ghost controller to emulate.
	 * @return The best child node of the root.
	 */
	public TreeNode search(Game game, long timeDue, int ghostMoveTime, Controller<EnumMap<GHOST, MOVE>> gs)
	{
		ghostStrategy = gs;
		GHOST_MOVE_TIME = ghostMoveTime;
		PILLS_AT_ROOT = game.getNumberOfActivePills();
		POWER_PILLS_AT_ROOT = game.getNumberOfActivePowerPills();
		ROOT_MAZE_INDEX = game.getMazeIndex();
		LIVES_AT_ROOT = game.getPacmanNumberOfLivesRemaining();

		root = new TreeNode(game.copy(), null, 0);
		TreeNode currNode = root;

		currIteration = 0;

		while (System.currentTimeMillis() < timeDue && currIteration < MAXIMUM_ITERATIONS)
		{
			// Selects the best child that is either a terminal node or a leaf node.
			currNode = selection();

			// Simulates play of PacMan until a terminal node is reached or PacMan dies.
			TreeNode node = playout(currNode);

			// Backpropagate pill and survival scores up the tree.
			backpropagate(currNode, node);

			// Count up iteration and time spent.
			currIteration++;
		}

		// Select child with best score as node to return.
		TreeNode chosenNode = null;
		double chosenNodeScore = -9001;
		for (TreeNode tn : root.children)
		{
			if (tn.getScore() > chosenNodeScore)
			{
				chosenNode = tn;
				chosenNodeScore = tn.getScore();
			}
		}

		// If average survival rate was lower than threshold, engage survival tactics.
		if (chosenNode.maxSurvival < MCTS.SURVIVAL_THRESHOLD)
		{
			SURVIVAL = true;
		}
		else
		{
			SURVIVAL = false;
		}

		return chosenNode;
	}

	/**
	 * Traverses the tree, selecting the best child until it reaches either a terminal node or a leaf node.
	 * 
	 * @return The terminal or leaf node found.
	 */
	private TreeNode selection()
	{
		TreeNode node = root;
		while (!node.isTerminalNode())
		{
			if (!node.isLeafNode())
			{
				node = node.bestChild();
			}
			else
			{
				return node.expand();
			}
		}

		return node;
	}

	/**
	 * Simulates play until a terminal node is reached.
	 * 
	 * @param n
	 *            The node to use in simulation.
	 * @return The node that playout terminated at.
	 */
	private TreeNode playout(TreeNode n)
	{
		TreeNode node = n;
		while (!node.isTerminalNode())
		{
			Game game = node.gameState.copy();

			// PacMan makes a random move to use until reaching the next junction.
			MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
			MOVE m = possibleMoves[random.nextInt(possibleMoves.length)];

			// Step out of junction.
			game.advanceGame(m, MCTS.ghostStrategy.getMove(game, MCTS.GHOST_MOVE_TIME));

			// Simulate until PacMan reaches a junction or is eaten.
			while (!MCTS.pacManAtJunction(game))
			{
				if (game.wasPacManEaten())
				{
					return new TreeNode(game, node, node.pathLength + 1);
				}

				game.advanceGame(m, MCTS.ghostStrategy.getMove(game, MCTS.GHOST_MOVE_TIME));
			}

			// Continue simulation from the new junction node.
			node = new TreeNode(game, node, node.pathLength + 1.0);
		}

		return node;
	}

	/**
	 * Backpropagates the survival and pill scores up the tree.
	 * 
	 * @param nodeToBeUpdated
	 *            The first node to update.
	 * @param finalSimulationNode
	 *            The final node of the simulation.
	 */
	public void backpropagate(TreeNode nodeToBeUpdated, TreeNode finalSimulationNode)
	{
		TreeNode tempNode = nodeToBeUpdated;
		double survivalScore = finalSimulationNode.getSurvivalScore();
		double pillScore = finalSimulationNode.getPillScore();
		while (tempNode != null)
		{
			tempNode.updateValues(pillScore, survivalScore);
			tempNode = tempNode.parent;
		}
	}

	/**
	 * Check if PacMan is in a corner or a junction in the specified game state.
	 * 
	 * @param game
	 *            The gamestate to consider.
	 * @return If PacMan is at a corner or a junction, returns true; otherwise, returns false.
	 */
	public static boolean pacManAtJunction(Game game)
	{
		MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		List<MOVE> moveList = new ArrayList<MOVE>();
		for (MOVE m : possibleMoves)
		{
			moveList.add(m);
		}

		moveList.remove(game.getPacmanLastMoveMade());
		moveList.remove(game.getPacmanLastMoveMade().opposite());

		return moveList.size() != 0;
	}
}