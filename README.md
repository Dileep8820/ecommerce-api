Perfect! Let’s make your **README.md** professional and complete, so anyone (or your evaluator) can run your project without issues. I’ll include **setup instructions, OAuth2 steps, sample API requests, and database info**.

Here’s a ready-to-use version for your project:

---

# E-Commerce REST API

A **secure E-Commerce REST API** built with **Java Spring Boot**, supporting **product browsing, category management, and order placement** with **role-based access control** using **OAuth2 authentication**.

---

## **Tech Stack**

* Java 17
* Spring Boot
* Spring Security with OAuth2 (JWT)
* Spring Data JPA / Hibernate
* PostgreSQL / H2 (for testing)
* Maven
* Lombok (optional)

---

## **Project Setup**

### **1. Clone the Repository**

```bash
git clone https://github.com/Dileep8820/ecommerce-api.git
cd ecommerce-api
```

### **2. Configure Database**

* **H2 (in-memory, default for testing)**

  * URL: `jdbc:h2:~/test`
  * Username: `sa`
  * Password: *(leave empty)*
  * H2 console: `http://localhost:8080/h2-console`

* **PostgreSQL (optional)**

  * Update `application.properties` with your DB details:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
    spring.datasource.username=postgres
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    ```

### **3. Build and Run**

```bash
mvn clean install
mvn spring-boot:run
```

Application runs on:

```
http://localhost:8080
```

---

## **OAuth2 Authentication**

### **Generate JWT Token**

* Endpoint: `/auth/token`
* Method: `POST`
* Body (example for CUSTOMER role):

```json
{
  "username": "customer1",
  "password": "password123"
}
```

* Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR..."
}
```

### **Roles**

* `ADMIN` → Can manage categories, products, and view all orders
* `CUSTOMER` → Can place orders and view own orders

> All endpoints (except public product listing) require a valid JWT token in header:

```
Authorization: Bearer <accessToken>
```

---

## **API Endpoints**

### **Category Management (Admin Only)**

| Method | Endpoint           | Description         |
| ------ | ------------------ | ------------------- |
| POST   | `/categories`      | Add new category    |
| PUT    | `/categories/{id}` | Update category     |
| DELETE | `/categories/{id}` | Delete category     |
| GET    | `/categories`      | List all categories |

### **Product Management**

| Method | Endpoint                  | Description          | Auth       |
| ------ | ------------------------- | -------------------- | ---------- |
| GET    | `/products`               | List all products    | Public     |
| GET    | `/products/category/{id}` | Products by category | Public     |
| POST   | `/products`               | Add product          | ADMIN only |
| PUT    | `/products/{id}`          | Update product       | ADMIN only |
| DELETE | `/products/{id}`          | Delete product       | ADMIN only |

### **Order Management**

| Method | Endpoint              | Description         | Auth          |
| ------ | --------------------- | ------------------- | ------------- |
| POST   | `/orders`             | Place an order      | CUSTOMER only |
| GET    | `/orders`             | View own orders     | CUSTOMER only |
| GET    | `/orders/all`         | View all orders     | ADMIN only    |
| PUT    | `/orders/{id}/status` | Update order status | ADMIN only    |

---

## **Java Streams Usage**

* Order total calculation
* Product price aggregation
* Quantity-based calculations

All implemented using **Java Streams** for efficiency and readability.

---

## **Error Handling**

* Global exception handling with meaningful HTTP status codes:

  * `400 Bad Request`
  * `401 Unauthorized`
  * `403 Forbidden`
  * `404 Not Found`

---

## **Database Schema**

* **Category**: `id`, `name`, `description`
* **Product**: `id`, `name`, `price`, `stock`, `category_id`
* **Order**: `id`, `customer_id`, `status`, `total`, `items`

*(ER diagram can be added as `ecommerce-ER-diagram.png` if needed)*

---

## **Sample cURL Requests**

### **Get All Products**

```bash
curl -X GET http://localhost:8080/products
```

### **Place an Order (Customer)**

```bash
curl -X POST http://localhost:8080/orders \
-H "Authorization: Bearer <accessToken>" \
-H "Content-Type: application/json" \
-d '{
  "items": [
    {"productId": 1, "quantity": 2},
    {"productId": 3, "quantity": 1}
  ]
}'
```

### **Add Category (Admin)**

```bash
curl -X POST http://localhost:8080/categories \
-H "Authorization: Bearer <accessToken>" \
-H "Content-Type: application/json" \
-d '{"name": "Electronics", "description": "Electronic devices"}'
```


