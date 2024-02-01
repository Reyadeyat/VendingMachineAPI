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

import com.google.gson.annotations.Expose;
import java.math.BigDecimal;

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
public class ProductModel {

    /**
     * auto increment sequence
     */
    @Expose(serialize = false)
    static public Integer productid_auto_increment_sequence = 0;
    /**
     * auto increment
     */
    @Expose(serialize = true)
    public Integer productid;
    @Expose(serialize = true)
    public String productmodel;
    /**
     * Units
     */
    @Expose(serialize = true)
    public Integer amountAvailable;
    /**
     * multiple of 5
     */
    @Expose(serialize = true)
    public Integer cost;
    /**
     * unique string
     */
    @Expose(serialize = true)
    public String productName;
    @Expose(serialize = true)
    public Integer sellerId;
    /**
     * Runtime access token
     */
    @Expose(serialize = false)
    public String access_token;
    /**
     * Runtime access time stamp
     */
    @Expose(serialize = false)
    public long time_stamp;

    public void initialize(UserModel user_model) {
        sellerId = user_model.userid;
        productid = ++productid_auto_increment_sequence;
        time_stamp = System.currentTimeMillis();
    }
}
