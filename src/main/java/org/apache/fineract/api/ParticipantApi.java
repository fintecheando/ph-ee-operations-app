package org.apache.fineract.api;

import java.util.List;

import org.apache.fineract.operations.ParticipantRepository;
import org.apache.fineract.operations.Participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ParticipantApi {


    @Autowired
    private ParticipantRepository participantRepository;

    @GetMapping("/dfsp")
    public List<Participant> participants() {
        return this.participantRepository.findAll();
    }
}