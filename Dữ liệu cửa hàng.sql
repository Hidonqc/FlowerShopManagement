-- Tạo cơ sở dữ liệu
CREATE DATABASE FlowerShop_Management;
GO
USE FlowerShop_Management;
GO

-- Tạo bảng Users
CREATE TABLE Users (
    ID_User INT PRIMARY KEY,
    Email VARCHAR(50) NOT NULL,
    Password VARCHAR(20) NOT NULL,
    VerifyCode VARCHAR(10) DEFAULT NULL,
    Role VARCHAR(20) CHECK (Role IN ('Khach Hang', 'Nhan Vien', 'Quan Ly'))
);
GO

-- Tạo bảng Staff
CREATE TABLE Staff (
    ID_Staff INT PRIMARY KEY,
    NameStaff VARCHAR(50) NOT NULL,
    JoinDate DATE NOT NULL,
    PhoneNumber VARCHAR(50) NOT NULL,
    Role VARCHAR(50) CHECK (Role IN ('Phuc vu', 'Quan ly')),
    ID_User INT NULL,
    ID_Manager INT NULL,
    FOREIGN KEY (ID_User) REFERENCES Users(ID_User),
    FOREIGN KEY (ID_Manager) REFERENCES Staff(ID_Staff)
);
GO

-- Tạo bảng Customers
CREATE TABLE Customers (
    customer_ID INT PRIMARY KEY,
    customer_Name VARCHAR(50) NOT NULL,
    JoinDate DATE NOT NULL,
    ID_User INT NOT NULL,
    FOREIGN KEY (ID_User) REFERENCES Users(ID_User)
);
GO

-- Tạo bảng Products
CREATE TABLE Products (
    product_ID INT PRIMARY KEY,
    product_Name NVARCHAR(100) NOT NULL,
    price FLOAT NOT NULL CHECK (price >= 0),
    stock INT NOT NULL CHECK (stock >= 0),
    type NVARCHAR(100) NOT NULL,
    season NVARCHAR(100) NOT NULL,
    CONSTRAINT UQ_ProductNameType UNIQUE (product_Name, type)
);
GO

-- Tạo bảng Orders
CREATE TABLE Orders (
    order_ID INT PRIMARY KEY,
    customer_Name NVARCHAR(250) NOT NULL,
    customer_ID INT NOT NULL,
    order_Day DATE NOT NULL,
    total_Amount FLOAT NOT NULL,
    FOREIGN KEY (customer_ID) REFERENCES Customers(customer_ID)
);
GO

-- Tạo bảng OrderDetails
CREATE TABLE OrderDetails (
    order_ID INT NOT NULL,
    product_ID INT NOT NULL,
    quantity INT NOT NULL,
    totalAmount FLOAT NOT NULL,
    PRIMARY KEY (order_ID, product_ID),
    FOREIGN KEY (order_ID) REFERENCES Orders(order_ID),
    FOREIGN KEY (product_ID) REFERENCES Products(product_ID)
);
GO

-- TẠO PROCEDURE
CREATE PROCEDURE getAllStaff
AS
BEGIN
    SELECT ID_Staff, NameStaff, JoinDate, PhoneNumber, Role, ID_Manager
    FROM Staff;
END


CREATE PROCEDURE addStaff
    @staff_id INT,
    @name NVARCHAR(255),
    @join_date DATE,
    @phone NVARCHAR(15),
    @role NVARCHAR(50),
    @manager_id INT
AS
BEGIN
    INSERT INTO Staff (ID_Staff, NameStaff, JoinDate, PhoneNumber, role, ID_Manager)
    VALUES (@staff_id, @name, @join_date, @phone, @role, @manager_id);
END


CREATE PROCEDURE updateStaff
    @staff_id INT,
    @name NVARCHAR(255),
    @join_date DATE,
    @phone NVARCHAR(15),
    @role NVARCHAR(50),
    @manager_id INT
