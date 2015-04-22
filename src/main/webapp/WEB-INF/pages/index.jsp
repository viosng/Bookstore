<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false"%>
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

<h1>Queries</h1>
<ul>
  <li><a href="author">To author list</a></li>
  <li><a href="book">To book list</a></li>
  <li><a href="alive">Books of alive authors</a></li>
</ul>
<a href="<c:url value="/logout"/>" > Logout</a>
</body>
</html>
