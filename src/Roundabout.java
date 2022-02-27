import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Roundabout extends Canvas implements EventHandler<KeyEvent> {

	public static final int WIDTH = 380;
	public static final int HEIGHT = 380;
	public static final int STREET_INNER_RAD = 300;
	public static final int STREET_MIDDLE_RAD = 325;
	public static final int STREET_OUTER_RAD = 350;
	public static final int DRIVEWAY_LENGTH = 350;
	public static final int DRIVEWAY_LEFT_ANGLE = 331;

	private Cars cars;

	private GraphicsContext gc;

	Roundabout() {
		super(2*WIDTH, 2*HEIGHT);
		gc = getGraphicsContext2D();
		cars = new Cars(2, this);
	}


	public void draw() {
		gc.setFill(Color.CORNSILK);
		gc.fillRect(0, 0, getWidth(), getHeight());

		gc.beginPath();
		gc.arc(WIDTH, HEIGHT, STREET_OUTER_RAD, STREET_OUTER_RAD, 0, DRIVEWAY_LEFT_ANGLE);
		gc.lineTo(WIDTH + STREET_INNER_RAD, HEIGHT + DRIVEWAY_LENGTH);
		gc.moveTo(WIDTH + STREET_OUTER_RAD, HEIGHT);
		gc.lineTo(WIDTH + STREET_OUTER_RAD, HEIGHT + DRIVEWAY_LENGTH);
		gc.stroke();

		gc.strokeOval(WIDTH - STREET_INNER_RAD,
					  HEIGHT - STREET_INNER_RAD,
					  2*STREET_INNER_RAD,
					  2*STREET_INNER_RAD);
		gc.setFill(Color.FORESTGREEN);
		cars.draw(gc);
	}

	public void update() {
		for(Car car : cars) {
			car.drive();
		}
	}

	@Override
	public void handle(final KeyEvent keyEvent) {
		if(keyEvent.getCode() == KeyCode.SPACE) {
			cars.addCar();
		}
	}
}