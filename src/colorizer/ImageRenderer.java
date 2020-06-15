package colorizer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Author: (Alex) Olexandr Matveyev
 * @author Olexandr Matveyev
 */
public class ImageRenderer implements Runnable
{
    //used to update rendering image for user preview
    //and update status of some buttons
    private GUI gui = null;

    private File output = null;

    private BufferedImage colorPattern = null;
    private BufferedImage grayImage = null;
    private BufferedImage newImg = null;

    private ArrayList<ImgData> colorImgData = null;
    private ArrayList<ImgData> grayImgData = null;

    private boolean isRunning = false;
    private boolean isPaused = false;

    private int maxUnits = 0;
    private int currentUnits = 0;
    private int unitsLeft = 0;

    ImageRenderer()
    {

    }

    ImageRenderer(BufferedImage colorPattern, BufferedImage grayImage)
    {
        this.colorPattern = colorPattern;
        this.grayImage = grayImage;
    }

    public void buildColorImgData()
    {
        colorImgData = new ArrayList<ImgData>();

        int pixel = 0;
        int alpha = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        int avg = 0;

        //get image width and height
        int width = colorPattern.getWidth();
        int height = colorPattern.getHeight();

        //===============================================================//
        //Loop through color image
        //Get color data
        //convert to gray-colors
        double val[] = null;
        double p[] = null;
        float[] hsbvals = null;
        Color color = null;
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                pixel = colorPattern.getRGB(x,y);

                alpha = (pixel>>24)&0xff;
                r = (pixel>>16)&0xff;
                g = (pixel>>8)&0xff;
                b = pixel&0xff;

                double avg_d = ((double)r+(double)g+(double)b)/3;
                avg_d = Math.round(avg_d);
                avg = (int)avg_d;

                ImgData data = new ImgData();
                data.setAlpha(alpha);
                data.setPixel(pixel);
                data.setAvg(avg);
                data.setR(r);
                data.setG(g);
                data.setB(b);
                colorImgData.add(data);
            }
        }
        //===============================================================//

        System.out.println("\tBuilding ColorImg Data DONE");
    }

    public void buildGrayImgData()
    {
        grayImgData = new ArrayList<ImgData>();

        int pixel = 0;
        int alpha = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        int avg = 0;

        //get image width and height
        int width = grayImage.getWidth();
        int height = grayImage.getHeight();

        //===============================================================//
        //Loop through grey image
        //Get image data
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                pixel = grayImage.getRGB(i,j);
                alpha = (pixel>>24)&0xff;
                r = (pixel>>16)&0xff;
                g = (pixel>>8)&0xff;
                b = pixel&0xff;

                double avg_d = ((double)r+(double)g+(double)b)/3;
                avg_d = Math.round(avg_d);
                avg = (int)avg_d;

                ImgData data = new ImgData();
                data.setAlpha(alpha);
                data.setPixel(pixel);
                data.setAvg(avg);
                data.setR(r);
                data.setG(g);
                data.setB(b);
                grayImgData.add(data);
            }
        }
        //===============================================================//

        System.out.println("\tBuilding GrayImg Data DONE");
    }

    public void run()
    {
        while (isRunning)
        {
            System.out.println("[THREAD STARTED]");

            maxUnits = grayImage.getWidth();
            gui.updateProgressBar("init", 0, maxUnits, 0);

            renderImage();
            isRunning = false;
        }
        System.out.println("[THREAD STOPPED]");
    }

    public void refresh()
    {
        isRunning = true;
    }

    public void stop()
    {
        isRunning = false;
    }

    public void pauseRendering(int sleepTime)
    {
        isPaused = true;
        System.out.println("[THREAD PAUSED]");
    }

    public void resume()
    {
        isPaused = false;
        System.out.println("[THREAD RESUMED]");
    }

    public void renderImage()
    {
        int pixel = 0;
        int alpha = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        int avg = 0;

        int width = grayImage.getWidth();
        int height = grayImage.getHeight();

        for(int i = 0; i < width; i++)
        {
            isPaused = false;

            for(int j = 0; j < height; j++)
            {
                pixel = grayImage.getRGB(i,j);
                alpha = (pixel>>24)&0xff;
                r = (pixel>>16)&0xff;
                g = (pixel>>8)&0xff;
                b = pixel&0xff;

                double avg_d = ((double)r+(double)g+(double)b)/3;
                avg_d = Math.round(avg_d);
                avg = (int)avg_d;

                for(int c = 0; c < colorImgData.size(); c++)
                {
                    //Color
                    float r_c = colorImgData.get(c).getR();
                    float g_c = colorImgData.get(c).getG();
                    float b_c = colorImgData.get(c).getB();

                    //Gray
                    float r_g = r, g_g = g, b_g = b;

                    if(colorImgData.get(c).getAvg() == avg && avg != 0)
                    {
                        Color newColor = new Color(colorImgData.get(c).getR(), colorImgData.get(c).getG(), colorImgData.get(c).getB());
                        grayImage.setRGB(i, j, newColor.getRGB());
                    }
                }
            }
            currentUnits = i;
            unitsLeft = maxUnits - currentUnits;

            newImg = grayImage;
            gui.updateRenderingImage();

            // float progress = 100.0f - ( 100.0f / (float)(maxUnits) ) * (float)currentUnits;
            float progress = 0.0f + ( 100.0f / (float)(maxUnits) ) * (float)currentUnits;

            //String srt = "[UNITS]: Units Left --: " + unitsLeft;
            String srt = "[PROGRESS]: " + progress + "%";
            System.out.println(srt);

            gui.updateInfoText("add", srt);
            gui.updateProgressBar("update", 0, 0, currentUnits);

            /*
            try
            {
                newImg = grayImage;
                isPaused = true;
                System.out.println("\t[THREAD]: Thread will sleep 0.5 sec");
                System.out.println("\t[THREAD]: Rendering image updated");
                System.out.println("[UNITS]: Units Left --: " + unitsLeft);
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                System.out.println("\t[THREAD-ERROR]: Thread cannot be paused");
            }
            */

            if(!isRunning)
            {
                break;
            }
        }
        gui.updateButtons("done");
        gui.updateRenderingImage();

        String srt = "\t[RENDERING]: Rendering finished";
        System.out.println(srt);
        gui.updateInfoText("add", srt);
    }

    public void saveImage(String imgPath, String imgName)
    {
        String defaultPath = null;
        if(imgPath == null)
        {
            defaultPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        }
        else
        {
            defaultPath = imgPath;
        }

        //write image
        try
        {
            //output = new File(path_output+fileName_output);
            output = new File(defaultPath + imgName);
            ImageIO.write(newImg, "jpg", output);

            System.out.println("\tImage Saved");
            System.out.println("\t[FILE-PATH]: " + (defaultPath + imgName));
        }
        catch(IOException e)
        {
            System.out.println(e);
            System.out.println("\t[ERROR]: Image is not Saved");
        }
    }

    public void setGUI(GUI gui)
    {
        this.gui = gui;
    }

    public void setColorPattern(BufferedImage colorPattern)
    {
        this.colorPattern = colorPattern;
    }

    public void setRenderImage(BufferedImage grayImage)
    {
        this.grayImage = grayImage;
    }

    public BufferedImage getNewImg()
    {
        return newImg;
    }

    public boolean IsRunning()
    {
        return isRunning;
    }

    public boolean IsPaused()
    {
        return isPaused;
    }
}
