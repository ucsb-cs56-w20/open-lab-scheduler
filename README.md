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

3.  Then, edit the `localhost.json` file and put in your client id and client secret in the places indicated. Additionally `localhost.json`can be used to overwrite the `app.admin.emails`, which is used to provide the initial admin emails for the application.

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

# Testing

## ArchUnit

This project uses [ArchUnit](https://www.archunit.org/) to enforce various architectural practices. These rules are 
defined in the file `src/test/java/ArchitectureTests.java` and enforce a number of standards that are considered
"good software design".

### Legacy code and "Frozen" rules

You may find many examples of code inside the codebase that violate one or more of the rules that are currently
in place. Non-compliant code that was written before an ArchRule was implemented will not fail the build, but new code
will. Note that this means you may not necessarily be able to copy-paste code from old parts of the codebase to
implement new features; you may need to refactor/adapt the code to conform to the new rules.

If you are a maintainer creating a new ArchRule and need to "freeze" a rule so that existing code does not fail a build:
1. Wrap your ArchRule in [`FreezingArchRule.freeze`](https://www.archunit.org/userguide/html/000_Index.html#_usage)
2. Run `mvn test -Pfreeze-new-archrules`
3. Commit the tests and the new files generated in the `archunit_store` folder.

If you are refactoring legacy code and have resolved all of the violations of a particular type, you may also remove
the `FreezingArchRule.freeze` wrapper around the ArchRule in question.
