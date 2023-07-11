<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
      <link rel="stylesheet" href="CSS/home.css">
    <title>Home</title>
  </head>

  <body>
  <h1>Lottery Draws</h1>
  <div class="login-create-container">
      <div class="left-box">
          <h2>Create an account.</h2>
          <form action="CreateAccount" method="post">
              <label>Choose an account role:</label>
              <div class="role-type">
                  <label for="admin1">Admin:</label>
                  <input type="radio" id="admin1" name="roles" value="admin">
                  <label for="public1">Public:</label>
                  <input type="radio" id="public1" name="roles" value="public">
              </div>

              <label for="firstname">First name:</label>
              <input type="text" id="firstname" name="firstname" required >

              <label for="lastname">Last name:</label>
              <input type="text" id="lastname" name="lastname" required>

              <label for="username">Username:</label>
              <input type="text" id="username" name="username" required>

              <label for="phone">Phone number:</label>
              <input type="text" id="phone" name="phone" maxlength="11" pattern="[0-9]{11}"
                     title="Format MUST be: 22-4444-7777777" placeholder="12-1234-1234567" required>

              <label for="email">Email:</label>
              <input type="email" id="email" name="email" placeholder="John@email.com" required>

              <label for="password">Password:</label>
              <input type="password" id="password" name="password" pattern="(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,15}"
                     title="Password must be between 8 and 15 characters long and MUST contain at least
                        one uppercase and lowercase letter and one digit." placeholder="•••••••••••••" required>

              <input type="submit" value="Submit">
          </form>
      </div>

      <div class="right-box">
          <h2>Login.</h2>
          <form action="UserLogin" method="post">
              <label for="username1">Username:</label>
              <input type="text" id="username1" name="username" required>
              <label for="password1">Password:</label>
              <input type="password" id="password1" name="password" required>
              <% if (request.getAttribute("attempts") != null){ %>
                    <p><%= request.getAttribute("attempts") %></p>
              <% } %>
              <input type="submit" id="loginbutton" value="Login">
          </form>
      </div>
  </div>
  </body>
<script>
    <% if (session.getAttribute("loginAttempts") != null) { %>
        <% if (session.getAttribute("loginAttempts").toString().equals("3")){ %>
            document.getElementById("loginbutton").hidden = true;
        <% } %>
    <% } %>
</script>
</html>
