import javafx.application.Application;	
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;

import javafx.scene.input.KeyEvent;

import javafx.animation.AnimationTimer;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		
		Roundabout roundabout = new Roundabout();
		roundabout.draw();

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				roundabout.draw();
				roundabout.update();
			}
		};

		final Group root = new Group(roundabout);
		final Scene scene = new Scene(root);
		scene.addEventHandler(KeyEvent.KEY_PRESSED, roundabout);
		stage.setScene(scene);
		stage.show();
		timer.start();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
