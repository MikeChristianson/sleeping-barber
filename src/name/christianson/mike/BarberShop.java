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
	private final static AtomicBoolean shopOpen = new AtomicBoolean();
	private final static AtomicInteger totalHaircuts = new AtomicInteger();
	private final static AtomicInteger lostCustomers = new AtomicInteger();
	private final BlockingQueue<Object> waitingRoom = new LinkedBlockingQueue<>(NUM_WAITING_ROOM_CHAIRS);

	public static void main(String[] args) throws InterruptedException {
		BarberShop shop = new BarberShop();

		ExecutorService executor = Executors.newFixedThreadPool(3);
		
		Runnable customerGenerator = new CustomerGenerator(shop);
		Runnable barber = new Barber(shop);
		Runnable progressTracker = new ProgressTracker(shop);
		
		shop.open();
		
		executor.execute(progressTracker);
		executor.execute(barber);
		executor.execute(customerGenerator);
		executor.shutdown();
		
		Thread.sleep(SHOP_RUNTIME_MILLIS);
		
		shop.close();
	}

	private void close() {
		shopOpen.set(false);
	}

	private void open() {
		shopOpen.set(true);
	}

	public boolean isOpen() {
		return shopOpen.get();
	}

	public boolean seatCustomerInWaitingRoom(Object customer) {
		boolean customerSeated = waitingRoom.offer(customer);
		if(!customerSeated) {
			lostCustomers.incrementAndGet();
		}
		return customerSeated;
	}
	
	public Object napUntilCustomerArrives() throws InterruptedException {
		return waitingRoom.take();
	}

	public void recordHaircut() {
		totalHaircuts.incrementAndGet();
	}

	public Object lostCustomers() {
		return lostCustomers.get();
	}

	public Object haircuts() {
		return totalHaircuts.get();
	}

}
