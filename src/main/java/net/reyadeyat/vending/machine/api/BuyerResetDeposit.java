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
@WebServlet(name = "BuyerDeposit", urlPatterns = {"/reset"})
public class BuyerResetDeposit extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+request.getMethod()+" start");
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        JsonObject json_response = new JsonObject();
        json_response.addProperty("WARNING", "It is not clear what reset shall do exactly since resetting deposit required some financial transaction that is not well defined to be implemented.");
        Writer response_writer = response.getWriter();
        gson.toJson(json_response, JsonObject.class, response_writer);
        //curl new line
        response_writer.write("\n");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getServletInfo() + " - "+request.getMethod()+" end success");
    }

    @Override
    public String getServletInfo() {
        return "Buyer Reset Deposit";
    }

}
