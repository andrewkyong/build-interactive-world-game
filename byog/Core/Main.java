package byog.Core;

import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;


/**
 * This is the main entry point for the program. This class simply parses
 * the command line inputs, and lets the byog.Core.Game class take over
 * in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            Game game = new Game();
            TETile[][] worldState = game.playWithInputString(args[0]);
            if (worldState != null) {
                System.out.println(TETile.toString(worldState));
            }
        } else {
            StdDraw.enableDoubleBuffering();
            Game game = new Game();
            Main.drawBeginning();
            game.playWithKeyboard();

        }

    }

    private static void drawBeginning() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.5, 0.5, "CS61B: The Game");
        StdDraw.text(0.5, 0.8, "New Game (N)");
        StdDraw.text(0.5, 0.85, "Load Game (L)");
        StdDraw.text(0.5, 0.9, "Quit Game (Q)");
        StdDraw.show();
        StdDraw.pause(100);
    }


}
