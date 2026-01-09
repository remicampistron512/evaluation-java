CREATE TABLE delivery_mode (
  code  VARCHAR(10) PRIMARY KEY,
  label VARCHAR(50) NOT NULL
);

CREATE TABLE order_status (
  code  VARCHAR(12) PRIMARY KEY,
  label VARCHAR(50) NOT NULL
);

CREATE TABLE user_ (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  first_name    VARCHAR(100) NOT NULL,
  last_name     VARCHAR(100) NOT NULL,
  login         VARCHAR(80)  NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

);

CREATE TABLE customer (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name  VARCHAR(100) NOT NULL,
  email      VARCHAR(190) NOT NULL,
  address    VARCHAR(255) NOT NULL,
  phone      VARCHAR(40)  NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE category (
  id   INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL,

);

CREATE TABLE course (
  id                 INT AUTO_INCREMENT PRIMARY KEY,
  name               VARCHAR(200) NOT NULL,
  description        TEXT NOT NULL,
  duration_days      INT NOT NULL,
  price              DECIMAL(10,2) NOT NULL,
  delivery_mode_code VARCHAR(10) NOT NULL,
  active             TINYINT(1) NOT NULL DEFAULT 1,
  created_at         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (delivery_mode_code) REFERENCES delivery_mode(code)
);

CREATE TABLE course_category (
  course_id   INT NOT NULL,
  category_id INT NOT NULL,
  PRIMARY KEY (course_id, category_id),
  FOREIGN KEY (course_id) REFERENCES course(id),
  FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE cart (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  user_id    INT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user_(id)
);

CREATE TABLE cart_item (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  cart_id    INT NOT NULL,
  course_id  INT NOT NULL,
  quantity   INT NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (cart_id) REFERENCES cart(id),
  FOREIGN KEY (course_id) REFERENCES course(id),
  UNIQUE (cart_id, course_id)
);

CREATE TABLE order_ (
  id           INT AUTO_INCREMENT PRIMARY KEY,
  user_id      INT NOT NULL,
  customer_id  INT NOT NULL,
  status_code  VARCHAR(12) NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user_(id),
  FOREIGN KEY (customer_id) REFERENCES customer(id),
  FOREIGN KEY (status_code) REFERENCES order_status(code)
);

CREATE TABLE line_order (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  order_id   INT NOT NULL,
  course_id  INT NOT NULL,
  quantity   INT NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (order_id) REFERENCES order_(id),
  FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE INDEX idx_course_mode ON course(delivery_mode_code);

CREATE INDEX idx_course_category_course ON course_category(course_id);
CREATE INDEX idx_course_category_category ON course_category(category_id);

CREATE INDEX idx_cart_user ON cart(user_id);
CREATE INDEX idx_cart_item_cart ON cart_item(cart_id);

CREATE INDEX idx_order_user ON order_(user_id);
CREATE INDEX idx_order_customer ON order_(customer_id);
CREATE INDEX idx_order_status ON order_(status_code);

CREATE INDEX idx_line_order_order ON line_order(order_id);
CREATE INDEX idx_line_order_course ON line_order(course_id);
