<%-- 
    Document   : breakView
    Created on : 07-Dec-2017, 18:23:38
    Author     : matteo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <link rel="stylesheet" href="css/button.css">

        <link rel="stylesheet" href="css/meetingView.css" type="text/css">

        <link href="./bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
        
        <link rel="stylesheet" href="css/simple.css">
        <link rel="stylesheet" href="css/navbar.css">
        <link rel="stylesheet" href="css/body.css">

        <title>Break page</title>
    </head>
    <body>
        <style>
            .wrapper > * {
                background-color: #a444446b;


            }
            .buttonsinpage {
                font-size: 16px;
                margin: 10px;
            }
        </style>

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
                                <a href="#" class="raise" onclick="$(this).closest('form').submit()">Modify preferences</a>
                            </form>
                        </li>
                    
                        <li>
                            <form action="DisplayTravelMeans">
                                <a href="#" class="raise" onclick="$(this).closest('form').submit()">Select travel means</a>
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
                                <a href="#" id="${warningcolor}" class="raise" onclick="$(this).closest('form').submit()">Warnings</a>
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

        <div style="height: 50px"></div>

        <div class="wrapper" action="BreakVisualization">
            <h2 class="header" style="font-size:50px">${b.name}</h2>

            <aside class="sidebar">

                <form action="DeleteBreak?BreakID=${b.breakPK.breakid}"  >
                    <input type="hidden" name="BreakID" value="${b.breakPK.breakid}">
                    <button type="submit" class="offset buttonsinpage"> Delete break</button> 

                </form>

            </aside>
            <article class="content">
                <p>
                    Break time window: ${b.startingtime.hours}:${b.startingtime.minutes} - ${b.endingtime.hours}:${b.endingtime.minutes}
                </p>
                <p>
                    Minimum duration: ${b.minduration.hours}:${b.minduration.minutes}.
                </p>
                <p>
                    Day of week: ${b.dayofweek}
                </p>
                <p>
                    Recurrent: ${b.recurrent}
                </p>
            </article>

        </div>                

    </body>
</html>


