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
	//one u:User | this = u.calendar
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

//**************************************************PREDICATES**********************************************//

pred CreateMeetingEmptyCalendar[m:Meeting, r:Regular, c,c':Calendar]{
//preconditions

r in m.status &&
m not in c.meetings &&
#c.meetings = 0 &&

//postconditions
c'.meetings = c.meetings + m &&
m in c'.meetings


}

run CreateMeetingEmptyCalendar for 1 but exactly 2 Calendar











