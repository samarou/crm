<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Customers</title>
</head>
<body>

<h2>Customers</h2>

<a href="<c:url value='/logout'/>">Logout</a>

<table>
    <thead>
    <tr>
        <th>First name</th>
        <th>Last name</th>
        <th>Email</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="customer" items="${customers}">
        <tr>
            <td>${customer.firstName}</td>
            <td>${customer.lastName}</td>
            <td>${customer.email}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>