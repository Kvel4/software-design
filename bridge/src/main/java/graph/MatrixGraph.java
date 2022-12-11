package graph;

import drawing.DrawingApi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MatrixGraph extends Graph {
    private final int[][] g;

    public MatrixGraph(DrawingApi drawingApi, int[][] g) {
        super(drawingApi);
        this.g = g;
    }

    public MatrixGraph(DrawingApi drawingApi, Path path) throws IOException {
        this(drawingApi, Files.readString(path));
    }

    public MatrixGraph(DrawingApi drawingApi, String def) {
        this(drawingApi, parseMatrix(def));
    }

    private static int[][] parseMatrix(String def) {
        String[] lines = def.split("\n");
        int size = Integer.parseInt(lines[0]);
        var res = new int[size][size];
        for (int a = 0; a < size; a++) {
            String[] line = lines[a + 1].split(" +");
            for (int b = 0; b < size; b++) {
                res[a][b] = Integer.parseInt(line[b]);
            }
        }
        return res;
    }

    protected void doDrawGraph() {
        for (int a = 0; a < size(); a++) {
            drawVertex(a);
            for (int b = 0; b < size(); b++) {
                if (g[a][b] == 1) {
                    drawEdge(a, b);
                }
            }
        }
    }

    @Override
    public int size() {
        return g.length;
    }
}
