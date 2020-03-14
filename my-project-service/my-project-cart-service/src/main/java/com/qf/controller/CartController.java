package com.qf.controller;

import com.qf.dto.ResultBean;
import com.qf.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @RequestMapping("addPro")
    public ResultBean addProduct(String id, Long productId, int count) {
        return cartService.addProduct(id, productId, count);
    }

    @RequestMapping("cleanPro")
    public ResultBean clean(String uuid) {
        return cartService.clean(uuid);
    }
    @RequestMapping("showPro")
    public ResultBean showCart(String id) {
        return cartService.showCart(id);
    }

    @RequestMapping("merge")
    public ResultBean merge(String uuid, String userId){
        return cartService.merge(uuid,userId);
    }
}
