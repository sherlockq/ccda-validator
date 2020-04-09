package com.ecw.ccd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class CCDValidatorFeature {

    CCDValidator ccdValidator;

    @BeforeEach
    void setUp() {
        ccdValidator = new CCDValidator();
    }

    @Test
    void generateReport() throws Exception {
        File ccda = new File(CCDValidatorFeature.class.getResource("/ccd-1.xml").getFile());
        File outputFile = File.createTempFile("temp", null);
        ccdValidator.validate(ccda, outputFile);

        assertTrue(outputFile.exists());
    }
}