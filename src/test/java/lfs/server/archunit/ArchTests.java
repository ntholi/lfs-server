package lfs.server.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;

class ArchTests {

	private JavaClasses classes;

	@BeforeEach
	public void setup() {
	    classes = new ClassFileImporter()
	        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
	        .importPackages("lfs.server");
	}

	@Test
	void validate_steriotype_annotations() {
		classes().that().haveSimpleNameEndingWith("Controller")
			.should().beAnnotatedWith(RestController.class)
	        .check(classes);
		classes().that().haveSimpleNameEndingWith("Service")
			.should().beAnnotatedWith(Service.class)
	        .check(classes);
		classes().that().haveSimpleNameEndingWith("Repository")
			.should().beAnnotatedWith(Repository.class)
	        .check(classes);
	}
}
