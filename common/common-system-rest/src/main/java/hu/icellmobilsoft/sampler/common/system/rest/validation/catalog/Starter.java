/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 i-Cell Mobilsoft Zrt.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package hu.icellmobilsoft.sampler.common.system.rest.validation.catalog;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.text.MessageFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

import org.jboss.logging.Logger;

import hu.icellmobilsoft.coffee.rest.validation.catalog.MavenURLStreamHandlerProvider;

/**
 * Common application starter. Starting on cdi {@code ApplicationScoped} initializing
 * 
 * @author imre.scheffer
 * @since 0.1.0
 */
@ApplicationScoped
public class Starter {

    private static final Logger LOGGER = Logger.getLogger(Starter.class);

    /**
     * Must be set maven url resolver into URL factory
     * 
     * @param init
     *            cdi init ibject
     */
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        String pkgs = System.getProperty("java.protocol.handler.pkgs", "");
        LOGGER.debug(MessageFormat.format("\n\nStarter\n\npkgs: [{0}]\n\n", pkgs));

        UrlStreamHandlerDelegator delegator = new UrlStreamHandlerDelegator();
        delegator.addUrlStreamHandlerFactory(new MavenURLStreamHandlerProvider());
        try {
            // Try doing it the normal way
            URL.setURLStreamHandlerFactory(delegator);
        } catch (final Error e) {
            // Force it via reflection (Quite rough hacking, but any other solution doesn't work)
            try {
                final Field factoryField = URL.class.getDeclaredField("factory");
                factoryField.setAccessible(true);
                // Set the already configured (default) factory to the delegator so that it can also run
                URLStreamHandlerFactory factory = (URLStreamHandlerFactory) factoryField.get(null);
                delegator.addUrlStreamHandlerFactory(factory);
                factoryField.set(null, delegator);
            } catch (NoSuchFieldException | IllegalAccessException e1) {
                throw new Error("Could not access factory field on URL class: {}", e);
            }
        }
    }
}
