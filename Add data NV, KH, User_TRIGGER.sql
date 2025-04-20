-- TẠO CÁC TRIGGER KIỂM TRA VÀ ĐỒNG BỘ DỮ LIỆU GIỮA ORDERS VÀ ORDERDETAIL
-- 1. Trigger kiểm tra và cập nhật tổng tiền khi OrderDetail thay đổi
CREATE TRIGGER trg_CheckOrderDetailedTotal
ON [OrderDetailed]
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;
    -- Kiểm tra tính hợp lệ của từng chi tiết đơn hàng (INSERT/UPDATE)
    IF EXISTS (SELECT * FROM inserted)
    BEGIN
    -- Kiểm tra thành tiền = SOLUONG * DONGIA
        IF EXISTS (
            SELECT 1
            FROM inserted i
            JOIN Products p ON i.productId = p.productId
            WHERE i.quantity * p.price <> 
                  (SELECT total FROM OrderDetailed od 
                   WHERE od.orderId = i.orderId AND od.productId = i.productId)
        )
        BEGIN
            RAISERROR(N'Thành tiền trong OrderDetail phải bằng quantity x price', 16, 1);
            ROLLBACK TRANSACTION;
            RETURN;
        END
		
    --Kiểm tra các đơn hàng bị ảnh hưởng
    DECLARE @affectedOrders TABLE (orderId INT PRIMARY KEY);
    INSERT INTO @affectedOrders
    SELECT DISTINCT orderId FROM inserted
    UNION
    SELECT DISTINCT orderId FROM deleted;
   
    -- Cập nhật lại tổng tiền cho từng đơn hàng
    UPDATE o
    SET o.totalAmount = ISNULL((
        SELECT SUM(od.quantity * p.price)
        FROM OrderDetailed od
        JOIN Products p ON od.productId = p.productId
        WHERE od.orderId = o.orderId ), 0)
    FROM Orders o
    JOIN @affectedOrders a ON o.orderId = a.orderId;
	END
END;
GO

-- 2. Trigger kiểm tra tính nhất quán khi Orders thay đổi
CREATE TRIGGER trg_CheckOrderTotal
ON [Order]
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    
    -- Kiểm tra tổng tiền đơn hàng có khớp với đơn hàng chi tiết không
    IF EXISTS (
        SELECT 1
        FROM inserted i
        WHERE i.total <> ISNULL((
            SELECT SUM(od.quantity * p.price)
            FROM OrderDetailed od
            JOIN Products p ON od.productId = p.productId
            WHERE od.orderId = i.orderId
        ), 0)
    )
    BEGIN
        RAISERROR(N'Tổng tiền đơn hàng không khớp với tổng thành tiền từ chi tiết đơn hàng', 16, 1);
        ROLLBACK TRANSACTION;
        RETURN;
    END
END;

