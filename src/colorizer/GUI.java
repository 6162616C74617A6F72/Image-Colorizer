package colorizer;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Author: (Alex) Olexandr Matveyev
 * @author Olexandr Matveyev
 */
public class GUI extends JFrame 
{
	private Thread thread = null;
	private ImageRenderer imageRenderer = null;

	private JPanel mainContentPane;
	private JPanel imageLoaderPanel;
	private JButton loadGrayImageButton;
	private JButton loadColorImageButton;
	private JLabel grayImageLabel;
	private JLabel colorImageLabel;
	private JPanel extraPanel;
	private JButton startButton;
	private JButton stopButton;
	private JButton saveButton;
	private JButton pauseButton;
	private JPanel imageRendererPanel;
	private JLabel renderedImageLabel;
	private JProgressBar progressBar;
	private JScrollPane infoScrollPane;
	private JTextArea infoText;
	
	private JFileChooser chooser;

	private JPanel progressPanel;

	private Image grayImage = null;
	private Image colorImage = null;
	private Image renderImage = null;
	private BufferedImage grayImageB = null;
	private BufferedImage colorImageB = null;
	private BufferedImage renderImageB = null;

	private boolean grayImageLoaded = false;
	private boolean colorImageLoaded = false;
	private boolean renderingImageLoaded = false;

	private String graiImagePath = null;
	private String colorImagePath = null;
	private JButton resetButton;

	private String str = "";

