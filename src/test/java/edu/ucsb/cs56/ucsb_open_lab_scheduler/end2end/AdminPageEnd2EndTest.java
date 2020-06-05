package edu.ucsb.cs56.ucsb_open_lab_scheduler.end2end;

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * This test runs a complete end-to-end test on the project as a single test.
 * Starts by logging in as a student, submitting a review, and reviewing 5 ideas
 *
 * If you are trying to duplicate this test in a project, make sure to copy the
 * html template under the test/resources/__files/ directory!
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties="spring.datasource.name=end2endtest")
public class AdminPageEnd2EndTest {

    static {
        System.setProperty("spring.security.oauth2.client.provider.wiremock.authorization-uri", "http://localhost:8077/oauth/authorize");
        System.setProperty("spring.security.oauth2.client.provider.wiremock.token-uri", "http://localhost:8077/oauth/token");
        System.setProperty("spring.security.oauth2.client.provider.wiremock.user-info-uri", "http://localhost:8077/userinfo");
        System.setProperty("spring.security.oauth2.client.provider.wiremock.user-name-attribute", "sub");
        System.setProperty("spring.security.oauth2.client.registration.wiremock.provider", "wiremock");
        System.setProperty("spring.security.oauth2.client.registration.wiremock.authorization-grant-type", "authorization_code");
        System.setProperty("spring.security.oauth2.client.registration.wiremock.scope", "email");
        System.setProperty("spring.security.oauth2.client.registration.wiremock.redirect-uri", "http://localhost:8080/login/oauth2/code/{registrationId}");
        System.setProperty("spring.security.oauth2.client.registration.wiremock.clientId", "wm");
        System.setProperty("spring.security.oauth2.client.registration.wiremock.clientSecret", "whatever");
    }

    private WebDriver webDriver;


    @Rule
    public WireMockRule mockOAuth2Provider = new WireMockRule(wireMockConfig()
            .port(8077)
            .extensions(new ResponseTemplateTransformer(true)));

    @Before
    public void setUp() {
        // Setup ChromeDriver (aided by the WebDriverManager)
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        //Run browser in headless mode for CI
        options.addArguments("--headless");
        webDriver = new ChromeDriver(options);

        // Configure mock oauth endpoints
        mockOAuth2Provider.stubFor(get(urlPathEqualTo("/favicon.ico")).willReturn(notFound()));
        mockOAuth2Provider.stubFor(get(urlPathMatching("/oauth/authorize?.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBodyFile("mockLogin.html")));
        mockOAuth2Provider.stubFor(post(urlPathEqualTo("/login"))
                .willReturn(temporaryRedirect("{{formData request.body 'form' urlDecode=true}}http://localhost:8080/login/oauth2/code/wiremock?code={{{randomValue length=30 type='ALPHANUMERIC'}}}&state={{{form.state}}}")));
        mockOAuth2Provider.stubFor(post(urlPathEqualTo("/oauth/token"))
                .willReturn(okJson("{\"token_type\": \"Bearer\",\"access_token\":\"{{randomValue length=20 type='ALPHANUMERIC'}}\"}")));
        mockOAuth2Provider.stubFor(get(urlPathEqualTo("/userinfo"))
                .willReturn(okJson("{\"sub\":\"my-id\",\"email\":\"joe@ucsb.edu\", \"hd\":\"ucsb.edu\", \"name\":\"Joe\", \"given_name\":\"Joe\", \"family_name\":\"Gaucho\"}")));
    }

    //Close the browser after tests are done
    @After
    public void reset() {
        if (webDriver != null) {
            webDriver.close();
        }
    }


    @Test
    public void runUserFlowEnd2EndTestWithAuthentication() {
        // Navigate to login page
        webDriver.get("http://localhost:8080/oauth2/authorization/wiremock");
        // Make sure Spring redirected us to the right place
        assert(webDriver.getCurrentUrl()).startsWith("http://localhost:8077/oauth/authorize");
        webDriver.findElement(By.id("submit")).click();
        // Verify login was successful (and did not result in /error path)
        assert(webDriver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/"));
        // Navigate to the admin page
        webDriver.get("http://localhost:8080/admin");
        // Verify that we are at the correct url
        assert(webDriver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/admin"));
        // Find the form input and create an admin
        webDriver.findElement(By.id("selenium-email-input")).sendKeys("testAdmin@ucsb.edu");
        webDriver.findElement(By.id("selenium-admin-add")).click();
        // Verify that the admin we tried to create exists
        assert(webDriver.findElement(By.xpath("//table/tbody/tr[3]/td[1]"))
                .getText()
                .equalsIgnoreCase("testAdmin@ucsb.edu"));
        // Delete the admin
        webDriver.findElement(By.id("selenium-admin-delete")).click();
        // Verify that the admin is deleted
        assert(webDriver.findElements(By.xpath("//table/tbody/tr[3]/td[1]")).isEmpty());
    }

}
