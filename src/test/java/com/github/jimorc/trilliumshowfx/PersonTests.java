package com.github.jimorc.trilliumshowfx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * PersonTests contains tests for the Person class.
 */
public class PersonTests {
    @Test
    void testPerson() {
        Person p = new Person("John", "Doe");
        assertEquals("John", p.getFirstName());
        assertEquals("Doe", p.getLastName());
        assertEquals("John Doe", p.getFullName());
        assertEquals("John D.", p.getFirstPlusInitial());
    }
}
