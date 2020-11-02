package org.upgrad.upstac.testrequests.consultation;


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
@RequestMapping("/api/consultations")
public class ConsultationController {

    Logger log = LoggerFactory.getLogger(ConsultationController.class);




    @Autowired
    private TestRequestUpdateService testRequestUpdateService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;


    @Autowired
    TestRequestFlowService  testRequestFlowService;

    @Autowired
    private UserLoggedInService userLoggedInService;



    @GetMapping("/in-queue")
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForConsultations()  {
        return testRequestQueryService.findBy(RequestStatus.LAB_TEST_COMPLETED); // this will find list of RequestStatus which are in LAB TEST COMPLETED stage and return the same for consultation

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForDoctor()  {

        User user = userLoggedInService.getLoggedInUser();//this will only consider current logged in user for further activities

        return testRequestQueryService.findByDoctor(user); //this will perform activities mentioned in testRequestQueryService class to the current user



    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForConsultation(@PathVariable Long id) {
        try {
            User doctor = userLoggedInService.getLoggedInUser();//this will only consider current logged in user for further activities
            TestRequest testRequest = new TestRequest();//this will create object of TestRequest class
            testRequest = testRequestUpdateService.assignForConsultation(id,doctor);//this will assigned all the test requests which are in LAB TEST COMPLETED stage for lab test to the current user along with their IDs
            return testRequest; //this will return LAB TEST COMPLETED tests to the current user
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/update/{id}")
    public TestRequest updateConsultation(@PathVariable Long id,@RequestBody CreateConsultationRequest testResult) {
        try {
            User user = userLoggedInService.getLoggedInUser();//this will only consider current logged in user for further activities
            TestRequest testRequest = new TestRequest();//this will create object of TestRequest class
            testRequest = testRequestUpdateService.updateConsultation(id,testResult, user);//this will update above test requests under the current user along with their IDs
            return testRequest; //this will return updated tests

        } catch (ConstraintViolationException e) {
            throw asConstraintViolation(e);
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }
}
