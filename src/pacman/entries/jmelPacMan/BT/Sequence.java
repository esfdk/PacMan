package pacman.entries.jmelPacMan.BT;

import java.util.ArrayList;

/**
 * A sequence behaviour; it performs the actions of each of its children in sequence and succeeds if all of its children succeed, otherwise, it
 * fails.
 * Based on code by notmagi, http://notmagi.me/behavior-trees-number-1/
 * @author Jakob Melnyk
 */
public class Sequence extends Behaviour
{
	/**
	 * A list of the children of the sequence.
	 */
	private ArrayList<Behaviour> children;

	/**
	 * The index of the child that is currently running.
	 */
	private int currentChild;

	/**
	 * Instantiates a new instance of the Sequence class.
	 */
	public Sequence()
	{
		super();
		this.children = new ArrayList<Behaviour>();
		this.currentChild = 0;
	}

	/**
	 * Instantiates a new instance of the Sequence class.
	 * @param behaviour A list of behaviours for the sequence.
	 */
	public Sequence(ArrayList<Behaviour> behaviour)
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
			if (s != STATUS.SUCCESS)
				return s;
			currentChild++;
			if (currentChild == children.size())
				return STATUS.SUCCESS;
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
