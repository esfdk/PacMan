package pacman.entries.jmelPacMan.jmelPacManBT.BT;

/**
 * Behaviour decorator for repeating a behaviour until it returns failure.
 * Based on code by notmagi, http://notmagi.me/behavior-trees-number-1/
 * @author Jakob Melnyk
 */
public class RepeatUntilFailDecorator extends Decorator
{
	/**
	 * Instantiates a new instance of the RepeatUntilFailDecorator.
	 * @param behaviour The behaviour to repeat.
	 */
	public RepeatUntilFailDecorator(Behaviour behaviour)
	{
		super(behaviour);
	}

	/*
	 * (non-Javadoc)
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#run(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		STATUS s = this.update(context);
		if(s != STATUS.FAILURE) return STATUS.RUNNING;
		return STATUS.SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#start(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
		// Not necessary for the decorator.
	}

	/*
	 * (non-Javadoc)
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#finish(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public void finish(Context context)
	{
		// Not necessary for the decorator.
	}
}
