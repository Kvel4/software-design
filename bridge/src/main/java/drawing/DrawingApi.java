package drawing;

public interface DrawingApi {

    int WIDTH = 600;
    int HEIGHT = 400;

    void drawCircle(Point c, int r);

    void drawLine(Point a, Point b);

    void visualize();

    default int getDrawingAreaWidth() {
        return WIDTH;
    }

    default int getDrawingAreaHeight() {
        return HEIGHT;
    }
}
