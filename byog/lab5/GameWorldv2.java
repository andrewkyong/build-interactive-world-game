package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class GameWorldv2 {
    private static final int w = 70;
    private static final int h = 40;

    public static class Position {
        private int x;
        private int y;

        public Position(int a, int b) {
            x = a;
            y = b;
        }
    }

    // Sets the possible locations for a room or hallway
    public static Position[] possible_locations;

    // Accepts a number from 0 to 7 and returns a tile type
    private static TETile chooseTile(int ref) {
        switch (ref) {
            case 0:
                return Tileset.FLOOR;
            case 1:
                return Tileset.FLOWER;
            case 2:
                return Tileset.GRASS;
            case 3:
                return Tileset.MOUNTAIN;
            case 4:
                return Tileset.SAND;
            case 5:
                return Tileset.TREE;
            case 6:
                return Tileset.WATER;
            case 7:
                return Tileset.STEEL;
            default:
                return Tileset.NOTHING;
        }
    }

    public static void blockGenerator(TETile[][] world, int seed) {
        possible_locations = new Position[1];
        possible_locations[0] = new Position(0, 0);
        Random rand = new Random(seed);
        int rooms = rand.nextInt(30);
        Random pos = new Random(seed * 2);
        for (int i = 0; i < rooms; i += 1) {
            Position p = possible_locations[pos.nextInt(possible_locations.length)];
            if (i % 2 == 0) {
                updateLocations(addBlock(world, p, seed), p);
            } else {
                // think of a clever way to add a hallway and update available locations for the next room
                int dir = getDirections(world, p);
                addHall(world, p, seed, dir);

            }
        }
    }

    private static void updateLocations(int[] dimensions, Position p) {
        int count = 0;
        int truecount = 0;
        if (p.x > 0) {
            count += dimensions[0] - 2;
        }
        if (p.x + dimensions[0] < w) {
            count += dimensions[0] - 2;
        }
        if (p.y > 0) {
            count += dimensions[1] - 2;
        }
        if (p.y + dimensions[1] < h) {
            count += dimensions[1] - 2;
        }
        possible_locations = new Position[count];
        for (int i = 0; i < dimensions[0] - 2; i += 1) {
            if (p.y > 0) {
                possible_locations[i] = new Position(p.x + i, p.y);
                truecount += 1;
            }
            if (p.y + dimensions[1] < h) {
                possible_locations[truecount] = new Position(p.x + i, p.y + dimensions[1]);
                truecount += 1;
            }
        }
        for (int i = 0; i < dimensions[1] - 2; i += 1) {
            if (p.x > 0) {
                possible_locations[truecount] = new Position(p.x, p.y + 1 + i);
                truecount += 1;
            }
            if (p.x + dimensions[0] < w) {
                possible_locations[truecount] =
                        new Position(p.x + dimensions[0], p.y + 1 + i);
                truecount += 1;
            }
        }
    }


    private static int[] addBlock(TETile[][] world, Position p, int seed) {

        Random dim = new Random(seed / 2);
        Random tiletype = new Random(3 * seed);
        int[] dimensions = new int[2];
        dimensions[0] = dim.nextInt(w - p.x);
        dimensions[1] = dim.nextInt(h - p.y);
        int tile = tiletype.nextInt(8);
        fillLenbyHei(world, dimensions[0], dimensions[1], p, Tileset.WALL, tile);
        return dimensions;
    }

    private static int[] addHall(TETile[][] world, Position p, int seed, int direction) {
        Random len = new Random(seed * 4);
        Random tiletype = new Random(seed * 24);
        direction = Math.floorMod(direction, 4);
        int tile = tiletype.nextInt(8);
        int[] dimensions = new int[2];
        switch (direction) { // 0 right, 1 up, 2 left, 3 down
            case 0:
                dimensions[0] = len.nextInt(w - p.x);
                dimensions[1] = 3;
                parallelLines(world, dimensions[0], dimensions[1], p, Tileset.WALL, direction);
                p.y += 1;
                parallelLines(world, dimensions[0], 1, p, chooseTile(tile), direction);
                return dimensions;
            case 1:
                dimensions[0] = 3;
                dimensions[1] = len.nextInt(h - p.y);
                parallelLines(world, dimensions[0], dimensions[1], p, Tileset.WALL, direction);
                p.x += 1;
                parallelLines(world, 1, dimensions[1], p, chooseTile(tile), direction);
                return dimensions;
            case 2:
                dimensions[0] = len.nextInt(p.x);
                dimensions[1] = 3;
                parallelLines(world, dimensions[0], dimensions[1], p, Tileset.WALL, direction);
                p.y += 1;
                parallelLines(world, dimensions[0], 1, p, chooseTile(tile), direction);
                return dimensions;
            case 3:
                dimensions[0] = 3;
                dimensions[1] = len.nextInt(p.y - 1);
                parallelLines(world, dimensions[0], dimensions[1], p, Tileset.WALL, direction);
                p.x += 1;
                parallelLines(world, 1, dimensions[1], p, chooseTile(tile), direction);
                return dimensions;
            default:
                return null;
        }
    }

    private static int getDirections(TETile[][] world, Position p) {
        if (world[p.x - 1][p.y] == Tileset.NOTHING) {
            return 2;
        } else if (world[p.x + 1][p.y] == Tileset.NOTHING) {
            return 0;
        } else if (world[p.x][p.y + 1] == Tileset.NOTHING) {
            return 1;
        } else {
            return 3;
        }
    }


    // adds len by hei block of chooseTile(tile) to position x, y

    /**
     * For reference:
     * case 0: return Tileset.FLOOR;
     * case 1: return Tileset.FLOWER;
     * case 2: return Tileset.GRASS;
     * case 3: return Tileset.MOUNTAIN;
     * case 4: return Tileset.SAND;
     * case 5: return Tileset.TREE;
     * case 6: return Tileset.WATER;
     * case 7: return Tileset.STEEL;
     * default: return Tileset.NOTHING;
     */

    public static void addManualBlock(TETile[][] world, int len, int hei, int x, int y, int tile) {
        tile = Math.floorMod(tile, 8);
        Position p = new Position(x, y);
        fillLenbyHei(world, len, hei, p, chooseTile(tile), tile);
    }

    private static void fillLenbyHei(TETile[][] world, int len, int hei, Position p, TETile t, int tile) {
        if (len > 0 && hei > 0) {
            parallelLines(world, len, hei, p, t, 0);
            parallelLines(world, len, hei, p, t, 1);
            len -= 2;
            hei -= 2;
            p.x += 1;
            p.y += 1;
            fillLenbyHei(world, len, hei, p, chooseTile(tile), tile);
        }
    }

    // dir 0 right, 1 up, 2 left, 3 down
    private static void parallelLines(TETile[][] world, int length, int height, Position p, TETile t, int dir) {
        switch (dir) {
            case 0:
                for (int i = 0; i < length; i += 1) {
                    world[p.x + i][p.y] = t;
                    world[p.x + i][p.y + height - 1] = t;
                }
                break;
            case 1:
                for (int i = 0; i < height; i += 1) {
                    world[p.x][p.y + i] = t;
                    world[p.x + length - 1][p.y + i] = t;
                }
                break;
            case 2:
                for (int i = 0; Math.abs(i) < length; i += 1) {
                    world[p.x - i][p.y] = t;
                    world[p.x - i][p.y + height - 1] = t;
                }
                break;
            case 3:
                for (int i = 0; Math.abs(i) < height; i += 1) {
                    world[p.x][p.y - i] = t;
                    world[p.x + length - 1][p.y - i] = t;
                }
        }
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(w, h);

        // initialize tiles
        TETile[][] world = new TETile[w][h];
        for (int x = 0; x < w; x += 1) {
            for (int y = 0; y < h; y += 1) {
                world[x][y] = Tileset.GRASS;
            }
        }
        Position yes = new Position(w - 5, h - 5);
        addHall(world, yes, 4322, 2);

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
