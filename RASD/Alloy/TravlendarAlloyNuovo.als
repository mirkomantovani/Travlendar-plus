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
}{
	one u:User | this = u.preferences // A set of preferences must refers only to one user
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
}{
	one u:User | this = u.calendar // A calendar must refers only to one user
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
	conflict: set Meeting
}

sig Warning {
	conflicts: set Meeting
} {
//meetings set has to be at least >=2 otherwise the conflict wouldn't exist
	#conflicts > 1
}

fun calendarOwner[c: Calendar]: set User {
	{u: User | u.calendar = c}
}

fact precedence {
	next=~previous
}

fact emailUnicity {
	no disj u1, u2: User | u1.email = u2.email
}

fact conflictualMeeting{
         some disj m1, m2: Meeting | m2 in m1.conflict implies m1 in m2.conflict
}

fact adjacentConflicts{//ogni meeting nel set dei conflitti ha almeno o il next o il previous nel set
	all m: Meeting | all m2: m.conflict | m2.previous in m2 or m2.next in m2 
								or m2 = m.previous or m2 = m.next
} 

fact warningExistence{ // se esistono due meeting in conflitto esiste un warning che li contiene entrambi
	all disj m1,m2: Meeting | m1 in m2.conflict implies one m1.warning && 
						one m2.warning && (some w: Warning | m1 in w.conflicts
												 && m2 in w.conflicts)
}

fact exclusiveWarning{//se un meeting è in un warning non è in altri warning
	all m:Meeting | some disj w1,w2:Warning | m in w1.conflicts implies m not in w2.conflicts
}

fact atLeastOneTravelMean{ //esiste almeno un mezzo di trasporto selezionato per ogni viaggio
	all t:Travel | some m: TravelMean | m in t.means
}

fact atLeastOneSelectedTravelMean{ //nelle preferenze deve essere selezionato almeno un mezzo di trasporto
	all p:Preferences | some t:TravelMean |one tr:true | tr in t.(p.activeMeansOfTransport) 
}

fact OneCalendarPerUser{ //user has only one calendar
	no disj c1,c2: Calendar | all u:User | c1 in u.calendar && c2 in u.calendar
}

fact onlyConflictsInSameCalendar{ //i conflitti valgono solo nello stesso calendario
	all w: Warning | all m: w.conflicts | some c: Calendar | m in c.meetings
}

fact NoEqualBreaks{
	no disj b1,b2: Break | all u: User | b1 = b2 and b1 in u.preferences.breaks and b2 in u.preferences.breaks

}

assert singleUserCalendar {
	all c:Calendar | one u:User | u.calendar = c
}

check singleUserCalendar



pred show {

}
run show { } for 3
