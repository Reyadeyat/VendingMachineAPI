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

package net.reyadeyat.vending.machine.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.reyadeyat.vending.machine.api.model.SystemCache;
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
@WebServlet(name = "Product Model CRUD", urlPatterns = {"/crud/product_model"})
public class CRUDProductModel extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+request.getMethod()+" start");
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject json_product = gson.fromJson(request.getReader(), JsonObject.class);
        String access_token = json_product.get("access_token").getAsString();
        UserModel user_model = SystemCache.getUser(access_token);
        if (user_model == null) {
            response.setStatus(400);
            JsonObject json_response = new JsonObject();
            json_response.addProperty("ERROR", "PRODUCT USER NOT EXISTS");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            return;
        }
        List<ProductModel> product_model_list = SystemCache.getProductList();
        response.setContentType("application/json;charset=UTF-8");
        if (product_model_list == null || product_model_list.size() == 0) {
            response.setStatus(400);
            JsonObject json_response = new JsonObject();
            json_response.addProperty("ERROR", "NO PRODUCTS TO LIST");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            return;
        }
        response.setStatus(200);
        JsonArray json_product_model_list = gson.toJsonTree(product_model_list).getAsJsonArray();
        JsonObject json_response = new JsonObject();
        json_response.addProperty("Action", "get product list");
        json_response.add("user", gson.toJsonTree(user_model, UserModel.class).getAsJsonObject());
        json_response.add("product_list", json_product_model_list);
        Writer response_writer = response.getWriter();
        gson.toJson(json_response, JsonObject.class, response_writer);
        //curl new line
        response_writer.write("\n");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+request.getMethod()+" end success");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response, "POST");
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response, "PUT");
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+request.getMethod()+" start");
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        ProductModel product_model = gson.fromJson(request.getReader(), ProductModel.class);
        if (SystemCache.getProductByName(product_model.productName) == null) {
            response.setStatus(400);
            JsonObject json_response = new JsonObject();
            json_response.addProperty("ERROR", "PRODUCT [ "+product_model.productName+" ] NOT EXISTS");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            return;
        }
        UserModel user_model = SystemCache.getUser(product_model.access_token);
        if (user_model == null) {
            response.setStatus(400);
            JsonObject json_response = new JsonObject();
            json_response.addProperty("ERROR", "PRODUCT USER NOT EXISTS");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            return;
        }
        if (user_model.role.equals("buyer") == true) {
            response.setStatus(400);
            JsonObject json_response = gson.toJsonTree(user_model, UserModel.class).getAsJsonObject();
            json_response.addProperty("ERROR", "PRODUCT USER IS BUYER - NO PRODUCT DELETED");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            return;
        }
        product_model.initialize(user_model);
        SystemCache.delete(product_model);
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        JsonObject json_response = gson.toJsonTree(product_model, ProductModel.class).getAsJsonObject();
        json_response.addProperty("Action", "deleted");
        Writer response_writer = response.getWriter();
        gson.toJson(json_response, JsonObject.class, response_writer);
        //curl new line
        response_writer.write("\n");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+request.getMethod()+" end success");
    }

    @Override
    public String getServletInfo() {
        return "CRUD Product Model";
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response, String action)
            throws ServletException, IOException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+action+" start");
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        ProductModel product_model = gson.fromJson(request.getReader(), ProductModel.class);
        if (SystemCache.getProductByName(product_model.productName) != null) {
            response.setStatus(400);
            JsonObject json_response = new JsonObject();
            json_response.addProperty("ERROR", "PRODUCT [ "+product_model.productName+" ] ALREADY EXISTS");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - ERROR - PRODUCT [ "+product_model.productName+" ] ALREADY EXISTS");
            return;
        }
        UserModel user_model = SystemCache.getUser(product_model.access_token);
        if (user_model == null) {
            response.setStatus(400);
            JsonObject json_response = new JsonObject();
            json_response.addProperty("ERROR", "PRODUCT USER NOT EXISTS");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - ERROR - PRODUCT USER NOT EXISTS");
            return;
        }
        if (user_model.role.equals("buyer") == true) {
            response.setStatus(400);
            JsonObject json_response = gson.toJsonTree(user_model, UserModel.class).getAsJsonObject();
            json_response.addProperty("ERROR", "PRODUCT USER IS BUYER - NO PRODUCT CREATED");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - ERROR - PRODUCT USER IS BUYER - NO PRODUCT CREATED");
            return;
        }
        product_model.initialize(user_model);
        SystemCache.cache(product_model);
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        JsonObject json_response = gson.toJsonTree(product_model, ProductModel.class).getAsJsonObject();
        json_response.addProperty("Action", action);
        Writer response_writer = response.getWriter();
        gson.toJson(json_response, JsonObject.class, response_writer);
        //curl new line
        response_writer.write("\n");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - " + action + " end success");
    }

}
