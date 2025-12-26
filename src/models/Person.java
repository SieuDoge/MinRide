package models;

public abstract class Person {
    protected String id;
    protected String name;
    protected Location location;

    public Person(String id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.location = new Location(x, y);
    }

    public abstract String getID();

    public String getId() { return id; }
    public String getName() { return name; }
    public Location getLocation() { return location; }
    public int getX() { return location.getX(); }
    public int getY() { return location.getY(); }

    @Override
    public String toString() {
        return String.format("[%s] %s - Location: %s", getID(), name, location);
    }
}