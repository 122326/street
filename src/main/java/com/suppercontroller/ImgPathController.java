package com.suppercontroller;

import com.util.HttpServletRequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/superadmin")
public class ImgPathController {

    @RequestMapping(value = "/getimgpath", method = RequestMethod.POST)
    @ResponseBody
    private String getimgpath(HttpServletRequest request){
//        String s="http://localhost:8080/SearchStreet/uploads";
        String s="/SearchStreet/uploads";
        s = s + HttpServletRequestUtil.getString(request, "profileImg");
        return s;
    }
}