AS
BEGIN
    UPDATE Staff
    SET NameStaff = @name,
        JoinDate = @join_date,
        PhoneNumber = @phone,
        role = @role,
        ID_Manager = @manager_id
    WHERE ID_Staff = @staff_id;
END


CREATE PROCEDURE sp_GetAllCustomers
AS
BEGIN
    SELECT customer_id, customer_Name AS getCustomer_Name, joinDate
    FROM Customers;
END

CREATE PROCEDURE sp_GetCustomerBasicInfo
    @customer_id INT
AS
BEGIN
    SELECT customer_id, customer_Name AS customer_name, JoinDate
    FROM Customers
    WHERE customer_id = @customer_id;
END


CREATE PROCEDURE sp_CreateNewOrder
    @customer_id INT,
    @customer_Name INT,
    @order_id INT OUTPUT
AS
BEGIN
    INSERT INTO Orders (customer_id, customer_Name, order_day)
    VALUES (@customer_id, @customer_Name, GETDATE());

    SET @order_id = SCOPE_IDENTITY();
END

CREATE PROCEDURE sp_ProcessOrderItem
    @order_id INT,
    @product_id INT,
    @quantity INT
AS
BEGIN
    INSERT INTO OrderDetails (order_id, product_id, quantity)
    VALUES (@order_id, @product_id, @quantity);
END

CREATE PROCEDURE getAllProducts
AS
BEGIN
    SELECT product_ID, product_Name, price, type, season, stock
    FROM Products
	;
END

CREATE PROCEDURE sp_GetMaxProductId
    @max_id INT OUTPUT
AS
BEGIN
    SELECT @max_id = MAX(product_ID) FROM Products;
END

CREATE PROCEDURE sp_SearchProducts
    @name NVARCHAR(255),
    @type NVARCHAR(255),
    @season NVARCHAR(255)
AS
BEGIN
    SELECT * FROM Products
    WHERE (product_Name LIKE '%' + @name + '%' OR @name IS NULL)
    AND (type = @type OR @type IS NULL)
    AND (season = @season OR @season IS NULL);
END

CREATE PROCEDURE addProduct
    @id INT,
    @name NVARCHAR(255),
    @type NVARCHAR(255),
    @season NVARCHAR(255),
    @price FLOAT,
    @stock INT
AS
BEGIN
    INSERT INTO Products (product_ID, product_Name, type, season, price, stock)
    VALUES (@id, @name, @type, @season, @price, @stock);
END

CREATE PROCEDURE updateProduct
    @id INT,
    @name NVARCHAR(255),
    @type NVARCHAR(255),
    @season NVARCHAR(255),
    @price FLOAT,
    @stock INT
AS
BEGIN
    UPDATE Products
    SET product_Name = @name, type = @type, season = @season, price = @price, stock = @stock
    WHERE product_ID = @id;
END

CREATE PROCEDURE getRevenueReport
    @period NVARCHAR(50),
    @date DATE
AS
BEGIN
    SELECT @period AS period, SUM(total_amount) AS total
    FROM Orders
    WHERE order_day >= DATEADD(DAY, -30, @date);
END

CREATE PROCEDURE sp_CheckStock
    @product_id INT,
    @stock INT OUTPUT
AS
BEGIN
    SELECT @stock = stock
    FROM Products
    WHERE product_id = @product_id;
END


CREATE PROCEDURE sp_UpdateCustomerName
    @customer_id INT,
    @name NVARCHAR(255),
    @joinDate DATE
AS
BEGIN
    UPDATE Customers
    SET customer_name = @name, joinDate = @joinDate
    WHERE customer_id = @customer_id;
END

CREATE PROCEDURE sp_GetCustomerOrders
    @customer_id INT
AS
BEGIN
    SELECT order_id, customer_id, customer_name, order_day, total_amount
    FROM Orders
    WHERE customer_id = @customer_id;
END

CREATE PROCEDURE sp_GetOrderDetails
    @order_id INT
