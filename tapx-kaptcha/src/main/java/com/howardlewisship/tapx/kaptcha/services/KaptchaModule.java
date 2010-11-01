package com.howardlewisship.tapx.kaptcha.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.LibraryMapping;

import com.howardlewisship.tapx.internal.kaptcha.services.KaptchaProducer;
import com.howardlewisship.tapx.internal.kaptcha.services.KaptchaProducerImpl;

public class KaptchaModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(KaptchaProducer.class, KaptchaProducerImpl.class);
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("tapx", "com.howardlewisship.tapx.kaptcha"));
    }

    public static void contributeComponentMessagesSource(OrderedConfiguration<Resource> configuration,
            @Value("com/howardlewisship/tapx/kaptcha/tapx-kaptcha.properties")
            Resource catalog)
    {
        configuration.add("TapxKaptcha", catalog, "before:AppCatalog");
    }
}
