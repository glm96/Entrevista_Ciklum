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

JSON body format should be like this:
{
        "id": 5750085036015616,
        "inbound": {
            "fcode": "SU2529",
            "departureCode": "AGP",
            "arrivalCode": "SVO",
            "arrivalDate": 1559471662000,
            "departureDate": 1559385262000
        },
        "outbound": {
            "fcode": "SU2529",
            "departureCode": "AGP",
            "arrivalCode": "SVO",
            "arrivalDate": 1559471662000,
            "departureDate": 1559385262000
        },
        "lodging": {
            "code": 12305314,
            "name": "Suimeikan",
            "starRating": 4,
            "geoLocation": {
                "latitude": 6.3250294931248785,
                "longitude": 9.282011421746361
            }
        },
        "price": 1.11
    }

