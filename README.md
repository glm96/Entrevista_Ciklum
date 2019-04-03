Thomas Cook Interview Java task for Gonzalo Luque Martin, on April 2019
https://github.com/glm96/Entrevista_Ciklum

An android app for quick testing of the API can be found on https://github.com/glm96/Entrevista_Ciklum_Android

DB hosted on https://ciklumentrevista.appspot.com using Google Datastore and App Engine

Possible petitions to the API are:
GET /holidaypackages/<id>
GET /holidaypackages?{params} where {params}:
	{
		sort1, sort2 with values [asctime,desctime,ascstarrating,descstarrating]
		IATAarrival
		IATAorigin
		rating
		Any combination of the above parameters
	}
DELETE /holidaypackages/<id>
PUT /holidaypackages/<id> (JSON body with HolidayPackage data)
POST /holidaypackages (JSON body with HolidayPackage data)

GET calls have application/JSON responses, while other calls have text/plain responses.

Documentation can be found on /doc/ with javadoc formatting.


