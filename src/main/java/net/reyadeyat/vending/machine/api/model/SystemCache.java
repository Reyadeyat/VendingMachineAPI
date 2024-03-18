/*
 * Copyright (C) 2024 Reyadeyat
 *
 * Reyadeyat/RELATIONAL.API is licensed under the
 * BSD 3-Clause "New" or "Revised" License
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://reyadeyat.net/LICENSE/RELATIONAL.API.LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.reyadeyat.vending.machine.api.model;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.reyadeyat.vending.machine.api.model.ProductModel;
import net.reyadeyat.vending.machine.api.model.UserModel;

/**
 * 
 * Description
 * 
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2024.01.31
 */
public class SystemCache {

    static private final Map<String, UserModel> user_model_cache = new HashMap<>();
    static private final Map<String, ProductModel> product_model_cache = new HashMap<>();
    
    static public void cache(UserModel user_model) {
        user_model_cache.put(user_model.access_token, user_model);
    }
    
    static public void cache(ProductModel product_model) {
        product_model_cache.put(product_model.productName, product_model);
    }
    
    static public UserModel getUser(String access_token) {
        return user_model_cache.get(access_token);
    }
    
    static public UserModel getUserByName(String user_name) {
        List<UserModel> user_model_list = new ArrayList<>(user_model_cache.values());
        for (UserModel user_model : user_model_list) {
            if (user_model.username.equals(user_name)) {
                return user_model;
            }
        }
        return null;
    }
    
    static public ProductModel getProduct(String product_name) {
        return product_model_cache.get(product_name);
    }
    
    static public ProductModel getProductByName(String product_name) {
        return getProduct(product_name);
    }
    
    static public List<ProductModel> getProductList() {
        return new ArrayList<>(product_model_cache.values());
    }
    
    static public void delete(ProductModel product_model) {
        synchronized (product_model_cache) {
            product_model_cache.remove(product_model.productName);
        }
    }
    
    static public void release() {
        long time_stamp = System.currentTimeMillis() - 300000;
        //Release Users
        List<UserModel> remove_cached_user_model = new ArrayList<>();
        Iterator<UserModel> cached_user_model_iterator = user_model_cache.values().iterator();
        while (cached_user_model_iterator.hasNext()) {
            UserModel user_model = cached_user_model_iterator.next();
            if (user_model.time_stamp < time_stamp) {
                remove_cached_user_model.add(user_model);
            }
        }
        synchronized (user_model_cache) {
            for (UserModel user_model : remove_cached_user_model) {
                user_model_cache.remove(user_model.access_token);
                Logger.getLogger(SystemCache.class.getName()).log(Level.CONFIG, "UserCache Removed user " + user_model.username);
            }
        }

        //Release Products
        List<ProductModel> remove_cached_product_model = new ArrayList<>();
        Iterator<ProductModel> iterator = product_model_cache.values().iterator();
        while (iterator.hasNext()) {
            ProductModel product_model = iterator.next();
            if (product_model.time_stamp < time_stamp) {
                remove_cached_product_model.add(product_model);
            }
        }
        synchronized (product_model_cache) {
            for (ProductModel product_model : remove_cached_product_model) {
                product_model_cache.remove(product_model.productName);
                Logger.getLogger(SystemCache.class.getName()).log(Level.CONFIG, "UserCache Removed user " + product_model.productName);
            }
        }
    }
}