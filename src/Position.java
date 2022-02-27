public class Position {
	private Roundabout roundabout;

	private double deg;
	
	public Position(double deg, Roundabout roundabout) {
		this.deg = deg;
		this.roundabout = roundabout;
	}

	public void set(Position p, double offset) {
		this.deg = (p.deg + offset);
	}

	public double getX() {
		return Roundabout.WIDTH + Math.cos(deg) * Roundabout.STREET_MIDDLE_RAD;
	}

	public double getY() {
		return Roundabout.HEIGHT - Math.sin(deg) * Roundabout.STREET_MIDDLE_RAD;
	}

	public void move(double distance) {
		deg += distance;
	}

	public double distanceTo(Position other) {
		double dist = other.deg - this.deg;
		if(dist < 0) {
			dist += 2*Math.PI;
		}
		return dist;
	}

	public double distanceToStart() {
		return 2*Math.PI -  deg % (2 * Math.PI);
	}
}