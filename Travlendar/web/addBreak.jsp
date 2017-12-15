<%-- 
    Document   : addBreak
    Created on : 6-dic-2017, 21.40.00
    Author     : Mirko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add break</title>

        <link rel="stylesheet" href="css/jquery.timepicker.min.css">
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




        <div class="page">
            <div class="page__demo">
                <div class="main-container">
                    <div class="page__container">  
                        <form action="AddBreakServlet" >
                            
                            <h2>Name</h2>
                            <input type="text" name="name" placeholder="Insert break name" />

                            <article>
                                <div class="demo">
                                    <h2>From</h2>
                                    <p>
                                        <input id="from" name="from" value="${nopublic}" type="text" class="time" />
                                    </p>
                                </div>
                                <script>
                                    $(function () {
                                        $('#from').timepicker({
                                            'timeFormat': 'H:i',
                                            'step': 15
                                        });
                                    });
                                </script>

                            </article>

                            <article>
                                <div class="demo">
                                    <h2>To</h2>
                                    <p>
                                        <input id="to" name="to" value="${nopublic}" type="text" class="time" />
                                    </p>
                                </div>
                                <script>
                                    $(function () {
                                        $('#to').timepicker({
                                            'timeFormat': 'H:i',
                                            'step': 15
                                        });
                                    });
                                </script>

                            </article>

                            <article>
                                <div class="demo">
                                    <h2>Duration</h2>
                                    <p>
                                        <input id="duration" name="duration" value="${nopublic}" type="text" class="time" />
                                    </p>
                                </div>
                                <script>
                                    $(function () {
                                        $('#duration').timepicker({
                                            'timeFormat': 'H:i',
                                            'disableTimeRanges': [
                                            ['0am', '0.05am'],
                                            ['3am', '24pm']
                                            ],
                                            'step': 5
                                        });
                                    });
                                </script>

                            </article>


                            <h2>Recurrent</h2>
                            <label class="switch switch_type1" role="switch">
                                <input type="checkbox" name="recurrent" class="switch__toggle" ${mincarbonfootprint}>
                                <span class="switch__label"></span>
                            </label>
                                
                                <br><br>
                            
                                <h2>Select days of week</h2>

                            <div class="weekDays-selector">
                                <input name="mon" type="checkbox" id="weekday-mon" class="weekday" />
                                <label for="weekday-mon">M</label>
                                <input name="tue" type="checkbox" id="weekday-tue" class="weekday" />
                                <label for="weekday-tue">T</label>
                                <input name="wed" type="checkbox" id="weekday-wed" class="weekday" />
                                <label for="weekday-wed">W</label>
                                <input name="thu" type="checkbox" id="weekday-thu" class="weekday" />
                                <label for="weekday-thu">T</label>
                                <input name="fri" type="checkbox" id="weekday-fri" class="weekday" />
                                <label for="weekday-fri">F</label>
                                <input name="sat" type="checkbox" id="weekday-sat" class="weekday" />
                                <label for="weekday-sat">S</label>
                                <input name="sun" type="checkbox" id="weekday-sun" class="weekday" />
                                <label for="weekday-sun">S</label>
                            </div>
                                
                            <style>
                                .weekDays-selector input {
                                    display: none!important;
                                }

                                .weekDays-selector input[type=checkbox] + label {
                                    display: inline-block;
                                    border-radius: 6px;
                                    background: #bbb;
                                    height: 40px;
                                    width: 30px;
                                    margin-right: 3px;
                                    line-height: 40px;
                                    text-align: center;
                                    cursor: pointer;
                                }

                                .weekDays-selector input[type=checkbox]:checked + label {
                                    background: #2f61a8;
                                    color: #ffffff;
                                }
                            </style>

                            <br>
                            <br>

                            <button type="submit" class="offset">Add break</button>
                            <br><br

                        </form>
                    </div>
                </div>
            </div>
            
        </div>

        <script type="text/javascript" src="./js/jquery.timepicker.min.js"></script>
    </body>
</html>
