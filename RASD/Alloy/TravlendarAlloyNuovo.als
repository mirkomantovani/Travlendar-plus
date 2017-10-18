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

sig Location {

}

sig Meeting {
	location: one Location,
	route: one Travel,
	starts: one Date,
	duration: one Int,
	//ends: one Date,
	next: lone Meeting,
	previous: lone Meeting,
	reminders: set Reminder,
//	priority: lone Priority (enum)
 	participants: set String, //just emails, or we could do users who has the app
	warning: lone Warning,
	conflict: set Meetings
//ogni meeting nel set dei conflitti ha almeno o il next o il previous nel set,
//che si può esprimere come il fatto che non esiste meeting nel set dei conflitti tale che
//non ha nè il prev nè il next nel set di conflitti

//2 meeting sono in conflitto se e solo se è presente un warning che li contiene nel set dei conflitti
}

sig Warning {
	conflicts: set Meeting
} {
//meetings set has to be at least >=2 otherwise the conflict wouldn't exist
	#conflicts > 1
}


//all meetings must have a starting date	 > user registration date

//fun areAdjacent[m,m' : Meeting] : set Boolean {

//}

//if there is a warning, every meeting has to be adjacent to another one in the set
//of the warning


//no same starting date meetings
//no same ID, no same email

fun calendarOwner[c: Calendar]: set User {
	{u: User | u.calendar = c}
}

fact precedence {
	next=~previous
}

fact emailUnicity {
	no disj u1, u2: User | u1.email = u2.email
}

//user has only one calendar

//fact calendarUnicity {
// no disj c1, c2 : Calendar | calendarOwner[c1]
//}

pred show {

}

run show { } for 3
