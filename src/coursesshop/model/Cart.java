package coursesshop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Cart {
  private int id;



  private int userId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private int quantity;
  private BigDecimal unitPrice;

  public Cart(){

  }
  public Cart(int id, int userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.userId = userId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Cart(int id,int userId, int quantity, BigDecimal unitPrice) {
    this.id = id;
    this.userId = userId;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }
}
