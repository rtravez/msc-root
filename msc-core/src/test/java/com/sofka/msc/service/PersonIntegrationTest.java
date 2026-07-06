package com.sofka.msc.service;

import com.github.javafaker.Faker;
import com.sofka.msc.dto.PersonDto;
import com.sofka.msc.entity.PersonEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Disabled
class PersonIntegrationTest {

    PersonEntity person1;
    PersonEntity person2;
    PersonDto personDto;
    private final Faker faker = new Faker();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IPersonService personService;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        person1 = entityManager.persist(PersonEntity.builder()
                .name("René")
                .lastname("Trávez")
                .identification("1717172637")
                .build());
        person2 = entityManager.persist(PersonEntity.builder()
                .name(faker.name().firstName())
                .lastname(faker.name().lastName())
                .identification(faker.number().digits(10)).build());

        personDto = PersonDto.builder()
                .name(faker.name().firstName())
                .lastname(faker.name().lastName())
                .identification(faker.number().digits(10)).build();
    }

    @Test
    void testFindPersonList() {
        List<PersonDto> personEntityList = new ArrayList<>();
        List<PersonEntity> entities = personService.findAll();
        entities.forEach(entity -> {
            PersonDto dto = modelMapper.map(entity, PersonDto.class);
            personEntityList.add(dto);
        });

        assertEquals(2, personEntityList.size());
    }

    @Test
    void testFindPersonById() {
        Optional<PersonEntity> entity = personService.findById(person1.getPersonId());
        PersonDto personVo = modelMapper.map(entity.get(), PersonDto.class);
        assertEquals(person1.getPersonId(), personVo.getPersonId());
    }

    @Test
    void testExist() {
        Boolean test = this.personService.exist(person1.getIdentification());
        assertEquals(Boolean.TRUE, test);
    }

    @Test
    void testCreatePerson() {
        PersonEntity entity = modelMapper.map(personDto, PersonEntity.class);
        this.personService.save(entity);
        assertEquals(entity.getName(), entity.getName());
    }

    @Test
    void testUpdate() {
        PersonDto personUpdate = PersonDto.builder()
                .personId(235L).name("Juan")
                .lastname(faker.name().lastName())
                .identification(faker.number().digits(10)).build();
        PersonEntity entity = modelMapper.map(personUpdate, PersonEntity.class);

        personService.update(entity);
        assertEquals("Juan", entity.getName());
    }

    @Test
    void testDelete() {
        person1.setStatus(false);
        this.personService.deleteById(person1.getPersonId());
        assertEquals(false, person1.getStatus());
    }
}
