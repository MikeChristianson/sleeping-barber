package name.christianson.mike;

import java.util.concurrent.ThreadLocalRandom;

public class CustomerGenerator implements Runnable {
	public static final int ARRIVAL_INTERVAL_OFFSET_MILLIS = 10;
	public static final int ARRIVAL_INTERVAL_RANGE_MILLIS = 20;
	private final BarberShop shop;

	public CustomerGenerator(BarberShop shop) {
		this.shop = shop;
	}

	@Override
	public void run() {
		while (shop.isOpen()) {
			try {
				Thread.sleep(nextRandomInterval());
				shop.seatCustomerInWaitingRoom(new Object());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}

	public int nextRandomInterval() {
		return ThreadLocalRandom.current().nextInt(ARRIVAL_INTERVAL_RANGE_MILLIS) + ARRIVAL_INTERVAL_OFFSET_MILLIS;
	}

}
