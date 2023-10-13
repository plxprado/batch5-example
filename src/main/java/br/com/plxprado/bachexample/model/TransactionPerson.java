package br.com.plxprado.bachexample.model;

import br.com.plxprado.bachexample.vo.FinancialSituation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPerson {

    private Integer id;
    private String name;
    private Integer age;
    private FinancialSituation financialSituation;
    private Boolean paidInstallment;

}
