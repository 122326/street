package com.suppercontroller;

import com.entity.AdminMenu;
import com.service.AdminMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/superadmin")
public class AdminMenuController {
    @Autowired
    AdminMenuService adminMenuService;

    @RequestMapping(value = "/getAdminMenu", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAdminMenu(HttpServletRequest request) {
        //创建一个ModelMap返回
        Map<String,Object> modelMap= new HashMap<String, Object>();

        List<AdminMenu> adminMenuList = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentuserName = authentication.getName();
            adminMenuList= adminMenuService.getAdminMenu(currentuserName);
            if(adminMenuList!=null){
                for(AdminMenu adminMenu:adminMenuList){
                    modelMap.put(adminMenu.getMname(),adminMenu.getMurl());
                }
                return modelMap;
            }
        }
        return null;
    }
}
