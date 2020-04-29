package com.breakoutms.lfs.server.preneed;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuiteDisplayName("All Policy Tests")
@SelectClasses(ValidatePolicy.class)
public class AllTests {

}
