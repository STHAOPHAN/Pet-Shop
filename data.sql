-- Tạo cơ sở dữ liệu
CREATE DATABASE pet_shop;
USE pet_shop;

-- Tạo bảng cho thông tin cửa hàng
CREATE TABLE Shop (
    shop_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(255)
);

INSERT INTO Shop (name, address, phone, email)
VALUES ('Pet World', '123 Main St, Cityville', '555-0123', 'info@petworld.com');

-- Tạo bảng cho thông tin về các loại vật nuôi
CREATE TABLE PetType (
    type_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

INSERT INTO PetType (name, description)
VALUES ('Dog', 'Man''s best friend'),
       ('Cat', 'Furry feline companions'),
       ('Bird', 'Feathered friends'),
       ('Fish', 'Aquatic pets');

-- Tạo bảng cho thông tin về các vật nuôi cụ thể
CREATE TABLE Pet (
    pet_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type_id INT NOT NULL,
    breed VARCHAR(255),
    age INT,
    weight FLOAT,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    image VARCHAR(255), -- Trường mới cho đường dẫn ảnh
    FOREIGN KEY (type_id) REFERENCES PetType(type_id)
);

INSERT INTO Pet (name, type_id, breed, age, weight, price, description, image)
VALUES ('Buddy', 1, 'Labrador Retriever', 3, 25.6, 500.00, 'Friendly and energetic', 'images/buddy.jpg'),
       ('Whiskers', 2, 'Siamese', 2, 4.2, 200.00, 'Playful and curious', 'images/whiskers.jpg'),
       ('Tweety', 3, 'Canary', 1, 0.1, 50.00, 'Cheerful singer', 'images/tweety.jpg'),
       ('Nemo', 4, 'Clownfish', 1, 0.02, 10.00, 'Vibrant and hardy', 'images/nemo.jpg');

-- Tạo bảng để lưu trữ thông tin về các đơn đặt hàng
CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    customer_email VARCHAR(255),
    order_date DATE NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status ENUM('Pending', 'Confirmed', 'Delivered') NOT NULL
);

INSERT INTO Orders (customer_name, customer_phone, customer_email, order_date, total_price, status)
VALUES ('John Doe', '555-1234', 'john@email.com', '2023-06-01', 550.00, 'Confirmed'),
       ('Jane Smith', '555-5678', 'jane@email.com', '2023-06-03', 210.00, 'Delivered');

-- Tạo bảng liên kết giữa Pet và Order để lưu trữ thông tin về các vật nuôi được đặt hàng
CREATE TABLE OrderPet (
    order_id INT NOT NULL,
    pet_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    PRIMARY KEY (order_id, pet_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (pet_id) REFERENCES Pet(pet_id)
);

INSERT INTO OrderPet (order_id, pet_id, quantity)
VALUES (1, 1, 1),
       (1, 3, 1),
       (2, 2, 1);