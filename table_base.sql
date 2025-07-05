--CREATE BY JerryNguyen
-- Date: 01/07/2025
CREATE DATABASE IF NOT EXISTS udemy_shopapp;
USE udemy_shopapp;

-- Roles 
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

-- Users
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL DEFAULT '',
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(100) NOT NULL DEFAULT '',
    create_at DATETIME,
    update_at DATETIME,
    is_active TINYINT(1) DEFAULT 1,
    date_of_birth DATE,
    facebook_id INT DEFAULT 0,
    google_id INT DEFAULT 0,
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Tokens 
CREATE TABLE tokens (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    expiration_date DATETIME,
    revoked TINYINT(1) NOT NULL,
    expired TINYINT(1) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) 
);

-- Social accounts 
CREATE TABLE social_accounts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    provider VARCHAR(20) NOT NULL COMMENT 'e.g., facebook, google',
    provider_id VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Categories 
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

-- Products 
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(300) NOT NULL,
    description LONGTEXT DEFAULT '',
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    category_id INT,
    thumpnail VARCHAR(300) DEFAULT '',
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Product images 
CREATE TABLE product_images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    image_url VARCHAR(300) NOT NULL,
    CONSTRAINT fk_product_image_id FOREIGN KEY (product_id)
        REFERENCES products(id) ON DELETE CASCADE
);

-- Orders 
CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    fullname VARCHAR(100) DEFAULT '',
    email VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    note VARCHAR(100) DEFAULT '',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'processing', 'shipped','delivered', 'cancelled') DEFAULT 'pending',
    total DECIMAL(10, 2) CHECK (total >= 0),
    shipping_method VARCHAR(100),
    shipping_address VARCHAR(200),
    shipping_date DATE,
    tracking_number VARCHAR(100),
    payment_method VARCHAR(100),
    active TINYINT(1),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Order details 
CREATE TABLE order_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL CHECK (quantity > 0),
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    total DECIMAL(10, 2) NOT NULL CHECK (total >= 0),
    color VARCHAR(20) DEFAULT '',
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
