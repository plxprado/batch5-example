package br.com.plxprado.bachexample.persistence.repository;

import br.com.plxprado.bachexample.persistence.entity.Person;
import br.com.plxprado.bachexample.vo.FinancialSituation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {
    List<Person> findByFinancialSituation(FinancialSituation financialSituation);
}
