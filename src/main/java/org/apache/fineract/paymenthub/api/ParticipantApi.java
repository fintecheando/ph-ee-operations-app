package org.apache.fineract.paymenthub.api;

import java.util.List;

import org.apache.fineract.paymenthub.domain.Participant;
import org.apache.fineract.paymenthub.domain.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ParticipantApi {

    private final static String API_PATH = "/dfsp";

    @Autowired
    private ParticipantRepository participantRepository;

    @GetMapping(API_PATH)
    public List<Participant> participants() {
        return this.participantRepository.findAll();
    }
}