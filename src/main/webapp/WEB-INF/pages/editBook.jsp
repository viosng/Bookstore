<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<body>
<form action="update" method="GET">
    Book Name: <input type="text" name="name" value="${book.name}" >
    <br />
    price: <input type="text" name="price" value="${book.price}" />
     <input type="hidden" name="id" value="${book.id}" />
    <input type="submit" value="Submit" />
</form>
</body>
</html>