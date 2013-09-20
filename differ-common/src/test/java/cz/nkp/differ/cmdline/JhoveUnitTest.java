package cz.nkp.differ.cmdline;

import cz.nkp.differ.cmdline.ValueTester.ValueTester;
import cz.nkp.differ.compare.metadata.external.ResultTransformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


/**
 * Test class for Jhove transformer
 * User: Jonatan Svensson <jonatansve@gmail.com>
 * Date: 2013-07-16
 * Time: 19:17
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:jhoveTestsCtx.xml"})
public class JhoveUnitTest {
    TestRunner t = new TestRunner();
    String thisExtractor = this.getClass().getSimpleName();

    @Resource
    private Map<String, Object> image14Test01;

    @Resource
    private Map<String, Object> image01Test02;

    @Resource
    private LinkedHashMap<String, Object> extractorSignificantProperties;
    @Autowired
    private cz.nkp.differ.compare.metadata.external.ResultTransformer jhoveMetadataTransformer;

    @Test
    public void testImage14() throws Exception {
        byte[] stdout = TestHelper.readFile("../docs/examples/images_01/14/output-jhove.raw");
        List<ResultTransformer.Entry> transformedData = jhoveMetadataTransformer.transform(stdout, null);
        ArrayList ignoredProperties = (ArrayList) image14Test01.get("ignoredSignificantProperties");
        ArrayList recognizedProperties = (ArrayList) image14Test01.get("recognizedSignificantProperties");
        LinkedHashMap<String, Object> specialProperties = (LinkedHashMap) image14Test01.get("specialSignificantProperties");
        LinkedHashMap significantProperties = (LinkedHashMap) image14Test01.get("significantProperties");

        t.runStandardTests(transformedData,
                recognizedProperties,
                ignoredProperties,
                specialProperties,
                extractorSignificantProperties,
                significantProperties,
                thisExtractor
        );
    }

    @Test
    public void testImage01() throws Exception {
        byte[] stdout = TestHelper.readFile("../docs/examples/images_01/01/output-jhove.raw");
        List<ResultTransformer.Entry> transformedData = jhoveMetadataTransformer.transform(stdout, null);
        ArrayList ignoredProperties = (ArrayList) image01Test02.get("ignoredSignificantProperties");
        ArrayList recognizedProperties = (ArrayList) image01Test02.get("recognizedSignificantProperties");
        LinkedHashMap<String, Object> specialProperties = (LinkedHashMap) image01Test02.get("specialSignificantProperties");
        LinkedHashMap significantProperties = (LinkedHashMap) image01Test02.get("significantProperties");

        t.runStandardTests(transformedData,
                recognizedProperties,
                ignoredProperties,
                specialProperties,
                extractorSignificantProperties,
                significantProperties,
                thisExtractor
        );
    }
}
