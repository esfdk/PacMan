package pacman.entries.jmelPacMan.BTPacMan.PacManBehaviours;

import pacman.entries.jmelPacMan.BT.Action;
import pacman.entries.jmelPacMan.BTPacMan.PacManContext;

/**
 * An action in the PacMan Behaviour tree.
 * @author Jakob Melnyk
 */
public abstract class PacManAction extends Action
{
	/**
	 * The context used in the behaviour tree.
	 */
	protected PacManContext context;
}
