CREATE PROCEDURE GetCustomerInfo
    @CustomerID INT
AS
BEGIN
    SELECT Customer_ID, Customer_Name
    FROM Customers
    WHERE Customer_ID = @CustomerID;
END;