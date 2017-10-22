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



pred show{}

run show for 3





//all meetings must have a starting date	 > user registration date


//no same starting date meetings
//no same ID, no same email
