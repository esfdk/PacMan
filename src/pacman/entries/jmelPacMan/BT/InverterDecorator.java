package pacman.entries.jmelPacMan.BT;

/**
 * A decorator that inverts the the results of a behaviour.
 * Based on code by notmagi, http://notmagi.me/behavior-trees-number-1/ 
 * @author Jakob Melnyk
 */
public class InverterDecorator extends Decorator
{
	/**
	 * Instantiates a new instance of the InverterDecorator.
	 * @param behaviour The behaviour to invert.
	 */
	public InverterDecorator(Behaviour behaviour)
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
		STATUS s = this.behaviour.update(context);
		if (s == STATUS.RUNNING) return STATUS.RUNNING;
		if (s == STATUS.SUCCESS) return STATUS.FAILURE;
		if (s == STATUS.FAILURE) return STATUS.SUCCESS;
		
		return s;
	}

	/*
	 * (non-Javadoc)
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#start(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
		// Not needed for the inverter.
	}

	/*
	 * (non-Javadoc)
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#finish(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public void finish(Context context)
	{
		// Not needed for the inverter.
	}
}
