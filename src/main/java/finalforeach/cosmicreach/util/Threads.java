package finalforeach.cosmicreach.util;

import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.PauseableThread;

public class Threads
{
	public static final Array<PauseableThread> allPauseableThreads = new Array<>();
	private static Consumer<Runnable> mainThreadRunner = task -> Gdx.app.postRunnable(task);
	private static Thread mainThread;

	public static void runOnMainThread(Runnable runnable)
	{
		if (Thread.currentThread() == mainThread)
		{
			runnable.run();
		} else
		{
			mainThreadRunner.accept(runnable);
		}
	}

	public static boolean isOnMainThread()
	{
		return Thread.currentThread() == mainThread;
	}

	public static Thread createThread(String name, Runnable runnable)
	{
		return new Thread(runnable, name);
	}

	public static PauseableThread createPauseableThread(String name, Runnable runnable)
	{
		PauseableThread thread = new PauseableThread(runnable);
		thread.setName(name);
		allPauseableThreads.add(thread);
		return thread;
	}

	public static PauseableThread createPauseableThread(String name, Consumer<PauseableThread> threadConsumer)
	{
		var threadRunner = new Runnable() {
			PauseableThread t;

			@Override
			public void run()
			{
				threadConsumer.accept(t);
			}
		};
		PauseableThread thread = new PauseableThread(threadRunner);
		threadRunner.t = thread;
		thread.setName(name);
		allPauseableThreads.add(thread);
		return thread;
	}

	/**
	 * Gracefully tells all threads to (eventually) finish, assuming each thread
	 * does not hang. Should only be called at the end of the program.
	 */
	public static void stopAllThreads()
	{
		for (PauseableThread t : Threads.allPauseableThreads)
		{
			t.stopThread();
		}
		allPauseableThreads.clear();
	}

	public static void setThisAsMainThread()
	{
		mainThread = Thread.currentThread();
	}
	
	public static void setMainThreadRunner(Consumer<Runnable> runner) 
	{
		mainThreadRunner = runner;
	}

}
