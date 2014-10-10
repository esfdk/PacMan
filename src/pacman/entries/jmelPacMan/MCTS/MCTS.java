package pacman.entries.jmelPacMan.MCTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.controllers.examples.Legacy2TheReckoning;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MCTS
{
	public static Game g;

	public static double LIVES_AT_ROOT;
	public static final double EXPLORATION_CONSTANT = 0.5;
	public static final double MAX_PATH_TO_ROOT = 10;
	public static final int VISIT_THRESHOLD = 3;
	public static final double SURVIVAL_THRESHOLD = 0.7;
	public static int ROOT_MAZE_INDEX;
	public static int PILLS_AT_ROOT;
	public static int POWER_PILLS_AT_ROOT;
	public static int GHOST_MOVE_TIME = -1;
	public static boolean SURVIVAL;
	public static Random random;
	public static Legacy2TheReckoning ghostStrategy = new Legacy2TheReckoning();
	private long timeLeft;
	private TreeNode root;
	private int currIteration;

	public MCTS()
	{
		random = new Random();
	}

	public TreeNode search(Game game, long time)
	{
		LIVES_AT_ROOT = game.getPacmanNumberOfLivesRemaining();
		currIteration = 0;
		g = game;
		timeLeft = time;
		PILLS_AT_ROOT = game.getNumberOfActivePills();
		POWER_PILLS_AT_ROOT = game.getNumberOfActivePowerPills();
		ROOT_MAZE_INDEX = game.getMazeIndex();
		root = new TreeNode(game.copy(), null, 0);
		TreeNode currNode = root;

		while (timeLeft > 0.5 && currIteration < 100)
		{
			long currentTime = System.currentTimeMillis();
			// Selection + Expansion
			currNode = selection();
			// Playout / simulation
			TreeNode tempNode = playout(currNode);
			// Backpropagation
			backpropagate(currNode, tempNode);
			currIteration++;
			timeLeft -= System.currentTimeMillis() - currentTime;
		}

		TreeNode bc = root.bestChild();

		if (bc.avgSSurvival < MCTS.SURVIVAL_THRESHOLD)
		{
			SURVIVAL = true;
		}
		else
		{
			SURVIVAL = false;
		}

		TreeNode chosenNode = null;
		double chosenNodeScore = -9001;

		for (TreeNode tn : root.children)
		{
			// System.out.println(tn.getMoveTo() + " | " + tn.visits + " | " + tn.getScore());
			if (tn.getScore() > chosenNodeScore)
			{
				chosenNode = tn;
				chosenNodeScore = tn.getScore();
			}
		}
		// System.out.println("Chosen move: " + chosenNode.getMoveTo());
		// System.out.println("--------------");

		return chosenNode;
	}

	/**
	 * Selection + Expansion from paper.
	 * 
	 * @return Newly expanded node.
	 */
	private TreeNode selection()
	{
		TreeNode tempNode = root;
		while (!tempNode.isTerminalNode())
		{
			if (!tempNode.isLeafNode())
				tempNode = tempNode.bestChild();
			else
				return tempNode.expand();
		}

		return tempNode;
	}

	/**
	 * Simulates a play starting at the given node.
	 * 
	 * @param node
	 * @return The node that the simulation ends at.
	 */
	private TreeNode playout(TreeNode node)
	{
		TreeNode tempNode = node;
		while (!tempNode.isTerminalNode())
		{
			Game tempGame = tempNode.getGameState().copy();

			MOVE[] possibleMoves = tempGame.getPossibleMoves(tempGame.getPacmanCurrentNodeIndex(),
					tempGame.getPacmanLastMoveMade());
			MOVE m = possibleMoves[random.nextInt(possibleMoves.length)];

			tempGame.advanceGame(m, MCTS.ghostStrategy.getMove(tempGame, MCTS.GHOST_MOVE_TIME));

			while (!MCTS.pacManAtJunction(tempGame))
			{
				if (tempGame.wasPacManEaten())
				{
					return new TreeNode(tempGame, tempNode, tempNode.getPathLength() + 1);
				}

				tempGame.advanceGame(m, MCTS.ghostStrategy.getMove(tempGame, MCTS.GHOST_MOVE_TIME));
			}

			tempNode = new TreeNode(tempGame, tempNode, tempNode.getPathLength() + 1.0);
		}

		return tempNode;
	}

	/**
	 * Backpropagates the tree, using the given node as start point.
	 * 
	 * @param nodeToBeUpdated
	 */
	public void backpropagate(TreeNode nodeToBeUpdated, TreeNode finalSimulationNode)
	{
		TreeNode tempNode = nodeToBeUpdated;
		double sSurvival = finalSimulationNode.getRewardSurvival();
		double sPill = finalSimulationNode.getRewardPill();
		while (tempNode != null)
		{
			tempNode.updateValues(sPill, sSurvival);
			tempNode = tempNode.parent;
		}
	}

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