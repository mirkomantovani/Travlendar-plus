<%-- 
    Document   : selectTravelMeans
    Created on : 1-dic-2017, 12.36.20
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
        <form action="ModifyTravelMeans" >
        <div class="page">
  <div class="page__demo">
    <div class="main-container">
      <div class="page__container">  
          <h2>Owned car</h2>
        <label class="switch switch_type1" role="switch">
            
          <input type="checkbox" name="oc" class="switch__toggle" ${oc}>
          <span class="switch__label"></span>
        </label>
          <h2>Shared car</h2>
        <label class="switch switch_type1" role="switch">
            
          <input type="checkbox" name="sc" class="switch__toggle" ${sc}>
          <span class="switch__label"></span>
        </label>
          <h2>Owned bike</h2>
        <label class="switch switch_type2" role="switch">
          <input type="checkbox" name="ob" class="switch__toggle" ${ob}>
          <span class="switch__label"> </span>
        </label> 
          <h2>Shared bike</h2>
        <label class="switch switch_type2" role="switch">
          <input type="checkbox" name="sb" class="switch__toggle" ${sb}>
          <span class="switch__label"> </span>
        </label> 
          <h2>Walking</h2>
        <label class="switch switch_type3" role="switch">
          <input type="checkbox" name="w" class="switch__toggle" ${w}>
          <span class="switch__label"></span>
        </label>          
          <h2>Public transports</h2>
        <label class="switch switch_type3" role="switch">
          <input type="checkbox" name="p" class="switch__toggle" ${p}>
          <span class="switch__label"></span>
        </label> 
          <br>
          <br>
          
          <button type="submit" class="offset">Submit</button>
          
          
      </div>
    </div>
  </div>
  
</div>
        </form>
    </body>
</html>