AS
BEGIN
    SELECT order_id, product_id, quantity, totalAmount
    FROM OrderDetails
    WHERE order_id = @order_id;
END

CREATE OR ALTER PROCEDURE sp_LoginUser
    @email NVARCHAR(255),
    @password NVARCHAR(255)
AS
BEGIN
    SELECT 
        u.ID_User  AS ID_User,
		c.customer_ID AS customer_ID, 
        s.ID_Staff AS ID_Staff, 
        CASE 
            WHEN c.customer_ID IS NOT NULL THEN 'Khach Hang'
            WHEN s.ID_Staff IS NOT NULL THEN 'Nhan Vien'
            ELSE 'Quan Ly'
        END AS Role
    FROM Users u
    LEFT JOIN Customers c ON u.ID_User = c.customer_ID
    LEFT JOIN Staff s ON u.ID_User = s.ID_Staff
	  WHERE u.email = CAST(@email AS NVARCHAR(255)) 
    AND u.password = CAST(@password AS NVARCHAR(255));
END

INSERT INTO Customers (customer_ID, customer_Name, joindate, ID_User)
VALUES (101, 'Nguyễn Ngọc Bảo Trân', '2020-01-15', 109);
INSERT INTO Customers (customer_ID, customer_Name, joindate, ID_User)
VALUES (102, 'Đỗ Đoàn Quốc Tín', '2020-02-20', 110);
INSERT INTO Customers (customer_ID, customer_Name, joindate, ID_User)
VALUES (103, 'Đào Quỳnh Thi', '2020-03-10', 111);
INSERT INTO Customers (customer_ID, customer_Name, joindate, ID_User)
VALUES (104, 'Nguyễn Trung Hiền', '2020-04-05', 112);
INSERT INTO Customers (customer_ID, customer_Name, joindate, ID_User)
VALUES (105, 'Phạm Minh Tuấn', '2020-05-12', 113);
INSERT INTO Customers (customer_ID, customer_Name, joindate, ID_User)
VALUES (106, 'Vũ Thị Eo', '2020-06-18', 114);
INSERT INTO Customers (customer_ID, customer_Name, joindate, ID_User)
VALUES (107, 'Tôn Thất Gia Huy', '2020-07-22', 115);
INSERT INTO Customers (customer_ID, customer_Name, joindate, ID_User)
VALUES (108, 'Bùi Thị Giang', '2020-08-30', 116);
INSERT INTO Customers (customer_ID, customer_Name, joindate, ID_User)
VALUES (109, 'Ngô Văn Hải', '2020-09-05', 117);
INSERT INTO Customers (customer_ID, customer_Name, joindate, ID_User)
VALUES (110, 'Dương Thị Hoa', '2020-10-10', 118);

-- Thêm data cho bảng Staff

INSERT INTO Staff (ID_Staff, NameStaff, JoinDate, PhoneNumber, Role, ID_Manager, ID_User)
VALUES (101, 'Trần Chiến Đấu', '2019-02-15', '0987654321', 'Quan ly', NULL, 1001);
INSERT INTO Staff (ID_Staff, NameStaff, JoinDate, PhoneNumber, Role, ID_Manager, ID_User)
VALUES (102, 'Nguyễn Nhân Sự', '2019-06-20', '0978123456', 'Phuc vu', 101, 102);
INSERT INTO Staff (ID_Staff, NameStaff, JoinDate, PhoneNumber, Role, ID_Manager, ID_User)
VALUES (103, 'Phạm Kế Toán', '2019-08-25', '0967832154', 'Phuc vu', 101, 103);
INSERT INTO Staff (ID_Staff, NameStaff, JoinDate, PhoneNumber, Role, ID_Manager, ID_User)
VALUES (104, 'Hoàng Văn Nhân', '2020-01-05', '0911223344', 'Phuc vu', 101, 104);
INSERT INTO Staff (ID_Staff, NameStaff, JoinDate, PhoneNumber, Role, ID_Manager, ID_User)
VALUES (105, 'Vũ Thị Viên', '2020-03-10', '0922334455', 'Phuc vu', 101, 105);
INSERT INTO Staff (ID_Staff, NameStaff, JoinDate, PhoneNumber, Role, ID_Manager, ID_User)
VALUES (106, 'Đặng Văn Chức', '2020-05-15', '0933445566', 'Phuc vu', 101, 106);
INSERT INTO Staff (ID_Staff, NameStaff, JoinDate, PhoneNumber, Role, ID_Manager, ID_User)
VALUES (107, 'Bùi Thị Năng', '2020-07-20', '0944556677', 'Phuc vu', 101, 107);
INSERT INTO Staff (ID_Staff, NameStaff, JoinDate, PhoneNumber, Role, ID_Manager, ID_User)
VALUES (108, 'Ngô Văn Sự', '2020-09-25', '0955667788', 'Phuc vu', 101, 108);

