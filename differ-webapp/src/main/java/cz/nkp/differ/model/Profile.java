package cz.nkp.differ.model;

/**
 *
 * @author xrosecky
 */
public class Profile {

    public static enum Kernel {
        Revesible5x3,
        Irreversible9x7
    }

    public static class Size {

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }
        
        public int width;
        public int height;
    }

    private String name;
    private Kernel kernel;
    private Size preccintSize;
    private Size tileSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Kernel getKernel() {
        return kernel;
    }

    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    public Size getPreccintSize() {
        return preccintSize;
    }

    public void setPreccintSize(Size preccintSize) {
        this.preccintSize = preccintSize;
    }

    public Size getTileSize() {
        return tileSize;
    }

    public void setTileSize(Size tileSize) {
        this.tileSize = tileSize;
    }

}
