//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package navigation;

public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    private Direction() {
    }

    public static Direction getNextDirectionClockwise(Direction direction) {
        return values()[(direction.ordinal() + 1) % values().length];
    }

    public static Direction getNextDirectionAClockwise(Direction direction) {
        return values()[(direction.ordinal() + values().length - 1) % values().length];
    }
}
