package thomastodon.io.sourceapp;

public class Egg {

    public Egg() {
        this.creator = "duck";
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

    @Override
    public String toString() {
        return "Egg{" +
            "color='" + color + '\'' +
            ", creator='" + creator + '\'' +
            '}';
    }

    private String color;
    private String creator;

    public void setColor(String color) {
        this.color = color;
    }
}
