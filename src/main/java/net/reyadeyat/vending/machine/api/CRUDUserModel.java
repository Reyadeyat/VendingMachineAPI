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
@WebServlet(name = "User Model CRUD", urlPatterns = {"/crud/user_model"})
public class CRUDUserModel extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+request.getMethod()+" start");
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject json_user = gson.fromJson(request.getReader(), JsonObject.class);
        String access_token = json_user.get("access_token").getAsString();
        UserModel user_model = SystemCache.getUser(access_token);
        response.setContentType("application/json;charset=UTF-8");
        if (user_model == null) {
            response.setStatus(400);
            JsonObject json_response = new JsonObject();
            json_response.addProperty("ERROR", "USER NOT EXIST");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - ERROR - USER NOT EXIST");
            return;
        }
        response.setStatus(200);
        JsonObject json_response = gson.toJsonTree(user_model, UserModel.class).getAsJsonObject();
        json_response.addProperty("Action", "get");
        Writer response_writer = response.getWriter();
        gson.toJson(json_response, JsonObject.class, response_writer);
        //curl new line
        response_writer.write("\n");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+request.getMethod()+" end success");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+request.getMethod()+" start");
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        UserModel user_model = gson.fromJson(request.getReader(), UserModel.class);
        if (SystemCache.getUserByName(user_model.username) != null) {
            response.setStatus(400);
            JsonObject json_response = new JsonObject();
            json_response.addProperty("ERROR", "USER [ "+user_model.username+" ] ALREADY EXISTS");
            Writer response_writer = response.getWriter();
            gson.toJson(json_response, JsonObject.class, response_writer);
            //curl new line
            response_writer.write("\n");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - ERROR - USER [ "+user_model.username+" ] ALREADY EXISTS");
            return;
        }
        user_model.initialize();
        SystemCache.cache(user_model);
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        JsonObject json_response = gson.toJsonTree(user_model, UserModel.class).getAsJsonObject();
        json_response.addProperty("Action", "post");
        Writer response_writer = response.getWriter();
        gson.toJson(json_response, JsonObject.class, response_writer);
        //curl new line
        response_writer.write("\n");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+request.getMethod()+" end success");
    }

    @Override
    public String getServletInfo() {
        return "CRUD User Model";
    }

}
