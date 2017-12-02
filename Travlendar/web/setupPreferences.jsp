<%-- 
    Document   : tog
    Created on : 1-dic-2017, 12.08.06
    Author     : Mirko
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="css/simple.css">
        <link rel="stylesheet" href="css/button.css">
    </head>
    <body>
       
        <form action="ModifyPreferences" >
        <div class="page">
  <div class="page__demo">
    <div class="main-container">
      <div class="page__container">  
          <h2>Minimize Carbon Footprint</h2>
        <label class="switch switch_type1" role="switch">
          <input type="checkbox" name="mincarbonfootprint" class="switch__toggle" ${mincarbonfootprint}>
          <span class="switch__label"></span>
        </label>
          <h2>Avoid Tolls</h2>
        <label class="switch switch_type2" role="switch">
          <input type="checkbox" name="avoidtolls" class="switch__toggle " ${avoidtolls}>
          <span class="switch__label"> </span>
        </label> 
          <h2>Avoid Motorways </h2>
        <label class="switch switch_type3" role="switch">
          <input type="checkbox" name="avoidmotorways" class="switch__toggle" ${avoidmotorways} >
          <span class="switch__label"></span>
        </label>
          <br>
          <br>
          
          <button type="submit" class="offset">Apply changes</button>
      </div>
    </div>
  </div>
  <footer class="footer">
   
  </footer>
</div>
        </form>
    </body>
</html>
