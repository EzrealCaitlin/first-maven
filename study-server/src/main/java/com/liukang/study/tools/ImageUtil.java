package com.liukang.study.tools;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

  
/**  
 * 对图片进行处理的方法  
 *   
 * @author SENHUI  
 */  
public class ImageUtil {   
    
    public static Float bili = 0.75f;
    
    public static void wrinteImage(OutputStream os,InputStream is)
    {
    	try {   
            if (is == null) {   
                return;   
            }
            
            Image src = ImageIO.read(is);         
            int imgWidth = src.getHeight(null);
            int imgHeight = src.getHeight(null);
            
            BufferedImage tag = new BufferedImage(imgWidth, imgHeight,BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(src.getScaledInstance(imgWidth, imgHeight,Image.SCALE_SMOOTH), 0, 0, null);   
            ImageIO.write(tag,"jpg",os);
        } catch (Exception ex) {   
            ex.printStackTrace();
        }
    }
    
    /**
     * 输出缩略图片
     * @param out
     * @param imgsrc  目标图片文件绝对路径
     * @param maxWidth  图片最大宽度
     * @param maxHeight 图片最大高度
     * @param proportion 是否约束图片比例
     */
    public static void OutputZoomImage(OutputStream out,InputStream is, int maxWidth, int maxHeight,boolean proportion) {   
        try {   
            if (is == null) {   
                return;   
            }
            
            Image src = ImageIO.read(is);         
            int ImgWidth = src.getWidth(null);   
            int ImgHeight = src.getHeight(null);
            
            //约束比例在图片太小时失效
            proportion = (ImgWidth > maxWidth || ImgHeight > maxHeight) && proportion;
            bili = Float.parseFloat(String.valueOf(ImgHeight)) / Float.parseFloat(String.valueOf(ImgWidth))  ;
            
            if(proportion){
            	if(ImgWidth > ImgHeight){
            		ImgWidth = maxWidth;
            		ImgHeight = (int)(ImgWidth * bili);
            	}else {
            		ImgHeight = maxHeight;
            		ImgWidth = (int)(ImgHeight / bili);
            	}
            }else{
            	ImgWidth = ImgWidth < maxWidth ? ImgWidth : maxWidth;
            	ImgHeight = ImgHeight < maxHeight ? ImgHeight : maxHeight;
            }
            
            BufferedImage tag = new BufferedImage(ImgWidth, ImgHeight,BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(src.getScaledInstance(ImgWidth, ImgHeight,Image.SCALE_SMOOTH), 0, 0, null);   
           // JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);//   JDK1.7中已移除 JPEGCodec
            //ImageIO.write(tag, imgsrc, out);
            ImageIO.write(tag,"jpg",out);
           // encoder.encode(tag);   
        } catch (Exception ex) {   
            ex.printStackTrace();
        }
    }
} 
