<%-- 
    Document   : homepage
    Created on : 27-nov-2017, 21.55.38
    Author     : Mirko
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Travlendar+ Homepage</title>
        
        
        <link rel="stylesheet" href="css/fullcalendar.min.css">
        <link rel="stylesheet" href="css/fullcalendar.print.min.css" media='print'>
        <link rel="stylesheet" href="css/button.css">
       
        <script type="text/javascript" src="./js/moment.min.js"></script>
        <script type="text/javascript" src="./js/jquery.min.js"></script>
        <script type="text/javascript" src="./js/fullcalendar.min.js"></script>
     
        <link rel="stylesheet" href="./bootstrap/css/bootstrap.min.css">
        
        
        <link rel="stylesheet" href="css/simple.css">
        <link rel="stylesheet" href="css/navbar.css">
        <link rel="stylesheet" href="css/body.css">
       <!-- <script type="text/javascript" src="./jquery/jquery-1.8.3.min.js" charset="UTF-8"></script> -->
        <script type="text/javascript" src="./bootstrap/js/bootstrap.min.js"></script>
        
        
        
    </head>
    <body>
        <%
            if (session.getAttribute("name") == null) {
                response.sendRedirect("login.jsp");
            }

        %>
        
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
                        
                        <br><br><br>

        
        
        <div id='calendar'></div>
        
        <script>
            

	$(document).ready(function() {
            
		
		$('#calendar').fullCalendar({
                    
                   
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'month,agendaWeek,agendaDay'
			},
			defaultDate: '${today}',
			navLinks: true, // can click day/week names to navigate views
			selectable: true,
			selectHelper: true,
			select: function(start, end) {
                                
				var title = prompt('Meeting Name:');
				var eventData;
				if (title) {
					eventData = {
						title: title,
						start: start,
						end: end
					};
                                        window.location.href = 'addmeeting.jsp?meetingname='+title;
					$('#calendar').fullCalendar('renderEvent', eventData, true); // stick? = true
				}
				$('#calendar').fullCalendar('unselect');
			},
			editable: true,
			eventLimit: true, // allow "more" link when too many events
			events: [
                             ${meeeets}
                           
			]
		});
		
	});

</script>
<style>

	body {
            margin: 40px 10px;
		padding: 0;
		/*font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;
		font-size: 14px; */
                color: #eee;
                
	}
        
        .fc-state-default {
            background-color: #3f65b7;
        }
        .fc-state-active {
            color: #fff;
        }

	#calendar {
		max-width: 900px;
		margin: 0 auto;
	}
        
        a{
            color: #eee;
        }

</style>
    </body>
</html>
