<%-- 
    Document   : Conflicts
    Created on : 02-Dec-2017, 15:30:56
    Author     : matteo
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
    <head>
        
      
        
      <link rel="stylesheet" href="css/list.css" type="text/css">    
      <link href="./bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
      <link rel="stylesheet" href="css/button.css">
      <link rel="stylesheet" href="css/simple.css">
        <link rel="stylesheet" href="css/navbar.css">
        <link rel="stylesheet" href="css/body.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
                <!--          ----NAVBAR----         -->
        <nav class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
            <div class="container-fluid">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand pulse" href="homepage.jsp">TRAVLENDAR+</a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        
                        <li class="active">
                            <form action="DisplayPreferences">
                                <a href="DisplayPreferences" class="raise" onclick="$(this).closest('form').submit()">Modify preferences</a>
                            </form>
                        </li>
                    
                        <li>
                            <form action="DisplayTravelMeans">
                                <a href="DisplayTravelMeans" class="raise" onclick="$(this).closest('form').submit()">Select travel means</a>
                            </form>
                        </li>
                        
                        <li>
                            <form>
                                <a href="addmeeting.jsp?meetingname=&quot;&quot" class="raise" >Add meeting</a>
                            </form>
                        </li>
                        
                        <li>
                            <form>
                                <a href="addBreak.jsp" class="raise" >Add break</a>
                            </form>
                        </li>
                        
                        <li>
                            <form action="ConflictVisualization">
                                <a href="ConflictVisualization" id="${warningcolor}" class="raise" onclick="$(this).closest('form').submit()">Warnings</a>
                            </form>
                            <style>
                                #red{
                                    color:#f00;
                                    font-weight: bold;
                                }
                            </style>
                        </li>
                        
                      <!--  <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <span class="caret"></span></a>
                            <ul class="dropdown-menu" role="menu">
                                <li><a href="#">Action</a></li>
                                <li><a href="#">Another action</a></li>
                                <li><a href="#">Something else here</a></li>
                                <li class="divider"></li>
                                <li><a href="#">Separated link</a></li>
                                <li class="divider"></li>
                                <li><a href="#">One more separated link</a></li>
                            </ul>
                        </li> -->
                    </ul>
                    <form action="SearchMeeting" class="navbar-form navbar-left" role="search">
                        <div class="form-group">
                            <input type="text" name="meetingname" class="form-control" placeholder="Search meeting">
                        </div>
                        <button type="submit" class="btn btn-default fill">Submit</button>
                    </form>
                    <ul class="nav navbar-nav navbar-right">
                        <li><p class="navbar-text">Logged in as: ${name} </p></li>
                        <li><form action="Logout">
                                <a href="#" class="raise" onclick="$(this).closest('form').submit()">Logout</a>
                            </form></li>
                        
                    </ul>
                </div><!-- /.navbar-collapse -->
            </div><!-- /.container-fluid -->
        </nav>
                      
                         
        <div style="height: 50px"></div>                 
                         
        <h1>
            <c:out value="Warnings"/>
        </h1>
        <div style="height: 5px"></div>
        
       

        <div action="ConflictVisualization" >
        
            <c:forEach items="${warnDetails}" var= "i" begin="0">
                <div id='modal'> 
                    <h1>Warning n.  Warning ${i.w.warningPK.warningid} <p> </h1>
                    
                        <h3>Involved meetings: </h3> 
                        <c:forEach items = "${i.meetings}" begin="0" var = "m">
                            <c:out value = "${m.name}"/> 
                            <br><br>
                             <form action="UpdateMeeting?MeetingID=${m.meetingPK.meetingid}"> 
                                <input type='hidden' name='meetingid' value='${m.meetingPK.meetingid}'>  
                                <a class='yes' href='DisplayUpdateMeeting?meetingid=${m.meetingPK.meetingid}' style='position: relative; left: 24%;'>Edit</a> 
                            </form>
                          
                                <br><br><br><br>
                         </c:forEach>
                    
                        <h3>Involved Breaks:</h3>
                        <c:forEach items="${i.breaks}" begin="0" var="b">
                            
                            <c:out value = "${b.name}"/><br>
                             
                         </c:forEach>
                    
                            
                        <form action="DeleteWarning?WarningID=${i.w.warningPK.warningid}">
                             <input type='hidden' name='warningid' value='${i.w.warningPK.warningid}'>
                             <a class='no' href='DeleteWarning?WarningID=${i.w.warningPK.warningid}' style='position:relative; right: 24%;'>Ignore</a>
                        </form>
                           
        </div>
        <div style='height: 15px'></div> 
            
            </c:forEach>   
       
        <form id="modal" action='DeleteAllWarnings'>
                     <div id='ignoreAll' style='position: relative; right: 19%;'>
                         <a class='no' href='DeleteAllWarnings' >IgnoreAll</a>
                     </div> 
                 </form>
      
        </div>   
   
        </form>
     </body>
    
</html>
