

import java.util.Random;


public class Mobility extends Thread {
	public static int rValue;

	private Random random;

	public Mobility() {
		random = new Random();
		rValue = random.nextInt(100);
		start();
	}



	

	public void run() {

		while (true) {
			try {
				int temp = random.nextInt(2);
				// System.out.println(temp);
				// Thread.sleep(2000);
				if (temp == 0) {

					if (rValue <= 100) {
						rValue += random.nextInt(10);
						if (rValue >= 100) {
							rValue -= random.nextInt(50);
						}
					} else {
						rValue -= random.nextInt(50);
					}
					nodeB.jp.setValue(rValue);
					nodeB.jp.setString(rValue + "%");
					// print(rValue);
					Thread.sleep(5000);
				} else {
					if (rValue >= 0) {
						rValue -= random.nextInt(10);
						if (rValue <= 0) {
							rValue += random.nextInt(50);
						}
					} else {
						rValue += random.nextInt(50);
					}
					nodeB.jp.setValue(rValue);
					nodeB.jp.setString(rValue + "%");
					// print(rValue);
					Thread.sleep(5000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	
}
