package name.christianson.mike;

public class Barber implements Runnable {
	private static final int HAIRCUT_TIME_MILLIS = 20;
	private final BarberShop shop;

	public Barber(BarberShop shop) {
		this.shop = shop;
	}

	@Override
	public void run() {
		while(shop.isOpen()) {
			try {
				Object customer = shop.napUntilCustomerArrives();
				cutHair(customer);
				shop.recordHaircut();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}

	private void cutHair(Object customer) throws InterruptedException {
		Thread.sleep(HAIRCUT_TIME_MILLIS);
	}

}
