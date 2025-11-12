package com.usuario.usuarios_api.modules.user.mappers;

import com.usuario.usuarios_api.modules.user.dto.UserSearchCriteriaDto;
import com.usuario.usuarios_api.modules.user.dto.UserSearchRequest;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class UserSearchMapperTest {

    private UserSearchRequest request;
    private LocalDate birthDateFrom;
    private LocalDate birthDateTo;

    @Before
    public void setUp() {
        birthDateFrom = LocalDate.of(1990, 1, 1);
        birthDateTo = LocalDate.of(2000, 12, 31);
    }

    @Test
    public void testToCriteriaOk() {
        request = new UserSearchRequest(
                "Hector",
                "hector@example.com",
                "ADMIN",
                "ACTIVE",
                birthDateFrom,
                birthDateTo
        );

        UserSearchCriteriaDto result = UserSearchMapper.toCriteria(request);

        assertNotNull(result);
        assertEquals(request.name(), result.name());
        assertEquals(request.email(), result.email());
        assertEquals(request.role(), result.role());
        assertEquals(request.status(), result.status());
        assertEquals(request.birthDateFrom(), result.birthDateFrom());
        assertEquals(request.birthDateTo(), result.birthDateTo());
    }

    @Test
    public void testToCriteriaWithNullFields() {
        request = new UserSearchRequest(
                null,
                null,
                null,
                null,
                null,
                null
        );

        UserSearchCriteriaDto result = UserSearchMapper.toCriteria(request);

        assertNotNull(result);
        assertNull(result.name());
        assertNull(result.email());
        assertNull(result.role());
        assertNull(result.status());
        assertNull(result.birthDateFrom());
        assertNull(result.birthDateTo());
    }

    @Test
    public void testToCriteriaWithPartialFields() {
        request = new UserSearchRequest(
                "Hector",
                null,
                "ADMIN",
                null,
                birthDateFrom,
                null
        );

        UserSearchCriteriaDto result = UserSearchMapper.toCriteria(request);

        assertNotNull(result);
        assertEquals("Hector", result.name());
        assertNull(result.email());
        assertEquals("ADMIN", result.role());
        assertNull(result.status());
        assertEquals(birthDateFrom, result.birthDateFrom());
        assertNull(result.birthDateTo());
    }

    @Test
    public void testToCriteriaWithDateRange() {
        request = new UserSearchRequest(
                null,
                null,
                null,
                null,
                birthDateFrom,
                birthDateTo
        );

        UserSearchCriteriaDto result = UserSearchMapper.toCriteria(request);

        assertNotNull(result);
        assertEquals(birthDateFrom, result.birthDateFrom());
        assertEquals(birthDateTo, result.birthDateTo());
        assertNull(result.name());
        assertNull(result.email());
        assertNull(result.role());
        assertNull(result.status());
    }
}

