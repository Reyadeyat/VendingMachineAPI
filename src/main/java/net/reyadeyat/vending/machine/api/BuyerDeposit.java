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
@WebServlet(name = "BuyerDeposit", urlPatterns = {"/deposit"})
public class BuyerDeposit extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+request.getMethod()+" start");
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject json_request = gson.fromJson(request.getReader(), JsonObject.class);
        String access_token = json_request.get("access_token").getAsString();
        Integer deposit = json_request.get("deposit").getAsInt();
        UserModel user_model = SystemCache.getUser(access_token);
        if (user_model == null) {
            response.setStatus(400);
            JsonObject json_response = new JsonObject();
            json_response.addProperty("ERROR", "BuyerDeposit USER NOT EXISTS");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - ERROR - BuyerDeposit USER NOT EXISTS");
            return;
        }
        if (user_model.role.equals("buyer") == false) {
            response.setStatus(400);
            JsonObject json_response = gson.toJsonTree(user_model, UserModel.class).getAsJsonObject();
            json_response.addProperty("ERROR", "BuyerDeposit IS NOT BUYER - NO DEPOSIT MADE");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - ERROR - BuyerDeposit IS NOT BUYER - NO DEPOSIT MADE");
            return;
        }
        if (deposit % 5 != 0 || deposit < 5 || deposit > 100) {
            response.setStatus(400);
            JsonObject json_response = gson.toJsonTree(user_model, UserModel.class).getAsJsonObject();
            json_response.addProperty("ERROR", "BuyerDeposit DEPOSITED AMOUNT IS NOT MULTIBLE OF 5 MAX 100");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - ERROR - BuyerDeposit DEPOSITED AMOUNT IS NOT MULTIBLE OF 5 MAX 100");
            return;
        }
        Integer previous_balance = user_model.deposit;
        user_model.deposit += deposit;
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        JsonObject json_response = gson.toJsonTree(user_model, UserModel.class).getAsJsonObject();
        json_response.addProperty("Action", "deposit");
        json_response.addProperty("Prvious_Balance", previous_balance);
        Writer response_writer = response.getWriter();
        gson.toJson(json_response, JsonObject.class, response_writer);
        //curl new line
        response_writer.write("\n");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - end success");
    }

    @Override
    public String getServletInfo() {
        return "Buyer Deposit";
    }

}
