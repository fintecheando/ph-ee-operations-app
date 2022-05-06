package org.apache.fineract.paymenthub.api;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.fineract.paymenthub.domain.Participant;
import org.apache.fineract.paymenthub.domain.ParticipantRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value=OperationsConstants.API_VERSION_PATH, produces=MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ParticipantApi {

    private final ParticipantRepository participantRepository;

    @GetMapping(OperationsConstants.API_DFSP_PATH)
    public List<Participant> participants() {
        return this.participantRepository.findAll();
    }

    @GetMapping(OperationsConstants.API_DFSP_PATH + "/{participantId}")
    public Participant participant(@PathVariable("participantId") Long participantId) {
        Optional<Participant> optEntity = participantRepository.findById(participantId);
        if(optEntity.isPresent()) {
            return optEntity.get();
        } 
        return null; 
    }

    @PostMapping(path = OperationsConstants.API_DFSP_PATH, consumes = MediaType.APPLICATION_JSON)
    public void create(@RequestBody Participant participant, HttpServletResponse response) {
        Participant existing = participantRepository.getParticipantByName(participant.getDfspName());
        if (existing == null) {
            participant.setId(null);
            participantRepository.saveAndFlush(participant);
        } else {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    @PutMapping(path = OperationsConstants.API_DFSP_PATH + "/{participantId}", consumes = MediaType.APPLICATION_JSON)
    public void update(@PathVariable("participantId") Long participantId, @RequestBody Participant participant, HttpServletResponse response) {
        Optional<Participant> optEntity = participantRepository.findById(participantId);
        if(optEntity.isPresent()) {
            Participant existing = optEntity.get();
            existing.setDfspName(participant.getDfspName());
            participantRepository.saveAndFlush(existing);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @DeleteMapping(path = OperationsConstants.API_DFSP_PATH + "/{participantId}")
    public void delete(@PathVariable("participantId") Long participantId, HttpServletResponse response) {
        Optional<Participant> optEntity = participantRepository.findById(participantId);
        if(optEntity.isPresent()) {
            participantRepository.delete(optEntity.get());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}