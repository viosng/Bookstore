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
    <title>${author.name} edit page</title>
</head>
<body>
<table style="border: 1px solid black;" cellpadding="6" cellspacing="0">
    <tr valign="baseline" bgcolor="#404060">
        <th align="center"> ID</th>
        <th align="left">Name</th>
        <th align="left">Birth date</th>
        <th align="left">Death date</th>
    </tr>
    <tr>
        <td align="center"> ${author.id} </td>
        <td align="left"> ${author.name} </td>
        <td align="left"> <fmt:formatDate value="${author.birthDate.toDate()}" pattern="yyyy-MM-dd" /> </td>
        <td align="left"> <fmt:formatDate value="${author.deathDate.toDate()}" pattern="MM-dd-yyyy" /> </td>
    </tr>
</table>

</body>
</html>
