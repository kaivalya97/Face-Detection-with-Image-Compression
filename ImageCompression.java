import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Scanner;
import Jama.*;
import Jama.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

class Image
{
    BufferedImage img;
    int width;
    int height;
    Matrix U,S,V;
    
    public Image()
    {
            width = 0;
            height = 0;
    }

    public void covertBinary(String inPath, String outPath)
    {
        try
        {
            File f = new File(inPath);
            img = ImageIO.read(f);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        height = img.getHeight();
        width = img.getWidth();
        for(int i = 0 ; i < height ; i++)
        {
            for(int j = 0 ; j < width ; j++)
            {
                Color color = new Color(img.getRGB(j, i));
                int red, green, blue;
                red = (int)(0.299*color.getRed());
                green = (int)(0.587*color.getGreen());
                blue = (int)(0.114*color.getBlue());
                int sum = red + green + blue;
                Color newColor;
                if(sum > 90)
                    newColor = new Color(0,0,0);
                else
                    newColor = new Color(255,255,255);
                img.setRGB(j, i, newColor.getRGB());
            }
        }
        try
        {
            File out = new File(outPath+".jpg");
            ImageIO.write(img, "jpg", out);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    public void convertGrayScale(String inPath, String outPath)
    {
        try
        {
            File f = new File(inPath);
            img = ImageIO.read(f);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        height = img.getHeight();
        width = img.getWidth();
		Matrix ig = new Matrix(height,width);
        for(int i = 0 ; i < height ; i++)
        {
            for(int j = 0 ; j < width ; j++)
            {
                Color color = new Color(img.getRGB(j, i));
                int red, green, blue;
                red = (int)(0.299*color.getRed());
                green = (int)(0.587*color.getGreen());
                blue = (int)(0.114*color.getBlue());
                int sum = red + green + blue;
				ig.set(i,j,sum);
                Color newColor = new Color(sum, sum, sum);
                img.setRGB(j, i, newColor.getRGB());
            }
        }
        try
        {
            File out = new File(outPath);
            ImageIO.write(img, "jpg", out);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void SVDCompression()
    {
        Picture pic1 = new Picture("GrayScale.jpg");
        int M = pic1.height();
        int N = pic1.width();
        Matrix A = new Matrix(M/2, N/2);
        for (int i = 0; i < M/2; i++)
        {
            for (int j = 0; j < N/2; j++)
            {
                Color color = pic1.get(2*i, 2*j);
                double red = color.getRed();
                A.set(i, j, red);
            }
        }
        SingularValueDecomposition svd = A.svd();
        U = svd.getU();  
        V = svd.getV();
        S = svd.getS();
        try
        {
            PrintWriter out = new PrintWriter("U");
            U.print(out,3,3);
            out = new PrintWriter("S");
            S.print(out,3,3);
            out = new PrintWriter("V");
            V.print(out,3,3);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }    
    }
    public void SVDfromfile()
    {
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(new File("U")));
            Matrix U = Matrix.read(in);
            in = new BufferedReader(new FileReader(new File("S")));
            Matrix S = Matrix.read(in);
            in = new BufferedReader(new FileReader(new File("V")));
            Matrix V = Matrix.read(in);
            Picture pic1 = new Picture("GrayScale.jpg");
            int M = pic1.height();
            int N = pic1.width();
            System.out.println("Enter a rank between 0 and "+M/2);
            Scanner input = new Scanner(System.in);
            int r = input.nextInt();
            Matrix Ar;
            Matrix Ur = U.getMatrix(0, M/2-1, 0, r-1);  // first r columns of U
            Matrix Vr = V.getMatrix(0, N/2-1, 0, r-1);  // first r columns of V
            Matrix Sr = S.getMatrix(0, r-1, 0, r-1);  // first r rows and columns of S
            Ar = Ur.times(Sr).times(Vr.transpose());
            Picture pic2 = new Picture(N/2,M/2);
            for (int i = 0; i < M/2; i++)
            {
                for (int j = 0; j < N/2; j++)
                {
                    int y = (int) (Math.round(truncate(Ar.get(i, j))));
                    Color gray = new Color(y, y, y);
                    pic2.set(j,i,gray);
                }
            }
            pic2.save("Compressed.jpg");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static int truncate(double c)
	{
        if (c <= 0) return 0;
        if (c >= 255) return 255;
        return (int) (c + 0.5);
    }
}

class Proj2
{
    public static void main(String[] args) 
    {
        Scanner input = new Scanner(System.in);
        int choice;
        Image obj = new Image();
        System.out.println(" 1) Convert to Binary");
        System.out.println(" 2) Convert to GrayScale");
        System.out.println(" 3) Perform SVD and write to files");
        System.out.println(" 4) Read U,S,V from files and compress the image");
		System.out.print(" Enter Your Choice : ");
        choice = input.nextInt();
        switch(choice)
        {
            case 1:
                obj.covertBinary("Input.jpg", "Binary.jpg");
                break;
            case 2:
                obj.convertGrayScale("Input.jpg", "GrayScale.jpg");
                break;
            case 3:
                obj.SVDCompression();
                break;
            case 4:
                obj.SVDfromfile();
                break;
            default:
                System.out.println("Invalid choice");
        }
    }   
}
