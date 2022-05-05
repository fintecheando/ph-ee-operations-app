package org.apache.fineract.paymenthub.api;

import org.apache.fineract.paymenthub.domain.ErrorCode;
import org.apache.fineract.paymenthub.domain.ErrorCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="/api/v1", produces=MediaType.APPLICATION_JSON_VALUE)
public class ErrorCodesApi {

    private final static String API_PATH = "/errorcode";

    @Autowired
    private ErrorCodeRepository errorCodesRepository;

    @PostMapping(API_PATH)
    public ErrorCode addErrorCode(@RequestBody ErrorCode errorCode) {
        ErrorCode response = errorCodesRepository.save(errorCode);
        return response;
    }

    @GetMapping(API_PATH)
    public List<ErrorCode> getAllErrorCode() {
        List<ErrorCode> errorCodes = new ArrayList<>();
        errorCodesRepository.findAll().forEach(errorCodes::add);
        return errorCodes;
    }

    @GetMapping(API_PATH + "/{id}")
    public ErrorCode getSpecificErrorCode(@PathVariable Long id) {
        Optional<ErrorCode> optEntity = errorCodesRepository.findById(id);
        if (optEntity.isPresent()) {
            return optEntity.get();
        }
        return null;
    }

    @GetMapping(API_PATH + "/filter")
    public List<ErrorCode> getErrorCodeByFilter(@RequestParam("by") String filterType, @RequestParam("value") Object value) {
        switch (filterType){
            case "errorCode":
                return errorCodesRepository.getErrorCodesByErrorCode(value.toString());
            case "recoverable":
                boolean option = value.toString().equals("true");
                return errorCodesRepository.getErrorCodesByRecoverable(option);
            case "transactionType":
                return errorCodesRepository.getErrorCodesByTransactionType(value.toString());
            default:
                return null;
        }
    }

    @PutMapping(API_PATH + "/{id}")
    public ErrorCode updateErrorCode(@PathVariable Long id, @RequestBody ErrorCode errorCode) {
        errorCode.setId(id);
        ErrorCode response = errorCodesRepository.save(errorCode);
        return response;
    }

    @DeleteMapping(API_PATH + "/{id}")
    public ErrorCode deleteErrorCode(@PathVariable Long id) {
        ErrorCode errorCode = getSpecificErrorCode(id);
        errorCodesRepository.delete(errorCode);
        return errorCode;
    }
}
