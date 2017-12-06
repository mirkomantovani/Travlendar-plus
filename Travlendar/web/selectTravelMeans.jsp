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
        <title>Select travel means</title>

        <link rel="stylesheet" href="css/button.css">
        <link rel="stylesheet" href="./bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="css/navbar.css">
        <link rel="stylesheet" href="css/simple.css">
        <script type="text/javascript" src="./jquery/jquery-1.8.3.min.js" charset="UTF-8"></script>
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
                            <form action="DisplayWarnings">
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




        <div class="page">
            <div class="page__demo">
                <div class="main-container">
                    <div class="page__container">  

                        <form action="ModifyTravelMeans" >
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

                            <button type="submit" class="offset">Apply changes</button>
                            
                            </form>


                    </div>
                </div>
            </div>

        </div>
    
</body>
</html>
