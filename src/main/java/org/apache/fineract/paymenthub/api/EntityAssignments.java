package org.apache.fineract.paymenthub.api;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EntityAssignments {
    private List<Long> entityIds;
}
