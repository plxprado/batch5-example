package br.com.plxprado.bachexample.service.writer;

import br.com.plxprado.bachexample.adapters.PersonTransactionMapper;
import br.com.plxprado.bachexample.model.TransactionPerson;
import br.com.plxprado.bachexample.persistence.entity.Person;
import br.com.plxprado.bachexample.persistence.repository.PersonRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

public class PersonUpdateWriter implements ItemWriter<TransactionPerson> {


    @Autowired
    private PersonRepository personRepository;

    private Logger logger = Logger.getLogger(PersonUpdateWriter.class.getName());

    @Override
    public void write(Chunk<? extends TransactionPerson> chunk) throws Exception {
        chunk.forEach(transactionPerson -> {
            logger.info("transactionPerson===> " + transactionPerson);
            final Person person = PersonTransactionMapper.tranactionPeronToperson(transactionPerson);
            logger.info("person===> " + person);
            personRepository.save(person);
        });
    }
}
