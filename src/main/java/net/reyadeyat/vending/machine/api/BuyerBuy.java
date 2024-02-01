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
import java.util.logging.Level;
import java.util.logging.Logger;
import net.reyadeyat.vending.machine.api.model.ProductModel;
import net.reyadeyat.vending.machine.api.model.SystemCache;
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
@WebServlet(name = "BuyerBuy", urlPatterns = {"/buy"})
public class BuyerBuy extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+request.getMethod()+" start");
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject json_request = gson.fromJson(request.getReader(), JsonObject.class);
        String access_token = json_request.get("access_token").getAsString();
        JsonArray product_list = json_request.get("product_list").getAsJsonArray();
        UserModel user_model = SystemCache.getUser(access_token);
        if (user_model == null) {
            response.setStatus(400);
            JsonObject json_response = new JsonObject();
            json_response.addProperty("ERROR", "BuyerBuy USER NOT EXISTS");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - ERROR - BuyerBuy USER NOT EXISTS");
            return;
        }
        if (user_model.role.equals("buyer") == false) {
            response.setStatus(400);
            JsonObject json_response = gson.toJsonTree(user_model, UserModel.class).getAsJsonObject();
            json_response.addProperty("ERROR", "BuyerBuy IS NOT BUYER - NO PURCHASE MADE");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - ERROR - BuyerBuy IS NOT BUYER - NO PURCHASE MADE");
            return;
        }
        if (product_list.size() == 0) {
            response.setStatus(400);
            JsonObject json_response = gson.toJsonTree(user_model, UserModel.class).getAsJsonObject();
            json_response.addProperty("ERROR", "BuyerBuy PRODUCT LIST HAS NO PRODUCT ELEMENTS");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - ERROR - BuyerBuy PRODUCT LIST HAS NO PRODUCT ELEMENTS");
            return;
        }
        JsonObject json_response = gson.toJsonTree(user_model, UserModel.class).getAsJsonObject();
        JsonArray json_prodcult_list_response = new JsonArray();
        json_response.add("product_list_response", json_prodcult_list_response);
        /**ATOMIC TRANSACTION - ALL OR NONE*/
        Integer deposit = user_model.deposit;
        Integer previouse_balance = user_model.deposit;
        Boolean insufficient_balance = false;
        for (int i = 0; i < product_list.size(); i++) {
            JsonObject json_product_response = new JsonObject();
            String productName = product_list.get(i).getAsString();
            ProductModel product_model = SystemCache.getProductByName(productName);
            if (product_model == null) {
                json_product_response.addProperty("ERROR", "Product Named [ "+productName+" ] is not exist");
                json_prodcult_list_response.add(json_product_response);
                continue;
            }
            json_product_response.addProperty("product_name", productName);
            json_product_response.addProperty("product_cost", product_model.cost);
            if (product_model.cost > deposit) {
                insufficient_balance = true;
                break;
            }
            deposit -= product_model.cost;
            json_prodcult_list_response.add(json_product_response);
        }
        if (insufficient_balance == true) {
            response.setStatus(400);
            JsonObject json_error_response = gson.toJsonTree(user_model, UserModel.class).getAsJsonObject();
            json_error_response.addProperty("ERROR", "BuyerBuy INSUFFICIENT BALANCE - TRANSACTION CANCELED");
            Writer response_writer = response.getWriter();
            gson.toJson(json_error_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - ERROR - BuyerBuy INSUFFICIENT BALANCE - TRANSACTION CANCELED");
            return;
        }
        user_model.deposit = deposit;
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        json_response.addProperty("Action", "deposit");
        json_response.addProperty("Prviouse_Balance", previouse_balance);
        json_response.addProperty("Current_Balance", user_model.deposit);
        json_response.addProperty("Total Cost", previouse_balance - user_model.deposit);
        Writer response_writer = response.getWriter();
        gson.toJson(json_response, JsonObject.class, response_writer);
        //curl new line
        response_writer.write("\n");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+request.getMethod()+" end success");
    }

    @Override
    public String getServletInfo() {
        return "Buyer Buy";
    }

}
