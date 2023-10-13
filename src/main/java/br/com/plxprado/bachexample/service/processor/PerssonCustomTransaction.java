package br.com.plxprado.bachexample.service.processor;

import br.com.plxprado.bachexample.model.TransactionPerson;
import br.com.plxprado.bachexample.vo.FinancialSituation;
import org.springframework.batch.item.ItemProcessor;

import static br.com.plxprado.bachexample.vo.FinancialSituation.IN_DEBIT;
import static br.com.plxprado.bachexample.vo.FinancialSituation.NO_DEBIT;

public class PerssonCustomTransaction implements ItemProcessor<TransactionPerson, TransactionPerson> {
    @Override
    public TransactionPerson process(TransactionPerson item) throws Exception {
        return  TransactionPerson.builder()
                .id(item.getId())
                .name(item.getName())
                .age(item.getAge())
                .financialSituation(financialSituationDecisor(item.getPaidInstallment()))
                .paidInstallment(item.getPaidInstallment())
                .build();
    }

    private FinancialSituation financialSituationDecisor(Boolean paidInstallment) {
        return paidInstallment ? NO_DEBIT : IN_DEBIT;
    }
}
