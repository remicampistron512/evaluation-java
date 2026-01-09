package coursesshop.model;

import java.math.BigDecimal;

public class CartItem {
  private int id;          // cit_id
  private int courseId;    // cou_id
  private int quantity;    // cit_quantity
  private BigDecimal unitPrice; // cit_unit_price

  public CartItem() {}

  public CartItem(int id, int courseId, int quantity, BigDecimal unitPrice) {
    this.id = id;
    this.courseId = courseId;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
  }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public int getCourseId() { return courseId; }
  public void setCourseId(int courseId) { this.courseId = courseId; }

  public int getQuantity() { return quantity; }
  public void setQuantity(int quantity) { this.quantity = quantity; }

  public BigDecimal getUnitPrice() { return unitPrice; }
  public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}
