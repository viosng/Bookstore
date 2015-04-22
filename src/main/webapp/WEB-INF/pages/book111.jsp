<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>

<body>
    <h1>BOOK</h1>
    <ul>
        <li><p><b>Name:</b>
            ${book.name}
        </p></li>
        <li><p><b>price:</b>
            ${book.price}
        </p></li>
    </ul>

    <sec:authorize access="hasRole('ROLE_ADMIN')">
    <a href="${book.id}/delete">delete</a>
    </sec:authorize>
</body>
</html>