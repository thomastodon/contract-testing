package thomastodon.io.sinkapp;

public class Egg {

    private String color;
    private String creator;

    public Egg() {
    }

    @Override
    public String toString() {
        return "Egg{" +
            "color='" + color + '\'' +
            ", creator='" + creator + '\'' +
            '}';
    }

    public Egg(String color, String creator) {
        this.color = color;
        this.creator = creator;
    }

    public String getColor() {
        return color;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
