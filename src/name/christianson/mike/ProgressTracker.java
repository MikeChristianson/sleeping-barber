package name.christianson.mike;

public class ProgressTracker implements Runnable {

	private final BarberShop shop;

	public ProgressTracker(BarberShop shop) {
		this.shop = shop;
	}

	@Override
	public void run() {
		while (shop.isOpen()) {
			try {
				Thread.sleep(100);
				printProgress();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		printProgress();
		System.out.println();
	}

	private void printProgress() {
		System.out.printf("The shop served %s customers but turned away %s.\r", shop.haircuts(), shop.lostCustomers());
	}
}
