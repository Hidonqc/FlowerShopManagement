CREATE PROCEDURE AddEmployee
    @EmployeeID INT,
    @EmployeeName NVARCHAR(100),
    @HireDate DATE,
    @PhoneNumber NVARCHAR(15),
    @Position NVARCHAR(50),
    @ManagerID INT
AS
BEGIN
    INSERT INTO Employees (Employee_ID, Employee_Name, Hire_Date, Phone_Number, Position, Manager_ID)
    VALUES (@EmployeeID, @EmployeeName, @HireDate, @PhoneNumber, @Position, @ManagerID);
END;