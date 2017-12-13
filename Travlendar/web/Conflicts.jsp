<%-- 
    Document   : Conflicts
    Created on : 02-Dec-2017, 15:30:56
    Author     : matteo
--%>

<%@page import="java.util.HashMap"%>
<%@page import="entities.Break"%>
<%@page import="java.util.Map"%>
<%@page import="entities.Meeting"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entities.Warning"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        
      
        
      <link rel="stylesheet" href="css/list.css" type="text/css">    
      <link rel="stylesheet" href="css/navbar.css">
      <link rel="stylesheet" href="css/simple.css">
      <link href="./bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
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
                    <a class="navbar-brand" href="homepage.jsp">TRAVLENDAR+</a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        
                        <li class="active">
                            <form action="DisplayPreferences">
                                <a href="#" onclick="$(this).closest('form').submit()">Modify preferences</a>
                            </form>
                        </li>
                    
                        <li>
                            <form action="DisplayTravelMeans">
                                <a href="#" onclick="$(this).closest('form').submit()">Select travel means</a>
                            </form>
                        </li>
                        
                        <li>
                            <form action="DisplayTravelMeans">
                                <a href="addmeeting.jsp" >Add meeting</a>
                            </form>
                        </li>
                        
                        <li class="dropdown">
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
                        </li>
                    </ul>
                    <form class="navbar-form navbar-left" role="search">
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="Search meeting">
                        </div>
                        <button type="submit" class="btn btn-default">Submit</button>
                    </form>
                    <ul class="nav navbar-nav navbar-right">
                        <li><p class="navbar-text">Logged in as: ${name} </p></li>
                        <li><form action="Logout">
                                <a href="#" onclick="$(this).closest('form').submit()">Logout</a>
                            </form></li>
                        
                    </ul>
                </div><!-- /.navbar-collapse -->
            </div><!-- /.container-fluid -->
        </nav>
                        
                        <br><br><br>
                         
        <div style="height: 50px"></div>                 
                         
        <h1>
            Conflicts
        </h1>
        <div style="height: 5px"></div>

        <form action="ConflictVisualization">
    <%
        if(session.getAttribute("error").equals("NO Warnings detected"))
            out.print("No warnings detected!");
        else{
        ArrayList<Warning> warnings = new ArrayList<Warning>();
        warnings = (ArrayList<Warning>)session.getAttribute("warnings");
        if(warnings == null){
          System.out.println("sto stampando i warning null");  
        }
        System.out.println("sto stampando i warning" + warnings.toString());
        Map<Warning,Meeting[]> meetings = new HashMap<Warning,Meeting[]>();
        meetings = (HashMap<Warning,Meeting[]>)session.getAttribute("meetings");
        Map<Warning,Break[]> breaks = new HashMap<Warning,Break[]>();
        breaks = (HashMap<Warning,Break[]>)session.getAttribute("breaks");
        int counter = 1;
            
    for(Warning w : warnings){
       out.print("<div id='modal'>" +
	"<h1>Warning n." + counter + "</h1>");
       if(meetings.get(w).length!=0){
            out.print("Involved meetings:\n");
            for(int j=0; j<meetings.get(w).length;j++){
                out.print(meetings.get(w)[j] + "\n");
            }
       }
       if(breaks.get(w).length != 0){
            out.print("Involved Breaks:\n");
            for(int k=0;k<breaks.get(w).length;k++){
                out.print(breaks.get(w)[k]);
            }
       }
       out.print("<a class='yes' href='javascript:void(0);'>Solve</a>" +
        "<form action='UpdateMeeting?MeetingID='${meetings.get(w)[0].meetingPK.meetingid}'  >" +
                    "<input type='hidden' name='meetingid' value='${meetings.get(w)[0].meetingPK.meetingid}'>" +
        "</form>" +
        "<form action='DeleteWarning?WarningID='${w.warningPK.warningid}'  >" +
                    "<input type='hidden' name='warningid' value='${w.warningPK.warningid}}'>" +
	"<a class='no' href='javascript:void(0);'>Ignore</a>" +
        "</form>" +
        "</div>" +
        "<div style='height: 15px'></div>");
       counter = counter + 1;
    }
        
     if(!warnings.isEmpty())
       out.print("<form action='DeleteAllWarnings'>" +
                     "<div id='ignoreAll' style='position: absolute; left: 45%;'>" +
                         "<a class='no' href='javascript:void(0);' >IgnoreAll</a>" + 
                     "</div>" +
                 "</form>");
        
     else 
        out.print("No warnings detected!");
     
        }
        %>
        </form>
     </body>
    
</html>
