<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
    <title>All books</title>
</head>
<body>
<a href="/">To main page</a>
<sec:authorize access="hasRole('ROLE_DBA')">
    <a href="book/create">create book</a>
</sec:authorize>
<table style="border: 1px solid #5eb283;" cellpadding="6" cellspacing="0">
    <tr valign="baseline" bgcolor="#404060">
        <th align="center">ID</th>
        <th align="left">Name</th>
        <th align="left">Publication date</th>
        <th align="left">Price</th>
        <th align="left">View</th>
    </tr>
    <c:forEach var="book" items="${books}" varStatus="lineInfo">
        <tr>
            <td align="center"> ${book.id} </td>
            <td align="left"> ${book.name} </td>
            <td align="left"> <fmt:formatDate value="${book.publicationDate.toDate()}" pattern="yyyy-MM-dd" /> </td>
            <td align="left"> ${book.price} </td>
            <td align="left"> <a href="/book/${book.id}">View</a> </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
