package org.upgrad.upstac.testrequests.consultation;

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
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    private static Logger logger = LoggerFactory.getLogger(ConsultationService.class);


    @Transactional
    public Consultation assignForConsultation( TestRequest testRequest, User doctor) {
        Consultation consultation = new Consultation();//this will create object of Consultation class
        consultation.setDoctor(doctor);//this will set doctor name or ID for current test
        consultation.setRequest(testRequest);//this will create consultation
        return consultationRepository.save(consultation); // this will save and return consultation report


    }

    public Consultation updateConsultation(TestRequest testRequest , CreateConsultationRequest createConsultationRequest) {
        Consultation consultation = consultationRepository.findByRequest(testRequest).get();//this will show the consultation
        consultation.setComments(createConsultationRequest.getComments());//this will set comments in consultation
        consultation.setSuggestion(createConsultationRequest.getSuggestion());//this will set Suggestions in consultation
        consultation.setUpdatedOn(LocalDate.now());//this will set consultation date in consultation report
        return consultationRepository.save(consultation); // this will save and return consultation report
    }
}