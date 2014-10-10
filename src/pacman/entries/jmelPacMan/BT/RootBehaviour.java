package pacman.entries.jmelPacMan.BT;

/**
 * The root of the tree. 
 * Based on code by notmagi, http://notmagi.me/behavior-trees-number-1/
 * @author Jakob Melnyk (jmel)
 */
public class RootBehaviour extends Behaviour
{
	/**
	 * The entity connected to the behaviour tree.
	 */
	private Object entity;
	
	/**
	 * The context used by the behaviour tree.
	 */
	private Context context;
	
	/**
	 * The root behaviour.
	 */
	private Behaviour root;

	/**
	 * Instantiates a new instance of the RootBehaviour class.
	 * @param entity The entity the behaviour tree is connected to.
	 * @param context The context used by the behaviour tree.
	 * @param behaviour The root behaviour.
	 */
	public RootBehaviour(Object entity, Context context, Behaviour behaviour)
	{
		super();
		this.entity = entity;
		this.root = behaviour;
		this.context = context;
		this.context.entity = entity;
	}
	
	/*
	 * (non-Javadoc)
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#run(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		return this.root.update(context);
	}

	/*
	 * (non-Javadoc)
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#start(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
		this.context.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#finish(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public void finish(Context context)
	{
		// Not necessary for the root.
	}
}
