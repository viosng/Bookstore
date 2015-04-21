<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<style type="text/css">
    th {
        color: white;
    }
</style>

<p>
    Library.
</p>

<form action="/editBook/find" method="GET">
    Book Name: <input type="text" name="name" >
    <br />
</form>

<table style="border: 1px solid black;" cellpadding="6" cellspacing="0">
    <tr valign="baseline" bgcolor="#404060">
        <th align="center"> ID</th>
        <th align="left"> Name</th>
        <th align="center"> VIEW</th>
        <th align="right"> EDIT</th>
    </tr>

    <c:forEach var="book" items="${books}" varStatus="lineInfo">

        <c:choose>
            <c:when test="${lineInfo.count % 2 == 0}"> <tr bgcolor="#f7f7e7"> </c:when>
            <c:otherwise> <tr bgcolor="white"> </c:otherwise>
        </c:choose>

        <td align="center"> ${book.id} </td>
        <td align="left"> ${book.name} </td>
        <td align="left"><a href="/books/${book.id}">view</a></td>
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <td align="left"><a href="/editBook/${book.id}">edit</a></td>
        </sec:authorize>


        </tr>

    </c:forEach>
</table>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <a href="/editBook/create">Create new book</a>
</sec:authorize>
<br/>
<a href="<c:url value="j_spring_security_logout" />" > Logout</a>
