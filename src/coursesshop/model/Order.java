package coursesshop.model;

import coursesshop.model.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {

  private int id;
  private LocalDateTime createdAt;
  private OrderStatus status;
  private BigDecimal totalAmount;

  public Order(int id, LocalDateTime createdAt, OrderStatus status, BigDecimal totalAmount) {
    this.id = id;
    this.createdAt = createdAt;
    this.status = status;
    this.totalAmount = totalAmount;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }
}
