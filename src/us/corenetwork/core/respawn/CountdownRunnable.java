package us.corenetwork.core.respawn;

public class CountdownRunnable implements Runnable {

	private RespawnCountdown countdown;
	private int time;
	
	public CountdownRunnable(RespawnCountdown countdown, int time)
	{
		this.countdown = countdown;
		this.time = time;
	}
	
	@Override
	public void run()
	{
		countdown.setTime(time);
	}

}