-- IMPORT DỮ LIỆU CHO NHANVIEN, KHACHHANG, USER
INSERT INTO Nhanvien (idnhanvien, ten, ngayVL, phonenum, role, idmanager, iduser) VALUES
(1001, 'Lê Quản Trị', '2018-05-10', '0912345678', 'Quản lý cấp cao', NULL, 1),
(1002, 'Trần Chiến Đấu', '2019-02-15', '0987654321', 'Quản lý', 1001, 2),
(1003, 'Nguyễn Nhân Sự', '2019-06-20', '0978123456', 'Nhân sự', 1001, 3),
(1004, 'Phạm Kế Toán', '2019-08-25', '0967832154', 'Kế toán', 1001, 4),
(1005, 'Hoàng Văn Nhân', '2020-01-05', '0911223344', 'Nhân viên', 1002, 10),
(1006, 'Vũ Thị Viên', '2020-03-10', '0922334455', 'Nhân viên', 1002, 11),
(1007, 'Đặng Văn Chức', '2020-05-15', '0933445566', 'Nhân viên', 1002, 12),
(1008, 'Bùi Thị Năng', '2020-07-20', '0944556677', 'Nhân viên', 1002, 13),
(1009, 'Ngô Văn Sự', '2020-09-25', '0955667788', 'Nhân viên', 1002, 14),
(1010, 'Dương Thị Nghiệp', '2021-01-30', '0966778899', 'Nhân viên', 1002, 33),
(1011, 'Lý Văn Chuyên', '2021-03-05', '0977889900', 'Nhân viên', 1002, 34),
(1012, 'Chu Thị Nghiệp', '2021-05-10', '0988990011', 'Nhân viên', 1002, 35),
(1013, 'Trịnh Văn Việc', '2021-07-15', '0999001122', 'Nhân viên', 1002, 36),
(1014, 'Đỗ Thị Làm', '2021-09-20', '0910011223', 'Nhân viên', 1002, 37),
(1015, 'Mai Văn Giám', '2020-02-01', '0911122334', 'Giám sát', 1001, 30),
(1016, 'Hồ Thị Sát', '2020-04-06', '0912233445', 'Giám sát', 1001, 31),
(1017, 'Võ Văn Kỹ', '2020-06-11', '0913344556', 'Kỹ thuật', 1015, 48),
(1018, 'Lưu Thị Thuật', '2020-08-16', '0914455667', 'Kỹ thuật', 1015, 49),
(1019, 'Phan Văn Nghệ', '2020-10-21', '0915566778', 'Kỹ thuật', 1015, 50),
(1020, 'Vương Thị Thuật', '2020-12-26', '0916677889', 'Kỹ thuật', 1016, 48),
(1021, 'Đinh Văn Công', '2021-01-31', '0917788990', 'Kỹ thuật', 1016, 49),
(1022, 'Lâm Thị Nghệ', '2021-03-07', '0918899001', 'Kỹ thuật', 1016, 50),
(1023, 'Tô Văn Trình', '2021-05-12', '0919900112', 'Kỹ thuật', 1015, 48),
(1024, 'Trương Thị Dịch', '2021-07-17', '0921001123', 'Nhân viên', 1015, 33),
(1025, 'Nguyễn Văn Vụ', '2021-09-22', '0922112234', 'Nhân viên', 1016, 34),
(1026, 'Trần Thị Án', '2021-11-27', '0923223345', 'Nhân viên', 1016, 35),
(1027, 'Lê Văn Hồ Sơ', '2022-01-01', '0924334456', 'Nhân viên', 1015, 36),
(1028, 'Phạm Thị Tài Liệu', '2022-03-08', '0925445567', 'Nhân viên', 1015, 37),
(1029, 'Hoàng Văn Phòng', '2022-05-13', '0926556678', 'Nhân viên', 1016, 33),
(1030, 'Vũ Thị Hành Chính', '2022-07-18', '0927667789', 'Nhân viên', 1016, 34),
(1031, 'Đặng Văn Tiếp', '2022-09-23', '0928778890', 'Nhân viên', 1002, 35),
(1032, 'Bùi Thị Tân', '2022-11-28', '0929889901', 'Nhân viên', 1002, 36),
(1033, 'Ngô Văn Khách', '2023-01-02', '0930990012', 'Nhân viên', 1002, 37),
(1034, 'Dương Thị Hàng', '2023-03-09', '0931001123', 'Nhân viên', 1002, 10),
(1035, 'Lý Văn Tiêu', '2023-05-14', '0932112234', 'Nhân viên', 1002, 11),
(1036, 'Chu Thị Dùng', '2023-07-19', '0933223345', 'Nhân viên', 1002, 12),
(1037, 'Trịnh Văn Nội', '2023-09-24', '0934334456', 'Nhân viên', 1002, 13),
(1038, 'Đỗ Thị Bộ', '2023-11-29', '0935445567', 'Nhân viên', 1002, 14),
(1039, 'Mai Văn Phận', '2024-01-03', '0936556678', 'Nhân viên', 1002, 33),
(1040, 'Hồ Thị Chức', '2024-03-09', '0937667789', 'Nhân viên', 1002, 34),
(1041, 'Võ Văn Năng', '2024-05-14', '0938778890', 'Nhân viên', 1002, 35),
(1042, 'Lưu Thị Lực', '2024-07-19', '0939889901', 'Nhân viên', 1002, 36),
(1043, 'Phan Văn Sĩ', '2024-09-24', '0940990012', 'Nhân viên', 1002, 37),
(1044, 'Vương Thị Số', '2024-11-29', '0941001123', 'Nhân viên', 1002, 10),
(1045, 'Đinh Văn Lượng', '2025-01-03', '0942112234', 'Nhân viên', 1002, 11),
(1046, 'Lâm Thị Chất', '2025-03-10', '0943223345', 'Nhân viên', 1002, 12),
(1047, 'Tô Văn Lượng', '2025-05-15', '0944334456', 'Nhân viên', 1002, 13),
(1048, 'Trương Thị Xả', '2025-07-20', '0945445567', 'Nhân viên', 1002, 14),
(1049, 'Nguyễn Văn Thải', '2025-09-25', '0946556678', 'Nhân viên', 1002, 33),
(1050, 'Trần Thị Trừ', '2025-11-30', '0947667789', 'Nhân viên', 1002, 34);