-- Thêm data cho bảng Users
-- Nhân viên
INSERT INTO Users (ID_User,Email,password,VerifyCode,Role) VALUES (1001,'manager01@gmail.com','123','Verified','Quan Ly');
INSERT INTO Users (ID_User,Email,password,VerifyCode,Role) VALUES (102,'NVAnhHong@gmail.com','123','Verified','Nhan Vien');
INSERT INTO Users (ID_User,Email,password,VerifyCode,Role) VALUES (103,'NVQuangDinh@gmail.com','123','Verified','Nhan Vien');
INSERT INTO Users (ID_User,Email,password,VerifyCode,Role) VALUES (104,'staff1@gmail.com','123','Verified','Nhan Vien');
INSERT INTO Users (ID_User,Email,password,VerifyCode,Role) VALUES (105,'staff2@gmail.com','123','Verified','Nhan Vien');
INSERT INTO Users (ID_User,Email,password,VerifyCode,Role) VALUES (106,'staff3@gmail.com','123','Verified','Nhan Vien');
INSERT INTO Users (ID_User,Email,password,VerifyCode,Role) VALUES (107,'staff4@gmail.com','123','Verified','Nhan Vien');
INSERT INTO Users (ID_User,Email,password,VerifyCode,Role) VALUES (108,'staff5@gmail.com','123','Verified','Nhan Vien');

-- Khach Hang

INSERT INTO Users(ID_User,Email,password,VerifyCode,Role) VALUES (109,'customer1@gmail.com','123','Verified','Khach Hang');
INSERT INTO Users(ID_User,Email,password,VerifyCode,Role) VALUES (110,'customer2@gmail.com','123','Verified','Khach Hang');
INSERT INTO Users(ID_User,Email,password,VerifyCode,Role) VALUES (111,'customer3@gmail.com','123','Verified','Khach Hang');
INSERT INTO Users(ID_User,Email,password,VerifyCode,Role) VALUES (112,'customer4@gmail.com','123','Verified','Khach Hang');
INSERT INTO Users(ID_User,Email,password,VerifyCode,Role) VALUES (113,'customer5@gmail.com','123','Verified','Khach Hang');
INSERT INTO Users(ID_User,Email,password,VerifyCode,Role) VALUES (114,'customer6@gmail.com','123','Verified','Khach Hang');
INSERT INTO Users(ID_User,Email,password,VerifyCode,Role) VALUES (115,'customer7@gmail.com','123','Verified','Khach Hang');
INSERT INTO Users(ID_User,Email,password,VerifyCode,Role) VALUES (116,'customer8@gmail.com','123','Verified','Khach Hang');
INSERT INTO Users(ID_User,Email,password,VerifyCode,Role) VALUES (117,'customer9@gmail.com','123','Verified','Khach Hang');
INSERT INTO Users (ID_User,Email,password,VerifyCode,Role) VALUES (118,'customer10@gmail.com','123','Verified','Khach Hang');

