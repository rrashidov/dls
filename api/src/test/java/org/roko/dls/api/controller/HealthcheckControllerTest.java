package org.roko.dls.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class HealthcheckControllerTest {

    private HealthcheckController controller;

    @BeforeEach
    public void setup(){
        controller = new HealthcheckController();
    }

    @Test
    public void healthcheckReturnsOK_whenCalled(){
        ResponseEntity<String> healtcheckResponse = controller.healtcheck();

        assertEquals(HttpStatusCode.valueOf(200),healtcheckResponse.getStatusCode());
    }
}
