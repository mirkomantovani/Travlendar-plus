//***************************************************SIGNATURES************************************************//

abstract sig Boolean {

}

one sig true extends Boolean { }

one sig false extends Boolean { }


sig Travel {
	means: set TravelMean
}

abstract sig TravelMean { }

one sig Car extends TravelMean {

}

one sig SharedCar extends TravelMean { }

one sig Bike extends TravelMean { }

one sig SharedBike extends TravelMean { }

one sig Train extends TravelMean { }

one sig Metro extends TravelMean { }

one sig Bus extends TravelMean { }

one sig OnFoot extends TravelMean { }

sig Preferences {
	minimizeCarbonFootprint: one Boolean,
	breaks: set Break,
	activeMeansOfTransport: TravelMean -> one Boolean
}{
	 // A set of preferences must refers only to one user
	one u:User | this = u.preferences
}

sig Break {
	active: one Boolean,
	lunch: one Boolean
}

sig User {
	calendar: one Calendar,
	preferences: one Preferences
 }

sig Calendar {
	meetings: set Meeting
}{
	 // A calendar must refers only to one user
	one u:User | this = u.calendar
}

sig Reminder {

}

sig Location {

}

sig Meeting {
	location: one Location,
	route: one Travel,
	next: lone Meeting,
	previous: lone Meeting,
	reminders: set Reminder,
	status: one MeetingState,
	warning: lone Warning,
	conflict: set Meeting,
	weather: one WeatherForecast
} 

abstract sig MeetingState {
 
}

one sig Regular extends MeetingState{ }

one sig WithWarning extends MeetingState{ }

one sig Happening extends MeetingState{ }

one sig Passed extends MeetingState{ }

abstract sig WeatherForecast {

}

//This symbolizes every case of 'good' weather (e.g. Cloudy, Partially Cloudy, etc.)
one sig Sunny extends WeatherForecast{ }

//This symbolizes every case of 'bad' weather (e.g. Snowy, Stormy, extremely Windy, etc.)
one sig Rainy extends WeatherForecast { }

//doppia freccia tra meeting e warning

sig Warning {
	conflicts: set Meeting
} {
//meetings set has to be at least >=2 otherwise the conflict wouldn't exist
	#conflicts > 1
}

//problema: tutti i meeting nella stessa location

//**************************************************CONSTRAINTS**********************************************//

//-----------------------------------------------------------------------------MEETING STATUS CONSTRAINTS

//The status=With Warning implies that there is a Warning
fact {
	all m:Meeting | m.status=WithWarning implies #m.warning=1
}

//Every Meeting with a warning must have their status set to With Warning
fact {
	all w:Warning | all m:w.conflicts | m.status=WithWarning
}

//If the status is Regular, Happening or passed, the Meeting does not have any Warning
fact {
	all m:Meeting | (m.status=Regular or m.status=Passed or m.status=Happening ) 
				  implies #m.warning=0
}

//-----------------------------------------------------------------------------------CARDINALITY CONSTRAINTS

//Function to retrieve the Calendar owner
fun calendarOwner[c: Calendar]: set User {
	{u: User | u.calendar = c}
}

//A User can only have one Calendar
fact OneCalendarPerUser{ 
	no disj c1,c2: Calendar | all u:User | c1 in u.calendar && c2 in u.calendar
}

//Different Users can't have the same Calendar
fact noSameCalendarDifferentUsers{
	no disj u1,u2: User | some c: Calendar | c in u1.calendar && c in u2.calendar
}

//It can't exist a Calendar not associated to any User
fact noCalendarWithoutUser{
	all c:Calendar | some u:User| u.calendar=c
}

//It can't exist a Break not associated to any Preferences
fact noBreakOutsidePreferences{
	all b:Break | some p:Preferences| b in p.breaks
}

//It can't exist a Location not associated to any Meeting
fact noUnneededLocation{
	all l:Location | some m:Meeting | m.location=l
}

//One meeting can be in only one calendar
fact MeetingInOnlyOneCalendar {
	no disj c1,c2:Calendar | some m:Meeting | m in c1.meetings  && m in c2.meetings

}

//A reminder is unique for a Meeting
fact uniqueReminderForMeeting {
	no disj m1,m2: Meeting | some r:Reminder | r in m1.reminders && r in m2.reminders
}

//Every reminder is associated to a meeting
fact reminderIsSetForAMeeting{
	all r:Reminder | some m:Meeting | r in m.reminders
}

//There exists at least one Travel Mean for any Travel
fact atLeastOneTravelMean{ 
	all t:Travel | some m: TravelMean | m in t.means
}

//-----------------------------------------------------------------------------------WEATHER CONSTRAINTS


//If the forecasts say it's going to rain (WeatherForecast=Rainy) in the date of a Meeting, the Travel
//associated to that Meeting can't contain the following means of transport: OnFoot, Bike, SharedBike
fact  dontWetUsers {
	all m:Meeting | m.weather=Rainy implies (OnFoot not in m.route.means and 
				Bike not in m.route.means and SharedBike not in m.route.means)
}

//-----------------------------------------------------------------------------------MEETINGS CONSTRAINTS

fact precedenceRelationConstraint {
	next=~previous
}

//For every Meeting in any Calendar, the previous (and therefore also the next) is in that Calendar
fact{ 
	all m:Meeting | all c:Calendar | m in c.meetings implies m.previous in c.meetings 
}

