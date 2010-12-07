package demo.components;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

public class Layout
{
    @Inject
    private Request request;

    void onReset()
    {
        Session session = request.getSession(false);

        if (session != null)
            session.invalidate();
    }
}
