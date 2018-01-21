package com.dnd;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class SqlConfig {

    @Bean
    public String insertNewMonster(@Value("classpath:sql/insertNewMonster.sql") Resource resource) throws IOException {
        return resourceToString(resource);
    }

    @Bean
    public String getAllMonsterNames(@Value("classpath:sql/getAllMonsterNames.sql") Resource resource) throws IOException {
        return resourceToString(resource);
    }

    @Bean
    public String getAllMonsters(@Value("classpath:sql/getAllMonsters.sql") Resource resource) throws IOException {
        return resourceToString(resource);
    }

    @Bean
    public String getMonstersByName(@Value("classpath:sql/getMonstersByName.sql") Resource resource) throws IOException {
        return resourceToString(resource);
    }

    private String resourceToString(Resource resource) throws IOException {
        File file = resource.getFile();
        String str = new String(Files.readAllBytes(file.toPath()));
        return str;
    }

}
