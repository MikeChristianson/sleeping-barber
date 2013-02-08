package name.christianson.mike;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import static java.util.concurrent.TimeUnit.*;

public class BarberShop {
	public static final int NUM_WAITING_ROOM_CHAIRS = 3;
	public static final long SHOP_RUNTIME_MILLIS = SECONDS.toMillis(10);

	public static void main(String[] args) throws InterruptedException {
		final AtomicBoolean shopOpen = new AtomicBoolean();
		final AtomicInteger totalHaircuts = new AtomicInteger();
		final AtomicInteger lostCustomers = new AtomicInteger();
		BlockingQueue<Object> waitingRoom = new LinkedBlockingQueue<>(NUM_WAITING_ROOM_CHAIRS);

		shopOpen.set(true);
		
		ExecutorService executor = Executors.newFixedThreadPool(3);
		
		Runnable customerGenerator = new CustomerGenerator(shopOpen, waitingRoom, lostCustomers);
		Runnable barber = new Barber(shopOpen, waitingRoom, totalHaircuts);
		Runnable progressTracker = new ProgressTracker(shopOpen, totalHaircuts, lostCustomers);
		
		executor.execute(barber);
		executor.execute(customerGenerator);
		executor.execute(progressTracker);
		executor.shutdown();
		
		Thread.sleep(SHOP_RUNTIME_MILLIS);
		
		shopOpen.set(false);
	}

}