-- Thêm data cho bảng Products

INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (1, N'Hoa Hồng Đỏ', 150000, 100, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (2, N'Hoa Cúc Vàng', 200000, 80, N'Hoa tươi', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (3, N'Hoa Ly Trắng', 80000, 60, N'Hoa tươi', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (4, N'Hoa Hồng Xanh', 120000, 50, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (5, N'Hoa Sen Hồng', 250000, 70, N'Hoa tươi', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (6, N'Hoa Cẩm Tú Cầu', 90000, 40, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (7, N'Hoa Mẫu Đơn', 50000, 45, N'Hoa tươi', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (8, N'Hoa Lan Hồ Điệp', 180000, 30, N'Hoa lan', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (9, N'Hoa Tulip Đỏ', 220000, 55, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (10, N'Hoa Cẩm Chướng', 95000, 75, N'Hoa tươi', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (11, N'Hoa Hướng Dương', 110000, 85, N'Hoa tươi', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (12, N'Hoa Thạch Thảo', 130000, 90, N'Hoa tươi', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (13, N'Hoa Lưu Ly', 170000, 95, N'Hoa khô', N'Đông');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (14, N'Hoa Dã Quỳ', 160000, 100, N'Hoa tươi', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (15, N'Hoa Tigon', 55000, 65, N'Hoa tươi', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (16, N'Hoa Đào', 140000, 50, N'Hoa Tết', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (17, N'Hoa Mai', 200000, 30, N'Hoa Tết', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (18, N'Hoa Giấy', 60000, 120, N'Hoa khô', N'Quanh năm');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (19, N'Hoa Baby', 180000, 110, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (20, N'Hoa Phăng', 210000, 75, N'Hoa tươi', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (21, N'Hoa Oải Hương', 160000, 60, N'Hoa khô', N'Quanh năm');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (22, N'Hoa Cát Tường', 230000, 70, N'Hoa tươi', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (23, N'Hoa Tử Đằng', 120000, 50, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (24, N'Hoa Ban',  95000, 80, N'Hoa tươi', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (25, N'Hoa Ngọc Lan', 45000, 75, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (26, N'Hoa Thiên Điểu', 140000, 40, N'Hoa tươi', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (27, N'Hoa Xuyến Chi', 75000, 95, N'Hoa dại', N'Quanh năm');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (28, N'Hoa Sứ', 100000, 55, N'Hoa chậu', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (29, N'Hoa Ngọc Thảo', 80000, 60, N'Hoa tươi', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (30, N'Hoa Cúc Họa Mi', 80000, 90, N'Hoa tươi', N'Đông');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (31, N'Hoa Cẩm Tú Cầu Xanh', 110000, 45, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (32, N'Hoa Hồng Nhung', 130000, 85, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (33, N'Hoa Lài', 70000, 100, N'Hoa tươi', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (34, N'Hoa Cát Đằng', 90000, 70, N'Hoa leo', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (35, N'Hoa Bướm', 60000, 80, N'Hoa dại', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (36, N'Hoa Tigon Hồng', 100000, 75, N'Hoa leo', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (37, N'Hoa Lan Rừng', 95000, 35, N'Hoa lan', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (38, N'Hoa Móng Cọp', 85000, 50, N'Hoa leo', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (39, N'Hoa Cỏ May', 190000, 120, N'Hoa dại', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (40, N'Hoa Nguyệt Quế', 65000, 65, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (41, N'Hoa Hồng Tím', 120000, 55, N'Hoa tươi', N'Quanh năm');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (42, N'Hoa Thược Dược', 85000, 75, N'Hoa tươi', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (43, N'Hoa Cẩm Nhung', 130000, 60, N'Hoa tươi', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (44, N'Hoa Lan Chuỗi Ngọc', 170000, 30, N'Hoa lan', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (45, N'Hoa Lồng Đèn', 150000, 85, N'Hoa leo', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (46, N'Hoa Mắt Nai', 70000, 100, N'Hoa dại', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (47, N'Hoa Cúc Vạn Thọ', 85000, 90, N'Hoa Tết', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (48, N'Hoa Sống Đời', 50000, 110, N'Hoa chậu', N'Quanh năm');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (49, N'Hoa Thiên Lý', 120000, 70, N'Hoa leo', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (50, N'Hoa Cẩm Quỳ', 250000, 80, N'Hoa dại', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (51, N'Hoa Phong Lữ', 150000, 60, N'Hoa tươi', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (52, N'Hoa Trà My', 180000, 70, N'Hoa tươi', N'Đông');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (53, N'Hoa Cẩm Tú Tròn', 170000, 55, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (54, N'Hoa Lan Vũ Nữ', 290000, 35, N'Hoa lan', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (55, N'Hoa Hồng Bạch', 150000, 80, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (56, N'Hoa Bách Hợp', 240000, 45, N'Hoa tươi', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (57, N'Hoa Cúc Tím', 130000, 90, N'Hoa tươi', N'Đông');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (58, N'Hoa Mười Giờ', 90000, 100, N'Hoa chậu', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (59, N'Hoa Quỳnh', 200000, 30, N'Hoa tươi', N'Đêm');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (60, N'Hoa Dâm Bụt', 110000, 85, N'Hoa dại', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (61, N'Hoa Chuông', 145000, 70, N'Hoa tươi', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (62, N'Hoa Sao Nháy', 105000, 95, N'Hoa dại', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (63, N'Hoa Lồng Đèn Nhật', 170000, 60, N'Hoa tươi', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (64, N'Hoa Hồng Trắng', 150000, 75, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (65, N'Hoa Hồng Cam', 155000, 85, N'Hoa tươi', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (66, N'Hoa Hồng Vàng', 160000, 90, N'Hoa tươi', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (67, N'Hoa Hồng Đen', 220000, 40, N'Hoa tươi', N'Đặc biệt');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (68, N'Hoa Cúc Đại Đóa', 120000, 95, N'Hoa Tết', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (69, N'Hoa Ngũ Sắc', 100000, 100, N'Hoa dại', N'Quanh năm');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (70, N'Hoa Chiều Tím', 95000, 85, N'Hoa dại', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (71, N'Hoa Mai Tứ Quý', 250000, 40, N'Hoa Tết', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (72, N'Hoa Súng', 135000, 55, N'Hoa nước', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (73, N'Hoa Thủy Tiên', 180000, 70, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (74, N'Hoa Dạ Yến Thảo', 155000, 80, N'Hoa chậu', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (75, N'Hoa Sử Quân Tử', 145000, 65, N'Hoa leo', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (76, N'Hoa Tóc Tiên', 110000, 90, N'Hoa chậu', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (77, N'Hoa Cúc Mâm Xôi', 140000, 100, N'Hoa Tết', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (78, N'Hoa Hồng Cổ', 195000, 50, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (79, N'Hoa Lan Dạ Hương', 270000, 35, N'Hoa lan', N'Đông');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (80, N'Hoa Hồng Juliet', 320000, 25, N'Hoa nhập khẩu', N'Quanh năm');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (81, N'Hoa Tulip Vàng', 230000, 60, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (82, N'Hoa Tulip Tím', 240000, 50, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (83, N'Hoa Lavender Khô', 180000, 80, N'Hoa khô', N'Quanh năm');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (84, N'Hoa Cúc Xuxi', 125000, 90, N'Hoa Tết', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (85, N'Hoa Vân Anh', 150000, 65, N'Hoa tươi', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (86, N'Hoa Hải Đường', 170000, 60, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (87, N'Hoa Lan Kiếm', 260000, 30, N'Hoa lan', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (88, N'Hoa Kim Ngân', 140000, 85, N'Hoa leo', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (89, N'Hoa Sứ Thái', 150000, 100, N'Hoa chậu', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (90, N'Hoa Lan Phi Điệp', 310000, 25, N'Hoa lan', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (91, N'Hoa Mận Trắng', 190000, 45, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (92, N'Hoa Sim', 130000, 80, N'Hoa dại', N'Thu');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (93, N'Hoa Lan Tỏi', 160000, 60, N'Hoa leo', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (94, N'Hoa Cẩm Tú Cầu Tím', 175000, 70, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (95, N'Hoa Lan Mokara', 280000, 40, N'Hoa lan', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (96, N'Hoa Mai Chiếu Thủy', 135000, 85, N'Hoa chậu', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (97, N'Hoa Tường Vi', 150000, 75, N'Hoa tươi', N'Hè');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (98, N'Hoa Cúc Mẫu Đơn', 155000, 90, N'Hoa Tết', N'Tết');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (99, N'Hoa Thủy Chung', 145000, 65, N'Hoa tươi', N'Xuân');
INSERT INTO Products (product_ID, product_Name, price, stock, type, season) VALUES (100, N'Hoa Bướm Đêm', 160000, 55, N'Hoa dại', N'Quanh năm');

-- Thêm data cho bảng Orders

INSERT INTO Orders (order_ID, customer_Name, customer_ID, order_Day, total_Amount)
VALUES
(1, N'Nguyễn Ngọc Bảo Trân', 101, '2025-04-01', 1000000),
(2, N'Đỗ Đoàn Quốc Tín', 102, '2025-04-02', 500000),
(3, N'Đào Quỳnh Thi', 103,'2025-04-03', 750000),
(4, N'Nguyễn Trung Hiền', 104, '2025-04-04', 1200000),
(5, N'Phạm Minh Tuấn', 105, '2025-04-05', 450000),
(6, N'Vũ Thị Eo', 106, '2025-04-06', 600000),
(7, N'Tôn Thất Gia Huy', 107, '2025-04-07', 300000),
(8, N'Bùi Thị Giang', 108, '2025-04-08', 800000),
(9, N'Ngô Văn Hải', 109, '2025-04-09', 1500000),
(10, N'Dương Thị Hoa', 110, '2025-04-10', 2000000),
(11, N'Phạm Minh Tuấn', 105, '2025-04-11', 900000),
(12, N'Đào Quỳnh Thi', 103, '2025-04-12', 400000),
(13, N'Nguyễn Trung Hiền', 104, '2025-04-13', 1100000),
(14, N'Tôn Thất Gia Huy', 107, '2025-04-14', 1300000),
(15, N'Phạm Minh Tuấn', 105, '2025-04-15', 750000),
(16, N'Đào Quỳnh Thi', 103, '2025-04-16', 600000),
(17, N'Phạm Minh Tuấn', 105, '2025-04-17', 850000),
(18, N'Đào Quỳnh Thi', 103, '2025-04-18', 1000000),
(19, N'Phạm Minh Tuấn', 105, '2025-04-19', 950000),
(20, N'Nguyễn Trung Hiền', 104, '2025-04-20', 1200000),
(21, N'Phạm Minh Tuấn', 105, '2025-04-21', 550000),
(22, N'Tôn Thất Gia Huy', 107, '2025-04-22', 650000),
(23, N'Bùi Thị Giang', 108, '2025-04-23', 500000),
(24, N'Đỗ Đoàn Quốc Tín', 102, '2025-04-24', 700000),
(25, N'Nguyễn Trung Hiền', 104, '2025-04-25', 900000),
(26, N'Nguyễn Ngọc Bảo Trân', 101, '2025-04-26', 550000),
(27, N'Tôn Thất Gia Huy', 107, '2025-04-27', 750000),
(28, N'Bùi Thị Giang', 108, '2025-04-28', 1000000),
(29, N'Đỗ Đoàn Quốc Tín', 102, '2025-04-29', 600000),
(30, N'Nguyễn Trung Hiền', 104, '2025-04-30', 1100000),
(31, N'Tôn Thất Gia Huy', 107, '2025-05-01', 1200000),
(32, N'Nguyễn Ngọc Bảo Trân', 101, '2025-05-02', 1300000),
(33, N'Đào Quỳnh Thi', 103, '2025-05-03', 800000),
(34, N'Tôn Thất Gia Huy', 107, '2025-05-04', 900000),
(35, N'Bùi Thị Giang', 108, '2025-05-05', 600000),
(36, N'Tôn Thất Gia Huy', 107, '2025-05-06', 1100000),
(37, N'Nguyễn Trung Hiền', 104, '2025-05-07', 950000),
(38, N'Đào Quỳnh Thi', 103, '2025-05-08', 1200000),
(39, N'Đỗ Đoàn Quốc Tín', 102, '2025-05-09', 800000),
(40, N'Tôn Thất Gia Huy', 107, '2025-05-10', 700000),
(41, N'Bùi Thị Giang', 108, '2025-05-11', 600000),
(42, N'Đỗ Đoàn Quốc Tín', 102, '2025-05-12', 550000),
(43, N'Nguyễn Ngọc Bảo Trân', 101, '2025-05-13', 950000),
(44, N'Bùi Thị Giang', 108, '2025-05-14', 1100000),
(45, N'Đào Quỳnh Thi', 103, '2025-05-15', 700000),
(46, N'Đỗ Đoàn Quốc Tín', 102, '2025-05-16', 1300000),
(47, N'Nguyễn Trung Hiền', 104, '2025-05-17', 800000),
(48, N'Đỗ Đoàn Quốc Tín', 102, '2025-05-18', 600000),
(49, N'Tôn Thất Gia Huy', 107, '2025-05-19', 1000000),
(50, N'Nguyễn Ngọc Bảo Trân', 101, '2025-05-20', 1200000);

-- Thêm data cho bảng OrderDetails

INSERT INTO OrderDetails (order_ID, product_ID, quantity, totalAmount)
VALUES
(1, 1, 2, 300000),
(2, 2, 3, 600000),
(3, 3, 1, 80000),
(4, 4, 1, 120000),
(5, 5, 2, 500000),
(6, 6, 3, 270000),
(7, 7, 2, 100000),
(8, 8, 1, 180000),
(9, 9, 1, 220000),
(10, 10, 2, 190000),
(11, 11, 1, 110000),
(12, 12, 2, 260000),
(13, 13, 3, 510000),
(14, 14, 1, 160000),
(15, 15, 2, 110000),
(16, 16, 3, 420000),
(17, 17, 2, 400000),
(18, 18, 5, 300000),
(19, 19, 1, 180000),
(20, 20, 3, 630000),
(21, 21, 2, 320000),
(22, 22, 1, 230000),
(23, 23, 2, 240000),
(24, 24, 3, 285000),
(25, 25, 2, 90000),
(26, 26, 1, 140000),
(27, 27, 3, 225000),
(28, 28, 1, 100000),
(29, 29, 2, 160000),
(30, 30, 4, 320000),
(31, 31, 1, 110000),
(32, 32, 2, 260000),
(33, 33, 3, 210000),
(34, 34, 2, 180000),
(35, 35, 1, 60000),
(36, 36, 3, 300000),
(37, 37, 2, 190000),
(38, 38, 1, 85000),
(39, 39, 2, 380000),
(40, 40, 2, 130000),
(41, 41, 2, 240000),
(42, 42, 3, 255000),
(43, 43, 2, 260000),
(44, 44, 1, 170000),
(45, 45, 2, 300000),
(46, 46, 2, 140000),
(47, 47, 1, 85000),
(48, 48, 3, 150000),
(49, 49, 2, 240000),
(50, 50, 1, 250000);


SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Customers';
EXEC sp_LoginUser 'customer1@gmail.com', '123'
;
SELECT * FROM Users WHERE email = 'customer1@gmail.com' AND password = '123';