package wtt.controller;


import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import wtt.utils.RedisUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码Controller
 * 主要生成验证码
 * 获取验证码 String code = (String)request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
 *
 */
@Controller

@RequestMapping("/secondhand")
public class CodeController {

    @Autowired
    private Producer captchaProducer;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private static Logger logger = LoggerFactory.getLogger(CodeController.class);


    @RequestMapping(value = "/getKaptchaImage",method = RequestMethod.GET)
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();

        String code = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        logger.debug("******************验证码是: " + code + "******************");
        //System.out.println(code);
        response.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Access-Control-Allow-Origin","*");
        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaProducer.createText();
        redisUtil.set("codeSession:code",capText);
        System.out.println(capText);
        // store the text in the session
        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();

        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }
}

