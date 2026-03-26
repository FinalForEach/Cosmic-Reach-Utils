package finalforeach.cosmicreach.util.math;

import com.badlogic.gdx.utils.Queue;

public class SimpleMovingAverage
{
	private final Queue<Float> window;
	private final int period;
	private float sum;

	public SimpleMovingAverage(int period)
	{
		this.period = period;
		this.window = new Queue<>();
		this.sum = 0;
	}

	public void add(float value)
	{
		sum += value;
		window.addLast(value);

		if (window.size > period)
		{
			sum -= window.removeFirst();
		}
	}

	public float getAverage()
	{
		return window.isEmpty() ? 0 : sum / window.size;
	}
}