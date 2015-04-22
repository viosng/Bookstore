<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<body>
<a href="/book">To book list</a>
<p>
    <c:if test="${bookIsAdded}">Book is added</c:if>
</p>
<form action="create" method="POST">
    <input hidden name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <label>Book Name:<input type="text" name="name"></label><br />
    <label>price:<input type="text" name="price"/></label><br />
    <label>Publication date<input type="date" name="publicationDate"/></label><br />
    <label>
        <select size="3" multiple name="authors[]" title="Choose authors">
            <option disabled>Choose authors</option>
            <c:forEach var="author" items="${authors}" varStatus="lineInfo">
                <option value="${author.id}">"${author.name}"</option>
            </c:forEach>
        </select>
    </label><br />
    <label>
        <input type="submit" value="Submit" />
    </label>
</form>
</body>
</html>