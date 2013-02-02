package cz.nkp.differ.rest;

import cz.nkp.differ.compare.io.SerializableImageProcessorResults;
import cz.nkp.differ.io.ResultManager;
import java.io.IOException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author xrosecky
 */
@Controller
public class DifferController implements ApplicationContextAware {


    private ResultManager manager;
    private ApplicationContext appCtx;

    @RequestMapping(value="/results", method=RequestMethod.GET)
    @ResponseBody
    public String getResults() {
	return "results";
    }

    @RequestMapping(value="/results", method=RequestMethod.POST)
    @ResponseBody
    public String addResult(@RequestBody SerializableImageProcessorResults body) {
	try {
	    manager.save(body);
	} catch (IOException ioe) {
	    return "<result><status>failed</status><message>io error</message></result>";
	}
	return "<result><id>1</id><status>ok</status><message>result saved</message></result>";
    }

    @RequestMapping(value="/results/{id}", method=RequestMethod.GET)
    @ResponseBody
    public SerializableImageProcessorResults getResult(@PathVariable("id") Long id) {
	SerializableImageProcessorResults result = new SerializableImageProcessorResults();
	return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
	this.manager = (ResultManager) ac.getBean("resultManager");
    }

}
