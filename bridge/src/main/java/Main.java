import drawing.AwtDrawing;
import drawing.DrawingApi;
import drawing.JavaFxDrawing;
import graph.ListGraph;
import graph.Graph;
import graph.MatrixGraph;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    private enum GraphRepresentation {LIST, MATRIX}
    private enum GraphicsApi {JAVAFX, AWT}

    public static void main(String[] args) throws IOException {
        if (args == null || args.length != 3 || Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.out.println("Usage: Main <javafx|awt> <list|matrix> <path-to-graph>");
            return;
        }
        try {
            DrawingApi drawingApi = getDrawingApi(GraphicsApi.valueOf(args[0].toUpperCase()));
            Graph graph = getGraph(drawingApi, GraphRepresentation.valueOf(args[1].toUpperCase()), Path.of(args[2]));
            graph.drawGraph();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static DrawingApi getDrawingApi(GraphicsApi graphicsApi) {
        return switch (graphicsApi) {
            case JAVAFX -> new JavaFxDrawing();
            case AWT -> new AwtDrawing();
        };
    }

    private static Graph getGraph(DrawingApi drawingApi, GraphRepresentation graphMode, Path path) throws IOException {
        return switch (graphMode) {
            case LIST -> new ListGraph(drawingApi, path);
            case MATRIX -> new MatrixGraph(drawingApi, path);
        };
    }
}
