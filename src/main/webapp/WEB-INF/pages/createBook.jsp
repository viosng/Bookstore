<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<body>
<p>
    <c:if test="${bookIsAdded}">Author is added</c:if>
</p>
<form action="create" method="POST">
    Book Name: <input type="text" name="name" >
    <br />
    price: <input type="text" name="price"/>
    <p>
        <select size="3" multiple name="authors[]">
            <option disabled>Choose authors</option>
            <c:forEach var="author" items="${authors}" varStatus="lineInfo">
                <option value="${author.id}">Чебурашка</option>
                <tr>
                    <td align="center"> ${author.id} </td>
                    <td align="left"> ${author.name} </td>
                    <td align="left"> ${author.birthDate.toDate()} </td>
                    <td align="left"> <fmt:formatDate value="${author.birthDate.toDate()}" pattern="yyyy-MM-dd" /> </td>
                    <td align="left"> <fmt:formatDate value="${author.deathDate.toDate()}" pattern="yyyy-MM-dd" /> </td>
                </tr>
            </c:forEach>
            <option disabled>Выберите героя</option>
            <option value="Чебурашка">Чебурашка</option>
            <option selected value="Крокодил Гена">Крокодил Гена</option>
            <option value="Шапокляк">Шапокляк</option>
            <option value="Крыса Лариса">Крыса Лариса</option>
        </select>
    </p>
    <input type="submit" value="Submit" />
</form>
</body>
</html>