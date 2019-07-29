package byog.lab5;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

public class GameWorldv3 implements Serializable {
    // Dimensions of the room
    private static final int W = 70;
    private static final int H = 40;
    private long SEED;

    // Whether or not there is a secret door
    private boolean hasSecretDoor = false;
    private boolean hasPlayer = false;
    private Position doorPos;
    private Position playerPos;
    private int numberTile;
    private TETile ref = chooseTile(numberTile);

    // Sets the start locations for rooms and hallways
    private Position[] startLocations;
    private TETile[][] world;

    public GameWorldv3(long seed) {
        SEED = seed;
    }

    public class Position implements Serializable {
        private int x;
        private int y;

        /**
         * Constructor for Position objects.
         *
         * @param a is the x position.
         * @param b is the y position.
         */
        public Position(int a, int b) {
            x = a;
            y = b;
        }

        /**
         * Determines if a position is the same as some position p.
         * Could be simplified, but leaving as is for clarity.
         *
         * @param p is the one we compare with
         * @return a truth value.
         */
        private boolean isSame(Position p) {
            if (p == null) {
                return false;
            }
            return (p.x == x && p.y == y);
        }

        private void print() {
            System.out.println(x + " " + y);
        }
    }

    /**
     * Generates the world.
     *
     * @param world1 the 2D array we're inputting tiles into
     * @param seed  the number that determines all parameters using randomness
     */
    public void generateWorld(TETile[][] world1, long seed) {
        // Initialize randomness
        Random ran = new Random(seed);

        // Generate starting points for rooms and hallways
        startingPoints(ran);

        // Choose a tile for the map
        int tile = ran.nextInt(8);
        numberTile = tile;

        // Add a block of random size to each point
        for (Position s : startLocations) {
            addRectangle(world1, s, ran, tile);
        }

        // Connects each point with at least two other points by hallway
        for (int i = 0; i < startLocations.length; i += 1) {
            int iter = ran.nextInt(3);
            connectPoints(startLocations[i],
                    startLocations[Math.floorMod(i + 1, startLocations.length)], world1, ran, tile);
            hallConnector(startLocations[i], iter, ran, world1, tile);
        }

        // Add locked door to the world
        addSecretDoor(world1, ran.nextInt(40) + 2);
        addPlayer(world1, ran.nextInt(50) + 1);


    }

    /**
     * Generates a set of 8 - 15 starting points for rooms
     *
     * @param rand the random object we use to generate random numbers
     */
    private void startingPoints(Random rand) {
        // sets the number of starting points between 8 - 15
        int pts = rand.nextInt(8) + 8;
        // fills up the Position array start_locations with designated number of start positions
        startLocations = new Position[pts];
        for (int i = 0; i < pts; i += 1) {
            startLocations[i] = new Position(rand.nextInt(W - 5) + 2,
                    rand.nextInt(H - 5) + 2);
        }
    }

    /**
     * Connects point p to iter number of other points in the start_locations
     *
     * @param p     point we start from
     * @param iter  number of other points p connects to
     * @param rand  random object to generate numbers
     * @param worldj the world we fill with tiles
     * @param tile  the tile type in question
     */
    private void hallConnector(Position p, int iter, Random rand, TETile[][] worldj, int tile) {
        for (int i = 0; i < iter; i += 1) {
            int ind = rand.nextInt(startLocations.length);
            int seed = rand.nextInt();
            if (!p.isSame(startLocations[ind])) {
                connectPoints(p, startLocations[ind], worldj, rand, tile);
            }
        }
    }

    /**
     * Connects points p1 and p2 with hallways of a specified tile
     *
     * @param p1    first point
     * @param p2    second point
     * @param worlx world we're filling with tiles
     * @param rand  Random object for random numbers
     * @param tile  type of tile
     */
    private void connectPoints(Position p1, Position p2, TETile[][] worlx, Random rand, int tile) {
        int order = rand.nextInt(2);
        switch (order) {
            case 0:
                connectx(p1, p2, tile, worlx);
                connecty(p1, p2, tile, worlx);
                break;
            case 1:
                connecty(p1, p2, tile, worlx);
                connectx(p1, p2, tile, worlx);
                break;
            default:
                break;
        }
    }

    /**
     * Connects a hallway of some tile from the x position of p to the x of end.
     *
     * @param p     starting point
     * @param end   endpoint
     * @param tile  type of tile
     * @param worldm the world we fill
     */
    private void connectx(Position p, Position end, int tile, TETile[][] worldm) {
        int x = p.x;
        addTile(worldm, p, tile);
        if (p.x == end.x) {
            return;
        } else if (end.x < p.x) {
            x -= 1;
        } else {
            x += 1;
        }
        Position update = new Position(x, p.y);
        connectx(update, end, tile, worldm);
    }

    // Connects a hallway of some tile from the y position of p to the y of end. Same as connectx
    private void connecty(Position p, Position end, int tile, TETile[][] worlds) {
        int y = p.y;
        addTile(worlds, p, tile);
        if (end.y == p.y) {
            return;
        } else if (end.y < p.y) {
            y -= 1;
        } else {
            y += 1;
        }
        Position update = new Position(p.x, y);
        connecty(update, end, tile, worlds);
    }


