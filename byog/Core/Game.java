package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import byog.lab5.GameWorldv3;

import edu.princeton.cs.introcs.StdDraw;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Game extends Main {

    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     * <p>
     * CURRENTLY WORKING ON THIS FOR PHASE 2!
     */

    public void playWithKeyboard() {
        TERenderer ter = new TERenderer();
        GameWorldv3 gamely = null;
        int i = 0;
        int size = 1;
        char[] cher = new char[size];
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        boolean turnSon = true;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();

                if (c == 'N' || c == 'n') {
                    Game.seedtext();
                } else if ((turnSon) && (c == 'S' || c == 's')) {
                    String seedstring = String.valueOf(cher);
                    long seed = Long.parseLong(seedstring);
                    gamely = new GameWorldv3(seed);
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(gamely.transform());
                    turnSon = false;
                } else if (c == 'L' || c == 'l') {
                    gamely = loadWorld();
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(gamely.transform());
                } else if (c == ':') {
                    break;
                } else if (c == 'Q' || c == 'q') {
                    System.exit(0);
                    break;
                } else if (c == 'W' || c == 'w') {
                    gamely.moveUp();
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(gamely.transform());
                } else if ((!turnSon) && (c == 'S' || c == 's')) {
                    gamely.moveDown();
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(gamely.transform());
                } else if (c == 'A' || c == 'a') {
                    gamely.moveLeft();
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(gamely.transform());
                } else if (c == 'D' || c == 'd') {
                    gamely.moveRight();
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(gamely.transform());
                } else {
                    if (i == 0) {
                        cher[i] = c;
                        size = size + 1;
                        i = i + 1;
                    } else {
                        char[] copy = new char[size];
                        System.arraycopy(cher, 0, copy, 0, cher.length);
                        cher = copy;
                        cher[i] = c;
                        size = size + 1;
                        i = i + 1;
                    }
                }
            }
        }
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char d = StdDraw.nextKeyTyped();
                if (d == 'Q' || d == 'q') {
                    saveWorld(gamely);
                    System.exit(0);
                    break;
                }

            }
        }

    }


    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        char[] charArray = input.toCharArray();
        int length = input.length();
        int indexn = -1;
        GameWorldv3 gamely = null;
        int i = 0;
        int size = 1;
        char[] cher = new char[size];
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        boolean turnSon = true;
        boolean turnNon = true;
        boolean semiColon = true;
        for (int j = 0; j < length; j++) {
            char c = charArray[j];
            if (c == 'N' || c == 'n') {
                turnNon = false;
            } else if ((turnSon) && (c == 'S' || c == 's') && !turnNon) {
                String seedstring = String.valueOf(cher);
                long seed = Long.parseLong(seedstring);
                gamely = new GameWorldv3(seed);
                world = gamely.transform();
                turnSon = false;
            } else if (c == 'L' || c == 'l') {
                gamely = loadWorld();
                if (gamely == null) {
                    world = null;
                } else {
                    world = gamely.transform();
                }
            } else if (c == ':') {
                semiColon = false;
            } else if ((c == 'Q' || c == 'q') && (semiColon)) {
                world = null;
            } else if ((c == 'Q' || c == 'q') && (!semiColon)) {
                saveWorld(gamely);
            } else if (c == 'W' || c == 'w') {
                gamely.moveUp();
                world = gamely.transform();
            } else if ((!turnSon) && (c == 'S' || c == 's')) {
                gamely.moveDown();
                world = gamely.transform();
            } else if ((!turnNon) && (c == 'S' || c == 's')) {
                gamely.moveDown();
                world = gamely.transform();
            } else if ((gamely != null) && (c == 'S' || c == 's')) {
                gamely.moveDown();
                world = gamely.transform();
            } else if (c == 'A' || c == 'a') {
                gamely.moveLeft();
                world = gamely.transform();
            } else if (c == 'D' || c == 'd') {
                gamely.moveRight();
                world = gamely.transform();
            } else if (!turnNon) {
                if (i == 0) {
                    cher[i] = c;
                    size = size + 1;
                    i = i + 1;
                } else {
                    char[] copy = new char[size];
                    System.arraycopy(cher, 0, copy, 0, cher.length);
                    cher = copy;
                    cher[i] = c;
                    size = size + 1;
                    i = i + 1;
                }
            } else if ((indexn == -1) && (j == length - 1)) {
                world = null;
            } else if (j == length - 1) {
                TERenderer ter = new TERenderer();
                ter.initialize(WIDTH, HEIGHT);
                ter.renderFrame(gamely.transform());
            }
        } return world;
    }


    static GameWorldv3 loadWorld() {
        File f = new File("./world.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                GameWorldv3 loadWorld = (GameWorldv3) os.readObject();
                os.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }

        }

        return null;
        /* In the case no World has been saved yet, we return a new one.
        return new GameWorldv3();
        */
    }

    static void saveWorld(GameWorldv3 w) {
        File f = new File("./world.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(w);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private static void seedtext() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.5, 0.5, "Please enter a seed");
        StdDraw.show();
        StdDraw.pause(100);
    }


}


