package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;
import org.flywaydb.core.Flyway;

public class FlywayMigrator {
    //body of this class from heroku devcenter docs: https://devcenter.heroku.com/articles/running-database-migrations-for-java-apps#using-flyway
    public static void main(String[] args) throws Exception {
        Flyway flyway = Flyway.configure().dataSource(System.getenv("DATABASE_URL"),
                             System.getenv("DATABASE_USERNAME"),
                             System.getenv("DATABASE_PASSWORD")).load();
        flyway.migrate();
    }
}