package com.ecw.ccd;

import com.helger.schematron.ISchematronResource;
import com.helger.schematron.svrl.SVRLMarshaller;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import com.helger.schematron.xslt.SchematronResourceSCH;

import javax.annotation.Nonnull;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class CCDValidator {

    public CCDValidator() {
    }

    public File validate(File ccdaXml) {
        File resultFile = new File(ccdaXml.getParent(), ccdaXml.getName() + ".validation-result.html");
        this.validate(ccdaXml, resultFile);
        return resultFile;
    }

    public void validate(File ccdaXml, File toReport) {
        File rules = new File(App.class.getResource("/ccda_2_1.sch").getPath());
        File resourceRoot = rules.getParentFile();

        SchematronOutputType validity = validateXMLViaXSLTSchematron(rules, ccdaXml);
        // Programmatic way to read results
        // SVRLHelper.getAllFailedAssertions(validity);


        File xsltForResultConversion = new File(resourceRoot, "svrl2html.xsl");
        try {
            resultTransform(validity, toReport, xsltForResultConversion);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

    }

    protected void resultTransform(SchematronOutputType result, File target, File xslt) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xsltSource = new StreamSource(xslt);
        Transformer transformer = factory.newTransformer(xsltSource);

        Source text = new DOMSource(new SVRLMarshaller().getAsDocument(result));
        transformer.transform(text, new StreamResult(target));
    }

    protected SchematronOutputType validateXMLViaXSLTSchematron(@Nonnull final File aSchematronFile, @Nonnull final File aXMLFile) {
        final ISchematronResource aResSCH = SchematronResourceSCH.fromFile(aSchematronFile);
        if (!aResSCH.isValidSchematron()) {
            throw new IllegalArgumentException("Invalid Schematron!");
        }
        try {
            return aResSCH.applySchematronValidationToSVRL(new StreamSource(aXMLFile));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}