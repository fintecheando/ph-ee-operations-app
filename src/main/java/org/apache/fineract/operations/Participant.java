package org.apache.fineract.operations;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.fineract.organisation.parent.AbstractPersistableCustom;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "ph_participants")
public class Participant extends AbstractPersistableCustom {

    @Column(name = "DFSP_NAME")
    private String dfspName;
}
