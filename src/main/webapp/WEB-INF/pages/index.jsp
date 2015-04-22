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
    <title>${message}</title>
</head>
<body>
<a href="author">To author list</a>
<a href="book">To book list</a>
<table style="border: 1px solid black;" cellpadding="6" cellspacing="0">
  <tr valign="baseline" bgcolor="#404060">
    <th align="center"> ID</th>
    <th align="left"> Name</th>

  </tr>

  <c:forEach var="book" items="${books}" varStatus="lineInfo">

    <tr>

    <td align="center"> ${book.id} </td>
    <td align="left"> ${book.name} </td>
    </tr>

  </c:forEach>
</table>

</body>
</html>
