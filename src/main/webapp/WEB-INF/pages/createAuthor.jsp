<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<body>
<a href="/author">To author list</a>
<p>
    <c:if test="${authorIsAdded}">Author is added</c:if>
</p>
<form action="create" method="POST">
    Author name: <input type="text" name="name">
    <br/>
    Birthdate: <input type="date" name="birthDate"/>
    <br/>
    Deathdate: <input type="date" name="deathDate"/>
    <input type="submit" value="Submit" />
</form>
</body>
</html>
