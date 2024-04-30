package solid;

public class Part {
    private final TopologyType type;
    private final int start;
    private final int count;

    public Part(TopologyType type, int start, int count) {
        this.type = type;
        this.start = start;
        this.count = count;
    }

    public TopologyType getType() {
        return type;
    }

    public int getStart() {
        return start;
    }

    public int getCount() {
        return count;
    }
}
