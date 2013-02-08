package name.christianson.mike;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerGenerator implements Runnable {
	public static final int ARRIVAL_INTERVAL_OFFSET_MILLIS = 10;
	public static final int ARRIVAL_INTERVAL_RANGE_MILLIS = 20;
	private final BlockingQueue<Object> waitingRoom;
	private final AtomicBoolean shopOpen;
	private final AtomicInteger lostCustomers;

	public CustomerGenerator(AtomicBoolean shopOpen, BlockingQueue<Object> waitingRoom, AtomicInteger lostCustomers) {
		this.shopOpen = shopOpen;
		this.waitingRoom = waitingRoom;
		this.lostCustomers = lostCustomers;
	}

	@Override
	public void run() {
		while (shopOpen.get()) {
			try {
				Thread.sleep(nextRandomInterval());
				boolean queued = waitingRoom.offer(new Object());
				if (!queued) {
					lostCustomers.incrementAndGet();
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}

	int nextRandomInterval() {
		return ThreadLocalRandom.current().nextInt(ARRIVAL_INTERVAL_RANGE_MILLIS) + ARRIVAL_INTERVAL_OFFSET_MILLIS;
	}

}
