package com.puc.psi;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.puc.psi");

        noClasses()
            .that()
                .resideInAnyPackage("com.puc.psi.service..")
            .or()
                .resideInAnyPackage("com.puc.psi.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.puc.psi.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
