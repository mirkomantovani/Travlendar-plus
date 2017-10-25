//*************************************SIGNATURES************************************//

abstract sig Boolean {

}

one sig true extends Boolean {

}

one sig false extends Boolean {

}


sig Travel {
	means: set TravelMean
}

abstract sig TravelMean {

}

sig Preferences {
	minimizeCarbonFootprint: one Boolean,
//distance in km
	breaks: set Break,
	activeMeansOfTransport: TravelMean -> one Boolean
}{
	one u:User | this = u.preferences // A set of preferences must refers only to one user
}

sig Break {
	active: one Boolean,
	lunch: one Boolean
//if lunch min 30 mins
}

sig User {
	calendar: one Calendar,
	preferences: one Preferences
 }

sig Calendar {
	meetings: set Meeting
}{
	 // A calendar must refers only to one user
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
//	priority: lone Priority (enum)
	warning: lone Warning,
	conflict: set Meeting
}

//doppia freccia tra meeting e warning

sig Warning {
	conflicts: set Meeting
} {
//meetings set has to be at least >=2 otherwise the conflict wouldn't exist
	#conflicts > 1
}

//*************************************CONSTRAINTS************************************//

//CARDINALITY CONSTRAINTS

fun calendarOwner[c: Calendar]: set User {
	{u: User | u.calendar = c}
}

fact OneCalendarPerUser{ //user has only one calendar
	no disj c1,c2: Calendar | all u:User | c1 in u.calendar && c2 in u.calendar
}

fact MeetingInOnlyOneCalendar {// one meeting can be in only one calendar
	no disj c1,c2:Calendar | some m:Meeting | m in c1.meetings  && m in c2.meetings

}

//fact emailUnicity {
	//no disj u1, u2: User | u1.email = u2.email
//}

fact atLeastOneTravelMean{ //esiste almeno un mezzo di trasporto usato per ogni viaggio
	all t:Travel | some m: TravelMean | m in t.means
}

//MEETINGS CONSTRAINTS

fact precedenceRelationConstraint {
	next=~previous
}

fact{ 
	all m:Meeting | all c:Calendar | m in c.meetings implies m.previous in c.meetings 
}

//A Meeting can't have itself as next or previous meeting nor it can be next 
//of the next and so on (transitive closure), (previous is automatically 
//verified because of the precedenceRelationConstraint fact
fact precedence{
	no m: Meeting | m in m.^next
}

//THIS CREATES "ERRORS", NO INSTANCE FOUND
fact conflictualMeeting{
        all disj m1, m2: Meeting | m2 in m1.conflict implies m1 in m2.conflict
}

fact noAutoConflict{
	no m: Meeting | m in m.conflict
}

fact noConflictualMeeting{
	some disj m1,m2: Meeting | m1 not in m2.conflict 
}

//WARNING CONSTRAINTS

fact adjacentConflicts{//ogni meeting nel set dei conflitti ha almeno o il next o il previous nel set
	all m: Meeting | all m2: m.conflict | m2.previous in m2 or m2.next in m2 
								or m2 = m.previous or m2 = m.next
} 

fact noWarningWithoutConflicts{ // not exists meeting in a warning without being in conflict
	all disj m1,m2:Meeting |some w:Warning |  m1 in w.conflicts && m2 in w.conflicts implies m1 in m2.conflict && m2 in m1.conflict
}

fact warningExistence{ // se esistono due meeting in conflitto esiste un warning che li contiene entrambi
	all disj m1,m2: Meeting | m1 in m2.conflict implies one m1.warning && 
						one m2.warning && (some w: Warning | m1 in w.conflicts
												 && m2 in w.conflicts)
}

fact exclusiveWarning{//se un meeting è in un warning non è in altri warning
	all m:Meeting | all disj w1,w2:Warning | m in w1.conflicts implies m not in w2.conflicts
}

fact onlyConflictsInSameCalendar{ //i conflitti valgono solo nello stesso calendario
	all disj m1,m2:Meeting | some c:Calendar | m1 in m2.conflict and m2 in m1.conflict implies m1 in c.meetings and m2 in c.meetings
}

//fact noConflictNoWarning{
//	some m1,m2:Meeting | m1 not in m2.conflict implies #Warning = 0
//}

fact {// empty conflict set implies empty warning set and viceversa
	all m:Meeting | #m.conflict=0 implies #m.warning=0
}
//PREFERENCES CONSTRAINTS

fact atLeastOneSelectedTravelMean{ //nelle preferenze deve essere selezionato almeno un mezzo di trasporto
	all p:Preferences | some t:TravelMean |one tr:true | tr in t.(p.activeMeansOfTransport) 
}


fact NoEqualBreaks{
	no disj b1,b2: Break | all u: User | b1 = b2 and b1 in u.preferences.breaks and b2 in u.preferences.breaks

}

fact TravelMeansAllowed{ // the travel must include only travelMeans that are selected in the preferences 
 	no t:TravelMean | all u:User | all p:u.preferences | all c: u.calendar | all m:c.meetings | all r: m.route |
	some f:false | t in r.means && f in t.(p.activeMeansOfTransport) 
	
}


//se un meeting è in un warning allora è in conflitto con tutti i meeting contenuti nel warning


//If a travel mean is deactivated, it can't be in any travel in any meeting

assert singleUserCalendar {
	all c:Calendar | one u:User | u.calendar = c
}

//check singleUserCalendar

pred show {

}
run show { } for 4
