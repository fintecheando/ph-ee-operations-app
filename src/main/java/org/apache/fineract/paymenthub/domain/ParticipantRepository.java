package org.apache.fineract.paymenthub.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long>, JpaSpecificationExecutor<Participant> {

    List<Participant> findAll();
        
    @Query("SELECT participant FROM Participant participant WHERE participant.dfspName = :dfspName")
    Participant getParticipantByName(@Param("dfspName") String dfspName);
}
