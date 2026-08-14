package org.demo.sharemgmt.strategy;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.demo.sharemgmt.domain.TransactionType;
import org.springframework.stereotype.Component;

@Component
public class TransactionProcessorFactory {

    private final Map<TransactionType, TransactionProcessor> processors = new EnumMap<>(TransactionType.class);

    public TransactionProcessorFactory(List<TransactionProcessor> processors) {
        for (TransactionProcessor processor : processors) {
            this.processors.put(processor.supports(), processor);
        }
    }

    public TransactionProcessor getProcessor(TransactionType transactionType) {
        TransactionProcessor processor = processors.get(transactionType);
        if (processor == null) {
            throw new IllegalArgumentException("No transaction processor configured for " + transactionType + ".");
        }
        return processor;
    }
}
