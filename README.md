# open-lab-scheduler

Display open lab hours for CS courses

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

3.  Then, edit the `localhost.json` file and put in your client id and client secret in the places indicated.

4.  Finally, IN EACH terminal session where you are going to run `mvn spring-boot:run`, and EACH TIME after you
    change the values in `localhost.json`, execute this command to load those values into the Unix environment:

    ```
    . env.sh
    ```

    That's a dot followed by a space followed by `env.sh`, not a typo. That means to source the contents of `env.sh` into the
    current shell. That loads the contents of `localhost.json` into the environment variable `SPRING_APPLICATION_JSON`, which
    causes those values to override those in the `application.properties` file.

5)  Now you are ready to do `mvn spring-boot:run` as usual, and see the application on <http://localhost:8080>.

    Try logging in with your GitHub account.

# Running on Heroku.

To run on Heroku, you must go BACK to GitHub and set up a DIFFERENT client id and client secret than the one you used for
localhost.

1.  Go to the heroku.com dashboard and create a new Heroku app with the name `cs56-f19-lab06-githubid`, replacing `github` with your
    github id.

1)  Now you must GitHub OAuth app for `https://cs56-f19-lab06-githubid.herokuapp.com` and obtain the client-id and client-secret.

    Follow the instructions here: <https://ucsb-cs56.github.io/topics/oauth_github_setup>.

    - For the application url, use <https://cs56-f19-lab06-githubid.herokuapp.com>
    - For the callback url, also use <https://cs56-f19-lab06-githubid.herokuapp.com>
    - Note that on Heroku, you typically need use `https` not `http`

2)  You must then copy the file `heroku.json.SAMPLE` to the file `heroku.json`.

    - Note that you SHOULD NOT edit `heroku.json.SAMPLE` directly.
    - The copied file `localhost.json` will NOT be commited to GitHub; it's in the `.gitignore`

3)  Then, edit the `heroku.json` file and put in your client id and client secret in the places indicated.

4)  Now, you need either to be logged into CSIL where you can run the heroku command line tool, or you need the heroku
    command line (CLI) installed on your local system.

    Use `heroku login` to login to the command line tool.

    The run the following script from the repo. You need to do this in the same directory where you entered
    the client id and client secret values into the `heroku.json` file.

    The name of the Heroku app should match yours (e.g. change `githubid` to your githubid)

    ```
    ./setHerokuEnv.py --app cs56-f19-lab06-githubid
    ```

    You should now be able to go to the Heroku Dashboard for your app online, e.g. this link (replacing `githubid` with yours)

    - <https://dashboard.heroku.com/apps/cs56-f19-lab06-githubid/settings>

    Click "Reveal Config Vars". You should see a configuration variable called `SPRING_APPLICATION_JSON` that contains
    the values that you entered for client id and client secret (i.e the contents of `heroku.json`).

5.  Now you are ready to do the steps you did in lab02 to connect your Heroku App to your Github repo, and deploy your app to Heroku and see it running.

    Try logging in with your github account here.

If you get all of that running, you are done with lab06, with the exception of doing some documentation on your links on Gauchospace (as you did for lab02.)
Lab06 only requires you to get this up and running on Heroku and gets you used to
configuring an OAuth app.

Lab07 will ask you to make some changes to the code. That is coming online soon, perhaps even before your current lab session is over. If you are done
with lab06, look online and see if the lab07 instructions are ready.

| Type this                                                                                                                                                                              | to get this result                                |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------- |
| `mvn package`                                                                                                                                                                          | to make a jar file                                |
| `mvn spring-boot:run`                                                                                                                                                                  | to run the web app                                |
| `./checkLocalhost.py`                                                                                                                                                                  | to check the syntax of your `localhost.json` file |
| `./setHerokuEnv.py --app APPNAME` | to check the syntax of your `heroku.json` file and set the configuration variables for Heroku app `APPNAME` (requires logging in to Heroku CLI first) |
