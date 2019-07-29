package byog.lab5;


public class TestUpdate {
    private static final int w = 70;
    private static final int h = 40;

    public static class Position {
        private int x;
        private int y;

        public Position(int a, int b) {
            x = a;
            y = b;
        }

        private void print() {
            System.out.println(x + "  " + y);
        }
    }

    // Sets the possible locations for a room or hallway
    public static Position[] possible_locations;

    public static void updateLocations(int[] dimensions, Position p) {
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

    public static void main(String[] args) {
        int[] yes = new int[]{3, 5};
        Position p = new Position(67, 35);
        updateLocations(yes, p);
        for (Position s : possible_locations) {
            s.print();
        }
    }
}
