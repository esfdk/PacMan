package pacman.entries.jmelPacMan.BT;

/**
 * Behaviour used for changing the output of another behaviour node.
 * Based on code by notmagi, http://notmagi.me/behavior-trees-number-1/
 * @author Jakob Melnyk (jmel)
 */
public abstract class Decorator extends Behaviour
{
	/**
	 * The behaviour to decorate.
	 */
	protected Behaviour behaviour;

	/**
	 * Instantiates an instance of the Decorator class.
	 * 
	 * @param behaviour The behaviour to decorate.
	 */
	public Decorator(Behaviour behaviour)
	{
		this.behaviour = behaviour;
	}
}
