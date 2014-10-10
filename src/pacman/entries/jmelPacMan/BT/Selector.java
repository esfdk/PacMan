package pacman.entries.jmelPacMan.BT;

import java.util.ArrayList;

/**
 * A selector behaviour; it performs the actions of each of its children in sequence and succeeds if any of its children succeeds, otherwise, it
 * fails.
 * Based on code by notmagi, http://notmagi.me/behavior-trees-number-1/
 * @author Jakob Melnyk (jmel)
 */
public class Selector extends Behaviour
{
	/**
	 * A list of the children of the selector.
	 */
	private ArrayList<Behaviour> children;

	/**
	 * The index of the child that is currently running.
	 */
	private int currentChild;

	/**
	 * Instantiates a new instance of the Selector class.
	 */
	public Selector()
	{
		super();
		this.children = new ArrayList<Behaviour>();
		this.currentChild = 0;
	}

	/**
	 * Instantiates a new instance of the Selector class.
	 * @param behaviour A list of behaviours for the selector.
	 */
	public Selector(ArrayList<Behaviour> behaviour)
	{
		this();
		this.children.addAll(behaviour);
	}

	/**
	 * Adds a behaviour to the children of the selector.
	 * @param behaviour The behaviour to add to the children.
	 */
	public void addBehaviour(Behaviour behaviour)
	{
		children.add(behaviour);
	}

	/*
	 * (non-Javadoc)
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#run(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		while (true)
		{
			STATUS s = children.get(currentChild).update(context);
			if (s != STATUS.FAILURE)
				return s;
			currentChild++;
			if (currentChild == children.size())
				return STATUS.FAILURE;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#start(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
		currentChild = 0;
	}

	/*
	 * (non-Javadoc)
	 * @see pacman.entries.jmelPacMan.BT.Behaviour#finish(pacman.entries.jmelPacMan.BT.Context)
	 */
	@Override
	public void finish(Context context)
	{
		// Not necessary for the Selector.
	}
}
