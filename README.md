# strava-harvester

#### Purpose:
Fetches user activities from Strava and writes them into a parquet file.

#### How to run it in Intellij
- Open Edit Configuration...
- Click on the plus sign (Add New Configuration) and add Gradle
- When the Run/Debug Configuration window pops up fill the following fields:

##### Gradle Project: strava-harvester

##### Run: add the following tasks and arguments
    clean bootRun --args='
    --accessToken=<strava access token> 
    --harvestedActivityDestination=<destination where the parquet will be written> 
    --activityYears=<number of years of activities>'
    --sportType=<optional: sport type(e.g. Run, Swim, etc). if not provided, all sports will be selected, see link below to list of sportTypes in Strava>

- See [SportType Link](https://developers.strava.com/docs/reference/#api-models-SportType)

- See command line example:
    
    clean bootRun --args='--accessToken=abc --harvestedDestination=/Users/bob/Documents/activities.parquet --activityYears=1 --sportType=Run'

#### Java version
Java 17 or above

#### How can I get the Strava AccessToken?
- See [Strava documentation](https://developers.strava.com/docs/getting-started/) to create an application in Strava. 
- Paste the following URI with the correct client id into the browser to authorise and get the auth code:
  - `https://www.strava.com/oauth/authorize?client_id=<YOUR APP CLIENT ID>&response_type=code&redirect_uri=http://localhost/exchange_token&approval_prompt=force&scope=read_all,activity:read_all`
- Once authorised the browser will return an auth_code, see example below:
  - `http://localhost/exchange_token?state=&code=abc2323q...&scope=read,activity:read_all,read_all`
- Note the browser won't be able to load the page as the uri is localhost. This doesn't matter as we are only interested in using the code from the uri.
- Once we have the auth_code from the browser uri then we can call the strava api to get the acces token.
  - Using Postman or any other rest api client we will send a POST request to Strava to exchange the auth code for the access token:
  - `https://www.strava.com/oauth/token?client_id=<YOUR APP CLIENT ID>&client_secret=<YOUR APP CLIENT SECRET>&code=<AUTH CODE FROM BROWSER>&grant_type=authorization_code`
- If the http status code is 200 then you should be able to copy the access_token and paste as one of the argument in this application.
- If the http status code is not 200 then review strava app, there might some misconfiguration.