	public GUI() 
	{
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 580);
		mainContentPane = new JPanel();
		mainContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainContentPane);
		mainContentPane.setLayout(null);

		imageLoaderPanel = new JPanel();
		imageLoaderPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		imageLoaderPanel.setBounds(10, 11, 200, 419);
		mainContentPane.add(imageLoaderPanel);
		imageLoaderPanel.setLayout(null);

		loadGrayImageButton = new JButton("Load Gray Image");
		loadGrayImageButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		loadGrayImageButton.setBounds(10, 11, 180, 34);
		imageLoaderPanel.add(loadGrayImageButton);
		loadGrayImageButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				str = "[LOAD GRAY IMAGE]";
				System.out.println(str);
				updateInfoText("add", str);
				str = "";

				loadImage("gray");
			}
		});

		loadColorImageButton = new JButton("Load Color Pattern");
		loadColorImageButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		loadColorImageButton.setBounds(10, 223, 180, 34);
		imageLoaderPanel.add(loadColorImageButton);
		loadColorImageButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				str = "[LOAD COLOR IMAGE]";
				System.out.println(str);
				updateInfoText("add", str);
				str = "";

				loadImage("color");
			}
		});

		grayImageLabel = new JLabel("Gray Image");
		grayImageLabel.setBounds(30, 56, 140, 140);
		imageLoaderPanel.add(grayImageLabel);
		grayImageLabel.setHorizontalAlignment(JLabel.CENTER);
		grayImageLabel.setVerticalAlignment(JLabel.CENTER);
		grayImageLabel.setBorder(new LineBorder(new Color(0, 0, 0)));

		colorImageLabel = new JLabel("Color Image");
		colorImageLabel.setBounds(30, 268, 140, 140);
		imageLoaderPanel.add(colorImageLabel);
		colorImageLabel.setHorizontalAlignment(JLabel.CENTER);
		colorImageLabel.setVerticalAlignment(JLabel.CENTER);
		colorImageLabel.setBorder(new LineBorder(new Color(0, 0, 0)));

		extraPanel = new JPanel();
		extraPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		extraPanel.setBounds(220, 11, 554, 56);
		mainContentPane.add(extraPanel);
		extraPanel.setLayout(null);

		startButton = new JButton("Start");
		startButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		startButton.setBounds(10, 11, 89, 34);
		startButton.setEnabled(false);
		extraPanel.add(startButton);
		startButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				start();
			}
		});

		stopButton = new JButton("Stop");
		stopButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		stopButton.setBounds(233, 11, 89, 34);
		stopButton.setEnabled(false);
		extraPanel.add(stopButton);
		stopButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				stop();
			}
		});

		saveButton = new JButton("Save");
		saveButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		saveButton.setBounds(332, 11, 114, 34);
		saveButton.setEnabled(false);
		extraPanel.add(saveButton);
		saveButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				save();
			}
		});

		pauseButton = new JButton("Pause");
		pauseButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		pauseButton.setEnabled(false);
		pauseButton.setBounds(109, 11, 114, 34);
		extraPanel.add(pauseButton);
		pauseButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				pause();
			}
		});

		resetButton = new JButton("Reset");
		resetButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		resetButton.setEnabled(false);
		resetButton.setBounds(456, 11, 88, 34);
		extraPanel.add(resetButton);
		resetButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				reset();
			}
		});

		imageRendererPanel = new JPanel();
		imageRendererPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		imageRendererPanel.setBounds(220, 78, 554, 352);
		mainContentPane.add(imageRendererPanel);
		imageRendererPanel.setLayout(null);

		renderedImageLabel = new JLabel("Rendered Image");
		renderedImageLabel.setBounds(10, 11, 534, 330);
		renderedImageLabel.setHorizontalAlignment(JLabel.CENTER);
		renderedImageLabel.setVerticalAlignment(JLabel.CENTER);
		imageRendererPanel.add(renderedImageLabel);

		progressPanel = new JPanel();
		progressPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		progressPanel.setBounds(784, 11, 40, 419);
		progressPanel.setLayout(null);
		mainContentPane.add(progressPanel);

		progressBar = new JProgressBar();
		progressBar.setOrientation(SwingConstants.VERTICAL);
		progressBar.setBounds(10, 11, 20, 397);
		progressPanel.add(progressBar);

		infoScrollPane = new JScrollPane();
		infoScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		infoScrollPane.setBounds(10, 441, 814, 89);
		mainContentPane.add(infoScrollPane);

		infoText = new JTextArea();
		infoText.setColumns(100);
		infoText.setRows(8);
		infoText.setEditable(false);
		infoText.setFont(new Font("Tahoma", Font.BOLD, 12));
		infoScrollPane.setViewportView(infoText);

		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));

		imageRenderer = new ImageRenderer();
		imageRenderer.setGUI(this);
	}
	
	public void loadImage(String arg)
	{
        int result = chooser.showOpenDialog(null);

        if(result == JFileChooser.APPROVE_OPTION)
        {
            String filePath = chooser.getSelectedFile().getPath();

            str = "\t[FILE-PATH]: " + filePath.toString();
            System.out.println(str);
			updateInfoText("add", str);
			str = "";

			if(arg.equals("gray"))
			{
				try
				{
					graiImagePath = filePath;
					File imageFile = new File(filePath);

					grayImage = ImageIO.read(imageFile);
					grayImageB = ImageIO.read(imageFile);

					renderImage = ImageIO.read(imageFile);
					renderImageB = ImageIO.read(imageFile);

					str = "\t[GRAY-IMAGE]: BufferedImage is created.";
					System.out.println(str);
					updateInfoText("add", str);
					str = "";

					int imgW = grayImageB.getWidth();
					int imgH = grayImageB.getHeight();

					BufferedImage scaled = scaleImage(arg,grayImage, imgW, imgH);
					showImage(arg,scaled);
				}
				catch (IOException e)
				{
					e.printStackTrace();

					str = "\t[GRAY-IMAGE]: BufferedImage cannot be created.";
					System.out.println(str);
					updateInfoText("add", str);
					str = "";
				}
			}
			else if(arg.equals("color"))
			{
				try
				{
					colorImagePath = filePath;
					File imageFile = new File(filePath);

					colorImage = ImageIO.read(imageFile);
					colorImageB = ImageIO.read(imageFile);

					str = "\t[COLOR-IMAGE]: BufferedImage is created.";
					System.out.println(str);
					updateInfoText("add", str);
					str = "";

					int imgW = colorImageB.getWidth();
					int imgH = colorImageB.getHeight();

					BufferedImage scaled = scaleImage(arg,colorImage, imgW, imgH);
					showImage(arg,scaled);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					System.out.println();

					str = "\t[COLOR-IMAGE]: BufferedImage cannot be created.";
					System.out.println(str);
					updateInfoText("add", str);
					str = "";
				}
			}
        }
	}
	
	public void showImage(String arg, BufferedImage img)
	{
		if(arg.equals("gray"))
		{
			grayImageLabel.setText(null);
			grayImageLabel.setIcon(new ImageIcon(img));

			grayImageLoaded = true;
		}
		else if(arg.equals("color"))
		{
			colorImageLabel.setText(null);
			colorImageLabel.setIcon(new ImageIcon(img));

			colorImageLoaded = true;
		}
		else if(arg.equals("render"))
		{
			renderedImageLabel.setText(null);
			renderedImageLabel.setIcon(new ImageIcon(img));

			renderingImageLoaded = true;

			str = "[RENDER-IMAGE]: Updated";
			System.out.println(str);
			updateInfoText("add", str);
			str = "";
		}
		else if(arg.equals("reset"))
		{
			grayImageLabel.setText("Gray Image");
			grayImageLabel.setIcon(null);

			colorImageLabel.setText("Color Image");
			colorImageLabel.setIcon(null);

			grayImageLoaded = false;
			colorImageLoaded = false;
			renderingImageLoaded = false;
		}

		if(grayImageLoaded && colorImageLoaded && !arg.equals("reset"))
		{
			startButton.setEnabled(true);
			grayImageLoaded = false;
			colorImageLoaded = false;
		}

		revalidate();
		repaint();

	}

	public void showRenderImage()
	{
		int rImgW = renderImageB.getWidth();
		int rImgH = renderImageB.getHeight();

		BufferedImage scaled = scaleImage("render",renderImage, rImgW, rImgH);
		showImage("render",scaled);
	}

	private BufferedImage scaleImage(String arg, Image image, int imgW, int imgH)
	{
		int w = 0;
		int h = 0;

		if(arg.equals("gray"))
		{
			float maxW = grayImageLabel.getWidth(), maxH = grayImageLabel.getHeight();
			float ratio = 0.0f;
			if(imgW > imgH)
			{
				ratio = maxW / imgW;
			}
			else
			{
				ratio = maxH / imgH;
			}

			w = (int)(ratio * imgW);
			h = (int)(ratio * imgH);
		}
		else if(arg.equals("color"))
		{
			float maxW = colorImageLabel.getWidth(), maxH = colorImageLabel.getHeight();
			float ratio = 0.0f;
			if(imgW > imgH)
			{
				ratio = maxW / imgW;
			}
			else
			{
				ratio = maxH / imgH;
			}

			w = (int)(ratio * imgW);
			h = (int)(ratio * imgH);
		}
		else if(arg.equals("render"))
		{
			float maxW = renderedImageLabel.getWidth(), maxH = renderedImageLabel.getHeight();
			float ratio = 0.0f;
			if(imgW > imgH)
			{
				ratio = maxW / imgW;
			}
			else
			{
				ratio = maxH / imgH;
			}

			w = (int)(ratio * imgW);
			h = (int)(ratio * imgH);
		}

		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(image, 0, 0, w, h, null);
		g2.dispose();

		return resizedImg;
	}

	public void start()
	{
		str = "[START]";
		System.out.println(str);
		updateInfoText("add", str);
		str = "";

		if(!imageRenderer.IsRunning())
		{
			if(grayImageLabel.getIcon() != null && colorImageLabel.getIcon() != null)
			{
				imageRenderer.refresh();

				imageRenderer.setColorPattern(colorImageB);
				imageRenderer.setRenderImage(renderImageB);
				imageRenderer.buildColorImgData();
				imageRenderer.buildGrayImgData();

				//==========================================================================//
				//[Show rendering image]
				showRenderImage();
				//==========================================================================//

				thread = new Thread(imageRenderer);
				thread.start();

				stopButton.setEnabled(true);
				startButton.setEnabled(false);
				saveButton.setEnabled(false);
				loadGrayImageButton.setEnabled(false);
				loadColorImageButton.setEnabled(false);
			}
			else
			{
				str = "[MESSAGE]: Please load images.";
				System.out.println(str);
				updateInfoText("add", str);
				str = "";
			}
		}
	}

	public void pause()
	{

	}

	public void stop()
	{
		str = "[STOP]";
		System.out.println(str);
		updateInfoText("add", str);
		str = "";

		imageRenderer.stop();

		stopButton.setEnabled(false);
		startButton.setEnabled(true);
		saveButton.setEnabled(true);
		loadGrayImageButton.setEnabled(true);
		loadColorImageButton.setEnabled(true);

		showImage("reset", null);

		//updateInfoText("reset", null);

        updateProgressBar("init", 0, 0, 0);

	}

	public void reset()
	{

	}

	public void save()
	{
		str = "[SAVE]";
		System.out.println(str);
		updateInfoText("add", str);
		str = "";

		// "C:\\Users\\Alex\\Desktop\\"
		String filePath = "";
		String fileName = "colorized_image.jpg";

		imageRenderer.saveImage(filePath, fileName);
	}

	public void updateButtons(String arg)
	{
		//On and off some buttons after rendering is done.

		if(arg.equals("start"))
		{
			stopButton.setEnabled(true);
		}
		else if(arg.equals("done"))
		{
			stop();
		}
	}

	public void updateRenderingImage()
	{
		renderImage = imageRenderer.getNewImg();
		int rImgW = imageRenderer.getNewImg().getWidth();
		int rImgH = imageRenderer.getNewImg().getHeight();

		BufferedImage scaled = scaleImage("render",renderImage, rImgW, rImgH);
		showImage("render",scaled);
	}

	public void updateInfoText(String arg, String str)
	{
		if(arg.equals("reset"))
		{
			infoText.setText(str);
		}
		else if(arg.equals("add"))
		{
			str = str + "\n" + infoText.getText();
			infoText.setText(str);
            infoScrollPane.getVerticalScrollBar().setValue(0);
		}
	}

	public void updateProgressBar(String arg, int min, int max, int val)
	{
		if(arg.equals("init") && min >= 0 && max > 0)
		{
			progressBar.setMinimum(min);
			progressBar.setMaximum(max);
            progressBar.setValue(val);
		}
		else if(arg.equals("update") && val >= 0)
		{
			progressBar.setValue(val);
		}
	}
}
