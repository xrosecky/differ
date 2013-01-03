package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.io.ImageProcessorResult;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 2.1.13
 * Time: 18:39
 * To change this template use File | Settings | File Templates.
 */
public interface ResultSaver {
    public void save(ImageProcessorResult result);
}
