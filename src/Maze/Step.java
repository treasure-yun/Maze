package Maze;

public class Step
{
	public int x;
	public int y;
	public int f;
	
	public Step(int x,int y,int f)
	{
		this.x=x;
		this.y=y;
		this.f=f;
	}
	public Step(Step step)
	{
		this.x=step.x;
		this.y=step.y;
		this.f=step.f;
	}
}
