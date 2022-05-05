package org.apache.fineract.operations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long>, JpaSpecificationExecutor<Participant> {

    List<Participant> findAll();
}
