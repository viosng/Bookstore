<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: vio
  Date: 22.04.2015
  Time: 2:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${book.name} edit page</title>
</head>
<body>
<a href="/book">To book list</a>
<p>
    <c:if test="${bookIsUpdated}">Book is added</c:if>
</p>
<form action="update" method="POST">
    <input hidden name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input hidden name="id" value="${book.id}"/>
    <label>Book Name:<input type="text" name="name" value="${book.name}"></label><br />
    <label>price:<input type="text" name="price" value="${book.price}"/></label><br />
    <label>Publication date<input type="date" name="publicationDate" value="<fmt:formatDate value="${book.publicationDate.toDate()}" pattern="yyyy-MM-dd" />"/></label><br />
    <label>
        <select size="3" multiple name="authors[]" title="Add authors">
            <option disabled>Add authors</option>
            <c:forEach var="author" items="${allAuthors}" varStatus="lineInfo">
                <option value="${author.id}">"${author.name}"</option>
            </c:forEach>
        </select>
    </label><br />
    <sec:authorize access="hasRole('ROLE_DBA')">
        <input type="submit" value="Submit" />
    </sec:authorize>
</form>
<p>Book authors</p>
<table style="border: 1px solid black;" cellpadding="6" cellspacing="0">
    <tr valign="baseline" bgcolor="#404060">
        <th align="center"> ID</th>
        <th align="left">Name</th>
        <th align="left">Birth date</th>
        <th align="left">Death date</th>
        <th align="left">View</th>
        <sec:authorize access="hasRole('ROLE_DBA')">
            <th align="left">Remove</th>
        </sec:authorize>
    </tr>
    <c:forEach var="author" items="${book.authors}" varStatus="lineInfo">
        <tr>
            <td align="center"> ${author.id} </td>
            <td align="left"> ${author.name} </td>
            <td align="left"> <fmt:formatDate value="${author.birthDate.toDate()}" pattern="yyyy-MM-dd" /> </td>
            <td align="left"> <fmt:formatDate value="${author.deathDate.toDate()}" pattern="yyyy-MM-dd" /> </td>
            <td align="left"> <a href="/author/${author.id}">View</a> </td>
            <sec:authorize access="hasRole('ROLE_DBA')">
                <td align="left">
                    <form action="removeAuthority" method="POST">
                        <input hidden name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input hidden name="book_id" value="${book.id}"/>
                        <input hidden name="author_id" value="${author.id}"/>
                        <input type="submit" value="Remove"/>
                    </form>
                </td>
            </sec:authorize>
        </tr>
    </c:forEach>
</table>

</body>
</html>
