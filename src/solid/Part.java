package solid;

public class Part {
    private TopologyType type;
    private int start;
    private int count;

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
