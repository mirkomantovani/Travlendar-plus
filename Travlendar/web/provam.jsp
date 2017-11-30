<%-- 
    Document   : addmeeting
    Created on : 30-nov-2017, 10.40.45
    Author     : Mirko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add meeting</title>
  
        <link rel="stylesheet" href="css/maps.css">
    </head>
    <body>
      
            
            <div class="pac-card" id="pac-card">
  <div>
    <div id="title">
      Autocomplete search
    </div>
    <div id="type-selector" class="pac-controls">
      <input type="radio" name="type" id="changetype-all" checked="checked">
      <label for="changetype-all">All</label>

      <input type="radio" name="type" id="changetype-establishment">
      <label for="changetype-establishment">Establishments</label>

      <input type="radio" name="type" id="changetype-address">
      <label for="changetype-address">Addresses</label>

      <input type="radio" name="type" id="changetype-geocode">
      <label for="changetype-geocode">Geocodes</label>
    </div>
    <div id="strict-bounds-selector" class="pac-controls">
      <input type="checkbox" id="use-strict-bounds" value="">
      <label for="use-strict-bounds">Strict Bounds</label>
    </div>
  </div>
  <div id="pac-container">
    <input id="pac-input" type="text" placeholder="Enter a location">
  </div>
</div>
<div id="map"></div>
<div id="infowindow-content">
  <img src="" width="16" height="16" id="place-icon">
  <span id="place-name" class="title"></span>
  <br>
  <span id="place-address"></span>
</div>
            
        

        <script type="text/javascript" src="./js/maps.js" charset="UTF-8"></script>
        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCgubm7qcpSLSZDSQKTCVrMaS5IHKcYySM&libraries=places&callback=initMap" async defer></script>
        
        
    </body>
</html>
