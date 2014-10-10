package pacman.entries.jmelPacMan.EA;

public class PMCGenotype
{
	public int minimumDistance;
	public int fleeDistance;
	public int eatDistance;
	
	public PMCGenotype(int min, int flee, int eat)
	{
		this.minimumDistance = min;
		this.fleeDistance = flee;
		this.eatDistance = eat;
	}
}
