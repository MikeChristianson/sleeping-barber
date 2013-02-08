package name.christianson.mike;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Barber implements Runnable {
	private static final int HAIRCUT_TIME_MILLIS = 20;
	private final AtomicBoolean shopOpen;
	private final BlockingQueue<?> waitingRoom;
	private final AtomicInteger totalHaircuts;

	public Barber(AtomicBoolean shopOpen, BlockingQueue<?> waitingRoom, AtomicInteger totalHaircuts) {
		this.shopOpen = shopOpen;
		this.waitingRoom = waitingRoom;
		this.totalHaircuts = totalHaircuts;
	}

	@Override
	public void run() {
		while(shopOpen.get()) {
			try {
				waitingRoom.take();
				Thread.sleep(HAIRCUT_TIME_MILLIS);
				totalHaircuts.incrementAndGet();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}

}
