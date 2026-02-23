-- =============================================
-- FoodApp Microservices - Database Init Script
-- PostgreSQL 16
-- =============================================

-- Create tables
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(500),
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(500),
    stock INTEGER DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    category_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(500) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_products_name ON products(name);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_token ON refresh_tokens(token);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user ON refresh_tokens(user_id);

-- =============================================
-- Seed Data
-- =============================================

-- Roles
INSERT INTO roles (name) VALUES ('ROLE_USER') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT (name) DO NOTHING;

-- Users (passwords hashed with BCrypt)
-- admin / admin123
INSERT INTO users (username, email, password, role_id, enabled) VALUES
    ('admin', 'admin@foodapp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 2, true)
ON CONFLICT (username) DO NOTHING;

-- user / user123
INSERT INTO users (username, email, password, role_id, enabled) VALUES
    ('user', 'user@foodapp.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 1, true)
ON CONFLICT (username) DO NOTHING;

-- Categories
INSERT INTO categories (name, description, image_url, active) VALUES
    ('Pizzas', 'Delicious handmade pizzas with fresh ingredients', 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=400', true),
    ('Burgers', 'Juicy gourmet burgers with premium toppings', 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400', true),
    ('Sushi', 'Fresh Japanese sushi and rolls', 'https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=400', true),
    ('Drinks', 'Refreshing beverages and cocktails', 'https://images.unsplash.com/photo-1544145945-f90425340c7e?w=400', true),
    ('Desserts', 'Sweet treats and homemade desserts', 'https://images.unsplash.com/photo-1551024601-bec78aea704b?w=400', true)
ON CONFLICT (name) DO NOTHING;

-- Products
INSERT INTO products (name, description, price, image_url, stock, active, category_id) VALUES
    -- Pizzas (category_id = 1)
    ('Margherita Pizza', 'Classic tomato sauce, fresh mozzarella, and basil', 12.99, 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=400', 50, true, 1),
    ('Pepperoni Pizza', 'Loaded with pepperoni and melted cheese', 14.99, 'https://images.unsplash.com/photo-1628840042765-356cda07504e?w=400', 40, true, 1),
    ('BBQ Chicken Pizza', 'Grilled chicken, BBQ sauce, red onion, and cilantro', 16.99, 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=400', 30, true, 1),

    -- Burgers (category_id = 2)
    ('Classic Cheeseburger', 'Angus beef patty with cheddar, lettuce, and tomato', 11.99, 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400', 60, true, 2),
    ('Bacon Deluxe Burger', 'Double patty with crispy bacon and special sauce', 15.99, 'https://images.unsplash.com/photo-1553979459-d2229ba7433b?w=400', 35, true, 2),
    ('Veggie Burger', 'Plant-based patty with avocado and sprouts', 13.99, 'https://images.unsplash.com/photo-1520072959219-c595dc870360?w=400', 25, true, 2),

    -- Sushi (category_id = 3)
    ('California Roll', 'Crab, avocado, and cucumber wrapped in rice', 9.99, 'https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=400', 45, true, 3),
    ('Salmon Nigiri', 'Fresh salmon over pressed vinegared rice', 12.99, 'https://images.unsplash.com/photo-1583623025817-d180a2221d0a?w=400', 30, true, 3),
    ('Dragon Roll', 'Eel, crab, avocado with special eel sauce', 16.99, 'https://images.unsplash.com/photo-1617196034796-73dfa7b1fd56?w=400', 20, true, 3),

    -- Drinks (category_id = 4)
    ('Fresh Lemonade', 'Homemade lemonade with mint leaves', 4.99, 'https://images.unsplash.com/photo-1621263764928-df1444c5e859?w=400', 100, true, 4),
    ('Tropical Smoothie', 'Mango, pineapple, and coconut milk blend', 6.99, 'https://images.unsplash.com/photo-1505252585461-04db1eb84625?w=400', 80, true, 4),
    ('Iced Coffee', 'Cold brew coffee with vanilla and cream', 5.99, 'https://images.unsplash.com/photo-1461023058943-07fcbe16d735?w=400', 90, true, 4),

    -- Desserts (category_id = 5)
    ('Chocolate Lava Cake', 'Warm chocolate cake with molten center', 8.99, 'https://images.unsplash.com/photo-1551024601-bec78aea704b?w=400', 40, true, 5),
    ('Tiramisu', 'Classic Italian coffee-flavored layered dessert', 7.99, 'https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=400', 35, true, 5),
    ('Cheesecake', 'New York style creamy cheesecake with berry compote', 9.99, 'https://images.unsplash.com/photo-1565958011703-44f9829ba187?w=400', 30, true, 5)
ON CONFLICT DO NOTHING;
