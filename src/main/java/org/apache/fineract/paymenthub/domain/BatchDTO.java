package org.apache.fineract.operations;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchDTO {

    private String batchId;

    private String requestId;

    private Long totalTransactions;

    private Long ongoing;

    private Long failed;

    private Long completed;

    private BigDecimal total_amount;

    private BigDecimal completed_amount;

    private BigDecimal ongoing_amount;

    private BigDecimal failed_amount;

    private String result_file;

    private Date resultGeneratedAt;

    private String note;

    public BatchDTO(String batchId, String requestId, Long totalTransactions, Long ongoing, Long failed, Long completed, BigDecimal total_amount, BigDecimal completed_amount, BigDecimal ongoing_amount, BigDecimal failed_amount, String result_file, Date resultGeneratedAt, String note) {
        this.batchId = batchId;
        this.requestId = requestId;
        this.totalTransactions = totalTransactions;
        this.ongoing = ongoing;
        this.failed = failed;
        this.completed = completed;
        this.total_amount = total_amount;
        this.completed_amount = completed_amount;
        this.ongoing_amount = ongoing_amount;
        this.failed_amount = failed_amount;
        this.result_file = result_file;
        this.resultGeneratedAt = resultGeneratedAt;
        this.note = note;
    }

}