INSERT INTO Khachhang (id, name, joindate, iduser) VALUES
(101, 'Nguyễn Văn An', '2020-01-15', 5),
(102, 'Trần Thị Bình', '2020-02-20', 6),
(103, 'Lê Văn Cường', '2020-03-10', 7),
(104, 'Phạm Thị Dung', '2020-04-05', 8),
(105, 'Hoàng Văn Đạt', '2020-05-12', 9),
(106, 'Vũ Thị Eo', '2020-06-18', 15),
(107, 'Đặng Văn Phúc', '2020-07-22', 16),
(108, 'Bùi Thị Giang', '2020-08-30', 17),
(109, 'Ngô Văn Hải', '2020-09-05', 18),
(110, 'Dương Thị Hoa', '2020-10-10', 19),
(111, 'Lý Văn Khánh', '2020-11-15', 20),
(112, 'Chu Thị Lan', '2020-12-20', 21),
(113, 'Trịnh Văn Minh', '2021-01-25', 22),
(114, 'Đỗ Thị Nga', '2021-02-28', 23),
(115, 'Mai Văn Oanh', '2021-03-05', 24),
(116, 'Hồ Thị Phương', '2021-04-10', 25),
(117, 'Võ Văn Quân', '2021-05-15', 26),
(118, 'Lưu Thị Rơm', '2021-06-20', 27),
(119, 'Phan Văn Sơn', '2021-07-25', 28),
(120, 'Vương Thị Tâm', '2021-08-30', 29),
(121, 'Đinh Văn Uy', '2021-09-05', 38),
(122, 'Lâm Thị Vân', '2021-10-10', 39),
(123, 'Tô Văn Xuyên', '2021-11-15', 40),
(124, 'Trương Thị Yến', '2021-12-20', 41),
(125, 'Nguyễn Văn Anh', '2022-01-25', 42),
(126, 'Trần Thị Bích', '2022-02-28', 43),
(127, 'Lê Văn Chiến', '2022-03-05', 44),
(128, 'Phạm Thị Duyên', '2022-04-10', 45),
(129, 'Hoàng Văn Đức', '2022-05-15', 46),
(130, 'Vũ Thị Em', '2022-06-20', 47),
(131, 'Đặng Văn Phú', '2022-07-25', 15),
(132, 'Bùi Thị Gái', '2022-08-30', 16),
(133, 'Ngô Văn Hùng', '2022-09-05', 17),
(134, 'Dương Thị Hương', '2022-10-10', 18),
(135, 'Lý Văn Kiên', '2022-11-15', 19),
(136, 'Chu Thị Lệ', '2022-12-20', 20),
(137, 'Trịnh Văn Mạnh', '2023-01-25', 21),
(138, 'Đỗ Thị Ngọc', '2023-02-28', 22),
(139, 'Mai Văn Oai', '2023-03-05', 23),
(140, 'Hồ Thị Phượng', '2023-04-10', 24),
(141, 'Võ Văn Quang', '2023-05-15', 25),
(142, 'Lưu Thị Rành', '2023-06-20', 26),
(143, 'Phan Văn Sang', '2023-07-25', 27),
(144, 'Vương Thị Thảo', '2023-08-30', 28),
(145, 'Đinh Văn Út', '2023-09-05', 29),
(146, 'Lâm Thị Vy', '2023-10-10', 38),
(147, 'Tô Văn Xuân', '2023-11-15', 39),
(148, 'Trương Thị Yến Nhi', '2023-12-20', 40),
(149, 'Nguyễn Văn Anh Tuấn', '2024-01-25', 41),
(150, 'Trần Thị Bích Ngọc', '2024-02-28', 42);

