package com.qf.service.impl;

import com.qf.constant.RedisConstant;
import com.qf.dto.ResultBean;
import com.qf.dto.TProductCartDTO;
import com.qf.entity.TProduct;
import com.qf.mapper.TProductMapper;
import com.qf.service.CartService;
import com.qf.util.StringUtil;
import com.qf.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Component
public class CartServiceImpl implements CartService {
    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ResultBean addProduct(String id, Long productId, int count) {
        String redisKey= StringUtil.getRedisKey(RedisConstant.USER_CART_PRE,id);
        Object o = redisTemplate.opsForValue().get(redisKey);
        if (o==null) {
            CartItem cartItem = new CartItem();
            cartItem.setCount(count);
            cartItem.setProductId(productId);
            cartItem.setUpdateTime(new Date());

            List<CartItem> carts=new ArrayList<>();
            carts.add(cartItem);
            redisTemplate.opsForValue().set(redisKey,carts);
            return ResultBean.success(carts,"添加购物车成功");


        }
        List<CartItem> carts= (List<CartItem>) o;
        System.out.println(carts);
        for (CartItem cartItem : carts) {
            if (cartItem.getProductId().longValue()==productId.longValue()) {
                cartItem.setCount(cartItem.getCount()+count);
                cartItem.setUpdateTime(new Date());

                redisTemplate.opsForValue().set(redisKey,carts);

                return ResultBean.success(carts,"添加购物车成功");
            }
        }
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setCount(count);
        cartItem.setUpdateTime(new Date());
        carts.add(cartItem);

        redisTemplate.opsForValue().set(redisKey,carts);
        return ResultBean.success(carts,"添加购物车成功");
    }

    @Override
    public ResultBean showCart(String id) {
        if (id!=null&&!"".equals(id)) {
            String redisKey = StringUtil.getRedisKey(RedisConstant.USER_CART_PRE, id);
            Object o = redisTemplate.opsForValue().get(redisKey);
            if (o!=null) {
                List<CartItem> carts = (List<CartItem>) o;
                List<TProductCartDTO> products = new ArrayList<>();
                for (CartItem cartItem : carts) {
                    String productKey = StringUtil.getRedisKey(RedisConstant.PRODUCT_PRE, cartItem.getProductId().toString());
                    TProduct pro = (TProduct) redisTemplate.opsForValue().get(productKey);
                    if (pro==null){
                        pro = productMapper.selectByPrimaryKey(cartItem.getProductId());
                        System.out.println(pro);
                        redisTemplate.opsForValue().set(productKey,pro);
                    }
                    TProductCartDTO cartDTO = new TProductCartDTO();
                    cartDTO.setProduct(pro);
                    cartDTO.setCount(cartItem.getCount());
                    cartDTO.setUpdateTime(cartItem.getUpdateTime());
                    products.add(cartDTO);
                }

                Collections.sort(products, new Comparator<TProductCartDTO>() {
                    @Override
                    public int compare(TProductCartDTO o2, TProductCartDTO o1) {
                        return (int)(o1.getUpdateTime().getTime()-o2.getUpdateTime().getTime());
                    }
                });
                return ResultBean.success(products);

            }
        }
        return ResultBean.error("当前用户没有购物车");
    }

    @Override
    public ResultBean clean(String uuid) {

        String redisKey = StringUtil.getRedisKey(RedisConstant.USER_CART_PRE, uuid);
        redisTemplate.delete(redisKey);


        return ResultBean.success("清空购物车成功");
    }

    @Override
    public ResultBean merge(String uuid, String userId) {
        String noLoginRedisKey = StringUtil.getRedisKey(RedisConstant.USER_CART_PRE, uuid);
        String loginRedisKey = StringUtil.getRedisKey(RedisConstant.USER_CART_PRE, userId);
        Object noLoginO = redisTemplate.opsForValue().get(noLoginRedisKey);//未登录下的购物车
        Object loginO = redisTemplate.opsForValue().get(loginRedisKey);//已登录下的购物车

        if (noLoginO==null) {
            return ResultBean.success("未登录状态下没有购物车，不需要合并");
        }

        if (loginO==null) {
            redisTemplate.opsForValue().set(loginRedisKey,noLoginO);
            redisTemplate.delete(noLoginRedisKey);
            return ResultBean.success(noLoginO,"合并成功");
        }

        List<CartItem> noLoginCarts = (List<CartItem>) noLoginO;
        List<CartItem> loginCarts = (List<CartItem>) loginO;

        Map<Long,CartItem> map = new HashMap<>();
        for (CartItem cartItem : noLoginCarts) {
            map.put(cartItem.getProductId(),cartItem);
        }

        for (CartItem loginCart : loginCarts) {
            CartItem currentCartItem = map.get(loginCart.getProductId());
            if (currentCartItem!=null) {
               currentCartItem.setCount(currentCartItem.getCount()+loginCart.getCount());
            }else {
                map.put(loginCart.getProductId(),loginCart);
            }
        }
        redisTemplate.delete(noLoginRedisKey);
        Collection<CartItem> values = map.values();
        List<CartItem> newCarts = new ArrayList<>(values);
        redisTemplate.opsForValue().set(loginRedisKey,newCarts);

        return ResultBean.success(newCarts,"合并成功");
    }
}
