package kaptcha.demo.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;

import com.howardlewisship.tapx.core.CoreSymbols;
import com.howardlewisship.tapx.core.services.CoreModule;
import com.howardlewisship.tapx.kaptcha.services.KaptchaModule;

@SubModule(
{ CoreModule.class, KaptchaModule.class })
public class AppModule
{
    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration)
    {
        configuration.add(CoreSymbols.TEST_MODE, "true");
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
    }
}
