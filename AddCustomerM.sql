CREATE PROCEDURE AddCustomer
    @CustomerID INT,
    @CustomerName NVARCHAR(100)
AS
BEGIN
    INSERT INTO Customers (Customer_ID, Customer_Name)
    VALUES (@CustomerID, @CustomerName);
END;