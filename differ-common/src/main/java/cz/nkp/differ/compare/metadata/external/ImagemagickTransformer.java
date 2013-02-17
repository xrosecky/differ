package cz.nkp.differ.compare.metadata.external;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author  Jan Stavel <stavel.jan@gmail.com>
 */
public class ImagemagickTransformer implements ResultTransformer {

    public class HeaderPartsGetter {
        private ArrayDeque<String> parts = new ArrayDeque<String>();
        private int depth = 0;

        public ArrayDeque<String> get(LineParts lineParts){
            if( this.depth == lineParts.depth ){
                if( !this.parts.isEmpty() ){
                    this.parts.removeLast();
                }
            } else {
                if( this.depth > lineParts.depth ){
                    if( !this.parts.isEmpty() ){
                        this.parts.removeLast();
                        if( !this.parts.isEmpty()){
                            this.parts.removeLast();
                        }
                    }
                }
            }
			if( lineParts.name != null ){
				this.parts.addLast(lineParts.name);
				this.depth = lineParts.depth;
			};
            return parts;
        }
    }
    public class LineParts {
        public int depth;
        public String name;
        public String value;

        public Pattern linePartsRegexp = Pattern.compile("^([ ]*)([A-Za-z0-9\\[\\], \\-_]+):[\\ \\t]*(.*)$");
        public Pattern subNameRegexp = Pattern.compile("^([a-zA-Z0-9\\[\\],\\-_]+)[ ]*:[ \\t]+(.*)$");
        public Pattern lineWithoutName = Pattern.compile("^([ ]+)(.*)");
        LineParts(String line) {
            Matcher headMatcher = linePartsRegexp.matcher(line);
            if( headMatcher.find() ){
                String spaces = headMatcher.group(1);
                this.depth = spaces.length();
                String name = headMatcher.group(2).trim();
                String value = headMatcher.group(3).trim();
                Matcher subNameMatcher = subNameRegexp.matcher(value);
                if( subNameMatcher.find() ){
                    this.name = name + "/" + subNameMatcher.group(1).trim();
                    this.value = subNameMatcher.group(2).trim();
                } else {
                    this.name = name;
                    this.value = value;
                }
            } else {
                Matcher withoutNameMatcher = lineWithoutName.matcher(line);
                if( withoutNameMatcher.find() ){
                    this.value = withoutNameMatcher.group(2);
                    this.depth = withoutNameMatcher.group(1).length();
                } else {
                    this.value = line;
                }
            }
        }
    }
    private class HistogramReader {
        public LineParts header;
        ArrayList<String> histogram = new ArrayList<String>();

        boolean isHistogramHeader(LineParts lineParts){
            if( lineParts.name != null && lineParts.name.equals("Histogram") ) {
                header = lineParts;
                return true;
            }
            return false;
        }
        boolean isHistogram( LineParts lineParts){
            if( lineParts.depth > header.depth ){
                histogram.add(String.format("%s: %s", lineParts.name, lineParts.value));
                return true;
            }
            return false;
        }

        ArrayList<String> getHistogram(){
            return histogram;
        }
    }

    private class ClippingPathReader {
        public LineParts header;
        ArrayList<String> clippingPath = new ArrayList<String>();

        boolean isClippingPathHeader(LineParts lineParts){
            if( lineParts.name != null && lineParts.name.equalsIgnoreCase("Clipping path")){
                header = lineParts;
                return true;
            }
            return false;
        }

        boolean isClippingPath(LineParts lineParts){
            if( lineParts.name == null){
                clippingPath.add(lineParts.value);
                return true;
            }
            return false;
        }

        ArrayList<String> getClippingPath(){
            return clippingPath;
        }
    }

    private class ColormapReader {
        public LineParts header;
        ArrayList<String> colormap = new ArrayList<String>();

        boolean isColormapHeader(LineParts lineParts){
            if( lineParts.name != null && lineParts.name.equalsIgnoreCase("Colormap")){
                header = lineParts;
                return true;
            }
            return false;
        }
        boolean isColormap(LineParts lineParts){
            if( lineParts.depth > header.depth ){
                colormap.add(String.format("%s: %s", lineParts.name, lineParts.value));
                return true;
            }
            return false;
        }
        ArrayList<String> getColormap(){
            return colormap;
        }
    }
    @Override
    public List<Entry> transform(byte[] stdout, byte[] stderr) throws IOException {

        List<Entry> result = new ArrayList<Entry>();
		BufferedReader bf = new BufferedReader(new StringReader(new String(stdout)));

        String line;

        HeaderPartsGetter headerPartsGetter = new HeaderPartsGetter();

        HistogramReader histogramReader = new HistogramReader();
        ClippingPathReader clippingPathReader = new ClippingPathReader();
        ColormapReader colormapReader = new ColormapReader();

 		while ((line = bf.readLine()) != null) {
             LineParts lineParts = new LineParts(line);
             if( histogramReader.isHistogramHeader(lineParts)){
                 lineParts = new LineParts(bf.readLine());
                 while( histogramReader.isHistogram(lineParts)){
                     lineParts = new LineParts(bf.readLine());
                 }
                 String name = histogramReader.header.name;
                 String value = StringUtils.arrayToDelimitedString(histogramReader.getHistogram().toArray(), "|");
                 Entry metadataEntry = new Entry();
                 metadataEntry.setKey(name);
                 metadataEntry.setValue(value);
                 result.add(metadataEntry);
             }
             if( clippingPathReader.isClippingPathHeader(lineParts)){
                 lineParts = new LineParts(bf.readLine());
                 while( clippingPathReader.isClippingPath(lineParts)){
                     lineParts = new LineParts(bf.readLine());
                 }
                 String name = clippingPathReader.header.name;
                 String value = StringUtils.arrayToDelimitedString(clippingPathReader.getClippingPath().toArray(), "\n");
                 Entry metadataEntry = new Entry();
                 metadataEntry.setKey(name);
                 metadataEntry.setValue(value);
                 result.add(metadataEntry);
             }
             if( colormapReader.isColormapHeader(lineParts)){
                 lineParts = new LineParts(bf.readLine());
                 while( colormapReader.isColormap(lineParts)){
                     lineParts = new LineParts(bf.readLine());
                 }
                 String name = colormapReader.header.name;
                 String value = StringUtils.arrayToDelimitedString(clippingPathReader.getClippingPath().toArray(), "|");
                 Entry metadataEntry = new Entry();
                 metadataEntry.setKey(name);
                 metadataEntry.setValue(value);
                 result.add(metadataEntry);
             }
             String name = StringUtils.arrayToDelimitedString(headerPartsGetter.get(lineParts).toArray(),"/");
             String value = lineParts.value;
             if (!value.isEmpty()) {
                 Entry metadataEntry = new Entry();
                 metadataEntry.setKey(name);
			     metadataEntry.setValue(value);
			     result.add(metadataEntry);
			}
		}
		return result;
    }
}
