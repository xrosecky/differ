[1mdiff --git a/differ-common/src/main/java/cz/nkp/differ/images/ThumbnailLoader.java b/differ-common/src/main/java/cz/nkp/differ/images/ThumbnailLoader.java[m
[1mindex 9171221..8680c23 100644[m
[1m--- a/differ-common/src/main/java/cz/nkp/differ/images/ThumbnailLoader.java[m
[1m+++ b/differ-common/src/main/java/cz/nkp/differ/images/ThumbnailLoader.java[m
[36m@@ -4,7 +4,8 @@[m [mimport cz.nkp.differ.exceptions.ImageDifferException;[m
 import java.awt.image.BufferedImage;[m
 import java.io.File;[m
 [m
[31m-/**[m
[32m+[m[32m/**	This class loads up the image and repaints it in the specified way[m
[32m+[m[32m *  Images are loaded on the fly the first time and saved until called next time[m
  *	 @author Jonatan Svensson[m
  */[m
 public ThumbnailLoader implements ImageLoader {[m
[36m@@ -15,9 +16,10 @@[m [mpublic ThumbnailLoader implements ImageLoader {[m
     [m
     }[m
     [m
[31m-    public BufferedImage resize(BufferedImage){[m
[32m+[m[32m    public BufferedImage resize(BufferedImage, int width, int height){[m
     	[m
     	return null;[m
     }[m
 [m
[32m+[m
 }[m
[1mdiff --git a/differ-webapp/src/main/java/cz/nkp/differ/compare/io/ImageFileAnalysisContainer.java b/differ-webapp/src/main/java/cz/nkp/differ/compare/io/ImageFileAnalysisContainer.java[m
[1mindex 14821b9..941ea88 100644[m
[1m--- a/differ-webapp/src/main/java/cz/nkp/differ/compare/io/ImageFileAnalysisContainer.java[m
[1m+++ b/differ-webapp/src/main/java/cz/nkp/differ/compare/io/ImageFileAnalysisContainer.java[m
[36m@@ -277,7 +277,7 @@[m [mpublic class ImageFileAnalysisContainer {[m
     [m
     /**[m
      * Set up component to generate reports[m
[31m-     *[m
[32m+[m[32m     * @author Jonatan Svensson[m
      */[m
     public void setupExport(final Layout mainLayout){[m
         Button downloadPDFButton = new Button();[m
[36m@@ -291,6 +291,8 @@[m [mpublic class ImageFileAnalysisContainer {[m
         // TODO Listener[m
     	// generateXls()[m
         // callback from dynamicreports[m
[32m+[m[41m        [m
[32m+[m[32m        // add buttons to mainLayout[m
     	}[m
      [m
     public Resource imageToResource(Image img) {[m
