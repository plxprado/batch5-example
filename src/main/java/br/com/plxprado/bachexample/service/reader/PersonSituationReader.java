package br.com.plxprado.bachexample.service.reader;

import br.com.plxprado.bachexample.adapters.PersonTransactionMapper;
import br.com.plxprado.bachexample.model.TransactionPerson;
import br.com.plxprado.bachexample.persistence.entity.Person;
import br.com.plxprado.bachexample.persistence.repository.PersonRepository;
import br.com.plxprado.bachexample.vo.FinancialSituation;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Predicate;

public class PersonSituationReader implements ItemReader<TransactionPerson> {

    @Autowired
    private PersonRepository personRepository;


    private ItemReader<TransactionPerson> delegate;



    @Override
    public TransactionPerson read() throws Exception {
        if (delegate == null) {
            delegate = new IteratorItemReader<>(personsWithDebit());
        }
        return delegate.read();

    }

    private Iterable<TransactionPerson> personsWithDebit() {
        Predicate<Person> personInDebit = Person::getPaidInstallment;
        List<Person> personWithDebit =  personRepository.findByFinancialSituation(FinancialSituation.IN_DEBIT);
        return personWithDebit.stream()
                .filter(personInDebit)
                .map(PersonTransactionMapper::personToTranasctionPerson).toList();
    }
}
