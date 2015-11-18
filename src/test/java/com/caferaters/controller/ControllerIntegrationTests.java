package com.caferaters.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.caferaters.controller.cafe.CafeControllerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   CafeControllerTest.class
})
public class ControllerIntegrationTests {

}
