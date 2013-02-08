package name.christianson.mike;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ProgressTracker implements Runnable {

	private final AtomicInteger lostCustomers;
	private final AtomicInteger totalHaircuts;
	private final AtomicBoolean shopOpen;

	public ProgressTracker(AtomicBoolean shopOpen, AtomicInteger totalHaircuts, AtomicInteger lostCustomers) {
		this.shopOpen = shopOpen;
		this.totalHaircuts = totalHaircuts;
		this.lostCustomers = lostCustomers;
	}

	@Override
	public void run() {
		while (shopOpen.get()) {
			try {
				Thread.sleep(100);
				printProgress();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		printProgress();
		System.out.println("");
	}

	private void printProgress() {
		System.out.printf("The shop served %s customers but turned away %s.\r", totalHaircuts, lostCustomers);
	}
}
