import com.helger.schematron.ISchematronResource;
import com.helger.schematron.svrl.SVRLHelper;
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

public class App {
    public static void main(String[] args) throws Exception {
        // CCDA file to check
        File ccdaFileToCheck = new File("/Users/sherlockq/Desktop/eCW/CDAR2_IG_CCDA_CLINNOTES_R1_DSTU2.1_2015AUG_2019JUNwith_errata/C-CDA_R2-1_CCD.xml");

        File schematronRules = new File(App.class.getResource("/ccda_2_1.sch").getPath());
        File resourceRoot = schematronRules.getParentFile();

        SchematronOutputType validity = validateXMLViaXSLTSchematron(schematronRules, ccdaFileToCheck);
        // Programmatic way to read results
        // SVRLHelper.getAllFailedAssertions(validity);

        File resultFile = new File(ccdaFileToCheck.getParent(), "validation-result.html");
        File xsltForResultConverstion = new File(resourceRoot, "svrl2html.xsl");
        resultTransform(validity, resultFile, xsltForResultConverstion);
    }

    public static SchematronOutputType validateXMLViaXSLTSchematron(@Nonnull final File aSchematronFile, @Nonnull final File aXMLFile) throws Exception {
        final ISchematronResource aResSCH = SchematronResourceSCH.fromFile(aSchematronFile);
        if (!aResSCH.isValidSchematron()) {
            throw new IllegalArgumentException("Invalid Schematron!");
        }
        return aResSCH.applySchematronValidationToSVRL(new StreamSource(aXMLFile));
    }

    public static void resultTransform(SchematronOutputType result, File target, File xslt) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xsltSource = new StreamSource(xslt);
        Transformer transformer = factory.newTransformer(xsltSource);

        Source text = new DOMSource(new SVRLMarshaller().getAsDocument(result));
        transformer.transform(text, new StreamResult(target));
    }

}
