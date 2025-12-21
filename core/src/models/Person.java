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

    public abstract String getRole();

    public String getId() { return id; }
    public String getName() { return name; }
    public Location getLocation() { return location; }

    @Override
    public String toString() {
        return String.format("[%s] %s - Loc: %s", getRole(), name, location);
    }
}