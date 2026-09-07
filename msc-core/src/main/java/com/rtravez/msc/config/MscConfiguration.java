package com.rtravez.msc.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * MscConfiguration spring configuration.
 *
 * @author renetravez
 * @version 1.0
 */
@EnableAsync
@EnableJpaRepositories(basePackages = { "com.rtravez.msc.repository" }, repositoryImplementationPostfix = "Impl")
public class MscConfiguration {
}
