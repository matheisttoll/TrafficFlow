import java.util.Iterator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


class Car {
	public static final int CAR_RAD = 18;
	public static final double ACCLERATION = 0.04;
	public static final double MAX_SPEED = 0.025;
	protected Car next;
	protected Position pos;
	protected double speed;
	protected Roundabout roundabout;
	protected Color color = Color.FORESTGREEN;
	
	protected Car(Position pos, double speed, Roundabout roundabout) {
		this.pos = pos;
		this.speed = speed;
		this.next = this;
		this.roundabout = roundabout;
	}

	public Car(Car next, Position pos, double speed, Roundabout roundabout) {
		this.next = next;
		this.pos = pos;
		this.speed = speed;
		this.roundabout = roundabout;
	}

	public Position getPos() {
		return pos;
	}

	public void draw(GraphicsContext gc) {
		gc.setFill(color);
		gc.fillOval(pos.getX() - CAR_RAD, pos.getY() - CAR_RAD, 2*CAR_RAD, 2*CAR_RAD);
	}

	public void drive() {
		speed += ACCLERATION * (speedProfile(distanceTo(next)) - speed);
		pos.move(speed);
	}

	public double distanceTo(Car other) {
		return pos.distanceTo(other.getPos());
	}

	protected static double speedProfile(double distance) {
		//return 0.035 * Math.sin(distance / 4) * (distance / (1 + distance));
		//return 0.035 * (distance / (1 + distance));
		return MAX_SPEED * (Math.tanh(distance - 0.7) + Math.tanh(0.7));
	}
}


class DummyCar extends Car {
	private Position fakePos;
	private double distToStart;
	private boolean waiting = true;
	private Car prev;


	public DummyCar(Car next, Car prev, Roundabout roundabout) {
		super(next, new Position(0, roundabout), next.speed, roundabout);
		distToStart = Roundabout.DRIVEWAY_LENGTH;
		fakePos = next.pos;
		this.prev = prev;
		prev.next = this;
	}
	


	//Behaupte einfach an der Position des vorderen Autos zu sein!
	@Override
	public Position getPos() {
		return fakePos;
	}

	@Override
	public void drive() {
		pos.set(prev.getPos(), prev.distanceTo(next) / 2);
		//Wenn ich im unteren rechten Quadranten bin sollte es losfahren
		if(pos.getX() >= Roundabout.WIDTH && pos.getY() > Roundabout.HEIGHT) {
		waiting = false;
		}
		if(!waiting && pos.getY() < Roundabout.HEIGHT) {
			Car realCar = new Car(next, pos, speed, roundabout);
			prev.next = realCar;
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(color);
		if(waiting) { 
			gc.fillOval(Roundabout.WIDTH + Roundabout.STREET_MIDDLE_RAD - CAR_RAD,
					    Roundabout.HEIGHT + Roundabout.DRIVEWAY_LENGTH - CAR_RAD,
					    2*CAR_RAD,
					    2*CAR_RAD);
		} else {
			gc.fillOval(Roundabout.WIDTH + Roundabout.STREET_MIDDLE_RAD - CAR_RAD,
						pos.getY() - CAR_RAD,
						2*CAR_RAD,
						2*CAR_RAD);
		}
	}
}


public class Cars implements Iterable<Car>, Iterator<Car> {
	private Car head;
	private Car cur;
	boolean first = true;
	private Roundabout roundabout;

	public Cars(int numberOfCars, Roundabout roundabout) {
		this.roundabout = roundabout;
		head = new Car(new Position(0, roundabout), 0, roundabout);
		for(int i = 1; i < numberOfCars; i++) {
			addCar(new Position(i * Math.PI / numberOfCars, roundabout), 0);
		}
	}

	public void addCar() {
		new DummyCar(head.next, head, roundabout); 
	}

	public void addCar(Position pos, double speed) {
		Car nCar = new Car(head.next, pos, speed, roundabout);
		head.next = nCar;
	}

	public void draw(GraphicsContext gc) {
		for(Car car : this) {
			car.draw(gc);
		}
	}

	@Override
	public Iterator<Car> iterator() {
		first = true;
		cur = head;
		return this;
	}

	@Override
	public boolean hasNext() {
		if(first || cur != head) {
			return true;
		}
		return false;
	}

	@Override
	public Car next() {
		first = false;
		Car tmp = cur;
		cur = cur.next;
		return tmp;
	}


	@Override
	public void remove() {
		//Nichts entfernen
	}	
}