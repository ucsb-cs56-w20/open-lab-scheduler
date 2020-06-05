import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.persistence.Entity;

import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(
    packages = "edu.ucsb.cs56.ucsb_open_lab_scheduler",
    importOptions = {ImportOption.DoNotIncludeTests.class}
)
public class ArchitectureTests {
    private static final DescribedPredicate<JavaAnnotation<?>> userDefinedEntityName =
        new DescribedPredicate<>("a user defined entity name") {
            @Override
            public boolean apply(JavaAnnotation annotation) {
                if (!annotation.getRawType().reflect().equals(Entity.class)) {
                    return false;
                }

                Entity entity = (Entity) annotation.as(Entity.class);
                return !entity.name().isEmpty();
            }
        };

    private static final DescribedPredicate<JavaAnnotation<?>> reservedEntityNames =
        new DescribedPredicate<>("a reserved entity name") {
            @Override
            public boolean apply(JavaAnnotation annotation) {
                if (!annotation.getRawType().reflect().equals(Entity.class)) {
                    return false;
                }

                Entity entity = (Entity) annotation.as(Entity.class);
                List<String> reservedNames = List.of("user");
                return reservedNames.contains(entity.name().toLowerCase());
            }
        };

    @ArchTest
    public static final ArchRule noReservedEntityNames =
        FreezingArchRule.freeze(
            classes()
                .that().resideInAPackage("..entities..")
                .and().areAnnotatedWith(Entity.class)
                .and().areNotAnnotatedWith(userDefinedEntityName)
                .should().notHaveSimpleName("User")
                .as("Entities without a user-defined name should not have a class name that is a reserved SQL keyword")
        );

    @ArchTest
    public static final ArchRule noReservedUserDefinedEntityNames =
        classes()
            .that().resideInAPackage("..entities..")
            .and().areAnnotatedWith(userDefinedEntityName)
            .should().notBeAnnotatedWith(reservedEntityNames)
            .as("Entities with a user-defined name should not have a name that is a reserved SQL keyword");

    @ArchTest
    public static final ArchRule controllerClassNames =
        FreezingArchRule.freeze(
            classes()
                .that().resideInAPackage("..controllers..")
                .should().haveSimpleNameEndingWith("Controller")
        );

    @ArchTest
    public static final ArchRule repositoryClassNames =
        classes()
            .that().resideInAPackage("..repositories..")
            .should().haveSimpleNameEndingWith("Repository");

    @ArchTest
    public static final ArchRule advicePackage =
        classes()
            .that().areAnnotatedWith(ControllerAdvice.class)
            .should().resideInAPackage("..advice..");

    @ArchTest
    public static final ArchRule decoupleAdviceClasses =
        FreezingArchRule.freeze(
            noClasses()
                .should().dependOnClassesThat().resideInAPackage("..advice..")
        );

}
