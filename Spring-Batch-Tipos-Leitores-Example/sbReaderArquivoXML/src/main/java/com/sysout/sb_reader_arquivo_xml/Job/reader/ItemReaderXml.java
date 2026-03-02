package com.sysout.sb_reader_arquivo_xml.Job.reader;

import com.sysout.sb_reader_arquivo_xml.domain.Employee;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;


@Configuration
public class ItemReaderXml {

    @Bean
    @StepScope
    public StaxEventItemReader<Employee> itemReaderArquivoXml(
            @Value("#{jobParameters['arquivoEmployees']}") Resource arquivoEmployee) {

        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(Employee.class);

        StaxEventItemReader<Employee> reader = new StaxEventItemReader<>();
        reader.setName("employeeReader");
        reader.setResource(arquivoEmployee);
        reader.setFragmentRootElementName("employee");
        reader.setUnmarshaller(unmarshaller);

        return reader;
    }
}
