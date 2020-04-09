package com.ecw.ccd;

import java.io.File;

public class App {

    public static void main(String[] args) throws Exception {

        CCDValidator ccdValidator = new CCDValidator();

        ccdValidator.validate(new File("/Users/sherlockq/Codes/eCW/ccda-validator/examples/ccd-1.xml"));

        ccdValidator.validate(new File("/Users/sherlockq/Codes/eCW/ccda-validator/examples/ccd-2.xml"));
    }

}
