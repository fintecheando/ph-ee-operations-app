package org.apache.fineract.paymenthub.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.fineract.organisation.parent.AbstractPersistableCustom;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "m_beneficiary")
public class Beneficiary extends AbstractPersistableCustom {
    @Column(name = "c_identifier")
    private String custIdentifier;

    @Column(name = "b_identifier")
    private String identifier;

    @Column(name = "b_name")
    private String name;

    @Column(name = "b_nick_name")
    private String nickName;

    @Column(name = "b_account_no")
    private String accountNo;

    @Column(name = "b_leId")
    private String leId;

    @Column(name = "b_currency_code")
    private String currencyCode;

    @Column(name = "b_country_code")
    private String countryCode;

    public Beneficiary(String custIdentifier, String identifier, String name, String nickName, String accountNo,
                       String leId, String currencyCode, String countryCode) {
        this.custIdentifier = custIdentifier;
        this.identifier = identifier;
        this.name = name;
        this.nickName = nickName;
        this.accountNo = accountNo;
        this.leId = leId;
        this.currencyCode = currencyCode;
        this.countryCode = countryCode;
    }

    public void removeId(){
        super.setId(null);
    }
}
