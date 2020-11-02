package org.upgrad.upstac.testrequests.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.users.User;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@Validated
public class LabResultService {


    @Autowired
    private LabResultRepository labResultRepository;


    private static Logger logger = LoggerFactory.getLogger(LabResultService.class);



    private LabResult createLabResult(User user, TestRequest testRequest) {
        LabResult labResult = new LabResult();//this will create object of LabResult class
        labResult.setTester(user);//this will set tester name or ID for current test
        labResult.setRequest(testRequest);//this will create lab result
        return saveLabResult(labResult);// this will save and return lab result
    }

    @Transactional
    LabResult saveLabResult(LabResult labResult) {
        return labResultRepository.save(labResult);
    }



    public LabResult assignForLabTest(TestRequest testRequest, User tester) {

        return createLabResult(tester, testRequest);


    }


    public LabResult updateLabTest(TestRequest testRequest, CreateLabResult createLabResult) {

        LabResult labResult = labResultRepository.findByRequest(testRequest).get();//this will show the LabResult
        labResult.setComments(createLabResult.getComments());//this will set comments in LabResult
        labResult.setBloodPressure(createLabResult.getBloodPressure());//this will set BloodPressure in LabResult
        labResult.setHeartBeat(createLabResult.getHeartBeat());//this will set HeartBeat in LabResult
        labResult.setOxygenLevel(createLabResult.getOxygenLevel());//this will set OxygenLevel in LabResult
        labResult.setTemperature(createLabResult.getTemperature());//this will set Temperature in LabResult
        labResult.setResult(createLabResult.getResult());//this will set Result in LabResult
        labResult.setUpdatedOn(LocalDate.now());//this will test date in LabResult
        return saveLabResult(labResult);// this will save and return lab result
    }
}
