# open-lab-scheduler

A project to:
* organize the open lab hours for the undergraduate tutoring program
* maintain data on tutor assignments across quarters

# Configuration for OAuth

# Running on Localhost.

1.  You _must first_ configure a Google OAuth app for http://localhost:8080 and obtain the client-id and client-secret.

    Follow the instructions here: <https://ucsb-cs56.github.io/topics/oauth_google_setup>.

    - For the application url, use <http://localhost:8080>
    - For the callback url, also use <http://localhost:8080>
    - Note that on localhost, you typically need use `http` not `https`

2.  You must then copy the file `localhost.json.SAMPLE` to the file `localhost.json`.

    - Note that you SHOULD NOT edit `localhost.json.SAMPLE` directly.
    - The copied file `localhost.json` will NOT be commited to GitHub; it's in the `.gitignore`

3.  Then, edit the `localhost.json` file and put in your client id and client secret in the places indicated. Additionally `localhost.json`can be used to overwrite the `app.admin.emails`, which is used to provide the initial admin emails for the application

4.  Finally, IN EACH terminal session where you are going to run `mvn spring-boot:run`, and EACH TIME after you
    change the values in `localhost.json`, execute this command to load those values into the Unix environment:

    ```
    . env.sh
    ```

    That's a dot followed by a space followed by `env.sh`, not a typo. That means to source the contents of `env.sh` into the
    current shell. That loads the contents of `localhost.json` into the environment variable `SPRING_APPLICATION_JSON`, which
    causes those values to override those in the `application.properties` file.

5)  Now you are ready to do `mvn spring-boot:run` as usual, and see the application on <http://localhost:8080>.

    Try logging in with your Google account.

| Type this                                                                                                                                                                              | to get this result                                |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------- |
| `mvn package`                                                                                                                                                                          | to make a jar file                                |
| `mvn spring-boot:run`                                                                                                                                                                  | to run the web app                                |
| `./checkLocalhost.py`                                                                                                                                                                  | to check the syntax of your `localhost.json` file |
| `./setHerokuEnv.py --app APPNAME` | to check the syntax of your `heroku.json` file and set the configuration variables for Heroku app `APPNAME` (requires logging in to Heroku CLI first) |
