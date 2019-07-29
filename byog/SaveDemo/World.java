package byog.SaveDemo;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import byog.Core.RandomUtils;

import java.awt.Color;
import java.io.Serializable;

public class World implements Serializable {
    private static final long serialVersionUID = 123123123123123L;
    private List<Square> squares;
    private Random r = new Random();


    public World() {
        squares = new ArrayList<Square>();
    }

    public void addRandomSquare() {
        double x = 0.5;
        double y = 0.5;
        double x1 = 1;
        double y1 = 0.5;
        double size = RandomUtils.uniform(r, 0.01, 0.1);
        int red = RandomUtils.uniform(r, 0, 256);
        int green = RandomUtils.uniform(r, 0, 256);
        int blue = RandomUtils.uniform(r, 0, 256);
        Color c = new Color(red, green, blue);
        Square newSquare = new Square(x, y, size, c);
        Square newSquare1 = new Square(x1, y1, size, c);
        squares.add(newSquare);
        squares.add(newSquare1);
    }

    public void draw() {
        for (int i = 0; i < squares.size(); i += 1) {
            squares.get(i).draw();
        }
    }
}
