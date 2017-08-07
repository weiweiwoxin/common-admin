package com.common.system.controller;

import com.common.system.entity.RcMenu;
import com.common.system.entity.RcPrivilege;
import com.common.system.service.MenuService;
import com.common.system.service.PrivilegeService;
import com.common.system.shiro.ShiroKit;
import com.common.system.shiro.ShiroUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Yangxiufeng on 2017/6/20.
 * Time:17:42
 * ProjectName:Common-admin
 */
@RestController
public class IndexController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private PrivilegeService privilegeService;

    @RequestMapping(value = {"/"},method = RequestMethod.GET)
    public ModelAndView index(ModelAndView modelAndView){
        ShiroUser user = (ShiroUser) ShiroKit.getSubject().getPrincipal();
        modelAndView.setViewName("/system/admin/index");
        List<RcPrivilege> privilegeList = privilegeService.getByRoleId(user.getRoleId());
        if (null != privilegeList){
            List<String> ids = new ArrayList<>();
            for (RcPrivilege p : privilegeList){
                ids.add(p.getMenuId());
            }
            List<Integer> wantList = new ArrayList<>();
            //得到一级菜单
            wantList.add(1);
            List<RcMenu> menuList = menuService.selectInIds(ids,wantList);
            if (menuList != null){
                for (RcMenu menu:menuList) {
                    //得到二级菜单
                    List<RcMenu> childList = menuService.getByParentId(menu.getId());
                    menu.setChild(childList);
                }
                modelAndView.addObject("menuList",menuList);
            }
        }
        return modelAndView;
    }
}
