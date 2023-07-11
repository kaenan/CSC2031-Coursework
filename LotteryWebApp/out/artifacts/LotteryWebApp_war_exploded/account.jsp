<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Account</title>
</head>
<body>
<h1>User Account</h1>
<p><%= request.getAttribute("message") %></p>
<h3>Session Information:</h3>
<p><%= session.getAttribute("firstname")%>,
 <%= session.getAttribute("lastname")%>,
 <%= session.getAttribute("email")%>,
 <%= session.getAttribute("username")%></p><br>
<div>
    <form action="AddUserNumbers" method="post">
        <label>Enter 6 numbers between 1 and 60:</label><br>
        <input type="number" id="num1" name="num1" size="2" min="1" max="60" required>
        <input type="number" id="num2" name="num2" size="2" min="1" max="60" required>
        <input type="number" id="num3" name="num3" size="2" min="1" max="60" required>
        <input type="number" id="num4" name="num4" size="2" min="1" max="60" required>
        <input type="number" id="num5" name="num5" size="2" min="1" max="60" required>
        <input type="number" id="num6" name="num6" size="2" min="1" max="60" required>
        <button type="button" onclick="randomNums()">Random</button><br><br>
        <input type="submit" value="Submit">
    </form>
</div>
<div>
    <form action="GetUserNumbers" method="post">
        <label>Get Draws:</label><br><br>
        <input type="submit" value="Get Draws"><br>
        <% if (request.getAttribute("draws") != null){ %>
                <% String[] draws1 = (String[]) request.getAttribute("draws"); %>
                <% for (String s : draws1){%>
             <p><%= s %></p>
                <% }; %>
        <input type="button" onclick="winner()" value="Winning Numbers">
        <p id="winNumbers"> </p>
        <p id="winMessage"> </p>
        <% }; %>
    </form>
</div>

<div>
    <form action="lotteryTryagain" method="post" id="tg" style="display: none">
        <input type="submit" value="Try Again">
    </form>
</div>

<a href="index.jsp">Home Page</a>
</body>
<script>
    function randomNums() {
        document.getElementById("num1").setAttribute("value", Math.floor((Math.random() * 60) + 1));
        document.getElementById("num2").setAttribute("value", Math.floor((Math.random() * 60) + 1));
        document.getElementById("num3").setAttribute("value", Math.floor((Math.random() * 60) + 1));
        document.getElementById("num4").setAttribute("value", Math.floor((Math.random() * 60) + 1));
        document.getElementById("num5").setAttribute("value", Math.floor((Math.random() * 60) + 1));
        document.getElementById("num6").setAttribute("value", Math.floor((Math.random() * 60) + 1));
    }
    function winner() {
        <% if (request.getAttribute("winningNumbers") != null){ %>
            document.getElementById("winNumbers").innerHTML = "<%= request.getAttribute("winningNumbers") %>";
            <% String[] draws1 = (String[]) request.getAttribute("draws"); %>
            <% String winners = request.getAttribute("winningNumbers").toString(); %>
            <% for (String s : draws1){%>
                    <% if (s.equals(winners)){ %>
                        document.getElementById("winMessage").innerText = "Winner winner chicken dinner";
                <% } else { %>
                        document.getElementById("winMessage").innerText = "Not a winner";
                    <% } %>
            <% } %>
        <% } %>
        document.getElementById("tg").style.display = "block"
    }
</script>
</html>

