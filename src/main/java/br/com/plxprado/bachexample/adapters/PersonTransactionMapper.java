package br.com.plxprado.bachexample.adapters;

import br.com.plxprado.bachexample.model.TransactionPerson;
import br.com.plxprado.bachexample.persistence.entity.Person;
import br.com.plxprado.bachexample.vo.FinancialSituation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Random;


public class PersonTransactionMapper {
    public static TransactionPerson personToTranasctionPerson(final Person person) {
        return TransactionPerson.builder()
                .id(person.getId())
                .name(person.getName())
                .age(person.getAge())
                .financialSituation(person.getFinancialSituation())
                .paidInstallment(person.getPaidInstallment())
                .build();
    }
    public  static Person tranactionPeronToperson(TransactionPerson transactionPerson){
        return Person.builder()
                .id(transactionPerson.getId())
                .name(transactionPerson.getName())
                .age(transactionPerson.getAge())
                .financialSituation(transactionPerson.getFinancialSituation())
                .paidInstallment(transactionPerson.getPaidInstallment())
                .build();
    }

    public static void main(String[] args) throws IOException {
        int id = 1;
        String nome="";
        int idade = 0;
        String situacao = FinancialSituation.IN_DEBIT.name();
        Boolean pagou =true;

        FileWriter fileWriter = new FileWriter("populaperson.sql");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for(int i = 0 ; i<=15000; i++){
            idade = new Random().nextInt(1,99);
            nome = "ABC";
            StringBuilder sql =  new StringBuilder()
                    .append("INSERT INTO public.person (" )
                    .append("id,")
                    .append("\"name\",")
                    .append("age,")
                    .append("financial_situation,")
                    .append("paid_installment)")
                    .append(" VALUES(")
                    .append(i + ",")
                    .append( "'" +nome +  i + "', ")
                    .append(idade +",")
                    .append("'" +situacao +"', ")
                    .append(pagou).append(");");
            printWriter.printf(sql.toString());
            printWriter.write("\n");






        }

        printWriter.close();




    }
}
