package br.feevale.accelerokey;

/**
 * Equilibrist is our waiter, with his tray on hands, he's ready to serve us.
 * But in this case, our job isn't to ask him for drinks/etc. We gotta disturb him!
 * By disturbing our Waiter, we can make his tray pend to any direction.
 * The more he tilts for some direction, our chances that he'll let something fall down increase.
 *
 * We must implement the fall method to check when he drops something.
 */
public abstract class Equilibrist {

	private static float LIMIT = 25f;
	private static float LEVEL = 2f;

	private float currentX = 0f;
	private float currentY = 0f;
	
	public void disturb(float x, float y){
		float calculatedX = convertFromSensorValue(x);
		float calculatedY = convertFromSensorValue(y);
		
		currentX += calculatedX;
		currentY += calculatedY;
	
		if (currentX > LIMIT){
			fall(CardinalDirection.WEST);
			currentX = 0f;
		}else if(currentY > LIMIT){
			fall(CardinalDirection.NORTH);
			currentY = 0f;
		}else if(currentX < -LIMIT){
			fall(CardinalDirection.EAST);
			currentX = 0f;
		}else if(currentY < -LIMIT){
			fall(CardinalDirection.SOUTH);
			currentY = 0f;
		}
		
	}
	
	private float convertFromSensorValue(float value) {
		float calculatedValue = Math.abs(value);
		calculatedValue -= LEVEL;
		if(calculatedValue < 0){
			return 0f;
		}
		calculatedValue = calculatedValue * calculatedValue;
		return value > 0 ? calculatedValue : -calculatedValue;
	}

	public abstract void fall(CardinalDirection cardinalDirection);
	
}
