package drawing;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class JavaFxDrawing implements DrawingApi {

    private static final List<Shape> shapes = new ArrayList<>();

    @Override
    public void drawCircle(Point c, int r) {
        Circle circle = new Circle(c.x(), c.y(), r);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        shapes.add(circle);
    }

    @Override
    public void drawLine(Point a, Point b) {
        shapes.add(new Line(a.x(), a.y(), b.x(), b.y()));
    }

    @Override
    public void visualize() {
        Application.launch(DrawingJavaFxApplication.class);
    }

    public static class DrawingJavaFxApplication extends Application {

        @Override
        public void start(Stage stage) {
            stage.setTitle("Drew graph using JavaFX");
            Group root = new Group();
            shapes.forEach(root.getChildren()::add);
            stage.setScene(new Scene(root, WIDTH, HEIGHT));
            stage.show();
        }
    }
}
