//defining basic data types? boolean, integers, strings,

abstract sig Boolean {
	
}

one sig true extends Boolean {

}

one sig false extends Boolean {

}

sig Time {
	hour: one Int,
	minutes: one Int
}

sig Date {
	year: one Int,
   	month: one Int,
	day: one Int,
	time: one Time
}

sig Travel {
//duration in minutes
	duration: one Int,
//length in km
	length: one Int,
	means: set TravelMean
}

abstract sig TravelMean {

}

sig Location{}

sig Preferences {
	minimizeCarbonFootprint: one Boolean,
//distance in km
	maxWalkingDistance: one Int,
	noPublicTransportsAfter: one Time,
	breaks: set Break,
	activeMeansOfTransport: TravelMean -> one Boolean
}

sig Break {
 	starts: one Date,
	ends: one Date,
	active: one Boolean,
	lunch: one Boolean
//if lunch min 30 mins
}

sig User {
	name: one String,
	surname: one String,
	ID: one String,
	email: one String,
	registrationDate: one Date,
	calendar: one Calendar,
	preferences: one Preferences
 }

sig Calendar {
	meetings: set Meeting
}

sig Reminder {

}

sig Meeting {
	location: one Location,
	route: one Travel,
	starts: one Date,
	ends: one Date,
	next: lone Meeting,
	previous: lone Meeting,
	reminders: set Reminder,
//	priority: lone Priority (enum)
 	participants: set String, //just emails, or we could do users who has the app
	warning: lone Warning
}

sig Warning {
	conflicts: set Meeting
} {
//meetings set has to be at least >=2 otherwise the conflict wouldn't exist
	#conflicts > 1
}

fun isPrecedent [m,m1: Meeting] : one Meeting{
	m.(starts.day) > m1.(starts.day) && m.(starts.month) >= m1.(starts.month) && m.(starts.year) >= m1.(starts.year) 
	&& m.(starts.time.hour) >= m1.(starts.time.hour) && m.(starts.time.minutes)>= m1.(starts.time.minutes) && 
	m.(ends.day) > m1.(ends.day) && m.(ends.month) >= m1.(ends.month) && m.(ends.year) >= m1.(ends.year) 
	&& m.(ends.time.hour) >= m1.(ends.time.hour) && m.(ends.time.minutes)>= m1.(ends.time.minutes)
	implies m1 
		else m
}

fact{
 Time.hour <24 && Time.minutes < 60 
}

fact {
Date.year >= 2017 && Date.day <=31 && Date.month <=12 
}

fact { //costraints on less than 31 days
Date.month = 2 implies Date.day<=28 
&&
(Date.month=11 || Date.month = 4 || Date.month= 6 || Date.month = 9) implies Date.day<=30
}

assert noOverlappedMeetings{
no m: Meeting, m1:Meeting | m.(starts.day) = m1.(starts.day) && m.(starts.month) = m1.(starts.month) && m.(starts.year) = m1.(starts.year) 
	&& m.(starts.time.hour) = m1.(starts.time.hour) && m.(starts.time.minutes)= m1.(starts.time.minutes) && 
	m.(ends.day) = m1.(ends.day) && m.(ends.month) = m1.(ends.month) && m.(ends.year) = m1.(ends.year) 
	&& m.(ends.time.hour) = m1.(ends.time.hour) && m.(ends.time.minutes)= m1.(ends.time.minutes)
}

check noOverlappedMeetings

pred show{}

run show for 2 User





//all meetings must have a starting date	 > user registration date


//no same starting date meetings
//no same ID, no same email
