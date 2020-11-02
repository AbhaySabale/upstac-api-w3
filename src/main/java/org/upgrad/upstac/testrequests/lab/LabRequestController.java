package org.upgrad.upstac.testrequests.lab;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.testrequests.TestRequestUpdateService;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.users.User;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.upgrad.upstac.exception.UpgradResponseStatusException.asBadRequest;
import static org.upgrad.upstac.exception.UpgradResponseStatusException.asConstraintViolation;


@RestController
@RequestMapping("/api/labrequests")
public class LabRequestController {

    Logger log = LoggerFactory.getLogger(LabRequestController.class);




    @Autowired
    private TestRequestUpdateService testRequestUpdateService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;
    @Autowired
    private TestRequestFlowService testRequestFlowService;



    @Autowired
    private UserLoggedInService userLoggedInService;



    @GetMapping("/to-be-tested")
    @PreAuthorize("hasAnyRole('TESTER')")
    public List<TestRequest> getForTests()  {
        return testRequestQueryService.findBy(RequestStatus.INITIATED); //this will find list of RequestStatus which are in INITIATED stage and return the same for testing
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TESTER')")
    public List<TestRequest> getForTester()  {
        User tester= userLoggedInService.getLoggedInUser();//this will only consider current logged in user for further activities
        return testRequestQueryService.findByTester(tester); //this will perform activities mentioned in testRequestQueryService class to the current user
    }


    @PreAuthorize("hasAnyRole('TESTER')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForLabTest(@PathVariable Long id) {
        try {
            User tester = userLoggedInService.getLoggedInUser();//this will only consider current logged in user for further activities
            TestRequest testRequest = new TestRequest();//created object of TestRequest class
            testRequest = testRequestUpdateService.assignForLabTest(id, tester);//this will assigned all the test requests which are initiated for lab test to the current user along with their IDs

            return testRequest; // this will return initiated test requests to the current user


        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('TESTER')")
    @PutMapping("/update/{id}")
    public TestRequest updateLabTest(@PathVariable Long id,@RequestBody CreateLabResult createLabResult) {
        try {
            User tester = userLoggedInService.getLoggedInUser();//this will only consider current logged in user for further activities
            TestRequest testRequest = new TestRequest();//this will create object of TestRequest class
            testRequest = testRequestUpdateService.updateLabTest(id, createLabResult,tester);//this will update test request under current logged in tester along with their ID
            return testRequest; // this will return test reports for further activities

        } catch (ConstraintViolationException e) {
            throw asConstraintViolation(e);
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }
}
