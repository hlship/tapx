package com.howardlewiship.tapx.heroku;


/**
 * Temporary shim; when first published and described on <a href="">James Ward's Blog</a>, the package name was off (missing an 's').
 * This has been corrected, and this shim makes the older, invalid package name work. It may be removed at some time in the next few months.
 *
 * @deprecated Use {@link com.howardlewisship.tapx.heroku.JettyMain} instead
 */
public class JettyMain {

    public static void main(String[] args) throws Exception {
        com.howardlewisship.tapx.heroku.JettyMain.main(args);
    }
}
