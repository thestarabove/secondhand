package wtt.utils;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Fileutil {
    private static final String FILE_DIRECTOR = "D:\\3.2\\javaweb\\secondhand2\\static\\images";
    private static final String IMAGE_DIRECTORY = FILE_DIRECTOR + "/images";
    private static final String IMAGE_LOCATION = "localhost:8081/images";
    private static String[] IMAGE_TYPE = new String[]{
            ".jpg", ".png", ".JPEG"
    };
    public static String save(MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();
      //  System.out.println(originalFileName+"original");
        String type = originalFileName.substring(originalFileName.lastIndexOf("."));
     //   System.out.println(type+"type");
        Boolean flag = false;
        for (int i = 0; i < IMAGE_TYPE.length; i++) {
            if (type.equals(IMAGE_TYPE[i])) {
                flag = true;
                break;
            }
        }
        if (flag) {
            try {
                return saveFile(multipartFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
                throw new ComFoundException("文件上传失败");

        }
        return type;
    }

    public static String saveFile(MultipartFile file) throws Exception {
        String path = "nothing" ;
     //   System.out.println(file+".........file");
        if (file != null) {					// 有文件上传
            if (file.getSize() > 0) {
                String fileName = FileNameGenerator.generateFileName() + "."
                        + file.getContentType().substring(
                        file.getContentType().lastIndexOf("/") + 1);	// 创建文件名称
            //    System.out.println(fileName+"..........fileName");
                path = FILE_DIRECTOR+"\\" + fileName;
            //    System.out.println(path+".............path");
                File saveFile = new File(path) ;
                file.transferTo(saveFile);// 文件保存
                path=IMAGE_LOCATION+"/"+fileName;
            }
        }
        return path ;
    }


}
