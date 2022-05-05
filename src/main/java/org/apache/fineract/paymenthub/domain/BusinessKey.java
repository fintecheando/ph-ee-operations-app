package org.apache.fineract.paymenthub.domain;


import org.apache.fineract.organisation.parent.AbstractPersistableCustom;
import org.eclipse.persistence.annotations.Index;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "ph_businesskeys")
public class BusinessKey extends AbstractPersistableCustom {

    @Column(name = "BUSINESS_KEY")
    @Index(name = "idx_businessKey")
    private String businessKey;

    @Column(name = "BUSINESS_KEY_TYPE")
    @Index(name = "idx_businessKeyType")
    private String businessKeyType;

    @Column(name = "WORKFLOW_INSTANCE_KEY")
    @Index(name = "idx_workflowInstanceKey")
    private Long workflowInstanceKey;

    @Column(name = "TIMESTAMP")
    private Long timestamp;
}
