package coursesshop.model;

public class Category {
    protected int id;
    protected String name;

    public Category(int id,String name){
        this.id = id;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }

}
