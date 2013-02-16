package utils;

import java.awt.Dimension;

public class ImageUtils {

	public static String getTempDirectory()
	{
		String dirName = System.getProperty("java.io.tmpdir");
		dirName = dirName.replaceAll("\\\\", "/");
		dirName = dirName.endsWith("/")?dirName:(dirName+"/");
		return dirName;
	}
	
	
	public static Dimension resizePreservingAspectRatio(Dimension imgSize, Dimension boundary) {

	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    
	    int new_width = 0;
	    int new_height = 0;

	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	        
	        return new Dimension(new_width, new_height);
	    }
	    
	    // then check if we need to scale even with the new height
	    if (original_height > bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	        return  new Dimension(new_width, new_height);
	    }
		
		return new Dimension(original_width, original_height);
	}
	
}
