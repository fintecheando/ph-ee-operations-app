package org.apache.fineract.operations;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

public class TransactionRequestSpecs {

    public static Specification<TransactionRequest> between(SingularAttribute<TransactionRequest, Date> attribute, Date from, Date to) {
        return where((root, query, builder) -> builder.and(
                builder.greaterThanOrEqualTo(root.get(attribute), from),
                builder.lessThanOrEqualTo(root.get(attribute), to)
        ));
    }

    public static Specification<TransactionRequest> later(SingularAttribute<TransactionRequest, Date> attribute, Date from) {
        return where((root, query, builder) -> builder.greaterThanOrEqualTo(root.get(attribute), from));
    }

    public static Specification<TransactionRequest> earlier(SingularAttribute<TransactionRequest, Date> attribute, Date to) {
        return where((root, query, builder) -> builder.lessThanOrEqualTo(root.get(attribute), to));
    }


    public static <T> Specification<TransactionRequest> match(SingularAttribute<TransactionRequest, T> attribute, T input) {
        return where((root, query, builder) -> builder.equal(root.get(attribute), input));
    }

    public static <T> Specification<TransactionRequest> in(SingularAttribute<TransactionRequest, T> attribute, List<T> inputs) {
        return where(((root, query, cb) -> {
            final Path<T> group = root.get(attribute);
            CriteriaBuilder.In<T> cr = cb.in(group);
            for(T input: inputs ) {
                cr.value(input);
            }
            return cr;
        }));
    }
}
