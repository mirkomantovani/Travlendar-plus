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
        <link href="./bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
        <link href="./css/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
        <link rel="stylesheet" href="css/style.css">
      
    </head>
    <body>
        <div class="container">
            <form action="AddMeetingServlet" class="form-horizontal"  role="form">
                <fieldset>
                    <legend>Add Meeting</legend>
                    <input type="text" name="name" value="" placeholder="Insert name " class="login-input" />
                    <div class="form-group">
                        <label for="dtp_input1" class="col-md-2 control-label">DateTime Picking</label>
                        <div class="input-group date form_datetime col-md-5" data-date="1979-09-16T05:25:07Z" data-date-format="dd MM yyyy - HH:ii p" data-link-field="dtp_input1">
                            <input class="form-control" size="20" type="text" value="" name="date" readonly>
                            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                            <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                        </div>
                        <input type="hidden" id="dtp_input1" value="" /><br/>
                    </div>

                    <input type="submit" value="Add meeting" class="login-submit"/>
                    
   
                </fieldset>
                

            </form>
            
        </div>

        
        <script type="text/javascript" src="./jquery/jquery-1.8.3.min.js" charset="UTF-8"></script>
        <script type="text/javascript" src="./bootstrap/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="./js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
        <script type="text/javascript" src="./js/locales/bootstrap-datetimepicker.fr.js" charset="UTF-8"></script>
        <script type="text/javascript">
            $('.form_datetime').datetimepicker({
                format: "dd MM yyyy - hh:ii",
                autoclose: true,
                todayBtn: true,
                startDate: "2017-11-10 10:00",
                minuteStep: 5
            });


        </script>  
        
    </body>
</html>
