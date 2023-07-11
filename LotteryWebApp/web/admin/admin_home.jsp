<%--
  Created by IntelliJ IDEA.
  User: Kaenan
  Date: 10/11/2020
  Time: 00:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Page</title>
</head>
<body>
<h1>Admin Page</h1>
<% if (request.getAttribute("message") == null){ %>
<p> </p>
<% } else { %>
<p><%= request.getAttribute("message") %></p>
<% } %>

    <form action="${pageContext.request.contextPath}/DBOutput" method="post">
        <label for="viewingDB">View Database:</label>
        <input type="radio" name="db" id="viewingDB" value="View Database">
        <input type="submit" value="Submit">
    </form>

    <% if (request.getAttribute("data") == null) {%>
    <p> </p>
    <% }else { %>
    <%= request.getAttribute("data") %>
    <% } %>

<a href="${pageContext.request.contextPath}/index.jsp">admin page</a>
</body>
</html>
