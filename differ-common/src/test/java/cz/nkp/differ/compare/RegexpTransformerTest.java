package cz.nkp.differ.compare;

import java.util.List;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration; 
import org.springframework.beans.factory.annotation.Autowired;

import cz.nkp.differ.compare.metadata.external.RegexpTransformer;
import cz.nkp.differ.compare.metadata.external.ResultTransformer.Entry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:appCtx-differ-common-test.xml" })
public class RegexpTransformerTest {

	@Autowired
	private RegexpTransformer regexpTransformer;
	
	private final String test = "width:256\nheight:256";
	
	@Test
	public void test() throws Exception {
		System.out.println("test executed");
		if (regexpTransformer == null) {
			throw new NullPointerException("regexpTransformer");
		}
		List<Entry> result = regexpTransformer.transform(test.getBytes(), null);
		Assert.assertEquals(result.size(), 2); 
	}
	
}
