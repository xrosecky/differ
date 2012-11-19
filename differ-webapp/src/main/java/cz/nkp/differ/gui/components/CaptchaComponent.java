package cz.nkp.differ.gui.components;

import com.vaadin.Application;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import eu.livotov.tpt.TPTApplication;
import eu.livotov.tpt.util.RandomPasswordGenerator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;


public class CaptchaComponent extends CustomComponent{
	private static final long serialVersionUID = -1189704707163658035L;
	private static final int CAPTCHA_LENGTH = 5;
	
	public CaptchaComponent(){
		this.setCompositionRoot(createComponent());
	}
	
	private Layout createComponent(){
		VerticalLayout layout = new VerticalLayout();
		embed = new CaptchaEmbeddedImage(CAPTCHA_LENGTH);
		layout.addComponent(embed);
		
		captchaResponse = new TextField();
		captchaResponse.addValidator(new NullValidator("You must provide a response to the validation!",false));
		layout.addComponent(captchaResponse);
		
		return layout;
	}
	
	public boolean passedValidation(){
		if(embed.verifyCaptchaCode((String)captchaResponse.getValue())){
			return true;
		}
		
		return false;
	}
	
	public void reset(){
		embed.generateCaptchaCode(CAPTCHA_LENGTH);
		captchaResponse.setValue("");
	}
	
	private CaptchaEmbeddedImage embed;
	private TextField captchaResponse;
}

/**
 * This component represents a captcha image that can be displayed for some form validations. You
 * can provide your own text for captcha image or let the component to automatically generate it for
 * you.
 * 
 * This is an adapted form of the TPTCaptcha, which does not function correctly on 
 * non Oracle JVM.
 */
