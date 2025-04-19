CREATE PROCEDURE GetOrdersByDateSorted
    @OrderDate DATE,          -- Ngày phát sinh đơn hàng
    @SortOrder NVARCHAR(4)    -- Thứ tự sắp xếp: 'ASC' hoặc 'DESC'
AS
BEGIN
    SET NOCOUNT ON;

    SELECT 
        o.order_ID,
        o.customer_Name,
        o.customer_ID,
        o.order_Day,
        o.total_Amount,
        c.customer_Name AS CustomerFullName,
        od.product_ID,
        p.product_Name,
        od.quantity,
        od.totalAmount AS OrderDetailTotal
    FROM Orders o
    INNER JOIN Customers c ON o.customer_ID = c.customer_ID
    LEFT JOIN OrderDetails od ON o.order_ID = od.order_ID
    LEFT JOIN Products p ON od.product_ID = p.product_ID
    WHERE o.order_Day = @OrderDate
    ORDER BY o.total_Amount @SortOrder;
END;