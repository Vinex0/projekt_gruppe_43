package com.gruppe43.moneymanager.arch;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;


import com.gruppe43.moneymanager.stereotypes.AggregateRoot;
import com.gruppe43.moneymanager.stereotypes.Wertobjekt;
import com.tngtech.archunit.core.importer.ImportOption;
import com.gruppe43.moneymanager.MoneyManagerApplication;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@AnalyzeClasses(packagesOf = MoneyManagerApplication.class,
    importOptions = {ImportOption.DoNotIncludeTests.class})
public class MoneyManagerArchTest {

  @ArchTest
  static final ArchRule noDeprecatedClasses = classes().should().notBeAnnotatedWith(java.lang.Deprecated.class);

  @ArchTest
  static final ArchRule noDeprecatedFields = fields().should().notBeAnnotatedWith(java.lang.Deprecated.class);

  @ArchTest
  static final ArchRule noDeprecatedMethods = methods().should().notBeAnnotatedWith(java.lang.Deprecated.class);

  @ArchTest
  static final ArchRule noFieldsInjected = GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

  @ArchTest
  static final ArchRule noComponentAnnotation = classes().should().notBeAnnotatedWith(Component.class);

  @ArchTest
  static final ArchRule annotationenTest = classes().that().resideInAnyPackage("..domain..")
      .and().arePublic()
      .or().areProtected()
      .should().beAnnotatedWith(AggregateRoot.class)
      .orShould().beAnnotatedWith(Wertobjekt.class);

  @ArchTest
  static final ArchRule onionLayeredArchitecture = layeredArchitecture()
      .consideringAllDependencies()
      .layer("Controller").definedBy("..web..")
      .layer("Service").definedBy("..service..")
      .layer("Database").definedBy("..database..")
      .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
      .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
      .whereLayer("Database").mayOnlyBeAccessedByLayers("Service");

  /*@ArchTest
  static final ArchRule onionArchitectureWirdEingehalten = onionArchitecture()
      .domainModels("..domain..")
      .domainServices( "..security..")
      .applicationServices("..services..")
      .adapter("web", "..controller.."); */



  @ArchTest
  static final ArchRule applicationServicesInServices = classes()
      .that().resideInAPackage("..service..")
      .should().beAnnotatedWith(Service.class)
      .andShould().haveSimpleNameEndingWith("Service");

  @ArchTest
  static final ArchRule controllerShouldBeNamedAsSuch = classes()
      .that().resideInAPackage("..web..")
      .should().haveSimpleNameEndingWith("Controller");

}
