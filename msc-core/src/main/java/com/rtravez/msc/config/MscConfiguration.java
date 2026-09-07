package com.rtravez.msc.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * MscConfiguration spring configuration.
 *
 * @author renetravez
 * @version 1.0
 */
@EnableAsync
@EnableJpaRepositories(repositoryImplementationPostfix = "Repository")
public class MscConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }

}
