package core.demo.pages;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;

public class SilkImageDemo
{
    @Property
    @InjectComponent
    private ClientElement camera;
}
