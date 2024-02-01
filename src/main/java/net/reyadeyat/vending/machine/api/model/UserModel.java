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
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

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
public class UserModel {
    /**
     * auto increment sequence
     */
    @Expose(serialize = false)
    static public Integer userid_auto_increment_sequence = 0;
    /**
     * auto increment
     */
    @Expose(serialize = true)
    public Integer userid;
    /**
     * unique string
     */
    @Expose(serialize = true)
    public String username;
    @Expose(serialize = false)
    public String password;
    /**
     * set of multiples of 5 cents
     */
    @Expose(serialize = true)
    public Integer deposit;
    /**
     * 'buyer' or 'seller'
     */
    @Expose(serialize = true)
    public String role;
    /**
     * Runtime access token
     */
    @Expose(serialize = true)
    public String access_token;
    /**
     * Runtime access time stamp
     */
    @Expose(serialize = false)
    public long time_stamp;
    
    static private String generateAccessToken() {
        return UUID.randomUUID().toString();

    }
    
    public void initialize() {
        deposit = deposit == null ? 0 : deposit;
        userid = ++userid_auto_increment_sequence;
        access_token = generateAccessToken();
        time_stamp = System.currentTimeMillis();
    }
}
