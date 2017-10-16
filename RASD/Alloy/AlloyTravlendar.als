//defining basic data types? boolean, integers, strings,

sig Date {
	year: one int,
   	month: one int,
	day: one int,
	hour: one int,
	minutes: one int
}

sig User {
	name: one String,
	surname: one String,
	ID: one String,
	email: one String,
	registrationDate: one Date,
	calendar: one Calendar
 }

sig Calendar {
	meetings: set Meeting
}

sig Meeting {
	location: one Location,
	starts: one Date,
	ends: one Date,
	next: lone Meeting,
	previous: lone Meeting,
//	priority: lone Priority (enum)
 	participants: set String //just emails, or we could do users who has the app
}

sig Warning {
	conflicts: set Meeting
} {
//meetings set has to be at least >=2 otherwise the conflict wouldn't exist
	#conflicts > 1
}

sig

//all meetings must have a starting date	 > user registration date



//no same starting date meetings

//no same ID, no same email
