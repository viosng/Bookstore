<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<body>
<form action="create" method="POST">
    Book Name: <input type="text" name="name" >
    <br />
    price: <input type="text" name="price"/>
    <input type="submit" value="Submit" />
</form>
</body>
</html>