//A Meeting can't have itself as next or previous meeting nor it can be next 
//of the next and so on (transitive closure), (previous is automatically 
//verified because of the precedenceRelationConstraint fact
fact precedence{
	no m: Meeting | m in m.^next
}

//If a Meeting is in conflict with another one, the second one must be in conflict with the first one
fact conflictualMeeting{
        all disj m1, m2: Meeting | m2 in m1.conflict implies m1 in m2.conflict
}

//A Meeting can't be in conflict with itself
fact noAutoConflict{
	no m: Meeting | m in m.conflict
}

//There can't be a conflict between two Meetings if they are in the same location
fact noConflictBetweenSameLocationMeetings{
	all disj m1,m2: Meeting | all l:Location | (m1.location = l && m2.location= l ) implies 
 	(m2 not in m1.conflict && m1 not in m2.conflict)
}


//---------------------------------------------------------------------------------WARNING CONSTRAINTS


//Every Meeting in his conflicts set must have the next Meeting or the Previous Meeting in the set
fact adjacentConflicts{
	all m: Meeting | all m2: m.conflict | m2.previous in m2 or m2.next in m2 
								or m2 = m.previous or m2 = m.next
} 

//It can't exist a Meeting in a Warning if this Meeting isn't in conflict with some other Meeting
fact noWarningWithoutConflicts{
	all disj m1,m2:Meeting |some w:Warning |  m1 in w.conflicts && m2 in w.conflicts implies m1 in m2.conflict && m2 in m1.conflict
}

//If there exists 2 Meetings in conflict with each other, there exists a Warning containing both of them
fact warningExistence{ 
	all disj m1,m2: Meeting | m1 in m2.conflict implies one m1.warning && 
						one m2.warning && (some w: Warning | m1 in w.conflicts
												 && m2 in w.conflicts)
}

//For every pair of disjoint Meetings contained in a Warning, one is in the conflict set of the other and viceversa
fact {
	all w: Warning | all disj m1,m2: w.conflicts | m1 in m2.conflict && m2 in m1.conflict
}

//A meeting can only be contained in one Warning              
fact exclusiveWarning{
	all m:Meeting | all disj w1,w2:Warning | m in w1.conflicts implies m not in w2.conflicts
}

//There can only exist a conflict between Meetings in the same Calendar
fact onlyConflictsInSameCalendar{ 
	all disj m1,m2:Meeting | some c:Calendar | m1 in m2.conflict and m2 in m1.conflict implies m1 in c.meetings and m2 in c.meetings
}

//Empty conflict set implies empty warning set and viceversa
fact {
	all m:Meeting | #m.conflict=0 implies #m.warning=0
}

//All meetings in a warning conflicts have to be in conflict with each other
fact noWarningIfNoConflicts{
	all w:Warning | all disj m1,m2: w.conflicts | m1 in m2.conflict && m2 in m1.conflict
}

//If a meeting is in a warning, the warning has the meeting in its conflicts
fact meetingWarningRelationCorrespondance{
	all w:Warning | all m:Meeting | (m in w.conflicts) implies (w in m.warning)
}



//--------------------------------------------------------------------------------------PREFERENCES CONSTRAINTS


//In every preferences at least one travel mean has to be selected
fact atLeastOneSelectedTravelMean{
	all p:Preferences | some t:TravelMean |one tr:true | tr in t.(p.activeMeansOfTransport) 
}

//A break has to be in one and only one Preferences
fact noEqualBreaks{
	no disj p1,p2:Preferences | some b:Break | b in p1.breaks and b in p2.breaks

}

//---------------------------------------------------------------------------------------TRAVELS CONSTRAINTS

//Meetings happening in different locations can't have the same travel to get there
fact noDifferentLocationsSameTravelMeetings{
	all disj m1,m2: Meeting | (m1.location != m2.location) implies (m1.route != m2.route)
	
}


//There cannot exist a travel not associated to any meeting
fact noDisassociatedTravel {
	all t:Travel | some m:Meeting | m.route=t
}

//The travel must include only travelMeans that are selected in the preferences 
fact onlyUseActiveTravelMeans{ 
 	no t:TravelMean | some u:User | some p:u.preferences | some c: u.calendar |
 some m:c.meetings | some r: m.route |
some f:false | t in r.means && f in t.(p.activeMeansOfTransport) 
	
}



//If a travel mean is deactivated, it can't be in any travel in any meeting


//**************************************************ASSERTIONS**********************************************//

assert singleUserCalendar {
	all c:Calendar | one u:User | u.calendar = c
}

check singleUserCalendar for 2

//There can't exist 2 warning with the same meeting in the conflicts set
assert noMoreWarningSameMeeting { 
	no disj w1,w2: Warning | some m:Meeting | m in w1.conflicts and m in w2.conflicts
}

check noMoreWarningSameMeeting for 3

assert usersAreNeverGettingWet {
	no m: Meeting | m.weather=Rainy and ((Bike in m.route.means or SharedBike 
							in m.route.means or OnFoot in m.route.means))
}

check usersAreNeverGettingWet for 4

assert neverUseInactiveTravelMeans{
 	no u:User | some t:TravelMean | some travel: u.calendar.meetings.route |
	 false in t.(u.preferences.activeMeansOfTransport) and t in travel.means
	
}

check neverUseInactiveTravelMeans





pred show {

}
run show { } for 3 but exactly 8 Meeting, exactly 1 User
