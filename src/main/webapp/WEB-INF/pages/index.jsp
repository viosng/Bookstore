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
<p>
  <c:if test="${databaseIsImported}">Database is imported</c:if>
</p>
<h1>Queries</h1>
<ul>
  <li><a href="author">To author list</a></li>
  <li><a href="book">To book list</a></li>
  <li><a href="alive">Books of alive authors</a></li>
</ul>
<sec:authorize access="hasRole('ROLE_DBA')">
  <form action="importJson" method="POST">
      <input hidden name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <input type="submit" value="Import JSON"/>
  </form>
</sec:authorize>

<h3>find author by name prefix</h3>
<form action="/author/findAuthorByPrefix" method="post">
    <input hidden name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <label>Author prefix:<input type="text" name="prefix"></label><br />
    <input type="submit" value="Find author by prefix"/>
</form>

<h3>find book by name prefix</h3>
<form action="/book/findBookByPrefix" method="post">
    <input hidden name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <label>Book prefix:<input type="text" name="prefix"></label><br />
    <input type="submit" value="Find book by prefix"/>
</form>

<a href="<c:url value="/logout"/>" > Logout</a>
</body>
</html>
