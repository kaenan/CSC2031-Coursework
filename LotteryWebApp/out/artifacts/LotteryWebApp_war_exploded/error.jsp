<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="CSS/home.css">
    <title>Error</title>
</head>
<body>
   <div class="container">
       <div class="background-image"></div>
       <div class="error">
           <img src="images/Error.png" alt="Error">
       </div>
       <div class="login-create-container">
           <% if (request.getAttribute("message") != null) { %>
               <p><%= request.getAttribute("message") %></p>
           <% } %>

           <% if (request.getAttribute("attempts") != null) { %>
                <p><%= session.getAttribute("attempts")%></p>
           <% } %>

           <a class="page-nav-button" href="index.jsp">Home Page</a>
       </div>
   </div>
</body>
</html>