    private void addTile(TETile[][] worldq, Position p, int tile) {
        worldq[p.x][p.y] = chooseTile(tile);
        for (int i = 0; i < 3; i += 1) {
            for (int j = 0; j < 3; j += 1) {
                addWall(worldq, p.x - 1 + i, p.y - 1 + j);
            }
        }
    }


    private void addWall(TETile[][] worldv, int x, int y) {
        if (worldv[x][y] == Tileset.NOTHING) {
            worldv[x][y] = Tileset.WALL;
        }
    }


    private void addSecretDoor(TETile[][] worldt, int stop) {
        // initializes count of valid walls
        int wallTally = 0;

        // replaces wall with door at once wall_tally is equal to stop
        for (int i = 0; !hasSecretDoor; i += 1) {
            for (int j = 0; j < world[i].length; j += 1) {
                if (worldt[i][j] == Tileset.WALL) {
                    wallTally += 1;
                }
                if (wallTally == stop) {
                    worldt[i][j] = Tileset.LOCKED_DOOR;
                    hasSecretDoor = true;
                    doorPos = new Position(i, j);
                    break;
                }
            }
        }
    }


    private void addPlayer(TETile[][] worldg, int stop) {
        // initializes count of valid walls
        int playerTally = 0;

        // replaces wall with door at once wall_tally is equal to stop
        for (int i = 0; !hasPlayer; i += 1) {
            for (int j = 0; j < worldg[i].length; j += 1) {
                if ((worldg[i][j] != Tileset.WALL) && (worldg[i][j] != Tileset.NOTHING)) {
                    playerTally += 1;
                }
                if (playerTally == stop) {
                    worldg[i][j] = Tileset.PLAYER;
                    hasPlayer = true;
                    playerPos = new Position(i, j);
                    break;
                }
            }
        }
    }

    private void addRectangle(TETile[][] worldy, Position p, Random rand, int tile) {
        // set desired parameters for rectangle, INCLUDING the walls
        int desiredWidth = rand.nextInt(W / 5) + 4;
        int desiredHeight = rand.nextInt(H / 5) + 4;

        // Initialize width parameter
        int width = 0;

        // drawing rightward and upward, updating the width
        width = drawTiles(width, desiredWidth, desiredHeight, 1, worldy, p, tile);

        //drawing leftward and downward
        drawTiles(width, desiredWidth, desiredHeight, -1, worldy, p, tile);
    }

    private int drawTiles(int width, int desiredWidth, int desiredHeight, int incr,
                          TETile[][] worldp, Position p, int tile) {
        for (int x = p.x; width + 2 <= desiredWidth; x += incr) {
            // checks we can place a block here relative to x limits
            if (x > 1 && x < W - 1) {
                int height = 0;
                for (int y = p.y; height + 2 <= desiredHeight; y += incr) {
                    if (y > 1 && y < H - 1) {
                        Position target = new Position(x, y);
                        addTile(worldp, target, tile);
                        height += 1;
                    } else {
                        break;
                    }
                }
                width += 1;
            } else {
                break;
            }
        }
        return width;
    }

    private TETile chooseTile(int fly) {
        switch (fly) {
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

    public TETile[][] transform() {

        // initialize tiles
        world = new TETile[W][H];
        for (int x = 0; x < W; x += 1) {
            for (int y = 0; y < H; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // generates the world, accepting some seed
        generateWorld(world, SEED);

        if (doorPos != null) {
            world[doorPos.x][doorPos.y] = Tileset.LOCKED_DOOR;
        }

        if (playerPos != null) {
            world[playerPos.x][playerPos.y] = Tileset.PLAYER;
        }

        return world;
    }

    public void moveRight() {
        if (playerPos.x == 0) {
            return;
        } else if (world[playerPos.x + 1][playerPos.y] != Tileset.WALL) {
            world[playerPos.x + 1][playerPos.y] = Tileset.PLAYER;
            world[playerPos.x][playerPos.y] = ref;
            playerPos.x += 1;

        }

    }

    public void moveLeft() {
        if (playerPos.x == 0) {
            return;
        } else if (world[playerPos.x - 1][playerPos.y] != Tileset.WALL) {
            world[playerPos.x - 1][playerPos.y] = Tileset.PLAYER;
            world[playerPos.x][playerPos.y] = ref;
            playerPos.x -= 1;
        }
    }

    public void moveUp() {
        if (playerPos.y == 0) {
            return;
        } else if (world[playerPos.x][playerPos.y + 1] != Tileset.WALL) {
            world[playerPos.x][playerPos.y + 1] = Tileset.PLAYER;
            world[playerPos.x][playerPos.y] = ref;
            playerPos.y += 1;
        }
    }

    public void moveDown() {
        if (playerPos.y == 0) {
            return;
        } else if (world[playerPos.x][playerPos.y - 1] != Tileset.WALL) {
            world[playerPos.x][playerPos.y - 1] = Tileset.PLAYER;
            world[playerPos.x][playerPos.y] = ref;
            playerPos.y -= 1;
        }
    }

}