class CaptchaEmbeddedImage extends Embedded implements StreamResource.StreamSource
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 220231548210606723L;

	/**
     * Image provider for captcha
     */
    private CaptchaImageProvider imageProvider = new DefaultCaptchaImageGenerator ();
    
    /**
     * Current captcha code
     */
    private String captchaCode = "";

    /**
     * Creates a captcha component with random 5-letter code generated
     */
    public CaptchaEmbeddedImage ()
    {
        super ();
        generateCaptchaCode ( 5 );
    }
    
    /**
     * Creates a captcha component with random n-letter code generated
     */
    public CaptchaEmbeddedImage (int length)
    {
        super ();
        if(length < 6){
        	length = 5;
        }
        generateCaptchaCode (length);
    }
    
    

    /**
     * Creates a captcha component with the specified text to be used as captcha code
     *
     * @param code captcha code
     */
    public CaptchaEmbeddedImage ( String code )
    {
        this ();
        setCaptchaCode ( code );
    }

    /**
     * Sets the new captcha code. Image will be regenerated automatically.
     *
     * @param code new captcha code
     */
    public void setCaptchaCode ( String code )
    {
        setCaptchaCode ( code, TPTApplication.getCurrentApplication () );
    }

    /**
     * Sets the new captcha code. Image will be regenerated automatically. Use this method
     * when you're using this widget without the using TPTApplication class. 
     * @param code new code to generate
     * @param app Vaadin application instance. Required to generate a new StreamSource
     */
    public void setCaptchaCode ( String code, Application app )
    {
        captchaCode = code;
        refreshCaptchaImageSource ( app );
    }

    /**
     * Provides the current captcha code that is displayed in the component
     *
     * @return current captcha code
     */
    public String getCaptchaCode ()
    {
        return captchaCode;
    }

    /**
     * Verifies the given code agains current captcha code.
     *
     * @param sample sample text to compare with the current captcha component
     * @return <code>true</code> if provided code matches the captcha (uses case-insensitive
     *         comparison)
     */
    public boolean verifyCaptchaCode ( final String sample )
    {
        return sample != null && sample.equalsIgnoreCase ( getCaptchaCode () );
    }

    /**
     * Generates and sets a random captcha code with the specified characters length.
     *
     * @param charactersCount number of characters in the new code
     * @return generated code. Note, that this method will also set the newly generated code to a
     *         component.
     */
    public String generateCaptchaCode ( int charactersCount )
    {
        setCaptchaCode ( RandomPasswordGenerator.generate ( charactersCount ) );
        return getCaptchaCode ();
    }

    /**
     * Sets the new image provider, that is responsible for creating captcha images. TPTCaptcha
     * compnent has its own default image generator, but you may specify your own implementation if
     * you wish.
     *
     * @param provider new image provider to use. Captcha code will be regenerated using this new
     *                 image provider immideately.
     */
    public void setCaptchaImageProvider ( CaptchaImageProvider provider )
    {
        setCaptchaImageProvider ( provider, TPTApplication.getCurrentApplication () );
    }

    /**
     * Sets the new image provider, that is responsible for creating captcha images. TPTCaptcha
     * compnent has its own default image generator, but you may specify your own implementation if
     * you wish.
     *
     * @param provider new image provider to use. Captcha code will be regenerated using this new
     *                 image provider immideately.
     */
    public void setCaptchaImageProvider ( CaptchaImageProvider provider, Application app)
    {
        this.imageProvider = provider;
        refreshCaptchaImageSource ( app );
    }

    public InputStream getStream ()
    {
        BufferedImage image = imageProvider.getCaptchaImage ( "" + captchaCode );
        ByteArrayOutputStream bos = new ByteArrayOutputStream ();
        try {
			ImageIO.write(image, "jpg", bos);
		} catch (IOException e) {
			Logger.getLogger(getClass()).error("Captcha IO write from bytestream produced an error",e);
		}
        return new ByteArrayInputStream ( bos.toByteArray () );
    }

    private void refreshCaptchaImageSource ( Application app )
    {
        setSource ( new StreamResource ( this, UUID.randomUUID ().toString () + ".jpg", app ) );
    }

    /**
     * API for connecting custom image generators. Use setImageProvider method of the TPTCaptcha
     * component to set the new image provider if you want the captcha images to be generated
     * differently.
     */
    public interface CaptchaImageProvider
    {

        /**
         * Should provide a BufferedImage that represents the capctha code.
         *
         * @param text text to encode in the image
         * @return encoded captcha image to be displayed
         */
        BufferedImage getCaptchaImage ( String text );
    }

    private class DefaultCaptchaImageGenerator implements CaptchaEmbeddedImage.CaptchaImageProvider
    {

        private static final int LETTER_WIDTH = 50;
        private static final int IMAGE_HEIGHT = 60;
        private static final double SKEW = 2.5;
        private static final int DRAW_LINES = 4;
        private static final int DRAW_BOXES = 1;
        private final Color[] RANDOM_BG_COLORS =
        {
            Color.RED, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK,
            Color.YELLOW
        };
        private final Color[] RANDOM_FG_COLORS =
        {
            Color.BLACK, Color.BLUE, Color.DARK_GRAY
        };

        public BufferedImage getCaptchaImage ( String code )
        {
            int MAX_LETTER_COUNT = code.length ();
            int MAX_X = LETTER_WIDTH * MAX_LETTER_COUNT;
            int MAX_Y = IMAGE_HEIGHT;

            BufferedImage outImage = new BufferedImage ( MAX_X, MAX_Y, BufferedImage.TYPE_INT_RGB );
            Graphics2D g2d = outImage.createGraphics ();
            g2d.setColor ( java.awt.Color.WHITE );
            g2d.fillRect ( 0, 0, MAX_X, MAX_Y );
            for ( int i = 0; i < DRAW_BOXES; i++ )
            {
                paindBoxes ( g2d, MAX_X, MAX_Y );
            }

            Font font = new Font ( "dialog", 1, 33 );
            g2d.setFont ( font );

            g2d.setColor ( Color.BLACK );
            g2d.drawRect ( 0, 0, ( MAX_X ) - 1, MAX_Y - 1 );

            AffineTransform affineTransform = new AffineTransform ();

            for ( int i = 0; i < MAX_LETTER_COUNT; i++ )
            {
                double angle = 0;
                if ( Math.random () * 2 > 1 )
                {
                    angle = Math.random () * SKEW;
                }
                else
                {
                    angle = Math.random () * -SKEW;
                }
                affineTransform.rotate ( angle, ( LETTER_WIDTH * i ) + ( LETTER_WIDTH / 2 ), MAX_Y / 2 );
                g2d.setTransform ( affineTransform );
                setRandomFont ( g2d );
                setRandomFGColor ( g2d );
                g2d.drawString ( code.substring ( i, i + 1 ), ( i * LETTER_WIDTH ) + 3,
                        28 + ( int ) ( Math.random () * 6 ) );

                affineTransform.rotate ( -angle, ( LETTER_WIDTH * i ) + ( LETTER_WIDTH / 2 ), MAX_Y / 2 );
            }

            g2d.setXORMode ( Color.RED );
            g2d.setStroke ( new BasicStroke ( 1 ) );
            g2d.drawLine ( 0, 0, MAX_X, MAX_Y );
            g2d.setXORMode ( Color.YELLOW );
            g2d.drawLine ( 0, MAX_Y, MAX_X, 0 );

            for ( int i = 0; i < DRAW_LINES; i++ )
            {
                g2d.setXORMode ( Color.RED );
                g2d.setStroke ( new BasicStroke ( 2 ) );
                int y1 = ( int ) ( Math.random () * MAX_Y );
                g2d.drawLine ( 0, y1, MAX_X, y1 );

            }

            return outImage;
        }

        private void paindBoxes ( Graphics2D g2d, int MAX_X, int MAX_Y )
        {
            int colorId = ( int ) ( Math.random () * RANDOM_BG_COLORS.length );
            g2d.setColor ( RANDOM_BG_COLORS[colorId] );
            g2d.fillRect ( getRandomX ( MAX_X ), getRandomY ( MAX_Y ), getRandomX ( MAX_X ),
                    getRandomY ( MAX_Y ) );
        }

        private int getRandomX ( int max_x )
        {
            return ( int ) ( Math.random () * max_x );
        }

        private int getRandomY ( int max_y )
        {
            return ( int ) ( Math.random () * max_y );
        }

        private void setRandomFont ( Graphics2D g2d )
        {
            Font font = new Font ( "dialog", 1, 33 );
            g2d.setFont ( font );
        }

        private void setRandomFGColor ( Graphics2D g2d )
        {
            int colorId = ( int ) ( Math.random () * RANDOM_FG_COLORS.length );
            g2d.setColor ( RANDOM_FG_COLORS[colorId] );
        }
    }
}
