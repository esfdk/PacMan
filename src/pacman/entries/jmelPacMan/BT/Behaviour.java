package pacman.entries.jmelPacMan.BT;

/**
 * A behaviour (node) in the behaviour tree.
 * Based on code by notmagi, http://notmagi.me/behavior-trees-number-1/
 * @author Jakob Melnyk (jmel)
 */
public abstract class Behaviour
{
	/**
	 * State of the behaviour.
	 */
	protected STATUS status = STATUS.INVALID;
	
	public Behaviour()
	{
	}
	
	/**
	 * Updates the status of the behaviour.
	 * @param context The context used by the behaviour tree.
	 * @return The status of the behaviour after the update.
	 */
	public STATUS update(Context context)
	{
		if(this.status != STATUS.RUNNING)
		{
			this.start(context);
		}
		
		this.status = this.run(context);
		
		if(this.status != STATUS.RUNNING)
		{
			this.finish(context);
		}
		
		return this.status;
	}
	
	/**
	 * Performs the actions of this behaviour.
	 * @param context The context used by the behaviour tree.
	 * @return The status of the behaviour after performing its actions.
	 */
	public abstract STATUS run(Context context);
	
	/**
	 * Initialises the behaviour. 
	 * @param context The context used by the behaviour tree.
	 */
	public abstract void start(Context context);
	
	/**
	 * Cleans up the behaviour.
	 * @param context The context used by the behaviour tree.
	 */
	public abstract void finish(Context context);
}
