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

package net.reyadeyat.vending.machine.pocess;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.reyadeyat.vending.machine.api.model.SystemCache;

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
@WebListener
public class SystemCacheProcess implements ServletContextListener {

    private ScheduledExecutorService executor_service;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Runnable scheduled_task = new Runnable() {
                @Override
                public void run() {
                    try {
                        Logger.getLogger(this.getClass().getName()).log(Level.CONFIG, "UserCache Process Release start");
                        SystemCache.release();
                        Logger.getLogger(this.getClass().getName()).log(Level.CONFIG, "UserCache Process Release end");
                    } catch (Exception ex) {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "UserCache Release Process Error, Task failed to execute, Contact Administrator\n" + ex.getMessage(), ex);
                    }
                }
            };
            executor_service = Executors.newSingleThreadScheduledExecutor();
            Integer minutes = 5;
            ScheduledFuture future = executor_service.scheduleAtFixedRate​(scheduled_task, minutes, minutes, TimeUnit.MINUTES);
        } catch (Exception ex) {
            throw new RuntimeException("UserCache Process Runtime Exception, Contact Administrator\n" + ex.getMessage(), ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Logger.getLogger(this.getClass().getName()).log(Level.CONFIG, "UserCache Process has destoyed");
    }

}