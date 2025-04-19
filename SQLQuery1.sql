CREATE PROCEDURE GetRevenueStats
    @PeriodType NVARCHAR(10), -- 'Day', 'Month', hoặc 'Year'
    @StartDate DATE,          -- Ngày bắt đầu (dùng cho Day hoặc Month)
    @Year INT                 -- Năm (dùng cho Year hoặc Month)
AS
BEGIN
    SET NOCOUNT ON;

    IF @PeriodType = 'Day'
    BEGIN
        SELECT 
            CAST(o.order_Day AS DATE) AS Period,
            SUM(o.total_Amount) AS TotalRevenue,
            COUNT(DISTINCT o.order_ID) AS OrderCount
        FROM Orders o
        WHERE o.order_Day = @StartDate
        GROUP BY CAST(o.order_Day AS DATE);
    END
    ELSE IF @PeriodType = 'Month'
    BEGIN
        SELECT 
            FORMAT(o.order_Day, 'yyyy-MM') AS Period,
            SUM(o.total_Amount) AS TotalRevenue,
            COUNT(DISTINCT o.order_ID) AS OrderCount
        FROM Orders o
        WHERE YEAR(o.order_Day) = @Year
            AND MONTH(o.order_Day) = MONTH(@StartDate)
        GROUP BY FORMAT(o.order_Day, 'yyyy-MM');
    END
    ELSE IF @PeriodType = 'Year'
    BEGIN
        SELECT 
            YEAR(o.order_Day) AS Period,
            SUM(o.total_Amount) AS TotalRevenue,
            COUNT(DISTINCT o.order_ID) AS OrderCount
        FROM Orders o
        WHERE YEAR(o.order_Day) = @Year
        GROUP BY YEAR(o.order_Day);
    END
END;