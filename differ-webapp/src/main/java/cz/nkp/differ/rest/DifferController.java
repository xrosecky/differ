package cz.nkp.differ.rest;

import cz.nkp.differ.compare.io.SerializableImageProcessorResult;
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

    private ApplicationContext appCtx;

    @RequestMapping(value="/results", method=RequestMethod.GET)
    @ResponseBody
    public String getResults() {
	return "results";
    }

    @RequestMapping(value="/results", method=RequestMethod.POST)
    @ResponseBody
    public String addResult(@RequestBody SerializableImageProcessorResult body) {
	return "OK";
    }

    @RequestMapping(value="/results/{id}", method=RequestMethod.GET)
    @ResponseBody
    public SerializableImageProcessorResult getResult(@PathVariable("id") Long id) {
	SerializableImageProcessorResult result = new SerializableImageProcessorResult();
	return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
	this.appCtx = ac;
    }

}