INSERT INTO User (id, email, mk, vaitro) VALUES
(1, 'admin@company.com', 'admin123', 'admin'),
(2, 'manager@company.com', 'manager123', 'manager'),
(3, 'hr_staff@company.com', 'hr123', 'staff'), 
(4, 'accountant@company.com', 'account123', 'accountant'),
(5, 'customer1@gmail.com', 'customer123', 'customer'),
(6, 'customer2@gmail.com', 'customer456', 'customer'),
(7, 'customer3@gmail.com', 'customer789', 'customer'),
(8, 'customer4@gmail.com', 'customer101', 'customer'),
(9, 'customer5@outlook.com', 'customer112', 'customer'),
(10, 'staff1@company.com', 'staff1123', 'staff'),
(11, 'staff2@company.com', 'staff2123', 'staff'),
(12, 'staff3@company.com', 'staff3123', 'staff'),
(13, 'staff4@company.com', 'staff4123', 'staff'),
(14, 'staff5@company.com', 'staff5123', 'staff'),
(15, 'customer6@gmail.com', 'pass123', 'customer'),
(16, 'customer7@gmail.com', 'pass456', 'customer'),
(17, 'customer8@gmail.com', 'pass789', 'customer'),
(18, 'customer9@gmail.com', 'pass101', 'customer'),
(19, 'customer10@gmail.com', 'pass112', 'customer'),
(20, 'customer11@gmail.com', 'pass131', 'customer'),
(21, 'customer12@gmail.com', 'pass141', 'customer'),
(22, 'customer13@gmail.com', 'pass151', 'customer'),
(23, 'customer14@gmail.com', 'pass161', 'customer'),
(24, 'customer15@gmail.com', 'pass171', 'customer'),
(25, 'customer16@outlook.com', 'pass181', 'customer'),
(26, 'customer17@outlook.com', 'pass191', 'customer'),
(27, 'customer18@outlook.com', 'pass201', 'customer'),
(28, 'customer19@outlook.com', 'pass211', 'customer'),
(29, 'customer20@outlook.com', 'pass221', 'customer'),
(30, 'marketing_staff@company.com', 'mkt123', 'staff'), 
(31, 'sales_staff@company.com', 'sales123', 'staff'),   
(32, 'support_staff@company.com', 'support123', 'staff'),
(33, 'staff6@company.com', 'staff6123', 'staff'),
(34, 'staff7@company.com', 'staff7123', 'staff'),
(35, 'staff8@company.com', 'staff8123', 'staff'),
(36, 'staff9@company.com', 'staff9123', 'staff'),
(37, 'staff10@company.com', 'staff10123', 'staff'),
(38, 'customer21@gmail.com', 'pass231', 'customer'),
(39, 'customer22@gmail.com', 'pass241', 'customer'),
(40, 'customer23@gmail.com', 'pass251', 'customer'),
(41, 'customer24@gmail.com', 'pass261', 'customer'),
(42, 'customer25@gmail.com', 'pass271', 'customer'),
(43, 'customer26@gmail.com', 'pass281', 'customer'),
(44, 'customer27@gmail.com', 'pass291', 'customer'),
(45, 'customer28@gmail.com', 'pass301', 'customer'),
(46, 'customer29@gmail.com', 'pass311', 'customer'),
(47, 'customer30@gmail.com', 'pass321', 'customer'),
(48, 'tech_staff1@company.com', 'tech123', 'staff'),    
(49, 'tech_staff2@company.com', 'tech456', 'staff'),     
(50, 'tech_staff3@company.com', 'tech789', 'staff');    
