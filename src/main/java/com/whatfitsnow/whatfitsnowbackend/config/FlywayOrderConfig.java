package com.whatfitsnow.whatfitsnowbackend.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ensures Flyway migrations run before Hibernate schema validation.
 */
@Configuration
public class FlywayOrderConfig {

  @Bean
  static BeanFactoryPostProcessor flywayBeforeJpaPostProcessor() {
    return new BeanFactoryPostProcessor() {
      @Override
      public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (!beanFactory.containsBeanDefinition("entityManagerFactory")) {
          return;
        }
        BeanDefinition emf = beanFactory.getBeanDefinition("entityManagerFactory");

        // Collect known Flyway initializer beans across Spring Boot versions and our explicit local migrator.
        java.util.LinkedHashSet<String> flywayBeans = new java.util.LinkedHashSet<>();

        // Common in auto-config
        addIfPresent(beanFactory, flywayBeans, "flywayInitializer");
        addIfPresent(beanFactory, flywayBeans, "flywayMigrationInitializer");

        // Our local-profile explicit migrator bean
        addIfPresent(beanFactory, flywayBeans, "explicitFlywayMigrator");

        // Fallback: detect by bean class name (varies by Boot versions)
        for (String name : beanFactory.getBeanDefinitionNames()) {
          BeanDefinition bd = beanFactory.getBeanDefinition(name);
          String className = bd.getBeanClassName();
          if (className == null) {
            continue;
          }
          if (className.endsWith("FlywayMigrationInitializer")
              || className.contains("FlywayMigrationInitializerAutoConfiguration")) {
            flywayBeans.add(name);
          }
        }

        if (flywayBeans.isEmpty()) {
          return;
        }

        // Merge with existing dependsOn, if any.
        java.util.LinkedHashSet<String> dependsOn = new java.util.LinkedHashSet<>();
        String[] existing = emf.getDependsOn();
        if (existing != null) {
          java.util.Collections.addAll(dependsOn, existing);
        }
        dependsOn.addAll(flywayBeans);
        emf.setDependsOn(dependsOn.toArray(String[]::new));
      }
    };
  }

  private static void addIfPresent(ConfigurableListableBeanFactory beanFactory,
                                  java.util.Set<String> out,
                                  String beanName) {
    if (beanFactory.containsBean(beanName) || beanFactory.containsBeanDefinition(beanName)) {
      out.add(beanName);
    }
  }
}

