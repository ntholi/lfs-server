package com.breakoutms.lfs.server.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.syntax.elements.ClassesThat;
import com.tngtech.archunit.lang.syntax.elements.GivenClassesConjunction;

class ArchTests {
	
	JavaClasses classes;
	
	
	@BeforeEach
	void init() {
		classes = new ClassFileImporter()
		        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
		        .importPackages("com.breakoutms.lfs.server");
	}
	
	@Test
	void unique_fields_should_be_declared_at_class_level() {
		classes().that().areAnnotatedWith(Entity.class).should();
		fields().that().areAnnotatedWith(Column.class).should();
	}
	
	@Test
	void validate_steriotype_annotations() {
		stereotypeThat().haveSimpleNameEndingWith("Controller")
			.should().beAnnotatedWith(RestController.class)
			.orShould().beAnnotatedWith(Controller.class)
			.andShould().beAnnotatedWith(RequestMapping.class)
	        .check(classes);
		stereotypeThat().haveSimpleNameEndingWith("Service")
			.should().beAnnotatedWith(Service.class)
	        .check(classes);
		stereotypeThat().haveSimpleNameEndingWith("Repository")
			.should().beAnnotatedWith(Repository.class)
	        .check(classes);
	}

//	@Test
//	void make_sure_that_interation_tests_use_test_profile() {
//		JavaClasses classes = new ClassFileImporter()
//		        .importPackages("..mortuary");
//		classes.forEach(System.out::println);
//	}
	
	private ClassesThat<GivenClassesConjunction> stereotypeThat() {
		return classes().that()
				.doNotHaveModifier(JavaModifier.ABSTRACT)
				.and().resideOutsideOfPackage("com.breakoutms.lfs.server.core")
				.and();
	}
}
