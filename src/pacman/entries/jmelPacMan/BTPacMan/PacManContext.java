package pacman.entries.jmelPacMan.BTPacMan;

import java.util.ArrayList;

import pacman.entries.jmelPacMan.BT.Context;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * The context of the PacMan game for use in the behaviour tree.
 * @author Jakob Melnyk (jmel)
 */
public class PacManContext extends Context
{
	/**
	 * The PacMan game to being played.
	 */
	public Game game;
	
	/**
	 * The move chosen for the next game tick.
	 */
	public MOVE nextMove;
	
	/**
	 * Current position of PacMan.
	 */
	public int currentPacManIndex;
	
	/**
	 * A ghost can be no closer than this before PacMan flees.
	 */
	public int MINIMUM_GHOST_DISTANCE;
	
	/**
	 * How far PacMan should look for other ghosts when deciding which way to flee from a ghost.
	 */
	public int FLEE_SEARCH_RANGE;
	
	/**
	 * How close a ghost should be before PacMan chases it in an attempt to eat it.
	 */
	public int EAT_GHOST_DISTANCE;
	
	/**
	 * Array of active pill indices.
	 */
	public int[] activePills;
	
	/**
	 * Array of active power pill indices.
	 */
	public int[] activePowerPills;
	
	/**
	 * Array of actual target node indices.
	 */
	public int[] targetNodeIndices;
	
	/**
	 * List of targets for PacMan to consider.
	 */
	public ArrayList<Integer> targetList;

	/**
	 * Instantiates a new instance of the PacManContext class.
	 */
	public PacManContext()
	{
		nextMove = MOVE.NEUTRAL;
		
		MINIMUM_GHOST_DISTANCE = 5;
		FLEE_SEARCH_RANGE = 45;
		EAT_GHOST_DISTANCE = 74;
		
		activePills = new int[0];
		activePowerPills = new int[0];
		targetNodeIndices = new int[0];
		targetList = new ArrayList<Integer>();
	}
	
	/**
	 * Instantiates a new instance of the PacManContext class.
	 * @param minDist The value of MINIMUM_GHOST_DISTANCE.
	 * @param fleeRange The value of FLEE_SEARCH_RANGE.
	 * @param eatDist The value of EAT_GHOST_DISTANCE.
	 */
	public PacManContext(int minDist, int fleeRange, int eatDist)
	{
		super();
		MINIMUM_GHOST_DISTANCE = minDist;
		FLEE_SEARCH_RANGE = fleeRange;
		EAT_GHOST_DISTANCE = eatDist;
	}
	
	/**
	 * Updates active pills, active power pills and current pacman position.
	 * @param game The game state used to update fields.
	 */
	public void setVariables(Game game)
	{
		this.game = game;
		this.activePills = game.getActivePillsIndices();
		this.activePowerPills = game.getActivePowerPillsIndices();
		this.currentPacManIndex = game.getPacmanCurrentNodeIndex();
	}
}
