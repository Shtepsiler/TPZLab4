package org.example.lab4;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@SpringBootTest
class Lab4ArchitectureTests {

    private JavaClasses applicationClasses;

    @BeforeEach
    void initialize() {
        applicationClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("org.example.lab4");
    }

    @Test
    void shouldFollowLayerArchitecture() {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..controllers..")
                .layer("Service").definedBy("..services..")
                .layer("Repository").definedBy("..repositories..")
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
                .check(applicationClasses);
    }

    @Test
    void controllersShouldNotDependOnOtherControllers() {
        noClasses()
                .that().resideInAPackage("..controllers..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..controllers..")
                .check(applicationClasses);
    }

    @Test
    void repositoriesShouldNotDependOnServices() {
        noClasses()
                .that().resideInAPackage("..repositories..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..services..")
                .check(applicationClasses);
    }

    @Test
    void controllerClassesShouldBeNamedXController() {
        classes()
                .that().resideInAPackage("..controllers..")
                .should()
                .haveSimpleNameEndingWith("Controller")
                .check(applicationClasses);
    }

    @Test
    void controllerClassesShouldBeAnnotatedByRestController() {
        classes()
                .that().resideInAPackage("..controllers..")
                .should()
                .beAnnotatedWith(RestController.class)
                .check(applicationClasses);
    }

    @Test
    void repositoryShouldBeInterface() {
        classes()
                .that().resideInAPackage("..repositories.interfaces..")
                .should()
                .beInterfaces()
                .check(applicationClasses);
    }

    @Test
    void anyControllerFieldsShouldNotBeAnnotatedAutowired() {
        noClasses()
                .that().resideInAPackage("..controllers..")
                .should()
                .beAnnotatedWith(Autowired.class)
                .check(applicationClasses);
    }

    @Test
    void modelFieldsShouldBePrivate() {
        fields()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..models..")
                .should().notBePublic()
                .check(applicationClasses);
    }

    // Additional 10 tests

    @Test
    void servicesShouldOnlyBeAccessedByControllersOrOtherServices() {
        classes()
                .that().resideInAPackage("..services..")
                .should().onlyBeAccessed().byAnyPackage("..controllers..", "..services..")
                .check(applicationClasses);
    }

    @Test
    void servicesShouldDependOnRepositoriesInterfaces() {
        classes()
                .that().resideInAPackage("..services..")
                .should().dependOnClassesThat()
                .resideInAPackage("..repositories.interfaces..")
                .check(applicationClasses);
    }

    @Test
    void dtosShouldBeInDtoPackageAndHaveDtoSuffix() {
        classes()
                .that().resideInAPackage("..DTOs..")
                .should().haveSimpleNameEndingWith("DTO")
                .check(applicationClasses);
    }

    @Test
    void modelClassesShouldOnlyBeAccessedByServicesOrRepositories() {
        classes()
                .that().resideInAPackage("..models..")
                .should().onlyBeAccessed().byAnyPackage("..services..", "..repositories..", "..repositories.interfaces..","..DTOs..","..models..")
                .check(applicationClasses);
    }

    @Test
    void serviceMethodsShouldBePublic() {
        methods()
                .that().areDeclaredInClassesThat().resideInAPackage("..services..")
                .should().bePublic()
                .check(applicationClasses);
    }

    @Test
    void servicesShouldNotHaveAutowiredFields() {
        noFields()
                .that().areDeclaredInClassesThat().resideInAPackage("..services..")
                .should().beAnnotatedWith(Autowired.class)
                .check(applicationClasses);
    }

    @Test
    void repositoriesInterfacesShouldNotDependOnEachOther() {
        noClasses()
                .that().resideInAPackage("..repositories.interfaces..")
                .should()
                .dependOnClassesThat().resideInAPackage("..repositories.interfaces..")
                .check(applicationClasses);
    }

    @Test
    void modelClassesShouldBeNamedCorrectly() {
        classes()
                .that().resideInAPackage("..models..")
                .should().haveSimpleNameNotContaining("Entity")
                .check(applicationClasses);
    }

    @Test
    void serviceClassesShouldBeAnnotatedWithService() {
        classes()
                .that().resideInAPackage("..services..")
                .should().beAnnotatedWith(Service.class)
                .check(applicationClasses);
    }

    @Test
    void controllersShouldNotDependOnRepositoriesDirectly() {
        noClasses()
                .that().resideInAPackage("..controllers..")
                .should().dependOnClassesThat().resideInAPackage("..repositories..")
                .check(applicationClasses);
    }

    @Test
    void dtosShouldNotDependOnControllers() {
        noClasses()
                .that().resideInAPackage("..DTOs..")
                .should().dependOnClassesThat().resideInAPackage("..controllers..")
                .check(applicationClasses);
    }

    @Test
    void servicesShouldNotDependOnControllers() {
        noClasses()
                .that().resideInAPackage("..services..")
                .should().dependOnClassesThat().resideInAPackage("..controllers..")
                .check(applicationClasses);
    }
}
