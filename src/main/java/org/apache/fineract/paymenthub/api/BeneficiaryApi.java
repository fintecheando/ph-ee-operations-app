package org.apache.fineract.paymenthub.api;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.fineract.paymenthub.domain.Beneficiary;
import org.apache.fineract.paymenthub.domain.BeneficiaryRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value=OperationsConstants.API_VERSION_PATH, produces=MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class BeneficiaryApi {

    private final BeneficiaryRepository beneficiaryRepository;

    @PostMapping(path = OperationsConstants.API_BENEFICIARY_PATH, consumes = MediaType.APPLICATION_JSON)
    public void create(@RequestBody Beneficiary beneficiary, HttpServletResponse response) {
        Beneficiary existing = beneficiaryRepository.findOneByCustIdentifierAndIdentifier(beneficiary.getCustIdentifier(),
                beneficiary.getIdentifier());
        if(existing == null){
            beneficiary.setId(null);
            beneficiaryRepository.save(beneficiary);
        }else{
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    @GetMapping(path = OperationsConstants.API_BENEFICIARY_PATH + "/{custIdentifier}", produces = MediaType.APPLICATION_JSON)
    public List<Beneficiary> getAllForCustomer(@PathVariable("custIdentifier") String custIdentifier, HttpServletResponse response) {
        List<Beneficiary> beneficiaries = beneficiaryRepository.findBycustIdentifier(custIdentifier);
        if(beneficiaries != null) {
            beneficiaries.forEach(b -> b.removeId());
            return beneficiaries;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @DeleteMapping(path = OperationsConstants.API_BENEFICIARY_PATH + "/{custIdentifier}/{identifier}", produces = MediaType.TEXT_HTML)
    public void delete(@PathVariable("custIdentifier") String custIdentifier,
                       @PathVariable("identifier") String identifier, HttpServletResponse response) {
        Beneficiary beneficiary = beneficiaryRepository.findOneByCustIdentifierAndIdentifier(custIdentifier, identifier);
        if(beneficiary != null){
            beneficiaryRepository.delete(beneficiary);
        }else{
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
