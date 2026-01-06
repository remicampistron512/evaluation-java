package coursesshop.model;
import coursesshop.model.enums.AttendanceMode;
import java.math.BigDecimal;

public class Course {
    protected int id;
    protected String name;
    protected String description;
    protected int durationDays;
    protected BigDecimal price;
    protected AttendanceMode mode;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationDays() {
        return durationDays;
    }



    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }



    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public AttendanceMode getMode() {
        return mode;
    }

    public void setMode(AttendanceMode mode) {
        this.mode = mode;
    }


    public Course(int id, String name,  String description,int durationDays, BigDecimal price, AttendanceMode mode) {
        this.id = id;
        this.name = name;
        this.durationDays = durationDays;
        this.description = description;
        this.price = price;
        this.mode = mode;
    }


}
